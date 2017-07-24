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
    public Number zero() {
        return new NaiveNumber(0);
    }

    @Override
    public Number one() {
        return new NaiveNumber(1);
    }

    @Override
    public Number valueOf(int val) {
        return new NaiveNumber(value);
    }

    @Override
    public Number valueOf(double val) {
        return new NaiveNumber(value);
    }
}
