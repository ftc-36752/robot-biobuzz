package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
	public final Drivetrain drivetrain;
	public final MultipleTelemetry telemetry;

	public Robot(HardwareMap hardwareMap, Telemetry telemetry){
		this.drivetrain = new Drivetrain(hardwareMap);
		this.telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
		this.telemetry.setAutoClear(true);
	}
}
