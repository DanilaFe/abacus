package org.nwapw.abacus.lexing.pattern;

/**
 * An interface that transforms a pattern chain into a different pattern chain.
 * @param <T> the type used to identify the nodes in the pattern chain.
 */
public interface Transformation<T> {

    /**
     * Performs the actual transformation.
     * @param from the original chain.
     * @return the resulting chain.
     */
    PatternChain<T> transform(PatternChain<T> from);

}