package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * The launch motor and the feeder (indexer) that pushes balls into it.
 * <p>
 * After the launch motor spins up, the feeder runs and pauses briefly whenever a shot is
 * detected, so the launch motor can recover before the next ball goes in.
 * <p>
 * A shot shows up as a sudden drop in launch motor RPM. The launcher follows the
 * flywheel's normal speed with a slow moving average (the baseline) and treats any
 * reading more than {@code shotDropRpm} below it as a shot. On a shot, the feeder briefly
 * runs backwards to pull the next ball away from the flywheel, then holds still.
 * <p>
 * A pause always ends: once the RPM has recovered, or after {@code feederPauseMaxSeconds}
 * at the latest.
 */
@Config
public class Launcher {
	public static double launchSpeed = 0.5;
	public static double spinUpSeconds = 2.5;            // time for the launch motor to get up to speed before feeding

	public static double shotDropRpm = 250;              // drop below baseline that counts as a shot
	public static double shotRecoveryRpm = 100;          // resume once within this of the baseline
	public static double feederPauseMinSeconds = 0.3;    // shortest pause after a shot
	public static double feederPauseMaxSeconds = 1.0;    // longest pause; the feeder always resumes after this
	public static double shotDetectLockoutSeconds = 0.2; // ignore dips this long after the feeder (re)starts
	public static double shotBaselineSmoothing = 0.05;   // how fast the baseline follows the rpm (0-1, per loop)
	public static double feederReverseSeconds = 0.08;    // how long to run the feeder backwards after a shot
	public static double feederReversePower = 0.5;       // feeder power while running backwards (0-1)

	private enum State {
		OFF("Off"), SPINNING_UP("Spinning up"), FEEDING("Feeding"), PAUSED("Paused");

		final String label;

		State(String label) {
			this.label = label;
		}
	}

	private final DcMotorEx launchMotor;
	private final CRServo feederServo;

	private State state = State.OFF;
	private final ElapsedTime stateTimer = new ElapsedTime();
	private double rpm = 0;
	private double baselineRpm = 0;
	private int shotCount = 0;

	public Launcher(HardwareMap hardwareMap) {
		launchMotor = hardwareMap.get(DcMotorEx.class, "launch");
		feederServo = hardwareMap.get(CRServo.class, "feeder");

		launchMotor.setDirection(DcMotor.Direction.FORWARD);
		feederServo.setDirection(CRServo.Direction.REVERSE);

		launchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}

	/** Starts spinning up; the feeder starts once the launch motor is up to speed. Safe to call every loop. */
	public void start() {
		if (state == State.OFF) setState(State.SPINNING_UP);
	}

	/** Stops the launch motor and the feeder. Safe to call every loop. */
	public void stop() {
		state = State.OFF;
		launchMotor.setPower(0.0);
		feederServo.setPower(0.0);
	}

	/** Call every loop. */
	public void update() {
		rpm = (launchMotor.getVelocity() / 28) * 60.0;
		if (state == State.OFF) return;

		launchMotor.setPower(launchSpeed);
		feederServo.setPower(feederPower());
	}

	public boolean isRunning() {
		return state != State.OFF;
	}

	public void addTelemetry(Telemetry telemetry) {
		telemetry.addData("Launcher", "%s, Launch (%.0f rpm), Feed (%.0f%%)", state.label, rpm, feederServo.getPower() * 100);
		telemetry.addData("Shots", "%d, Baseline (%.0f rpm)", shotCount, baselineRpm);
	}

	private double feederPower() {
		double elapsed = stateTimer.seconds();

		if (state == State.SPINNING_UP) {
			if (elapsed < spinUpSeconds) return 0.0;
			baselineRpm = rpm;
			setState(State.FEEDING);
			return 1.0;
		}

		if (state == State.FEEDING) {
			// ignore the first moments after (re)starting so one dip is only counted once
			boolean armed = elapsed >= shotDetectLockoutSeconds;
			if (armed && rpm < baselineRpm - shotDropRpm) {
				shotCount++;
				setState(State.PAUSED);
				return -feederReversePower;
			}

			// follow slow changes (e.g. battery sag) so only sudden dips count as shots
			baselineRpm += shotBaselineSmoothing * (rpm - baselineRpm);
			return 1.0;
		}

		// PAUSED: resume once the launch motor has recovered, or after the maximum pause regardless
		boolean recovered = rpm >= baselineRpm - shotRecoveryRpm;
		if ((elapsed >= feederPauseMinSeconds && recovered) || elapsed >= feederPauseMaxSeconds) {
			// restart the baseline from here so a launcher that never fully recovers can't cause repeated pauses
			baselineRpm = rpm;
			setState(State.FEEDING);
			return 1.0;
		}
		// briefly run backwards to pull the next ball away from the flywheel, then hold still
		return elapsed < feederReverseSeconds ? -feederReversePower : 0.0;
	}

	private void setState(State newState) {
		state = newState;
		stateTimer.reset();
	}
}
