package org.nwapw.abacus.exception;

/**
 * Exception thrown by parsers.
 */
public class ParseException extends AbacusException {

    /**
     * Creates a new ParseException with no additional message.
     */
    public ParseException(){
        this("");
    }

    /**
     * Creates a new ParseException with the given additional message.
     * @param message the message.
     */
    public ParseException(String message){
        super("Failed to parse string", message);
    }

}
