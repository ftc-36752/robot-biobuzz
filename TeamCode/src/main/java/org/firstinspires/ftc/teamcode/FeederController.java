package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

/**
 * Runs the feeder and pauses it briefly whenever a shot is detected,
 * so the launch motor can recover before the next ball goes in.
 * <p>
 * A shot shows up as a sudden drop in launch motor RPM. The controller follows the
 * flywheel's normal speed with a slow moving average (the baseline) and treats any
 * reading more than {@code shotDropRpm} below it as a shot.
 * <p>
 * A pause always ends: once the RPM has recovered, or after {@code feederPauseMaxSeconds}
 * at the latest.
 */
public class FeederController {
	private enum State { FEEDING, PAUSED }

	private State state = State.FEEDING;
	private final ElapsedTime stateTimer = new ElapsedTime();
	private double baselineRpm = Double.NaN;
	private int shotCount = 0;

	/**
	 * Call every loop while the feeder should be running.
	 *
	 * @param launchRpm Current launch motor speed in RPM
	 * @return Power to give the feeder servo
	 */
	public double update(double launchRpm) {
		double elapsed = stateTimer.seconds();

		if (state == State.FEEDING) {
			if (Double.isNaN(baselineRpm)) baselineRpm = launchRpm;

			// ignore the first moments after (re)starting so one dip is only counted once
			boolean armed = elapsed >= shotDetectLockoutSeconds;
			if (armed && launchRpm < baselineRpm - shotDropRpm) {
				shotCount++;
				setState(State.PAUSED);
				return 0.0;
			}

			// follow slow changes (e.g. battery sag) so only sudden dips count as shots
			baselineRpm += shotBaselineSmoothing * (launchRpm - baselineRpm);
			return 1.0;
		}

		// PAUSED: resume once the launch motor has recovered, or after the maximum pause regardless
		boolean recovered = launchRpm >= baselineRpm - shotRecoveryRpm;
		if ((elapsed >= feederPauseMinSeconds && recovered) || elapsed >= feederPauseMaxSeconds) {
			// restart the baseline from here so a launcher that never fully recovers can't cause repeated pauses
			baselineRpm = launchRpm;
			setState(State.FEEDING);
			return 1.0;
		}
		return 0.0;
	}

	/** Call while the feeder is off, so it starts fresh next time. */
	public void reset() {
		baselineRpm = Double.NaN;
		setState(State.FEEDING);
	}

	public boolean isPaused() {
		return state == State.PAUSED;
	}

	public double getBaselineRpm() {
		return baselineRpm;
	}

	public int getShotCount() {
		return shotCount;
	}

	private void setState(State newState) {
		state = newState;
		stateTimer.reset();
	}
}
