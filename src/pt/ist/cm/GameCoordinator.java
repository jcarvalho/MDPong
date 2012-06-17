package pt.ist.cm;

import java.io.IOException;

import pt.ist.cm.game.GameActivity;
import pt.ist.cm.game.NetworkEventListener;
import pt.ist.cm.game.ScreenConfiguration;
import pt.ist.cm.game.ball.Ball;
import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.ball.Referential;
import pt.ist.cm.game.ball.Vector;
import pt.ist.cm.game.dto.BallControlDTO;
import pt.ist.cm.game.dto.BallDTO;
import pt.ist.cm.game.dto.PlayerJoinRequest;
import pt.ist.cm.game.dto.PlayerJoinedDTO;
import pt.ist.cm.game.dto.PlayerLeftDTO;
import pt.ist.cm.game.dto.PlayerScoreDTO;
import pt.ist.cm.game.dto.SystemStateDTO;
import pt.ist.cm.game.player.Player;
import pt.ist.cm.game.player.PlayerListener;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.game.wall.WallManager;
import pt.ist.cm.network.ConnectionManager;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class GameCoordinator implements NetworkEventListener, PlayerListener {

	private final MDPongActivity baseActivity;

	public GameCoordinator(MDPongActivity baseActivity) {
		this.baseActivity = baseActivity;
		NotificationManager.addListenerForEvent(this, EventType.NETWORK_EVENT);
		NotificationManager.addListenerForEvent(this, EventType.PLAYER_SCORED);
		NotificationManager.addListenerForEvent(this, EventType.PLAYER_LOST);
		NotificationManager.addListenerForEvent(this, EventType.PLAYER_WON);
	}

	public void systemState(final SystemStateDTO dto) {

		baseActivity.dismissDialog();

		Log.d(this.getClass().getName(), "Got new game info! " + dto.getId());

		try {

			PlayerManager.addNewPlayer(dto.getId());

			PlayerManager.addNewPlayer(PlayerManager.MASTER_PLAYER_ID);

			PlayerManager.setPlayerId(dto.getId());

			for (String player : dto.getCurrentStatus().keySet()) {
				PlayerManager.addNewPlayer(player);
			}

			// Since status does not include the master nor myself
			ScreenConfiguration.setConfig(dto.getCurrentStatus().size() + 1);

			ConnectionManager.updateStatus(dto.getCurrentStatus());

			BallManager.updateStatus(dto.getBalls());

			baseActivity.startGameActivity();
		} catch (IOException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
			builder.setMessage("Error: cannot connect to peer " + e)
					.setPositiveButton("Dismiss", null).create().show();
		}
	}

	public void receivedString(String string) {

		baseActivity.dismissDialog();

		Log.d(this.getClass().getName(), "Got garbage string: " + string);
	}

	public void ballMoved(BallDTO ballDTO) {

		Vector position = ballDTO.getPosition();

		BallManager.updateMap();

		if (Referential.getInstance().isInReferential(position)) {
			BallManager.takeControlOfBall(ballDTO.getId(),
					ballDTO.getPosition(), ballDTO.getDirection());
		} else {
			WallManager.checkBallProximity(ballDTO);
		}
	}

	public void playerJoined(PlayerJoinedDTO dto) {

		try {

			System.out.println("Player: " + PlayerManager.getNumPlayers());

			BallManager.cleanBallPerPlayer();

			PlayerManager.addNewPlayer(dto.getPlayerId());

			ScreenConfiguration.setConfig(PlayerManager.getNumPlayers() - 1);

			ConnectionManager.connectPlayer(dto.getPlayerId(),
					dto.getPlayerURL());

			restartActivity();

			Toast.makeText(baseActivity,
					"Player " + dto.getPlayerId() + " has joined the game!",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void playerDisconnected(Player player) {
		// TODO Auto-generated method stub
		// TODO Handle player disconnection
	}

	public void playerJoinRequest(PlayerJoinRequest dto) {

		if (!PlayerManager.isMasterPlayer())
			return;

		try {

			SystemStateDTO systemState = ConnectionManager
					.registerNewPlayer(dto.getPlayerIp() + ":" + dto.getPort());

			PlayerManager.addNewPlayer(systemState.getId());

			ConnectionManager.sendToPlayer(systemState.getId(), systemState);

			PlayerJoinedDTO playerJoinedDTO = new PlayerJoinedDTO(
					systemState.getId(), dto.getPlayerIp() + ":"
							+ dto.getPort());

			ConnectionManager.broadcastToAllBut(systemState.getId(),
					playerJoinedDTO);

			ScreenConfiguration
					.setConfig(systemState.getCurrentStatus().size() + 1);

			restartActivity();

		} catch (IOException e) {
			// Do nothing...
		}

	}

	public void newBall(BallDTO ballDTO) {

		Ball ball = new Ball(ballDTO.getId(), ballDTO.getPosition(),
				ballDTO.getDirection());

		NotificationManager.notifyListeners(EventType.BALL_CREATED_EVENT,
				this.getClass(), ball);

	}

	public void runOnUiThread(Runnable runnable) {
		baseActivity.runOnUiThread(runnable);
	}

	public void playerScored(PlayerScoreDTO playerScoreDTO) {

		if (playerScoreDTO.hasWon())
			NotificationManager.notifyListeners(EventType.PLAYER_WON,
					this.getClass(),
					PlayerManager.getPlayerById(playerScoreDTO.getPlayerId()));
		else
			BallManager.ballFell(playerScoreDTO.getPlayerId(),
					playerScoreDTO.getBallId());
	}

	/*
	 * PlayerListener compliance
	 */

	public void playerJoined(Player p) {
		Toast.makeText(baseActivity,
				"Player " + p.getPlayerId() + " has joined the game!",
				Toast.LENGTH_SHORT).show();
	}

	public void playerLeft(Player p) {

	}

	public void playerScored(Player p) {
		Toast.makeText(baseActivity,
				"Player " + p.getPlayerId() + " lost a point!",
				Toast.LENGTH_SHORT).show();
	}

	public void playerLost(Player p) {

		if (PlayerManager.haveIWon()) {
			NotificationManager.notifyListeners(EventType.PLAYER_WON,
					this.getClass(), PlayerManager.getCurrentPlayer());
			ConnectionManager.broadcast(new PlayerScoreDTO(
					PlayerManager.playerId, null, true));
		}

		Toast.makeText(baseActivity,
				"Player " + p.getPlayerId() + " has lost the game!",
				Toast.LENGTH_SHORT).show();
	}

	public void playerWon(Player p) {

		BallManager.gameOver();

		Toast.makeText(baseActivity,
				"Player " + p.getPlayerId() + " has won the game!",
				Toast.LENGTH_SHORT).show();
	}

	public void ballControl(BallControlDTO bdto) {
		Log.d(this.getClass().getName(), "AHAHAH received ball control from "
				+ bdto.getPlayer() + " with id " + bdto.getId());
		BallManager.updateBallPlayer(bdto.getPlayer(), bdto.getId(),
				bdto.toDelete());
	}

	public void playerLeft(PlayerLeftDTO playerLeftDTO) {
		System.out.println("Player " + playerLeftDTO.getPlayerId()
				+ " has left the game!");

		PlayerManager.removePlayer(playerLeftDTO.getPlayerId());

		ConnectionManager.removePlayer(playerLeftDTO.getPlayerId());

		ScreenConfiguration.setConfig(ScreenConfiguration.getConfig() - 1);

		restartActivity();

	}

	private void restartActivity() {
		if (GameActivity.currentActivity != null)
			GameActivity.currentActivity.finish();
		baseActivity.startGameActivity();
	}

}
