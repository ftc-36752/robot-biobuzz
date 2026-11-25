package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
	public final MultipleTelemetry telemetry;
	public final Drivetrain drivetrain;
	public final Intake intake;
	public final Launcher launcher;

	public Robot(HardwareMap hardwareMap, Telemetry telemetry){
		telemetry.setAutoClear(true);
		this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

		this.drivetrain = new Drivetrain(hardwareMap);
		this.intake = new Intake(hardwareMap);
		this.launcher = new Launcher(hardwareMap);
	}
}
