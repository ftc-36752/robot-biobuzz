package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous(name = "Park", group = "Linear OpMode")
public class Park extends LinearOpMode {
	private Robot robot;

	@Override
	public void runOpMode(){
		robot = new Robot(hardwareMap, telemetry);
		telemetry = robot.telemetry;

		telemetry.addData("Status", "Initialized");
		telemetry.update();

		waitForStart();

		if(opModeIsActive()){
			robot.drivetrain.driveFor(0.5, 0, 0.2, this);
		}
	}
}