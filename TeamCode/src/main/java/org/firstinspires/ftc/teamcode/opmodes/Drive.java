package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.utils.ToggleAction;

@TeleOp(name = "Drive", group = "TeleOp")
public class Drive extends OpMode {
	private Robot robot;

	ToggleAction launchToggle;
	ToggleAction intakeToggle;
	ToggleAction intakeDirToggle;

	private Gamepad.RumbleEffect pulseEffect;
	private boolean wasLaunchActive = false;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry);
		telemetry = robot.telemetry;

		launchToggle = new ToggleAction();
		intakeToggle = new ToggleAction();
		intakeDirToggle = new ToggleAction();

		pulseEffect = new Gamepad.RumbleEffect.Builder()
				.addStep(1.0, 1.0, 200) // Left motor 100%, Right motor 100% for 200ms
				.addStep(0.0, 0.0, 200) // Both motors 0% for 200ms
				.build();

		telemetry.addData("Status", "Initialized.");
		telemetry.update();
	}

	@Override
	public void loop(){
		robot.drivetrain.drive(-gamepad1.left_stick_y * driveSpeedMultiplier, gamepad1.left_stick_x * driveSpeedMultiplier, gamepad1.right_stick_x * driveSpeedMultiplier);

		launchToggle.update(gamepad1.b);
		intakeToggle.update(gamepad1.a);
		intakeDirToggle.update(gamepad1.y);

		if(launchToggle.isActive()){
			robot.launcher.start();
			if(!wasLaunchActive) gamepad1.runRumbleEffect(pulseEffect);
		} else {
			robot.launcher.stop();
			if(wasLaunchActive) gamepad1.stopRumble();
		}
		wasLaunchActive = launchToggle.isActive();
		robot.launcher.update();

		if(intakeToggle.isActive()) robot.intake.set(intakeDirToggle.isActive() ? Intake.State.OUT : Intake.State.IN);
		else {
			intakeDirToggle.reset();
			robot.intake.stop();
		}

		// Display telemetry data
		telemetry.addLine();
		robot.drivetrain.addTelemetry(telemetry);
		robot.intake.addTelemetry(telemetry);
		robot.launcher.addTelemetry(telemetry);
		telemetry.update();
	}

	@Override
	public void stop(){
		robot.drivetrain.stop();
	}
}
