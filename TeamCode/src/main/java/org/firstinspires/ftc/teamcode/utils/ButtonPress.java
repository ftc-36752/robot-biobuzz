
package org.firstinspires.ftc.teamcode.utils;

public class ButtonPress {
	private boolean previousState = false;
	private boolean pressed = false;

	public void update(boolean currentState) {
		pressed = currentState && !previousState;
		previousState = currentState;
	}

	public boolean wasPressed() {
		return pressed;
	}

	public void reset() {
		previousState = false;
		pressed = false;
	}
}
