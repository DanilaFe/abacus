package org.nwapw.abacus.tree;

/**
 * Enum to represent the type of the token that has been matched
 * by the lexer.
 */
public enum TokenType {

    INTERNAL_FUNCTION_END(-1),
    ANY(0), WHITESPACE(1), COMMA(2), OP(3), NUM(4), FUNCTION(5), OPEN_PARENTH(6), CLOSE_PARENTH(7);

    /**
     * The priority by which this token gets sorted.
     */
    public final int priority;

    /**
     * Creates a new token type with the given priority.
     *
     * @param priority the priority of this token type.
     */
    TokenType(int priority) {
        this.priority = priority;
    }

}
