package eu.assault2142.hololol.chess.server.util.loginqueue;

/**
 *
 * @author hololol2
 */
public abstract class ListElement {

    public abstract Node addLast(DataElement d);

    public abstract DataElement getData();

    public abstract ListElement getNext();

    public abstract int size();

    public abstract void setNext(ListElement l);

    public abstract boolean isEmpty();
}
