package pt.ist.cm.game.ball;

import pt.ist.cm.game.GameActivity;
import pt.ist.cm.game.dto.BallDTO;
import pt.ist.cm.network.ConnectionManager;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.os.AsyncTask;

public class BallCreationTask extends AsyncTask<Void, Ball, Void> {

	private final static int BALL_CREATION_INTERVAL = 10;

	@Override
	protected Void doInBackground(Void... params) {

		while (true) {

			if (GameActivity.currentActivity == null)
				break;

			if (BallManager.isRunning()) {
				Ball b = BallManager.createNewBall();

				NotificationManager.notifyListeners(
						EventType.BALL_CREATED_EVENT, this.getClass(), b);

				ConnectionManager.broadcast(new BallDTO(b));

				System.out.println("Creating new ball... " + b.getId());
			} else if (BallManager.isGameOver())
				return null;

			try {
				Thread.sleep(BALL_CREATION_INTERVAL * 9000);
			} catch (InterruptedException e) {
				break;
			}

		}

		return null;
	}

}
