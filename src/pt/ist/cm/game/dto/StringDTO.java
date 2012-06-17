package pt.ist.cm.game.dto;

import pt.ist.cm.game.NetworkEventListener;

public class StringDTO extends MDPongDTO {

	private final String string;

	public StringDTO(String string) {
		super();
		this.string = string;
	}

	@Override
	public void visit(NetworkEventListener listener) {
		listener.receivedString(string);
	}
}
