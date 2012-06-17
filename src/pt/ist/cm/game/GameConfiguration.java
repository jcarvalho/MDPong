package pt.ist.cm.game;


public abstract class GameConfiguration {

	protected PlayerConfiguration[] playerConfigurations;

	public PlayerConfiguration getConfigurationForPlayer(String id) {
		return playerConfigurations[Integer.parseInt(id)];
	}

}
