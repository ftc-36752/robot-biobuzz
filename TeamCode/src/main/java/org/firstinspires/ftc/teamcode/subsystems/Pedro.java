
package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import com.pedropathing.revhub.drivetrains.Mecanum;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Pedro {
	public final Follower follower;

	DcMotorSimple fl;
	DcMotorSimple fr;
	DcMotorSimple bl;
	DcMotorSimple br;

	public Pedro(HardwareMap hardwareMap){
		follower = Constants.create(hardwareMap);

		fl = hardwareMap.get(
				DcMotorSimple.class,
				Constants.drivetrainConfig.frontLeftName.get()
		);
		fr = hardwareMap.get(
				DcMotorSimple.class,
				Constants.drivetrainConfig.frontRightName.get()
		);
		bl = hardwareMap.get(
				DcMotorSimple.class,
				Constants.drivetrainConfig.backLeftName.get()
		);
		br = hardwareMap.get(
				DcMotorSimple.class,
				Constants.drivetrainConfig.backRightName.get()
		);
	}

	public void update(){
		follower.update();
	}

	public void stop(){
		follower.stop();
	}

	public void addTelemetry(Telemetry telemetry){
		Pose pose = follower.pose();

		telemetry.addLine("=== PEDRO ===");

		telemetry.addData("Mode", follower.mode());
		telemetry.addData("Following", follower.following());

		telemetry.addData("X", "%.2f", pose.x());
		telemetry.addData("Y", "%.2f", pose.y());

		telemetry.addData(
				"Heading",
				"%.2f deg",
				Math.toDegrees(pose.heading())
		);

		telemetry.addLine();
		telemetry.addLine("=== DRIVETRAIN ===");
		telemetry.addData("Motors",
				"FL (%.0f%), FR (%.0f%), BL (%.0f%), BR (%.0f%)",
				fl.getPower() * 100,
				fr.getPower() * 100,
				bl.getPower() * 100,
				br.getPower() * 100
		);
	}
}
