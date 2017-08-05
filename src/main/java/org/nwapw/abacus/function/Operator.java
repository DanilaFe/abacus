package org.nwapw.abacus.function;

/**
 * A class that represents a single infix operator.
 */
public class Operator {

    /**
     * The associativity of the operator.
     */
    private OperatorAssociativity associativity;
    /**
     * The type of this operator.
     */
    private OperatorType type;
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
     *
     * @param associativity the associativity of the operator.
     * @param operatorType  the type of this operator, like binary infix or unary postfix.
     * @param precedence    the precedence of the operator.
     * @param function      the function that the operator calls.
     */
    public Operator(OperatorAssociativity associativity, OperatorType operatorType, int precedence, Function function) {
        this.associativity = associativity;
        this.type = operatorType;
        this.precedence = precedence;
        this.function = function;
    }

    /**
     * Gets the operator's associativity.
     *
     * @return the associativity.
     */
    public OperatorAssociativity getAssociativity() {
        return associativity;
    }

    /**
     * Gets the operator's type.
     *
     * @return the type.
     */
    public OperatorType getType() {
        return type;
    }

    /**
     * Gets the operator's precedence.
     *
     * @return the precedence.
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * Gets the operator's function.
     *
     * @return the function.
     */
    public Function getFunction() {
        return function;
    }

}
