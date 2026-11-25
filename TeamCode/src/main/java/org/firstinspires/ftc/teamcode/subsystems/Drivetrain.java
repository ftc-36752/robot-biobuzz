package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Drivetrain {
	public enum Mode {DIFFERENTIAL, HOLONOMIC}

	private Mode mode = Mode.HOLONOMIC;

	private final DcMotor flDrive;
	private final DcMotor frDrive;
	private final DcMotor blDrive;
	private final DcMotor brDrive;

	private double flPower;
	private double frPower;
	private double blPower;
	private double brPower;

	public Drivetrain(HardwareMap hardwareMap){
		flDrive = hardwareMap.get(DcMotor.class, "fl_drive");
		frDrive = hardwareMap.get(DcMotor.class, "fr_drive");
		blDrive = hardwareMap.get(DcMotor.class, "bl_drive");
		brDrive = hardwareMap.get(DcMotor.class, "br_drive");

		flDrive.setDirection(DcMotorSimple.Direction.REVERSE);
		frDrive.setDirection(DcMotorSimple.Direction.FORWARD);
		blDrive.setDirection(DcMotorSimple.Direction.REVERSE);
		brDrive.setDirection(DcMotorSimple.Direction.FORWARD);

		flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		blDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		brDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}

	public void setDriveMode(Mode newMode){
		mode = newMode;
	}

	public void drive(double drive, double strafe, double turn){
		if(mode == Mode.DIFFERENTIAL){
			differentialDrive(drive, turn);
		} else {
			holonomicDrive(drive, strafe, turn);
		}
	}

	private void differentialDrive(double drive, double turn){
		flPower = drive + turn;
		blPower = drive + turn;

		frPower = drive - turn;
		brPower = drive - turn;

		applyPower();
	}

	private void holonomicDrive(double drive, double strafe, double turn){
		flPower = drive + strafe + turn;
		frPower = drive - strafe - turn;
		blPower = drive - strafe + turn;
		brPower = drive + strafe - turn;

		applyPower();
	}

	private void applyPower(){
		double max = 1.0;
		for(double power : new double[]{
				flPower, frPower, blPower, brPower
		}){
			max = Math.max(max, Math.abs(power));
		}

		flPower /= max;
		frPower /= max;
		blPower /= max;
		brPower /= max;

		flDrive.setPower(flPower);
		frDrive.setPower(frPower);
		blDrive.setPower(blPower);
		brDrive.setPower(brPower);
	}

	public void addTelemetry(Telemetry telemetry){
		telemetry.addData("Drive Mode", mode);
		telemetry.addData("Front Left", "%.0f%%", flPower * 100);
		telemetry.addData("Front Right", "%.0f%%", frPower * 100);
		telemetry.addData("Back Left", "%.0f%%", blPower * 100);
		telemetry.addData("Back Right", "%.0f%%", brPower * 100);
	}

	public void stop(){
		drive(0, 0, 0);
	}

	public void driveFor(double drive, double strafe, double turn, double seconds, LinearOpMode opMode){
		drive(drive, strafe, turn);
		opMode.sleep((long) (seconds * 1000));
		stop();
	}
}
