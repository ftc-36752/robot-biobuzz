package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class RobotConfig {
	public static double driveSpeedMultiplier = 1.0;
	public static double launchSpeed = 0.5;

	// shot detection (see FeederController); a shot drops the launch motor by ~400 rpm at 50% power
	public static double shotDropRpm = 250;              // drop below baseline that counts as a shot
	public static double shotRecoveryRpm = 100;          // resume once within this of the baseline
	public static double feederPauseMinSeconds = 0.3;    // shortest pause after a shot
	public static double feederPauseMaxSeconds = 1.0;    // longest pause; the feeder always resumes after this
	public static double shotDetectLockoutSeconds = 0.2; // ignore dips this long after the feeder (re)starts
	public static double shotBaselineSmoothing = 0.05;   // how fast the baseline follows the rpm (0-1, per loop)
}
