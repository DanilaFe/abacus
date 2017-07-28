package org.nwapw.abacus.number;

/**
 * An implementation of NumberInterface using a double.
 */
public class NaiveNumber implements NumberInterface {

    /**
     * The value of this number.
     */
    private double value;

    /**
     * Creates a new NaiveNumber with the given value.
     * @param value the value to use.
     */
    public NaiveNumber(double value) {
        this.value = value;
    }

    /**
     * The number zero.
     */
    public static final NaiveNumber ZERO = new NaiveNumber(0);
    /**
     * The number one.
     */
    public static final NaiveNumber ONE = new NaiveNumber(1);

    @Override
    public int precision() {
        return 18;
    }

    @Override
    public NumberInterface multiply(NumberInterface multiplier) {
        return new NaiveNumber(value * ((NaiveNumber)multiplier).value);
    }

    @Override
    public NumberInterface divide(NumberInterface divisor) {
        return new NaiveNumber(value / ((NaiveNumber)divisor).value);
    }

    @Override
    public NumberInterface add(NumberInterface summand) {
        return new NaiveNumber(value + ((NaiveNumber)summand).value);
    }

    @Override
    public NumberInterface subtract(NumberInterface subtrahend) {
        return new NaiveNumber(value - ((NaiveNumber)subtrahend).value);
    }

    @Override
    public NumberInterface negate() {
        return new NaiveNumber(-value);
    }

    @Override
    public NumberInterface intPow(int exponent) {
        if(exponent == 0){
            return NaiveNumber.ONE;
        }
        boolean takeReciprocal = exponent < 0;
        exponent = Math.abs(exponent);
        NumberInterface power = this;
        for(int currentExponent = 1; currentExponent < exponent; currentExponent++){
            power = power.multiply(this);
        }
        if(takeReciprocal){
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
    public NumberInterface promoteTo(Class<? extends NumberInterface> toClass) {
        if(toClass == this.getClass()) return this;
        return null;
    }

    public String toString(){
        double shiftBy = Math.pow(10, 10);
        return Double.toString(Math.round(value * shiftBy) / shiftBy);
    }

}

