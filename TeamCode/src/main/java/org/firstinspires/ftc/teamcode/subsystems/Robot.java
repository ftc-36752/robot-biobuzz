package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
	public final Drivetrain drivetrain;

	public Robot(HardwareMap hardwareMap){
		this.drivetrain = new Drivetrain(hardwareMap);
	}
}
