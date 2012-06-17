package pt.ist.cm.network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pt.ist.cm.game.ball.BallManager;
import pt.ist.cm.game.dto.MDPongDTO;
import pt.ist.cm.game.dto.PlayerJoinRequest;
import pt.ist.cm.game.dto.SystemStateDTO;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.util.SerializationTool;
import pt.ist.cm.util.events.EventType;
import pt.ist.cm.util.events.NotificationManager;
import android.util.Log;

public class ConnectionManager {

	public static int PORT = 16003;
	public static int masterPort = 16003;

	private static HashMap<String, BufferedWriter> bufferMap = new HashMap<String, BufferedWriter>();
	private static HashMap<String, String> playerToIpMap = new HashMap<String, String>();

	private static String masterURL = null;

	public static SystemStateDTO registerNewPlayer(String playerIpAndPort)
			throws IOException {

		String playerId = PlayerManager.getNewPlayerId();

		SystemStateDTO dto = new SystemStateDTO(playerId, playerIpAndPort,
				new HashMap<String, String>(playerToIpMap),
				BallManager.getBalls());

		connectPlayer(playerId, playerIpAndPort);

		return dto;
	}

	public static void unregisterPlayer(String playerId) {
		bufferMap.remove(playerId);
	}

	public static <T> void broadcast(T object) {
		for (Entry<String, BufferedWriter> writer : bufferMap.entrySet()) {
			if (writer.getKey().equals(PlayerManager.playerId))
				continue;
			writeObject(object, writer.getValue(), writer.getKey());
		}
	}

	// PlayerId == playerId
	public static <T> void sendToPlayer(String playerId, T object) {
		BufferedWriter writer = bufferMap.get(playerId);
		if (writer == null) {
			throw new RuntimeException(
					"Error, trying to send a command to a non-existing player!"
							+ playerId);
		}

		writeObject(object, writer, playerId);
	}

	public static void setMasterURL(String masterURL) {
		ConnectionManager.masterURL = masterURL;
	}

	public static void connectToMaster() throws Exception {
		connectPlayer(PlayerManager.MASTER_PLAYER_ID, masterURL);
		PlayerJoinRequest request = new PlayerJoinRequest(""
				+ ConnectionManager.PORT);
		sendToPlayer(PlayerManager.MASTER_PLAYER_ID, request);
	}

	public static void clear() {
		bufferMap.clear();
		playerToIpMap.clear();
		masterURL = null;
	}

	public static void updateStatus(Map<String, String> currentStatus)
			throws IOException {
		playerToIpMap.clear();
		playerToIpMap.put(PlayerManager.playerId, "127.0.0.1");
		for (Entry<String, String> entry : currentStatus.entrySet()) {
			connectPlayer(entry.getKey(), entry.getValue());
		}
	}

	/*
	 * Private methods
	 */

	private static <T> void writeObject(T object, BufferedWriter writer,
			String player) {
		try {
			String objectRepresentation = SerializationTool
					.getStringFromObject(object);
			Log.d(ConnectionManager.class.getName(), "Writing object: "
					+ objectRepresentation);
			writer.write(objectRepresentation);
			writer.flush();
		} catch (IOException e) {
			Log.d(ConnectionManager.class.getName(), "Player " + player
					+ " has disconnected!");

			NotificationManager.notifyListeners(EventType.PLAYER_DISCONNECTED,
					ConnectionManager.class,
					PlayerManager.getPlayerById(player));
		}
	}

	public static void connectPlayer(String playerId, String URL)
			throws IOException {
		Socket socket;
		if (PlayerManager.isMasterPlayer()) {

			Log.d(ConnectionManager.class.getName(),
					"Connecting to port + url " + URL.split("\\:")[1]
							+ URL.split("\\:")[0]);
			socket = new Socket(URL.split("\\:")[0], Integer.parseInt(URL
					.split("\\:")[1]));
		} else {

			Log.d(ConnectionManager.class.getName(), "Connecting to port "
					+ (masterPort) + " 2222");
			socket = new Socket(URL.split("\\:")[0], Integer.parseInt(URL
					.split("\\:")[1]));
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		bufferMap.put(playerId, writer);
		playerToIpMap.put(playerId, URL);
	}

	public static void broadcastToAllBut(String id, MDPongDTO dto) {
		for (String player : bufferMap.keySet()) {
			if (player.equals(id) || player.equals(PlayerManager.playerId))
				continue;
			writeObject(dto, bufferMap.get(player), player);
		}
	}

	public static void removePlayer(String playerId) {
		try {
			playerToIpMap.remove(playerId);
			bufferMap.get(playerId).close();
			bufferMap.remove(playerId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
