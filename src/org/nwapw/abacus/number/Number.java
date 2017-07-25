package org.nwapw.abacus.number;

public interface Number {

    int precision();
    Number multiply(Number multiplier);
    Number divide(Number divisor);
    Number add(Number summand);
    Number subtract(Number subtrahend);
    Number negate();
    Number intPow(int exponent);
    int compareTo(Number number);
    int signum();

    Number promoteTo(Class<? extends Number> toClass);

}
