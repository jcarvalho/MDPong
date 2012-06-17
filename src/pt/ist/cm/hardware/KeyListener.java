package pt.ist.cm.hardware;

import pt.ist.cm.game.KeyDirection;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.view.KeyEvent;

public class KeyListener {

	public static void onKeyDown(int keyCode, KeyEvent event) {

		KeyDirection direction = null;

		ScreenOrientation orientation = ScreenOrientation
				.getCurrentOrientation();

		if (orientation.getLeftCode() == keyCode)
			direction = KeyDirection.LEFT;
		else if (orientation.getRightCode() == keyCode)
			direction = KeyDirection.RIGHT;

		if (direction != null)
			NotificationManager.notifyListeners(EventType.KEY_EVENT,
					KeyListener.class, direction);

	}
}
