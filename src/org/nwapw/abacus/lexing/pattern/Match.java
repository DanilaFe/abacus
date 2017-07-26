package org.nwapw.abacus.lexing.pattern;

/**
 * A match that has been generated by the lexer.
 * @param <T> the type used to represent the ID of the pattern this match belongs to.
 */
public class Match<T> {

    /**
     * The bottom range of the string, inclusive.
     */
    private int from;
    /**
     * The top range of the string, exclusive.
     */
    private int to;
    /**
     * The pattern type this match matched.
     */
    private T type;

    /**
     * Creates a new match with the given parameters.
     * @param from the bottom range of the string.
     * @param to the top range of the string.
     * @param type the type of the match.
     */
    public Match(int from, int to, T type){
        this.from = from;
        this.to = to;
        this.type = type;
    }

    /**
     * Gets the bottom range bound of the string.
     * @return the bottom range bound of the string.
     */
    public int getFrom() {
        return from;
    }

    /**
     * Gets the top range bound of the string.
     * @return the top range bound of the string.
     */
    public int getTo() {
        return to;
    }

    /**
     * Gets the pattern type of the node.
     * @return the ID of the pattern that this match matched.
     */
    public T getType() {
        return type;
    }
}