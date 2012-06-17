package pt.ist.cm.game.wall;

import java.util.ArrayList;
import java.util.List;

import pt.ist.cm.game.ColisionType;
import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.ball.Vector;
import pt.ist.cm.game.dto.BallDTO;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.game.wall.Wall.WallType;
import pt.ist.cm.hardware.ScreenOrientation;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.util.Log;

public class WallManager {

	private final static List<Wall> walls = new ArrayList<Wall>();
	private final static List<Neighbor> neighbors = new ArrayList<Neighbor>();

	public static void registerWall(Wall wall) {
		walls.add(wall);
		if (wall instanceof Neighbor)
			neighbors.add((Neighbor) wall);
	}

	public static void clear() {
		walls.clear();
		neighbors.clear();
	}

	public static ColisionType collidesWithAnyWall(Vector position) {

		for (Wall wall : walls) {

			ColisionType type = wall.colidesWith(position);
			if (type != ColisionType.NONE)
				return type;
		}

		return ColisionType.NONE;
	}

	public static Wall createWall(WallType type) {
		ScreenOrientation.getCurrentOrientation();

		return new Wall(type);
	}

	public static List<Neighbor> getNeighbors() {
		return neighbors;
	}

	public static void checkBallProximity(BallDTO ballDTO) {

		Vector prevPosition;
		Vector nextPosition;

		String ballOwner = BallManager.getPlayerForBall(ballDTO.getId());
		String curPlayer = PlayerManager.playerId;
		int numPlayers = PlayerManager.getNumPlayers();
		if (ballOwner != null && numPlayers == 3) {
			if ((curPlayer.equals("0") || curPlayer.equals("2"))
					&& !ballOwner.equals("1"))
				return;
		}

		for (Neighbor n : WallManager.getNeighbors()) {
			prevPosition = ballDTO.getPosition();
			// Is ball approaching the neighbor?
			nextPosition = new Vector(ballDTO.getPosition().getX()
					+ ballDTO.getDirection().getX(), ballDTO.getPosition()
					.getY() + ballDTO.getDirection().getY());
			if (!n.isCloserTo(prevPosition, nextPosition))
				continue;

			ColisionType colision = n.ballApproachingNeighbor(ballDTO
					.getPosition());
			if (colision == ColisionType.NEIGHBOR) {
				Log.d("WallManager", "Notifying that ball #" + ballDTO.getId()
						+ " is entering");
				n.setWarning(true);
				NotificationManager.notifyListeners(
						EventType.BALL_ENTERING_EVENT, WallManager.class,
						BallManager.getBall(ballDTO.getId()));
				return;
			}
		}

	}
}
