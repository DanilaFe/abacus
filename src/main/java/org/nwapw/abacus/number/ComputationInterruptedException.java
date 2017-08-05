package org.nwapw.abacus.number;

public class ComputationInterruptedException extends RuntimeException {

    public ComputationInterruptedException(){
        super("Computation interrupted by user.");
    }

}
