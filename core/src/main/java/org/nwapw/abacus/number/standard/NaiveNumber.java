package org.nwapw.abacus.number.standard;

import org.nwapw.abacus.number.NumberInterface;

/**
 * An implementation of NumberInterface using a double.
 */
public class NaiveNumber extends NumberInterface {

    /**
     * The number zero.
     */
    public static final NaiveNumber ZERO = new NaiveNumber(0);
    /**
     * The number one.
     */
    public static final NaiveNumber ONE = new NaiveNumber(1);
    /**
     * The value of this number.
     */
    private double value;

    /**
     * Creates a new NaiveNumber with the given string.
     *
     * @param value the value, which will be parsed as a double.
     */
    public NaiveNumber(String value) {
        this(Double.parseDouble(value));
    }

    /**
     * Creates a new NaiveNumber with the given value.
     *
     * @param value the value to use.
     */
    public NaiveNumber(double value) {
        this.value = value;
    }

    @Override
    public int getMaxPrecision() {
        return 18;
    }

    @Override
    public NumberInterface multiplyInternal(NumberInterface multiplier) {
        return new NaiveNumber(value * ((NaiveNumber) multiplier).value);
    }

    @Override
    public NumberInterface divideInternal(NumberInterface divisor) {
        return new NaiveNumber(value / ((NaiveNumber) divisor).value);
    }

    @Override
    public NumberInterface addInternal(NumberInterface summand) {
        return new NaiveNumber(value + ((NaiveNumber) summand).value);
    }

    @Override
    public NumberInterface subtractInternal(NumberInterface subtrahend) {
        return new NaiveNumber(value - ((NaiveNumber) subtrahend).value);
    }

    @Override
    public NumberInterface negateInternal() {
        return new NaiveNumber(-value);
    }

    @Override
    public NumberInterface intPowInternal(int exponent) {
        if (exponent == 0) {
            return NaiveNumber.ONE;
        }
        boolean takeReciprocal = exponent < 0;
        exponent = Math.abs(exponent);
        NumberInterface power = this;
        for (int currentExponent = 1; currentExponent < exponent; currentExponent++) {
            power = power.multiply(this);
        }
        if (takeReciprocal) {
            power = NaiveNumber.ONE.divide(power);
        }
        return power;
    }

    @Override
    public int compareTo(NumberInterface number) {
        NaiveNumber num = (NaiveNumber) number;
        return Double.compare(value, num.value);
    }

    @Override
    public int signum() {
        return this.compareTo(ZERO);
    }

    @Override
    public NumberInterface ceilingInternal() {
        return new NaiveNumber(Math.ceil(value));
    }

    @Override
    public NumberInterface floorInternal() {
        return new NaiveNumber(Math.floor(value));
    }

    @Override
    public NumberInterface fractionalPartInternal() {
        return new NaiveNumber(value - Math.floor(value));
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    public String toString() {
        double shiftBy = Math.pow(10, 10);
        return Double.toString(Math.round(value * shiftBy) / shiftBy);
    }

    @Override
    public NumberInterface getMaxError() {
        return new NaiveNumber(Math.pow(10, -18));
    }
}

