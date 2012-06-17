package pt.ist.cm.game.player;

import pt.ist.cm.game.GameActivity;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PlayerView extends View implements PlayerListener {

	private enum GameState {
		STARTED, LOST, WON, GAME_OVER;
	}

	private final String LOSE_TEXT = "You lost! :(";
	private final String WON_TEXT = "You won! :)";
	private final String GAME_OVER_TEXT = "Game Over!";
	private final String PLAYER_WON = "Player & won!";

	private final float loseTextSize;
	private final float wonTextSize;
	private final float gameOverTextSize;
	private final float playerWonSize;

	private String winner = "";

	// Current player
	private final Player player;
	private String[] allScores;

	private final Paint green = new Paint();
	private final Paint bigGreen = new Paint();
	private final Paint red = new Paint();

	private GameState state = GameState.STARTED;

	public PlayerView(Context context) {
		super(context);
		green.setColor(Color.GREEN);
		green.setTextSize(15);
		red.setColor(Color.RED);
		red.setTextSize(30);

		bigGreen.setColor(Color.GREEN);
		bigGreen.setTextSize(30);

		loseTextSize = red.measureText(LOSE_TEXT);
		wonTextSize = bigGreen.measureText(WON_TEXT);
		gameOverTextSize = red.measureText(GAME_OVER_TEXT);
		playerWonSize = red.measureText(PLAYER_WON);

		this.player = new Player(PlayerManager.playerId);

		allScores = PlayerManager.getAllScores();

		NotificationManager.addListenerForEvent(this, EventType.PLAYER_SCORED);
		NotificationManager.addListenerForEvent(this, EventType.PLAYER_LOST);
		NotificationManager.addListenerForEvent(this, EventType.PLAYER_WON);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawScores(canvas);
		drawGameResult(canvas);
	}

	private void drawGameResult(Canvas canvas) {
		if (state == GameState.LOST)
			canvas.drawText(LOSE_TEXT, (GameActivity.screenWidth / 2)
					- (loseTextSize / 2), GameActivity.screenHeight / 2, red);
		else if (state == GameState.WON) {
			canvas.drawText(WON_TEXT, (GameActivity.screenWidth / 2)
					- (wonTextSize / 2), GameActivity.screenHeight / 2,
					bigGreen);
		} else if (state == GameState.GAME_OVER) {
			canvas.drawText(GAME_OVER_TEXT, (GameActivity.screenWidth / 2)
					- (gameOverTextSize / 2), GameActivity.screenHeight / 2,
					red);
			canvas.drawText(PLAYER_WON.replace("&", winner),
					(GameActivity.screenWidth / 2) - (playerWonSize / 2),
					(GameActivity.screenHeight / 2) + 30, red);
		}
	}

	private void drawScores(Canvas canvas) {
		for (int i = 0; i < allScores.length; i++) {
			String msg = allScores[i];
			canvas.drawText(msg, GameActivity.screenWidth - 100, 20 + i * 20,
					green);
		}
	}

	public void playerJoined(Player p) {

	}

	public void playerLeft(Player p) {

	}

	public void playerScored(Player p) {

		allScores = PlayerManager.getAllScores();

		this.invalidate();
	}

	public void playerLost(Player p) {
		if (p.getPlayerId().equals(this.player.getPlayerId()))
			state = GameState.LOST;

		allScores = PlayerManager.getAllScores();

		this.invalidate();
	}

	public void playerWon(Player p) {
		if (p.getPlayerId().equals(this.player.getPlayerId()))
			state = GameState.WON;
		else {
			state = GameState.GAME_OVER;
			winner = p.getPlayerId();
		}
		allScores = PlayerManager.getAllScores();
		this.invalidate();
	}

}
