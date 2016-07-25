package eu.assault2142.hololol.chess.client.game.ai.util;

/**
 * A simple, fast LinkedList. Does NOT perform any tests if the action is
 * possible!
 *
 * @author hololol2
 *
 * @param <T> the data-type to be stored
 */
public class LinkedList<T> {

    private ListElement<T> first;
    private int size = 0;

    /**
     * Adds a new Element to the beginning of the list
     *
     * @param t the data
     */
    public void add(T t) {
        first = new ListElement<>(t, first);
        size++;
    }

    /**
     * Resets the list
     */
    public void clear() {
        first = null;
        size = 0;
    }

    /**
     * Returns the size of the list
     *
     * @return the current size
     */
    public int size() {
        return size;
    }

    /**
     * Removes the element at the given position
     *
     * @param pos the position
     * @return the element
     */
    public T remove(int pos) {
        if (pos == 0) {
            T data = first.data;
            first = first.next;
            return data;
        }
        return first.remove(pos);
    }

    /**
     * Returns the element at the given position
     *
     * @param pos the position
     * @return the element
     */
    public T get(int pos) {
        return first.get(pos);
    }

    /**
     * Checks if the list is empty
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() == LinkedList.class) {
            LinkedList list = (LinkedList) other;
            if (size != list.size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!get(i).equals(list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
