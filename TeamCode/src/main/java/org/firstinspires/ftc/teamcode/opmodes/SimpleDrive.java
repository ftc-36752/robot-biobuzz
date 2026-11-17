package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

import org.firstinspires.ftc.teamcode.subsystems.FeederController;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.utils.ToggleAction;

@TeleOp(name = "Simple Drive", group = "TeleOp")
public class SimpleDrive extends OpMode {
	private Robot robot;
	private DcMotorEx launchMotor;
	private CRServo feederServo;

	ToggleAction launchToggle;
	ToggleAction intakeToggle;
	ToggleAction intakeDirToggle;
	FeederController feeder = new FeederController();

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry);
		telemetry = robot.telemetry;

		launchMotor = hardwareMap.get(DcMotorEx.class, "launch");
		feederServo = hardwareMap.get(CRServo.class, "feeder");

		// set rotation directions
		launchMotor.setDirection(DcMotor.Direction.FORWARD);
		feederServo.setDirection(CRServo.Direction.REVERSE);

		// set zero power behaviour
		launchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		launchToggle = new ToggleAction();
		intakeToggle = new ToggleAction();
		intakeDirToggle = new ToggleAction();

		telemetry.addData("Status", "Initialized.");
		telemetry.update();
	}

	@Override
	public void loop(){
		robot.drivetrain.drive(-gamepad1.left_stick_y * driveSpeedMultiplier, gamepad1.right_stick_x * driveSpeedMultiplier);

		launchToggle.update(gamepad1.b);
		intakeToggle.update(gamepad1.a);
		intakeDirToggle.update(gamepad1.y);

		double launchRpm = (launchMotor.getVelocity() / 28) * 60.0;
		if(launchToggle.isActive()){
			gamepad1.rumble(1.0, 1.0, Gamepad.RUMBLE_DURATION_CONTINUOUS);
			launchMotor.setPower(launchSpeed);
			// allow time for launch motor to get up to speed
			if(launchToggle.getStateDuration() >= 2.5){
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

		if(intakeToggle.isActive()) robot.intake.run(intakeDirToggle.isActive());
		else {
			intakeDirToggle.reset();
			robot.intake.stop();
		}

		// Display telemetry data
		robot.drivetrain.addTelemetry(telemetry);
		robot.intake.addTelemetry(telemetry);
		telemetry.addData("Launch Mechanism", "%s, Launch (%.0f rpm), Feed (%.0f%%)", launchToggle.isActive() ? "Active" : "Inactive", launchRpm, feederServo.getPower() * 100);
		telemetry.addData("Feeder", "%s, Shots %d, Baseline (%.0f rpm)", !launchToggle.isActive() ? "Inactive" : feeder.isPaused() ? "Paused" : "Active", feeder.getShotCount(), feeder.getBaselineRpm());
		telemetry.update();
	}
}
