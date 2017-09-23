package org.nwapw.abacus.exception;

/**
 * Exception thrown by the NumberReducer if something goes wrong when
 * transforming a parse tree into a single value.
 */
public class NumberReducerException extends AbacusException {

    /**
     * Creates a new NumberReducerException with
     * no additional message.
     */
    public NumberReducerException() {
        this("");
    }

    /**
     * Creates a new NumberReducerException with the given message.
     * @param message the message.
     */
    public NumberReducerException(String message) {
        super("Error evaluating expression", message);
    }

}
