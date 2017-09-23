package org.nwapw.abacus.exception;

/**
 * Exception thrown by Lexers when they are unable to tokenize the input string.
 */
public class TokenizeException extends AbacusException {

    /**
     * Create a new tokenize exception with no additional data.
     */
    public TokenizeException() {
        this("");
    }

    /**
     * Create a new tokenize exception with the given message.
     * @param message the message to use.
     */
    public TokenizeException(String message){
        super("Failed to tokenize string", message);
    }

}
