package pt.ist.cm.game;

import pt.ist.cm.game.ball.Referential;
import pt.ist.cm.game.player.PlayerManager;
import pt.ist.cm.hardware.ScreenOrientation;

public class ScreenConfiguration {
	private static GameConfiguration confs[];

	private static int currentConfig = 0;

	public static void setConfig(int conf) {
		currentConfig = conf;
		PlayerConfiguration temp = confs[currentConfig]
				.getConfigurationForPlayer(PlayerManager.playerId);
		Referential.setInstance(temp.getReferential());
		ScreenInformation[] info = temp.getScreenDefs();
		for (int j = 0; j < 4; j++) {
			if (info[j] == ScreenInformation.PADDLE) {
				ScreenOrientation.setCurrentOrientation(j);
				return;
			}
		}

	}

	public static Referential getNewReferential(String playerId) {
		PlayerConfiguration temp = confs[currentConfig]
				.getConfigurationForPlayer(playerId);
		return temp.getReferential();
	}

	public static int getConfig() {
		return currentConfig;
	}

	public static PlayerConfiguration getPlayerConfig(String playerId) {
		PlayerConfiguration temp = confs[currentConfig]
				.getConfigurationForPlayer(playerId);
		return temp;
	}

	public static GameConfiguration getGeneralConfig() {
		return confs[currentConfig];
	}

	static {
		confs = new GameConfiguration[5];
		confs[0] = new GameConfiguration() {
			{
				this.playerConfigurations = new PlayerConfiguration[1];
				this.playerConfigurations[0] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.WALL, ScreenInformation.WALL,
								ScreenInformation.WALL,
								ScreenInformation.PADDLE }, new int[] { -1, -1,
								-1, -1 });
						this.setReferential(new Referential(0, 0, 320, 480));
					}
				};
			}
		};
		confs[1] = new GameConfiguration() {
			{
				playerConfigurations = new PlayerConfiguration[2];
				playerConfigurations[0] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.PADDLE,
								ScreenInformation.WALL,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(0, 0, 320, 480));
					}
				};
				playerConfigurations[1] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL,
								ScreenInformation.PADDLE,
								ScreenInformation.WALL }, new int[] { 0, -1,
								-1, -1 });
						this.setReferential(new Referential(320, 0, 640, 480));
					}
				};
			}
		};
		confs[2] = new GameConfiguration() {
			{
				playerConfigurations = new PlayerConfiguration[3];
				playerConfigurations[0] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.PADDLE,
								ScreenInformation.WALL,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(0, 0, 320, 480));
					}
				};
				playerConfigurations[1] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.PADDLE,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL }, new int[] { 0, -1, 1,
								-1 });
						this.setReferential(new Referential(320, 0, 640, 480));
					}
				};
				playerConfigurations[2] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL,
								ScreenInformation.PADDLE,
								ScreenInformation.WALL }, new int[] { 1, -1,
								-1, -1 });
						this.setReferential(new Referential(640, 0, 960, 480));
					}
				};
			}
		};
		confs[3] = new GameConfiguration() {
			{
				playerConfigurations = new PlayerConfiguration[4];
				playerConfigurations[0] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.PADDLE,
								ScreenInformation.WALL,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR }, new int[] { -1,
								-1, 1, -1 });
						this.setReferential(new Referential(0, 0, 320, 480));
					}
				};
				playerConfigurations[1] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL,
								ScreenInformation.PADDLE,
								ScreenInformation.NEIGHBOR }, new int[] { -1,
								-1, 1, -1 });
						this.setReferential(new Referential(320, 0, 640, 480));
					}
				};
				playerConfigurations[2] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.PADDLE,
								ScreenInformation.WALL }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(320, 480, 640, 960));
					}
				};
				playerConfigurations[3] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.PADDLE,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(0, 480, 320, 960));
					}
				};
			}
		};
		confs[4] = new GameConfiguration() {
			{
				playerConfigurations = new PlayerConfiguration[6];
				playerConfigurations[0] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.PADDLE,
								ScreenInformation.WALL,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR }, new int[] { -1,
								-1, 1, -1 });
						this.setReferential(new Referential(0, 0, 320, 480));
					}
				};
				playerConfigurations[1] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.PADDLE,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR }, new int[] { -1,
								-1, 1, -1 });
						this.setReferential(new Referential(320, 0, 640, 480));
					}
				};
				playerConfigurations[2] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL,
								ScreenInformation.PADDLE,
								ScreenInformation.NEIGHBOR }, new int[] { -1,
								-1, 1, -1 });
						this.setReferential(new Referential(640, 0, 960, 480));
					}
				};
				playerConfigurations[3] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.PADDLE,
								ScreenInformation.WALL }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(640, 480, 960, 960));
					}
				};
				playerConfigurations[4] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.PADDLE }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(320, 480, 640, 960));
					}
				};
				playerConfigurations[5] = new PlayerConfiguration() {
					{
						this.setScreenConfiguration(new ScreenInformation[] {
								ScreenInformation.PADDLE,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.NEIGHBOR,
								ScreenInformation.WALL }, new int[] { -1, -1,
								1, -1 });
						this.setReferential(new Referential(0, 480, 320, 960));
					}
				};
			}
		};

	}

}
