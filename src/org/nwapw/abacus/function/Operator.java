package org.nwapw.abacus.function;

/**
 * A class that represents a single infix operator.
 */
public abstract class Operator {

    /**
     * The associativity of the operator.
     */
    private OperatorAssociativity associativity;
    /**
     * The precedence of the operator.
     */
    private int precedence;
    /**
     * The function that is called by this operator.
     */
    private Function function;

    /**
     * Creates a new operator with the given parameters.
     * @param associativity the associativity of the operator.
     * @param precedence the precedence of the operator.
     * @param function the function that the operator calls.
     */
    public Operator(OperatorAssociativity associativity, int precedence, Function function){
        this.associativity = associativity;
        this.precedence = precedence;
        this.function = function;
    }

    /**
     * Gets the operator's associativity.
     * @return the associativity.
     */
    public OperatorAssociativity getAssociativity() {
        return associativity;
    }

    /**
     * Gets the operator's precedence.
     * @return the precedence.
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * Gets the operator's function.
     * @return the function.
     */
    public Function getFunction() {
        return function;
    }

}
