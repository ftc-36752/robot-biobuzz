package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config
public class AimController {
	// Distance (inches), target RPM
	private static final double[][] CALIBRATION = {
			{24, 1200},
			{36, 1450},
			{48, 1700},
			{60, 1950}
	};

	public static double SMOOTHING_TIME_SECONDS = 0.5;
	public static double AUTO_HEADING_KP = 0.025;
	public static double AUTO_HEADING_MAX_TURN = 0.5;
	public static double AUTO_HEADING_TOLERANCE = 1.5;

	private final ElapsedTime timer = new ElapsedTime();

	private double currentRpm;

	private final Launcher launcher;

	public AimController(Launcher launcher){
		this.launcher = launcher;
		this.currentRpm = launcher.getTargetRpm();
	}

	public void update(double distance){
		double dt = timer.seconds();
		timer.reset();

		double desiredRpm = calculateRpm(distance);
		double smoothingTime = Math.max(0.001, SMOOTHING_TIME_SECONDS);
		double alpha = 1.0 - Math.exp(-dt / smoothingTime);

		currentRpm += (desiredRpm - currentRpm) * alpha;
		launcher.setTargetRpm(currentRpm);
	}

	private double calculateRpm(double distance){
		if(!Double.isFinite(distance) || distance <= 0){
			return currentRpm;
		}

		if(distance <= CALIBRATION[0][0]){
			return CALIBRATION[0][1];
		}

		int last = CALIBRATION.length - 1;
		if(distance >= CALIBRATION[last][0]){
			return CALIBRATION[last][1];
		}

		for(int i = 0; i < last; i++){
			double d1 = CALIBRATION[i][0];
			double r1 = CALIBRATION[i][1];

			double d2 = CALIBRATION[i + 1][0];
			double r2 = CALIBRATION[i + 1][1];

			if(distance >= d1 && distance <= d2){
				double t = (distance - d1) / (d2 - d1);
				return r1 + t * (r2 - r1);
			}
		}

		return currentRpm;
	}

	public double calculateTurn(double bearing){
		if(Math.abs(bearing) < AUTO_HEADING_TOLERANCE){
			return 0;
		}

		return Range.clip(
				bearing * AUTO_HEADING_KP,
				-AUTO_HEADING_MAX_TURN,
				AUTO_HEADING_MAX_TURN
		);
	}

	public double getCurrentRpm(){
		return currentRpm;
	}
}
