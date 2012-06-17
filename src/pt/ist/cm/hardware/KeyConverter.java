package pt.ist.cm.hardware;

import pt.ist.cm.game.KeyDirection;
import android.hardware.SensorEvent;

public class KeyConverter {

	private static final long THRESHOLD_TIME = 10;
	private static final float THRESHOLD_VALUE = 2f;

	private long millis = 0;

	private KeyDirection keystroke = null;

	public void updateWithEvent(SensorEvent event, int index, float val) {

		ScreenOrientation orientation = ScreenOrientation
				.getCurrentOrientation();

		if (orientation.getCoordinateIndex() != index)
			return;

		if (event.timestamp - millis < THRESHOLD_TIME) {
			return;
		}

		millis = event.timestamp;

		if (Math.abs(val) < THRESHOLD_VALUE) {
			return;
		}

		if (val * orientation.getReferential() > 0) {
			this.keystroke = KeyDirection.RIGHT;
		} else
			this.keystroke = KeyDirection.LEFT;

	}

	public boolean hasKeyStroke() {
		return keystroke != null;
	}

	public KeyDirection getKeyStroke() {
		KeyDirection dir = this.keystroke;
		this.keystroke = null;
		return dir;
	}

}
