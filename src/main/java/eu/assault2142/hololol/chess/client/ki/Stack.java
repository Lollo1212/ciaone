package eu.assault2142.hololol.chess.client.ki;

/**
 * A simple, high-performance Stack
 * @author Johannes Wirth
 *
 * @param <T> the data the Stack holds
 */
public class Stack<T> {
	private StackElement<T> first;
	
	/**
	 * Pushes a new element onto the stack
	 * @param t the element
	 */
	public void push(T t){
		if(first==null){
			first = new StackElement(t);
		}else{
			first = new StackElement(t,first);
		}
	}
	
	/**
	 * Removes and returns the first element
	 * @return the first element
	 */
	public T pop(){
		StackElement<T> e = first;
		first = e.next;
		return e.data;
	}
	
	/**
	 * Clears the cache
	 */
	public void clear(){
		first = null;
	}
	
	/**
	 * Checks if the stack is empty
	 * @return true if the stack contains no elements, false otherwise
	 */
	public boolean isEmpty(){
		return first==null;
	}
}
