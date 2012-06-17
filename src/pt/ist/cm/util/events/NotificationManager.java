package pt.ist.cm.util.events;

public class NotificationManager {

	public static <T extends Listener, E> void addListenerForEvent(T listener,
			EventType<T, E> type) {

		type.addListener(listener);
	}

	public static <T extends Listener, E> void notifyListeners(
			EventType<T, E> type, Class<?> sender, E event) {

		type.runWithEvent(event, sender);

	}

	public static <T extends Listener, E> void removeListener(T listener,
			EventType<T, E> type) {

		type.removeListener(listener);

	}
}
