package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Alliance {
	public enum Color { RED, BLUE }

	private Alliance(){}

	private static Color color = Color.RED;

	public static Color getColor(){
		return color;
	}

	public static void setColor(Color color){
		Alliance.color = color;
	}

	public static void updateFromGamepad(Gamepad gamepad, Telemetry telemetry) {
		if (gamepad.b) {
			color = Color.RED;
		} else if (gamepad.x) {
			color = Color.BLUE;
		}
		telemetry.addData("Alliance", color);
		telemetry.addLine("Press [B] for RED | [X] for BLUE");
	}

	public static void addTelemetry(Telemetry telemetry){
		telemetry.addData("Selected Alliance", color);
	}
}
