package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.utils.ToggleAction;

@TeleOp(name = "Simple Drive", group = "TeleOp")
public class SimpleDrive extends OpMode {
	private Robot robot;

	ToggleAction launchToggle;
	ToggleAction intakeToggle;
	ToggleAction intakeDirToggle;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry);
		telemetry = robot.telemetry;

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

		if(launchToggle.isActive()){
			gamepad1.rumble(1.0, 1.0, Gamepad.RUMBLE_DURATION_CONTINUOUS);
			robot.launcher.start();
		} else {
			gamepad1.stopRumble();
			robot.launcher.stop();
		}
		robot.launcher.update();

		if(intakeToggle.isActive()) robot.intake.run(intakeDirToggle.isActive());
		else {
			intakeDirToggle.reset();
			robot.intake.stop();
		}

		// Display telemetry data
		robot.drivetrain.addTelemetry(telemetry);
		robot.intake.addTelemetry(telemetry);
		robot.launcher.addTelemetry(telemetry);
		telemetry.update();
	}
}
