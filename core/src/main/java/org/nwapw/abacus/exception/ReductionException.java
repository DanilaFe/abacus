package org.nwapw.abacus.exception;

/**
 * An exception thrown from TreeReducers.
 */
public class ReductionException extends AbacusException {

    /**
     * Creates a new EvaluationException with the default string.
     */
    public ReductionException() {
        this("");
    }

    /**
     * Creates a new EvaluationError with the given message string.
     * @param message the message string.
     */
    public ReductionException(String message) {
        super("Evaluation error", message);
    }

}
