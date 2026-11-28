package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Robot {
	public final MultipleTelemetry telemetry;

	public final Pedro pedro;
	public final Intake intake;
	public final Launcher launcher;
	public final Vision vision;

	public Robot(HardwareMap hardwareMap, Telemetry telemetry){
		telemetry.setAutoClear(true);
		this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

		this.pedro = new Pedro(hardwareMap);
		this.intake = new Intake(hardwareMap);
		this.launcher = new Launcher(hardwareMap);
		this.vision = new Vision(hardwareMap);
	}
}
