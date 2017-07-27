package org.nwapw.abacus.function;

public abstract class Operator {

    private OperatorAssociativity associativity;
    private int precedence;
    private Function function;

    public Operator(OperatorAssociativity associativity, int precedence, Function function){
        this.associativity = associativity;
        this.precedence = precedence;
        this.function = function;
    }

    public OperatorAssociativity getAssociativity() {
        return associativity;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Function getFunction() {
        return function;
    }

}
