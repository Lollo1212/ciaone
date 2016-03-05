package eu.assault2142.hololol.chess.client.ki;

/**
 * Holds a data-element of the Stack
 * @author Johannes Wirth
 *
 * @param <T> the data-element
 */
public class StackElement<T> {
	protected StackElement<T> next;
	protected T data;
	/**
	 * Creates a new StackElement
	 * @param t the data to store
	 */
	public StackElement(T t){
		data = t;
	}
	/**
	 * Creates a new StackElement
	 * @param t the data to store
	 * @param n the next StackElement
	 */
	public StackElement(T t,StackElement<T>n){
		data = t;
		next = n;
	}
}
