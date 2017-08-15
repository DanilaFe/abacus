package org.nwapw.abacus.number;

/**
 * Exception thrown when the computation is interrupted by
 * the user.
 */
public class ComputationInterruptedException extends RuntimeException {

    /**
     * Creates a new exception of this type.
     */
    public ComputationInterruptedException() {
        super("Computation interrupted by user.");
    }

}
