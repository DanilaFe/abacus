package org.nwapw.abacus.number;

public class NaiveNumber implements Number {

    private double value;

    public NaiveNumber(double value) {
        this.value = value;
    }

    static final NaiveNumber ZERO = new NaiveNumber(0);
    static final NaiveNumber ONE = new NaiveNumber(1);

    @Override
    public int precision() {
        return 4;
    }

    @Override
    public Number multiply(Number multiplier) {
        return new NaiveNumber(value * ((NaiveNumber)multiplier).value);
    }

    @Override
    public Number divide(Number divisor) {
        return new NaiveNumber(value / ((NaiveNumber)divisor).value);
    }

    @Override
    public Number add(Number summand) {
        return new NaiveNumber(value + ((NaiveNumber)summand).value);
    }

    @Override
    public Number subtract(Number subtrahend) {
        return new NaiveNumber(value - ((NaiveNumber)subtrahend).value);
    }

    @Override
    public Number negate() {
        return new NaiveNumber(-value);
    }

    @Override
    public int compareTo(Number number) {
        NaiveNumber num = (NaiveNumber) number;
        return Double.compare(value, num.value);
    }

    @Override
    public int signum() {
        return this.compareTo(ZERO);
    }

    @Override
    public Number promoteTo(Class<? extends Number> toClass) {
        if(toClass == this.getClass()) return this;
        return null;
    }

}

