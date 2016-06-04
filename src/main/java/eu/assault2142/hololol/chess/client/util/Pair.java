/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.client.util;

/**
 * A Pair
 *
 * @author hololol2
 * @param <T> the first element
 * @param <S> the second element
 */
public class Pair<T, S> {

    private final T left;
    private final S right;

    public Pair(T left, S right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public S getRight() {
        return right;
    }

}
