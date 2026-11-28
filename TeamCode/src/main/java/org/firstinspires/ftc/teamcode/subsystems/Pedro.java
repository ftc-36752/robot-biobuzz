
package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Pedro {
	public final Follower follower;

	public Pedro(HardwareMap hardwareMap) {
		follower = Constants.create(hardwareMap);
	}

	public void update() {
		follower.update();
	}

	public void stop() {
		follower.stop();
	}

	public void addTelemetry(Telemetry telemetry) {
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
	}
}
