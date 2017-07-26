package org.nwapw.abacus.tree;

/**
 * Enum to represent the type of the token that has been matched
 * by the lexer.
 */
public enum TokenType {

    ANY(0), OP(1), NUM(2), WORD(3), OPEN_PARENTH(4), CLOSE_PARENTH(5);

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
