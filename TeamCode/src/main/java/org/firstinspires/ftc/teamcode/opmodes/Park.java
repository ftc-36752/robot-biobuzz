package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

import static com.pedropathing.api.Paths.*;

@Autonomous(name = "Park", group = "Linear OpMode", preselectTeleOp = "Drive")
public class Park extends LinearOpMode {
	private Robot robot;

	@Override
	public void runOpMode() throws InterruptedException{
		robot = new Robot(hardwareMap, telemetry);
		telemetry = robot.telemetry;

		Follower follower = robot.pedro.follower;
		PoseFactory p = PoseFactory.radians();

		telemetry.addData("Status", "Initialized");
		telemetry.update();

		waitForStart();
		if(isStopRequested()) return;

		Pose startPose = p.of(0, 0, 0);
		follower.setPose(startPose);

		Pose parkPose = p.of(24, 0, 0);

		follower.follow(
				line(startPose, parkPose).linear(startPose, parkPose)
		);

		while(opModeIsActive() && follower.isBusy()){
			follower.update();

			Pose currentPose = follower.pose();

			telemetry.addData("X", "%.2f", currentPose.x());
			telemetry.addData("Y", "%.2f", currentPose.y());
			telemetry.addData("Heading", "%.2f",
					Math.toDegrees(currentPose.heading()));

			telemetry.update();
		}

		follower.stop();
	}
}
