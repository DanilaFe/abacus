package org.nwapw.abacus.lexing.pattern;

/**
 * A node that matches a range of characters.
 *
 * @param <T> the type that's used to tell which pattern this node belongs to.
 */
public class RangeNode<T> extends PatternNode<T> {

    /**
     * The bottom bound of the range, inclusive.
     */
    private char from;
    /**
     * The top bound of the range, inclusive.
     */
    private char to;

    /**
     * Creates a new range node from the given range.
     *
     * @param from the bottom bound of the range.
     * @param to   the top bound of hte range.
     */
    public RangeNode(char from, char to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean matches(char other) {
        return other >= from && other <= to;
    }

}
