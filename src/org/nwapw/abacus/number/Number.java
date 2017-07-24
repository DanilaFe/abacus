package org.nwapw.abacus.number;

public interface Number {

    Number multiply(Number multiplier);
    Number divide(Number divisor);
    Number add(Number summand);
    Number subtract(Number subtrahend);
    Number negate();

}
