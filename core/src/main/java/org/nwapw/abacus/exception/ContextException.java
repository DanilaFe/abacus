package org.nwapw.abacus.exception;

/**
 * Exception thrown by the Context in cases where lookup fails
 * where it should not.
 */
public class ContextException extends AbacusException {

    /**
     * Creates a new ContextException without an extra message.
     */
    public ContextException() {
        this("");
    }

    /**
     * Creates a ContextException with the given message.
     * @param message the message to use.
     */
    public ContextException(String message){
        super("Context exception", message);
    }

}
