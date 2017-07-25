package org.nwapw.abacus.number;

public class NaiveNumber implements NumberInterface {

    private double value;

    public NaiveNumber(double value) {
        this.value = value;
    }

    public static final NaiveNumber ZERO = new NaiveNumber(0);
    public static final NaiveNumber ONE = new NaiveNumber(1);

    @Override
    public int precision() {
        return 4;
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
        NumberInterface power = this;
        for(int currentExponent = 1; currentExponent <= exponent; currentExponent++){
            power = power.multiply(this);
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
        return Double.toString(value);
    }

}

