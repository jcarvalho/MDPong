package pt.ist.cm.util.events;

import java.util.ArrayList;
import java.util.List;

import pt.ist.cm.game.KeyDirection;
import pt.ist.cm.game.KeyEventListener;
import pt.ist.cm.game.NetworkEventListener;
import pt.ist.cm.game.ball.Ball;
import pt.ist.cm.game.ball.BallListener;
import pt.ist.cm.game.dto.MDPongDTO;
import pt.ist.cm.game.player.Player;
import pt.ist.cm.game.player.PlayerListener;

public class EventType<T extends Listener, E> {

	public static EventType<KeyEventListener, KeyDirection> KEY_EVENT = new EventType<KeyEventListener, KeyDirection>(
			new Closure<KeyEventListener, KeyDirection>() {

				public void execute(final KeyEventListener target,
						final KeyDirection event, Class<?> sender) {
					target.keyPressed(event, sender);
				}

			});

	public static EventType<NetworkEventListener, MDPongDTO> NETWORK_EVENT = new EventType<NetworkEventListener, MDPongDTO>(
			new Closure<NetworkEventListener, MDPongDTO>() {

				public void execute(final NetworkEventListener target,
						final MDPongDTO dto, Class<?> sender) {
					target.runOnUiThread(new Runnable() {

						public void run() {
							dto.visit(target);

						}
					});
				}

			});

	public static EventType<BallListener, Ball> BALL_LOST_EVENT = new EventType<BallListener, Ball>(
			new Closure<BallListener, Ball>() {

				public void execute(BallListener target, Ball event,
						Class<?> sender) {
					target.ballFell(event);
				}
			});
	public static EventType<BallListener, Ball> BALL_ENTERING_EVENT = new EventType<BallListener, Ball>(
			new Closure<BallListener, Ball>() {

				public void execute(BallListener target, Ball event,
						Class<?> sender) {
					target.ballEntering(event);
				}
			});
	public static EventType<BallListener, Ball> BALL_ENTRY_EVENT = new EventType<BallListener, Ball>(
			new Closure<BallListener, Ball>() {

				public void execute(BallListener target, Ball event,
						Class<?> sender) {
					target.ballEntry(event);
				}
			});

	public static EventType<BallListener, Ball> BALL_CREATED_EVENT = new EventType<BallListener, Ball>(
			new Closure<BallListener, Ball>() {

				public void execute(BallListener target, Ball event,
						Class<?> sender) {
					target.ballCreated(event);
				}
			});

	public static EventType<NetworkEventListener, Player> PLAYER_DISCONNECTED = new EventType<NetworkEventListener, Player>(
			new Closure<NetworkEventListener, Player>() {

				public void execute(NetworkEventListener target, Player player,
						Class<?> sender) {
					target.playerDisconnected(player);
				}
			});

	public static final EventType<PlayerListener, Player> PLAYER_JOINED = new EventType<PlayerListener, Player>(
			new Closure<PlayerListener, Player>() {

				public void execute(PlayerListener target, Player event,
						Class<?> sender) {
					target.playerJoined(event);
				}
			});

	public static final EventType<PlayerListener, Player> PLAYER_SCORED = new EventType<PlayerListener, Player>(
			new Closure<PlayerListener, Player>() {

				public void execute(PlayerListener target, Player event,
						Class<?> sender) {
					target.playerScored(event);
				}
			});

	public static final EventType<PlayerListener, Player> PLAYER_LOST = new EventType<PlayerListener, Player>(
			new Closure<PlayerListener, Player>() {

				public void execute(PlayerListener target, Player event,
						Class<?> sender) {
					target.playerLost(event);
				}
			});

	public static final EventType<PlayerListener, Player> PLAYER_WON = new EventType<PlayerListener, Player>(
			new Closure<PlayerListener, Player>() {

				public void execute(PlayerListener target, Player event,
						Class<?> sender) {
					target.playerWon(event);
				}
			});

	private final Closure<T, E> closure;

	private final List<T> listeners = new ArrayList<T>();

	private EventType(Closure<T, E> closure) {
		this.closure = closure;
	}

	public Closure<T, E> getClosure() {
		return closure;
	}

	public void addListener(T listener) {
		listeners.add(listener);
	}

	public void removeListener(T listener) {
		listeners.remove(listener);
	}

	public void runWithEvent(E event, Class<?> sender) {
		for (T listener : listeners) {
			closure.execute(listener, event, sender);
		}
	}

}
