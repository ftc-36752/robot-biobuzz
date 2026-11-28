package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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

	public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
		c.name.set("odometry");
		c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
		c.xPodOffset.set(-6.18263304702879);
		c.yPodOffset.set(-0.9219340076596718);
		c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
		c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
		c.globalDistanceUnit.set(DistanceUnit.INCH);
		c.offsetUnits.set(DistanceUnit.INCH);
	});

	public static ForesightConfig foresightConfig = new ForesightConfig(
			c -> {
				Controller primaryTranslationalForward = Controller.proportional(0.23598973314464686);
				Controller secondaryTranslationalForward = Controller.proportional(0.08719190063514251);
				Controller primaryTranslationalLateral = Controller.proportional(0.3115146460362954);
				Controller secondaryTranslationalLateral = Controller.proportional(0.115096337885767);

				c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
				c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

				c.coast.set(Controller.proportionalFeedforward(0.016684542685610534));
				c.brake.set(Controller.proportionalFeedforward(0.014181861282768953));

				c.headingFeedback.set(Controller.proportional(3.5054705869937957));
				c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04483201714841704, 0.011876023330411947));

				c.linearBrakeCoefficients.set(Matrix.diag(0.03992656421330765, 0.06757788647767111));
				c.quadraticBrakeCoefficients.set(Matrix.diag(0.0038303362959784724, 0.002849618261778365));

				c.maxAchievableForwardVelocity.set(60.9327070053689);
				c.maxAchievableStrafeVelocity.set(53.84696376614638);
				c.naturalForwardDeceleration.set(22.27592414860939);
				c.naturalStrafeDeceleration.set(29.950400386589575);
			}
	);

    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }
}