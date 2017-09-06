package org.nwapw.abacus.function;

/**
 * Exception thrown if the function parameters do not match
 * requirements.
 */
public class DomainException extends RuntimeException {

    /**
     * Creates a new DomainException.
     * @param reason the reason for which the exception is thrown.
     */
    public DomainException(String reason) {
        super(reason);
    }

    /**
     * Creates a new DomainException with a default message.
     */
    public DomainException(){
        this("Domain Error");
    }

}
