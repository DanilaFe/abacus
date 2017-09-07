package org.nwapw.abacus.exception;

/**
 * An exception thrown primarily from Tree Value operators and functions,
 * which have to deal with the result of a Reducer as well as the results
 * of Applicable.
 */
public class EvaluationException extends AbacusException {

    /**
     * Creates a new EvaluationException with the default string.
     */
    public EvaluationException() {
        this(null);
    }

    /**
     * Creates a new EvaluationError with the given message string.
     * @param message the message string.
     */
    public EvaluationException(String message) {
        super("Evaluation error", message);
    }

}
