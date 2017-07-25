package org.nwapw.abacus.number;

public interface NumberInterface {

    int precision();
    NumberInterface multiply(NumberInterface multiplier);
    NumberInterface divide(NumberInterface divisor);
    NumberInterface add(NumberInterface summand);
    NumberInterface subtract(NumberInterface subtrahend);
    NumberInterface negate();
    NumberInterface intPow(int exponent);
    int compareTo(NumberInterface number);
    int signum();

    NumberInterface promoteTo(Class<? extends NumberInterface> toClass);

}
