package pt.ist.cm.game;

import pt.ist.cm.util.events.Listener;

public interface KeyEventListener extends Listener {

	public void keyPressed(KeyDirection direction, Class<?> sender);

}
