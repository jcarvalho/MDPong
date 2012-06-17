package pt.ist.cm.util.events;

public interface Closure<T extends Listener, E> {

	public void execute(T target, E event, Class<?> sender);

}
