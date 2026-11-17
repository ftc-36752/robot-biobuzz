package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
	private final DcMotor centralIntake;
	private final CRServo rightIntake;
	private final CRServo leftIntake;

	private boolean running = false;

	public Intake(HardwareMap hardwareMap){
		centralIntake = hardwareMap.get(DcMotor.class, "intake");
		rightIntake = hardwareMap.get(CRServo.class, "right_intake");
		leftIntake = hardwareMap.get(CRServo.class, "left_intake");

		centralIntake.setDirection(DcMotor.Direction.FORWARD);
		rightIntake.setDirection(DcMotor.Direction.REVERSE);
		leftIntake.setDirection(DcMotor.Direction.FORWARD);

		centralIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}

	public void addTelemetry(Telemetry telemetry){
		telemetry.addData("Intake", "%s, Left (%.0f%%), Central (%.0f%%), Right (%.0f%%)", running ? "Active" : "Inactive", leftIntake.getPower() * 100, centralIntake.getPower() * 100, rightIntake.getPower() * 100);
	}

	public void run(boolean reversed){
		running = true;
		centralIntake.setPower(reversed ? -1.0 : 1.0);
		rightIntake.setPower(1.0);
		leftIntake.setPower(1.0);
	}

	public void stop(){
		running = false;
		centralIntake.setPower(0.0);
		rightIntake.setPower(0.0);
		leftIntake.setPower(0.0);
	}
}
