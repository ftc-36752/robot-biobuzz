package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.ToggleAction;

@TeleOp(name = "Drive", group = "1TeleOp")
public class Drive extends OpMode {
	private Robot robot;

	ToggleAction launchToggle;
	ToggleAction intakeToggle;
	ToggleAction intakeDirToggle;

	private Gamepad.RumbleEffect pulseEffect;

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
	public void init_loop(){
		Alliance.updateFromGamepad(gamepad1, telemetry);
		telemetry.update();
	}

	@Override
	public void loop(){
		Vision.TagData tagData = robot.vision.detect();

		double strafe = gamepad1.left_stick_x * driveSpeedMultiplier;
		if (Math.abs(gamepad1.left_stick_x) < 0.5) {
			strafe = 0;
		}
		robot.drivetrain.drive(-gamepad1.left_stick_y * driveSpeedMultiplier, strafe, gamepad1.right_stick_x * driveSpeedMultiplier);

		launchToggle.update(gamepad1.b);
		intakeToggle.update(gamepad1.a);
		intakeDirToggle.update(gamepad1.y);

		if(launchToggle.isActive()){
			robot.launcher.start();
			if (!gamepad1.isRumbling()) gamepad1.runRumbleEffect(pulseEffect);
		} else {
			robot.launcher.stop();
			if(launchToggle.wasActive()) gamepad1.stopRumble();
		}
		robot.launcher.update();

		if(intakeToggle.isActive()) robot.intake.set(intakeDirToggle.isActive() ? Intake.State.OUT : Intake.State.IN);
		else {
			intakeDirToggle.reset();
			robot.intake.stop();
		}

		// Display telemetry data
		Alliance.addTelemetry(telemetry);
		telemetry.addLine();
		robot.drivetrain.addTelemetry(telemetry);
		telemetry.addLine();
		robot.intake.addTelemetry(telemetry);
		telemetry.addLine();
		robot.launcher.addTelemetry(telemetry);
		telemetry.addLine();
		robot.vision.addTelemetry(telemetry);
		telemetry.update();
	}

	@Override
	public void stop(){
		robot.drivetrain.stop();
	}
}
