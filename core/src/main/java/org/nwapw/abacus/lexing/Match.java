package org.nwapw.abacus.lexing;

/**
 * A match that has been generated by the lexer.
 *
 * @param <T> the type used to represent the ID of the pattern this match belongs to.
 */
public class Match<T> {

    /**
     * The content of this match.
     */
    private String content;
    /**
     * The pattern type this match matched.
     */
    private T type;

    /**
     * Creates a new match with the given parameters.
     *
     * @param content the content of this match.
     * @param type    the type of the match.
     */
    public Match(String content, T type) {
        this.content = content;
        this.type = type;
    }

    /**
     * Gets the content of this match.
     *
     * @return the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the pattern type of the node.
     *
     * @return the ID of the pattern that this match matched.
     */
    public T getType() {
        return type;
    }
}
