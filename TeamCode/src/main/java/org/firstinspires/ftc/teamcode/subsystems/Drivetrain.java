package org.firstinspires.ftc.teamcode.subsystems;

//import com.acmerobotics.dashboard.FtcDashboard;
import static org.firstinspires.ftc.teamcode.RobotConfig.driveSpeedMultiplier;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Drivetrain {
	// Currently unused as we only have two drive motors
//	public enum Mode {DIFFERENTIAL, HOLONOMIC}
//
//	public static Mode mode = Mode.DIFFERENTIAL;
//
//	public void setDriveMode(Mode mode){
//		Drivetrain.mode = mode;
//		FtcDashboard.getInstance().updateConfig();
//	}

	private final DcMotor leftDrive;
	private final DcMotor rightDrive;

	public Drivetrain(HardwareMap hardwareMap) {
		leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
		rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

		leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
		rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

		leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}

	private double leftPower;
	private double rightPower;
	public void drive(double drive, double turn){
		// Calculate power for each side
		leftPower = drive + turn;
		rightPower = drive - turn;

		// Normalise powers if they exceed 1.0
		double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
		if (max > 1.0) {
			leftPower /= max;
			rightPower /= max;
		}

		// Apply power to the motors
		leftDrive.setPower(leftPower);
		rightDrive.setPower(rightPower);
	}

	public void addTelemetry(Telemetry telemetry){
		telemetry.addData("Drive", "Left (%.0f%%), Right (%.0f%%)", leftPower * 100, rightPower * 100);
	}

	public void stop(){
		drive(0, 0);
	}

	public void driveFor(double drive, double turn, double seconds, LinearOpMode opMode){
		drive(drive, turn);
		opMode.sleep((long) (seconds * 1000));
		stop();
	}
}
