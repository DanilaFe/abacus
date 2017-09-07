package org.nwapw.abacus.exception;

/**
 * Exception thrown when the computation is interrupted by
 * the user.
 */
public class ComputationInterruptedException extends AbacusException {

    /**
     * Creates a new exception of this type.
     */
    public ComputationInterruptedException() {
        super("Computation interrupted", null);
    }

}
