package pt.ist.cm.game;

import pt.ist.cm.game.dto.BallControlDTO;
import pt.ist.cm.game.dto.BallDTO;
import pt.ist.cm.game.dto.PlayerJoinRequest;
import pt.ist.cm.game.dto.PlayerJoinedDTO;
import pt.ist.cm.game.dto.PlayerLeftDTO;
import pt.ist.cm.game.dto.PlayerScoreDTO;
import pt.ist.cm.game.dto.SystemStateDTO;
import pt.ist.cm.game.player.Player;
import pt.ist.cm.util.events.Listener;

public interface NetworkEventListener extends Listener {

	public void systemState(SystemStateDTO dto);

	public void receivedString(String string);

	public void ballMoved(BallDTO ballDTO);

	public void playerJoined(PlayerJoinedDTO dto);

	public void playerDisconnected(Player player);

	public void playerJoinRequest(PlayerJoinRequest dto);

	public void newBall(BallDTO ballDTO);

	public void runOnUiThread(Runnable runnable);

	public void playerScored(PlayerScoreDTO playerScoreDTO);

	public void ballControl(BallControlDTO ballControlDTO);

	public void playerLeft(PlayerLeftDTO playerLeftDTO);

}
