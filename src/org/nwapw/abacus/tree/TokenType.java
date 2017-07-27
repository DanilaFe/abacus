package org.nwapw.abacus.tree;

/**
 * Enum to represent the type of the token that has been matched
 * by the lexer.
 */
public enum TokenType {

    INTERNAL_FUNCTION_END(-1),
    ANY(0), COMMA(1), OP(2), NUM(3), FUNCTION(4), OPEN_PARENTH(5), CLOSE_PARENTH(6);

    /**
     * The priority by which this token gets sorted.
     */
    public final int priority;

    /**
     * Creates a new token type with the given priority.
     * @param priority the priority of this token type.
     */
    TokenType(int priority){
        this.priority = priority;
    }

}
