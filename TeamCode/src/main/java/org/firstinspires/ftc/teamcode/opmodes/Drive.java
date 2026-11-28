package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import static org.firstinspires.ftc.teamcode.RobotConfig.*;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.ToggleAction;

@TeleOp(name = "Drive", group = "1TeleOp")
public class Drive extends OpMode {
	private Robot robot;
	private Follower follower;

	private ToggleAction launchToggle;
	private ToggleAction intakeToggle;
	private ToggleAction intakeDirToggle;

	private Gamepad.RumbleEffect pulseEffect;

	@Override
	public void init(){
		robot = new Robot(hardwareMap, telemetry);
		follower = robot.pedro.follower;
		telemetry = robot.telemetry;

		follower.setPose(
				PoseFactory.radians().of(0, 0, 0)
		);

		launchToggle = new ToggleAction();
		intakeToggle = new ToggleAction();
		intakeDirToggle = new ToggleAction();

		pulseEffect = new Gamepad.RumbleEffect.Builder()
				.addStep(1.0, 1.0, 200)
				.addStep(0.0, 0.0, 200)
				.build();

		telemetry.addData("Status", "Initialized.");
		telemetry.update();
	}

	@Override
	public void init_loop(){
		Alliance.updateFromGamepad(gamepad1, telemetry);
		robot.pedro.update();

		telemetry.addLine("=== PEDRO ===");
		telemetry.addData("Mode", follower.mode());
		telemetry.addData("X", "%.2f", follower.pose().x());
		telemetry.addData("Y", "%.2f", follower.pose().y());
		telemetry.addData(
				"Heading",
				"%.2f deg",
				Math.toDegrees(follower.pose().heading())
		);
		telemetry.update();
	}

	@Override
	public void loop(){
		Vision.TagData tagData = robot.vision.detect();

		// ---------------- //
		//    Drivetrain    //
		// ---------------- //
		double forward = -gamepad1.left_stick_y;
		double strafe = -gamepad1.left_stick_x;
		double turn = -gamepad1.right_stick_x;

		DrivePowers powers = ManualDrive.fieldCentric(
				forward * driveSpeedMultiplier,
				strafe * driveSpeedMultiplier,
				turn * driveSpeedMultiplier,
				follower.pose().heading()
		);
		ManualDrive.driveOrHold(follower, powers);
		follower.update();

		// --------------- //
		//   Toggleables   //
		// --------------- //
		launchToggle.update(gamepad1.b);
		intakeToggle.update(gamepad1.a);
		intakeDirToggle.update(gamepad1.y);

		if(launchToggle.isActive()){
			robot.launcher.start();
			if(!gamepad1.isRumbling()) gamepad1.runRumbleEffect(pulseEffect);
		} else {
			robot.launcher.stop();
			if(launchToggle.wasActive()) gamepad1.stopRumble();
		}
		robot.launcher.update();

		if(intakeToggle.isActive()){
			robot.intake.set(
					intakeDirToggle.isActive()
							? Intake.State.OUT
							: Intake.State.IN
			);
		} else {
			intakeDirToggle.reset();
			robot.intake.stop();
		}

		// ---------------- //
		//    Pressables    //
		// ---------------- //
		if (gamepad1.backWasPressed()) {
			Pose currentPose = follower.pose();
			follower.setPose(
					PoseFactory.radians().of(
							currentPose.x(),
							currentPose.y(),
							0
					)
			);
		}

		// --------------- //
		//    Telemetry    //
		// --------------- //
		Alliance.addTelemetry(telemetry);

		telemetry.addLine();
		robot.pedro.addTelemetry(telemetry);

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
		robot.pedro.stop();
		robot.launcher.stop();
		robot.intake.stop();
		robot.vision.close();
	}
}
