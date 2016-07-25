package eu.assault2142.hololol.chess.client.game.ai.util;

/**
 * A simple, high-performance Stack. Does NOT perform any checks if the action
 * is possible!
 *
 * @author hololol2
 *
 * @param <T> the data the Stack holds
 */
public class Stack<T> {

    private ListElement<T> first;

    /**
     * Pushes a new element onto the stack
     *
     * @param t the element
     */
    public void push(T t) {
        first = new ListElement<>(t, first);
    }

    /**
     * Removes and returns the first element
     *
     * @return the first element
     */
    public T pop() {
        ListElement<T> e = first;
        first = e.next;
        return e.data;
    }

    /**
     * Clears the cache
     */
    public void clear() {
        first = null;
    }

    /**
     * Checks if the stack is empty
     *
     * @return true if the stack contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }
}
