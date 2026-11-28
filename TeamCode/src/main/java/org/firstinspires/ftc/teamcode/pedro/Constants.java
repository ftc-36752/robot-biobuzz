package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
	public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
		c.frontLeftName.set("fl_drive");
		c.frontRightName.set("fr_drive");
		c.backLeftName.set("bl_drive");
		c.backRightName.set("br_drive");
		c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
		c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
		c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
		c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
	});

    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }
}