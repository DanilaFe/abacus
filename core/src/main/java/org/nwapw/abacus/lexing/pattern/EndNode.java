package org.nwapw.abacus.lexing.pattern;

/**
 * A node that represents a successful match.
 *
 * @param <T> the type that's used to tell which pattern this node belongs to.
 */
public class EndNode<T> extends PatternNode<T> {

    /**
     * The ID of the pattenr that has been matched.
     */
    private T patternId;

    /**
     * Creates a new end node with the given ID.
     *
     * @param patternId the pattern ID.
     */
    public EndNode(T patternId) {
        this.patternId = patternId;
    }

    /**
     * Gets the pattern ID.
     *
     * @return the pattern ID.
     */
    public T getPatternId() {
        return patternId;
    }

}
