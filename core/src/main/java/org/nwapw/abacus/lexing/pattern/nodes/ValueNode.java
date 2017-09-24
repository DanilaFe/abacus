package org.nwapw.abacus.lexing.pattern.nodes;

/**
 * A node that matches a single value.
 *
 * @param <T> the type that's used to tell which pattern this node belongs to.
 */
public class ValueNode<T> extends PatternNode<T> {

    /**
     * The value this node matches.
     */
    private char value;

    /**
     * Creates a new node that matches the given character.
     *
     * @param value the character value of the node.
     */
    public ValueNode(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(char other) {
        return other == value;
    }

    @Override
    public char range() {
        return value;
    }
}
