package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ToggleAction {
    private boolean active = false;
	private boolean previousActive = false;
    private boolean previousInput = false;
    private final ElapsedTime actionTimer = new ElapsedTime();

    /**
     * Call this every frame.
     *
     * @param currentInput Raw input boolean (e.g. gamepad1.x)
     */
    public void update(boolean currentInput) {
		previousActive = active;
        if (currentInput && !previousInput) {
            active = !active;
            actionTimer.reset(); // Automatically resets timer on state change
        }
        previousInput = currentInput;
    }

	public void reset() {
		active = false;
		previousActive = false;
		previousInput = false;
		actionTimer.reset();
	}

    public boolean isActive() {
        return active;
    }
	public boolean wasActive(){
		return previousActive;
	}

    /**
     * Returns the number of seconds that have elapsed since the
     * last state change (how long it has been ON, or how long
     * it has been resting while OFF).
     */
    public double getStateDuration() {
        return actionTimer.seconds();
    }
}