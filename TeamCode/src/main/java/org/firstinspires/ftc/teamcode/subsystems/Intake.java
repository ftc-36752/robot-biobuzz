package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
	public enum State {
		OFF("Inactive"), IN("Intaking"), OUT("Outtaking");

		final String label;
		State(String label){
			this.label = label;
		}
	}

	private final DcMotor centralIntake;
	private final CRServo rightIntake;
	private final CRServo leftIntake;

	private State state = State.OFF;

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
		telemetry.addData("Intake", "%s, Left (%.0f%%), Central (%.0f%%), Right (%.0f%%)", state.label, leftIntake.getPower() * 100, centralIntake.getPower() * 100, rightIntake.getPower() * 100);
	}

	public void set(State state){
		this.state = state;

		if(state == State.OFF){
			centralIntake.setPower(0.0);
			rightIntake.setPower(0.0);
			leftIntake.setPower(0.0);
		} else {
			// only the central intake reverses; the side intakes always pull in
			centralIntake.setPower(state == State.OUT ? -1.0 : 1.0);
			rightIntake.setPower(1.0);
			leftIntake.setPower(1.0);
		}
	}

	public void stop(){
		set(State.OFF);
	}
}
