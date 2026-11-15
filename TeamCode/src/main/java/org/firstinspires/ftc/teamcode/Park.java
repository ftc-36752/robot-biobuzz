package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Leave/Park Auto", group = "Linear OpMode")
public class Park extends LinearOpMode {
	private DcMotor leftDrive;
	private DcMotor rightDrive;
	private final ElapsedTime runtime = new ElapsedTime();

	@Override
	public void runOpMode(){
		leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
		rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

		leftDrive.setDirection(DcMotor.Direction.FORWARD);
		rightDrive.setDirection(DcMotor.Direction.REVERSE);

		leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		telemetry.addData("Status", "Initialized");
		telemetry.update();

		waitForStart();
		runtime.reset();

		if(opModeIsActive()){
			leftDrive.setPower(0.5);
			rightDrive.setPower(0.5);

			sleep(500); // run for 2000 milliseconds (2 seconds)

			leftDrive.setPower(0);
			rightDrive.setPower(0);
		}
	}
}