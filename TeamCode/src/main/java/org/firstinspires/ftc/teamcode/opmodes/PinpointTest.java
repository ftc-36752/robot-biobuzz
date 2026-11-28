
package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp(name = "Pinpoint Test", group = "Test")
public class PinpointTest extends OpMode {
	GoBildaPinpointDriver pinpoint;

	@Override
	public void init(){
		GoBildaPinpointDriver pinpoint =
				hardwareMap.get(GoBildaPinpointDriver.class, "odometry");

		pinpoint.setEncoderResolution(
				GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD
		);

		pinpoint.setOffsets(-1.5, 6.0, DistanceUnit.INCH);

		pinpoint.setEncoderDirections(
				GoBildaPinpointDriver.EncoderDirection.REVERSED,
				GoBildaPinpointDriver.EncoderDirection.FORWARD
		);

		pinpoint.resetPosAndIMU();

		telemetry.addLine("Pinpoint detected.");
		telemetry.addLine("Keep robot still during calibration.");
		telemetry.update();
	}

	@Override
	public void loop(){
		pinpoint.update();

		Pose2D position = pinpoint.getPosition();

		telemetry.addData("Device ID", pinpoint.getDeviceID());
		telemetry.addData("Device Status", pinpoint.getDeviceStatus());
		telemetry.addData("Loop Time", pinpoint.getLoopTime());
		telemetry.addData("Frequency", "%.1f Hz", pinpoint.getFrequency());

		telemetry.addLine("--- Raw Encoders ---");
		telemetry.addData("X Encoder", pinpoint.getEncoderX());
		telemetry.addData("Y Encoder", pinpoint.getEncoderY());

		telemetry.addLine("--- Position ---");
		telemetry.addData("X", "%.2f in",
				position.getX(DistanceUnit.INCH));
		telemetry.addData("Y", "%.2f in",
				position.getY(DistanceUnit.INCH));
		telemetry.addData("Heading", "%.2f deg",
				position.getHeading(AngleUnit.DEGREES));

		telemetry.update();
	}
}
