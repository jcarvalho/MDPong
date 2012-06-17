package pt.ist.cm.hardware;

import pt.ist.cm.game.GameActivity;
import android.util.Log;
import android.view.KeyEvent;

public enum ScreenOrientation {

	LANDSCAPE_LEFT(1, 1, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN),

	LANDSCAPE_RIGHT(1, -1, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_UP),

	PORTRAIT_BOTTOM(0, -1, KeyEvent.KEYCODE_DPAD_LEFT,
			KeyEvent.KEYCODE_DPAD_RIGHT),

	PORTRAIT_TOP(0, 1, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_LEFT);

	private static ScreenOrientation currentOrientation = PORTRAIT_BOTTOM;

	private ScreenOrientation(int coordinateIndex, int referential,
			int leftCode, int rightCode) {
		this.leftCode = leftCode;
		this.rightCode = rightCode;
		this.coordinateIndex = coordinateIndex;
		this.referential = referential;
	}

	private int coordinateIndex, referential, leftCode, rightCode;

	public int getCoordinateIndex() {
		return coordinateIndex;
	}

	public int getReferential() {
		return referential;
	}

	public int getLeftCode() {
		return leftCode;
	}

	public int getRightCode() {
		return rightCode;
	}

	public static ScreenOrientation getCurrentOrientation() {
		ScreenOrientation orientation = currentOrientation;
		return orientation;
	}

	public static void setCurrentOrientation(ScreenOrientation orientation) {
		currentOrientation = orientation;
	}

	public static void setCurrentOrientation(int paddlePosition) {
		switch (paddlePosition) {
		case 0:
			currentOrientation = LANDSCAPE_LEFT;
			break;
		case 1:
			currentOrientation = PORTRAIT_TOP;
			break;
		case 2:
			currentOrientation = LANDSCAPE_RIGHT;
			break;
		case 3:
			currentOrientation = PORTRAIT_BOTTOM;
			break;
		}
	}

	public static boolean isPortrait() {
		return currentOrientation == PORTRAIT_BOTTOM
				|| currentOrientation == PORTRAIT_TOP;
	}

	public static boolean isLandscape() {
		return currentOrientation == LANDSCAPE_LEFT
				|| currentOrientation == LANDSCAPE_RIGHT;
	}

	public static int getScreenWidth() {
		Log.d("ScreenOrientation", "I am a portrait? " + isPortrait()
				+ "Orientation #: " + currentOrientation);
		if (isPortrait())
			return GameActivity.screenWidth;
		return GameActivity.screenHeight;
	}

	public static int getScreenHeight() {
		if (isPortrait())
			return GameActivity.screenHeight;
		return GameActivity.screenWidth;
	}
}
