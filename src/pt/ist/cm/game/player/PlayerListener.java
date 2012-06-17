package pt.ist.cm.game.player;

import pt.ist.cm.util.events.Listener;

public interface PlayerListener extends Listener {

	public void playerJoined(Player p);

	public void playerLeft(Player p);

	public void playerScored(Player p);

	public void playerLost(Player p);

	public void playerWon(Player p);

}
