package org.nwapw.abacus.parsing.standard;

/**
 * Enum to represent the type of the token that has been matched
 * by the lexer.
 */
public enum TokenType {

    INTERNAL_FUNCTION_END(-1),
    ANY(0), WHITESPACE(1), COMMA(2), VARIABLE(3), OP(4), TREE_VALUE_OP(4),
    NUM(5), FUNCTION(6), TREE_VALUE_FUNCTION(6), OPEN_PARENTH(7), CLOSE_PARENTH(7);

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
