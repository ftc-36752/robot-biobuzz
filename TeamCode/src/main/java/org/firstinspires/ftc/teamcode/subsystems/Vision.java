package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class Vision {
	private final AprilTagProcessor aprilTagProcessor;
	private final VisionPortal visionPortal;
	@Nullable private TagData currentTag;

	public static class TagData {
		public final Alliance.Color alliance;
		public final double range;
		public final double bearing;

		public TagData(Alliance.Color alliance, double range, double bearing) {
			this.alliance = alliance;
			this.range = range;
			this.bearing = bearing;
		}
	}

	public Vision(HardwareMap hardwareMap){
		aprilTagProcessor = new AprilTagProcessor.Builder().build();
		visionPortal = new VisionPortal.Builder()
				.setCamera(hardwareMap.get(
						WebcamName.class, "webcam"
				))
				.addProcessor(aprilTagProcessor)
				.build();

		FtcDashboard.getInstance().startCameraStream(visionPortal, 0);
	}

	@Nullable
	public TagData detect() {
		currentTag = null;
		for (AprilTagDetection detection : aprilTagProcessor.getDetections()) {
			if (detection instanceof AprilTagClusterDetection) {
				AprilTagClusterDetection cluster = (AprilTagClusterDetection) detection;

				// Disregard clusters without metadata
				if (cluster.metadata == null || cluster.metadata.name == null) continue;

				// Disregard opposite-alliance Hive clusters
				Alliance.Color clusterColor =
						cluster.metadata.name.startsWith("R")
								? Alliance.Color.RED
								: Alliance.Color.BLUE;
				if (clusterColor != Alliance.getColor()) continue;

				// Disregard upside-down cells
				if (Math.abs(cluster.ftcPose.roll) >= 90.0) continue;

				currentTag = new TagData(
						clusterColor,
						cluster.ftcPose.range,
						cluster.ftcPose.bearing
				);
				return currentTag;
			}
		}
		return null;
	}

	public void addTelemetry(Telemetry telemetry){
		telemetry.addLine("=== APRIL TAG ===");

		if (currentTag == null) {
			telemetry.addLine("No valid AprilTag detected");
			return;
		}

		telemetry.addData("# Alliance", currentTag.alliance);
		telemetry.addData("# Distance", currentTag.range);
		telemetry.addData("# Bearing", currentTag.bearing);
	}

	public void close() {
		visionPortal.close();
	}
}
