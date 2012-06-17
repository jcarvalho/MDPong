package pt.ist.cm;

import pt.ist.cm.game.GameActivity;
import pt.ist.cm.game.ScreenConfiguration;
import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.hardware.SensorActivity;
import pt.ist.cm.network.ConnectionManager;
import pt.ist.cm.network.IncomingCommTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MDPongActivity extends Activity {

	/** Called when the activity is first created. */

	private Button startGameButton, sensorButton, connectButton;
	private TextView ipText;
	private ProgressDialog dialog;
	private TextView portText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Start Game

		startGameButton = (Button) findViewById(R.id.startGameButton);
		startGameButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				cleanup();

				PlayerManager.setThisAsMaster();

				prepareStartGame();
			}
		});

		// Connect to server

		ipText = (TextView) findViewById(R.id.ipText);
		portText = (TextView) findViewById(R.id.editPort);
		this.connectButton = (Button) findViewById(R.id.connectButton);

		connectButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				cleanup();

				String url = ipText.getText().toString();
				ConnectionManager.PORT = Integer.parseInt(""
						+ portText.getText());
				ConnectionManager.setMasterURL(url + ":"
						+ ConnectionManager.masterPort);

				prepareStartGame();
			}
		});

		// Sensor Test

		this.sensorButton = (Button) findViewById(R.id.sensorButton);
		this.sensorButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent sensorActivityIntent = new Intent(MDPongActivity.this,
						SensorActivity.class);
				startActivity(sensorActivityIntent);
			}
		});

		new GameCoordinator(this);

	}

	private void prepareStartGame() {

		IncomingCommTask.run();

		if (!PlayerManager.isMasterPlayer()) {

			dialog = ProgressDialog.show(this, "", "Connecting to master...",
					true);

			// Do the actual connection to the server in a background thread

			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						ConnectionManager.connectToMaster();

					} catch (final Exception e) {

						runOnUiThread(new Runnable() {

							public void run() {

								dialog.dismiss();
								AlertDialog.Builder builder = new AlertDialog.Builder(
										MDPongActivity.this);
								builder.setMessage(
										"Error while connecting to the server: "
												+ e)
										.setPositiveButton("Dismiss", null)
										.create().show();
							}

						});

					}
					return null;
				}

			}.execute();

		} else {

			Toast.makeText(this, "Starting a new Game!", Toast.LENGTH_LONG)
					.show();

			ScreenConfiguration.setConfig(0);

			startGameActivity();

		}

	}

	public void startGameActivity() {
		Intent gameActivityIntent = new Intent(MDPongActivity.this,
				GameActivity.class);
		startActivity(gameActivityIntent);
	}

	private void cleanup() {
		BallManager.clear();
		PlayerManager.clear();
		ConnectionManager.clear();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void dismissDialog() {
		if (dialog != null)
			dialog.dismiss();
	}
}