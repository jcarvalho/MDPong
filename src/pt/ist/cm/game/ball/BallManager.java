package pt.ist.cm.game.ball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.cm.game.dto.BallControlDTO;
import pt.ist.cm.game.dto.BallDTO;
import pt.ist.cm.game.dto.PlayerScoreDTO;
import pt.ist.cm.game.player.MiniMapView;
import pt.ist.cm.game.player.Player;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.network.ConnectionManager;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.content.Context;
import android.util.Log;

public class BallManager {

	private final static Map<String, Ball> balls = new HashMap<String, Ball>();

	private final static Map<String, BallView> views = new HashMap<String, BallView>();

	private final static Map<String, String> ballPerPlayer = new HashMap<String, String>(); // ball,player

	private static boolean running = false;

	private static boolean gameOver = false;

	public static int numBallsInside = 0;

	private static int newBallNumber = 0;

	private static MiniMapView miniMap;

	public static void registerBall(Ball ball) {
		balls.put(ball.getId(), ball);
		if (Referential.getInstance().isInReferential(ball.getPosition())) {
			numBallsInside++;
			updateBallPlayer(PlayerManager.playerId, ball.getId(), false);
			ConnectionManager.broadcast(new BallControlDTO(ball,
					PlayerManager.playerId, false));
			Log.d(BallManager.class.getName(), "inc num balls: "
					+ numBallsInside + ". Map has " + balls.size());
		}
	}

	public static BallView getViewForBall(Context context, String id) {
		Ball ball = balls.get(id);
		if (ball == null)
			throw new RuntimeException("No ball with ID " + id);

		BallView view = views.get(id);

		if (view == null) {
			view = new BallView(context, ball);
			views.put(id, view);
		}

		return view;
	}

	public static Ball getBall(String id) {
		return balls.get(id);
	}

	public static void clear() {

		for (Ball ball : balls.values()) {
			ball.stop();
		}

		balls.clear();
		views.clear();

		gameOver = false;
		running = false;
		numBallsInside = 0;
		newBallNumber = 0;
		ballPerPlayer.clear();
	}

	public static void ballMoved(Ball ball) {
		BallView view = views.get(ball.getId());
		if (view != null) {
			view.ballMoved();
		}
	}

	public static void startAllBalls() {

		if (gameOver)
			return;

		for (Ball ball : balls.values()) {
			if (running) {
				ball.stop();
			} else {
				ball.start();
			}
		}

		if (running)
			running = false;
		else
			running = true;

	}

	public static void ballIsAboutToChange(Ball ball) {

		// ConnectionManager.sendToPlayer(playerId, new BallDTO(ball));

		// Default - stable
		ConnectionManager.broadcast(new BallDTO(ball));
	}

	public static void updateStatus(List<Ball> balls) {
		for (Ball ball : balls) {
			registerBall(ball);
		}
	}

	public static List<Ball> getBalls() {
		return new ArrayList<Ball>(balls.values());
	}

	public static void ballLeftScreen(Ball ball) {

		ConnectionManager.broadcast(new BallDTO(ball));
		if (ballPerPlayer.containsKey(ball.getId()))
			numBallsInside--;
		Log.d(BallManager.class.getName(),
				"decremented numballsInside in ballleftscreen: "
						+ numBallsInside);
		updateMap();
	}

	public static void updateMap() {
		if (miniMap != null)
			miniMap.ballMoved();
	}

	public static void setMiniMapView(MiniMapView v) {
		miniMap = v;
	}

	public static void takeControlOfBall(String id, Vector position,
			Vector direction) {

		Ball ball = balls.get(id);
		if (Referential.getInstance().isInReferential(ball.getPosition()))
			return;

		numBallsInside++;
		Log.d(BallManager.class.getName(),
				"Incremented num balls in takecontrol, number is now "
						+ numBallsInside);
		ball.setPosition(position);
		ball.setDirection(direction);

		ball.revive();
		if (running)
			ball.start();
		else
			startAllBalls();
		ConnectionManager.broadcast(new BallControlDTO(ball,
				PlayerManager.playerId, false));
		NotificationManager.notifyListeners(EventType.BALL_ENTRY_EVENT,
				BallManager.class, ball);

	}

	public static boolean isRunning() {
		return running;
	}

	public static void gameOver() {
		running = false;

		for (Ball b : balls.values()) {
			b.stop();
		}

		gameOver = true;
	}

	public static boolean isGameOver() {
		return gameOver;
	}

	public static boolean ballExists(String id) {
		return balls.containsKey(id);
	}

	public static Ball createNewBall() {
		return new Ball("" + (newBallNumber++));
	}

	public static void removeBall(String ballId) {

		System.out.println("Removing ball " + ballId);
		// if (balls.containsKey(ballId)) {
		// Ball ball = balls.get(ballId);
		// ball.kill();
		// balls.remove(ballId);
		// views.remove(ballId);
		// }
	}

	public static void ballFell(String playerId, String ballId) {
		Ball ball = balls.get(ballId);
		ConnectionManager.broadcast(new BallControlDTO(ballId, playerId, true));
		ballPerPlayer.remove(ballId);
		Log.d(BallManager.class.getName(), "removi a bola :" + ballId);
		if (PlayerManager.playerId.equals(playerId))
			numBallsInside--;
		Log.d(BallManager.class.getName(),
				"Decremented ball in ballFell, number is now " + numBallsInside);
		Player player = PlayerManager.getPlayerById(playerId);
		player.score();
		removeBall(ballId);

		NotificationManager.notifyListeners(EventType.BALL_LOST_EVENT,
				BallManager.class, ball);

		if (playerId.equals(PlayerManager.playerId)) {
			PlayerScoreDTO dto = new PlayerScoreDTO(player.getPlayerId(),
					ball.getId(), false);
			ConnectionManager.broadcast(dto);
		}
	}

	public static void updateBallPlayer(String player, String id, boolean b) {
		if (!b)
			ballPerPlayer.put(id, player);
		else
			ballPerPlayer.remove(id);
		Log.d(BallManager.class.getName(), "Coloquei Bola " + id
				+ " no player " + player);

	}

	public static String getBallsWithinPlayer(String string) {
		// TODO Auto-generated method stub
		int i = 0;
		for (String s : ballPerPlayer.values()) {
			if (string.equals(s))
				i++;
		}
		return "" + i;
	}

	public static void cleanBallPerPlayer() {
		ballPerPlayer.clear();
		Log.d(BallManager.class.getName(), "cleared ballperplayer");

	}

	public static String getPlayerForBall(String id) {
		return ballPerPlayer.get(id);
	}
}
