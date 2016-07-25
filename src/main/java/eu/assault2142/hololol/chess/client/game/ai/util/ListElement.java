package eu.assault2142.hololol.chess.client.game.ai.util;

/**
 * Holds a data-element of a List
 *
 * @author hololol2
 *
 * @param <T> the data-element
 */
public class ListElement<T> {

    protected ListElement<T> next;
    protected T data;

    /**
     * Creates a new StackElement
     *
     * @param t the data to store
     */
    public ListElement(T t) {
        data = t;
    }

    /**
     * Creates a new StackElement
     *
     * @param t the data to store
     * @param n the next StackElement
     */
    public ListElement(T t, ListElement<T> n) {
        data = t;
        next = n;
    }

    /**
     * Removes the data at the given position
     *
     * @param pos the "remaining" position
     * @return the data
     */
    public T remove(int pos) {
        if (pos == 1) {
            T data = next.data;
            next = next.next;
            return data;
        } else {
            return next.remove(--pos);
        }
    }

    /**
     * Returns the data at the given position
     *
     * @param pos the "remaining" position
     * @return the data
     */
    public T get(int pos) {
        if (pos == 0) {
            return data;
        } else {
            return next.get(--pos);
        }
    }
}
