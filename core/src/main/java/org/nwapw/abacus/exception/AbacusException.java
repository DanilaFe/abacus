package org.nwapw.abacus.exception;

public class AbacusException extends RuntimeException {

    public AbacusException(String baseMessage, String description){
        super(baseMessage + ((description == null) ? "." : (": " + description)));
    }

}
