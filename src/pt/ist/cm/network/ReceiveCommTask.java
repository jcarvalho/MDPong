package pt.ist.cm.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import pt.ist.cm.game.dto.MDPongDTO;
import pt.ist.cm.game.dto.PlayerJoinRequest;
import pt.ist.cm.game.dto.StringDTO;
import pt.ist.cm.util.SerializationTool;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.util.Log;

public class ReceiveCommTask implements Runnable {

	private final Socket socket;
	private BufferedReader reader;

	public ReceiveCommTask(Socket socket) {
		this.socket = socket;
	}

	public void run() {

		Log.d(this.getClass().getSimpleName(), "Incoming connection from "
				+ socket);

		try {

			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()), 1024);

			// Main loop

			String st = "";
			while ((st = reader.readLine()) != null) {
				if (st.equalsIgnoreCase("quit"))
					return;
				onProgressUpdate(st);
			}

		} catch (IOException e) {
			Log.e(this.getClass().getName(),
					"Error: Error while receiving information from socket!" + e);
		} finally {
			if (!socket.isClosed())
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	protected void onProgressUpdate(String value) {
		Log.d(this.getClass().getSimpleName(), "Receiving: " + value);

		MDPongDTO dto;

		try {
			dto = SerializationTool.getObjectFromString(value);

			// HACK
			if (dto instanceof PlayerJoinRequest) {
				((PlayerJoinRequest) dto).setPlayerIp(socket.getInetAddress()
						.getHostAddress());
			}
		} catch (Exception e) {
			dto = new StringDTO(value);
		}

		NotificationManager.notifyListeners(EventType.NETWORK_EVENT,
				this.getClass(), dto);
	}

}
