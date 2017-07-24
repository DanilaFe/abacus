package org.nwapw.abacus.number;

public class NaiveNumber implements Number {

    private double value;

    public NaiveNumber(double value) {
        this.value = value;
    }

    @Override
    public int precision() {
        return 4;
    }

    @Override
    public Number multiply(Number multiplier) {
        if(!(multiplier instanceof NaiveNumber)) throw new IllegalArgumentException();
        return new NaiveNumber(value * ((NaiveNumber)multiplier).value);
    }

    @Override
    public Number divide(Number divisor) {
        if(!(divisor instanceof NaiveNumber)) throw new IllegalArgumentException();
        return new NaiveNumber(value * ((NaiveNumber)divisor).value);
    }

    @Override
    public Number add(Number summand) {
        if(!(summand instanceof NaiveNumber)) throw new IllegalArgumentException();
        return new NaiveNumber(value * ((NaiveNumber)summand).value);
    }

    @Override
    public Number subtract(Number subtrahend) {
        if(!(subtrahend instanceof NaiveNumber)) throw new IllegalArgumentException();
        return new NaiveNumber(value * ((NaiveNumber)subtrahend).value);
    }

    @Override
    public Number negate() {
        return new NaiveNumber(-value);
    }
}
