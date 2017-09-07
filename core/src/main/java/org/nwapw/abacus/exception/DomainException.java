package org.nwapw.abacus.exception;

/**
 * Exception thrown if the function parameters do not match
 * requirements.
 */
public class DomainException extends AbacusException {

    /**
     * Creates a new DomainException.
     * @param reason the reason for which the exception is thrown.
     */
    public DomainException(String reason) {
        super("Domain error", reason);
    }

    /**
     * Creates a new DomainException with a default message.
     */
    public DomainException(){
        this(null);
    }

}
