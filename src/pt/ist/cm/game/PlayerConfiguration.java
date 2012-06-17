package pt.ist.cm.game;

import pt.ist.cm.game.ball.Referential;

public class PlayerConfiguration {

	private ScreenInformation[] info;
	private Referential referential;

	public ScreenInformation[] getScreenDefs() {
		return info;
	}

	public ScreenInformation[] getNgb() {
		return info;
	}

	public Referential getReferential() {
		return referential;
	}

	public void setScreenConfiguration(ScreenInformation[] temp, int[] neighbors) {
		info = temp;

	}

	public void setReferential(Referential ref) {
		referential = ref;
	}

}
