package org.nwapw.abacus.lexing.pattern.nodes;

/**
 * A pattern node that matches any character.
 *
 * @param <T> the type that's used to tell which pattern this node belongs to.
 */
public class AnyNode<T> extends PatternNode<T> {

    @Override
    public boolean matches(char other) {
        return true;
    }

}
