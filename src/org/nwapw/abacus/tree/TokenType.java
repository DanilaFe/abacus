package org.nwapw.abacus.tree;

public enum TokenType {

    ANY(0), OP(1), NUM(2), WORD(3), OPEN_PARENTH(4), CLOSE_PARENTH(5);

    public final int priority;

    TokenType(int priority){
        this.priority = priority;
    }

}
