package org.firstinspires.ftc.teamcode.utils;

public class EdgeDetector {
	private boolean previousState = false;
	private boolean rising = false;
	private boolean falling = false;

	public void update(boolean currentState) {
		rising = currentState && !previousState;
		falling = !currentState && previousState;
		previousState = currentState;
	}

	public boolean risingEdge() {
		return rising;
	}

	public boolean fallingEdge() {
		return falling;
	}

	public void reset() {
		previousState = false;
		rising = false;
		falling = false;
	}
}
