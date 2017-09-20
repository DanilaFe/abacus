package org.nwapw.abacus.exception;

/**
 * Exception thrown when a promotion fails.
 */
public class PromotionException extends AbacusException {

    /**
     * Creates a new PromotionException with the default message
     * and no additional information.
     */
    public PromotionException() {
        this("");
    }

    /**
     * Creates a new PromotionException with the given additional message.
     * @param message the additional message to include with the error.
     */
    public PromotionException(String message) {
        super("Failed to promote number instances", message);
    }

}
