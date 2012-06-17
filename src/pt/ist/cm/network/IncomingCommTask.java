package pt.ist.cm.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

public class IncomingCommTask extends AsyncTask<Void, Socket, Void> {

	private static IncomingCommTask instance = null;

	private static final Executor threadPool = Executors
			.newCachedThreadPool(new ThreadFactory() {

				public Thread newThread(final Runnable r) {
					return new Thread() {

						@Override
						public void run() {
							Looper.prepare();

							r.run();
						}

					};
				}
			});

	public static void run() {
		if (instance == null) {
			instance = new IncomingCommTask();
			instance.execute();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		ServerSocket listenSocket = null;

		try {

			listenSocket = new ServerSocket(ConnectionManager.PORT);

			Log.d(this.getClass().getName(), "Now listening on port "
					+ ConnectionManager.PORT);

		} catch (IOException e) {
			Log.e(this.getClass().getName(),
					"Error: Unable to create a server socket on port "
							+ ConnectionManager.PORT);
			return null;
		}

		while (true) {
			try {

				Log.d(this.getClass().getName(),
						"Ready to accept new connections!");
				Socket sock = listenSocket.accept();

				Log.d(this.getClass().getName(), "Connection accepted!");

				publishProgress(sock);

				Log.d(this.getClass().getName(), "Looping");

			} catch (IOException e) {
				Log.e(this.getClass().getName(),
						"Error: Error accepting connection!");
			}
		}
	}

	@Override
	protected void onProgressUpdate(Socket... values) {
		Log.d(this.getClass().getName(), "Attempting to create new task.");
		ReceiveCommTask comm = new ReceiveCommTask(values[0]);
		threadPool.execute(comm);
	}
}
