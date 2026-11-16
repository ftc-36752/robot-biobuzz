package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

import org.firstinspires.ftc.teamcode.subsystems.FeederController;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.utils.ToggleAction;

@TeleOp(name = "Simple Drive", group = "TeleOp")
public class SimpleDrive extends OpMode {
    private Robot robot;
    private DcMotorEx launchMotor;
    private DcMotor centralIntake;
	private CRServo rightIntake;
	private CRServo leftIntake;
    private CRServo feederServo;

    ToggleAction launchToggle;
	ToggleAction intakeToggle;
	ToggleAction intakeDirToggle;
	FeederController feeder = new FeederController();

    @Override
    public void init() {
		robot = new Robot(hardwareMap);

        launchMotor = hardwareMap.get(DcMotorEx.class, "launch");
        centralIntake = hardwareMap.get(DcMotor.class, "intake");

        rightIntake = hardwareMap.get(CRServo.class, "right_intake");
        leftIntake = hardwareMap.get(CRServo.class, "left_intake");
        feederServo = hardwareMap.get(CRServo.class, "feeder");

        // set rotation directions
        launchMotor.setDirection(DcMotor.Direction.FORWARD);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);
        leftIntake.setDirection(DcMotor.Direction.FORWARD);
        feederServo.setDirection(CRServo.Direction.REVERSE);

        // set zero power behaviour
        launchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        centralIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		launchToggle = new ToggleAction();
		intakeToggle = new ToggleAction();
		intakeDirToggle = new ToggleAction();

		telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		telemetry.setAutoClear(true);
        telemetry.addData("Status", "Initialized.");
        telemetry.update();
    }

    @Override
    public void loop() {
		robot.drivetrain.drive(-gamepad1.left_stick_y * driveSpeedMultiplier, gamepad1.right_stick_x * driveSpeedMultiplier);

        launchToggle.update(gamepad1.b);
		intakeToggle.update(gamepad1.a);
		intakeDirToggle.update(gamepad1.y);

        double launchRpm = (launchMotor.getVelocity() / 28) * 60.0;
        if (launchToggle.isActive()) {
			gamepad1.rumble(1.0, 1.0, Gamepad.RUMBLE_DURATION_CONTINUOUS);
            launchMotor.setPower(launchSpeed);
            // allow time for launch motor to get up to speed
            if (launchToggle.getStateDuration() >= 2.5) {
                feederServo.setPower(feeder.update(launchRpm));
            } else {
                feeder.reset();
                feederServo.setPower(0.0);
            }
        } else {
			gamepad1.stopRumble();
            feeder.reset();
            launchMotor.setPower(0.0);
            feederServo.setPower(0.0);
        }

		if (intakeToggle.isActive()) {
			centralIntake.setPower(1.0);
			rightIntake.setPower(1.0);
			leftIntake.setPower(1.0);

			if (intakeDirToggle.isActive()) {
				centralIntake.setDirection(DcMotor.Direction.REVERSE);
			} else {
				centralIntake.setDirection(DcMotor.Direction.FORWARD);
			}

		} else {
			intakeDirToggle.reset();

			centralIntake.setDirection(DcMotor.Direction.FORWARD);

			centralIntake.setPower(0.0);
			rightIntake.setPower(0.0);
			leftIntake.setPower(0.0);
		}

        // Display telemetry data
		robot.drivetrain.addTelemetry(telemetry);
        telemetry.addData("Intake", "%s, Left (%.0f%%), Central (%.0f%%), Right (%.0f%%)", intakeToggle.isActive() ? "Active" : "Inactive", leftIntake.getPower() * 100, centralIntake.getPower() * 100, rightIntake.getPower() * 100);
        telemetry.addData("Launch Mechanism", "%s, Launch (%.0f rpm), Feed (%.0f%%)", launchToggle.isActive() ? "Active" : "Inactive", launchRpm, feederServo.getPower() * 100);
        telemetry.addData("Feeder", "%s, Shots %d, Baseline (%.0f rpm)", !launchToggle.isActive() ? "Inactive" : feeder.isPaused() ? "Paused" : "Active", feeder.getShotCount(), feeder.getBaselineRpm());
        telemetry.update();
    }
}
