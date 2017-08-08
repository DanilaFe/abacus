package org.nwapw.abacus.number;

import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;

import java.math.BigInteger;

public class RationalNumber extends NumberInterface{

    static final RationalNumber ONE = new RationalNumber(BigFraction.ONE);

    /**
     * The value of the number.
     */
    private BigFraction value;

    /**
     * Constructs a new instance with the given value.
     * @param value
     */
    public RationalNumber(BigFraction value){
        this.value = value;
    }

    @Override
    public int getMaxPrecision() {
        return 0;
    }

    @Override
    protected NumberInterface multiplyInternal(NumberInterface multiplier) {
        return new RationalNumber(value.multiply(((RationalNumber)multiplier).value));
    }

    @Override
    protected NumberInterface divideInternal(NumberInterface divisor) {
        return new RationalNumber(value.divide(((RationalNumber)divisor).value));
    }

    @Override
    protected NumberInterface addInternal(NumberInterface summand) {
        return new RationalNumber(value.add(((RationalNumber)summand).value));
    }

    @Override
    protected NumberInterface subtractInternal(NumberInterface subtrahend) {
        return new RationalNumber(value.subtract(((RationalNumber)subtrahend).value));
    }

    @Override
    protected NumberInterface negateInternal() {
        return new RationalNumber(value.negate());
    }

    @Override
    protected NumberInterface intPowInternal(int exponent) {
        return new RationalNumber(value.pow(exponent));
    }

    @Override
    public int compareTo(NumberInterface number) {
        return value.compareTo(((RationalNumber)number).value);
    }

    @Override
    public int signum() {
        return value.getNumerator().signum();
    }

    @Override
    protected NumberInterface ceilingInternal() {
        if(value.getNumeratorAsInt() != 1){
            return floorInternal().add(ONE);
        }
        return this;
    }

    @Override
    protected NumberInterface floorInternal() {
        BigInteger floor = value.bigDecimalValue().toBigInteger();
        if(value.compareTo(BigFraction.ZERO) < 0 && value.getDenominatorAsInt() != 1){
            floor = floor.subtract(BigInteger.ONE);
        }
        return new RationalNumber(new BigFraction(floor));
    }

    @Override
    protected NumberInterface fractionalPartInternal() {
        return this.subtractInternal(floorInternal());
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    protected NumberInterface promoteToInternal(Class<? extends NumberInterface> toClass) {
        return null;
    }

    @Override
    public NumberInterface getMaxError() {
        return toPreciseNumber().getMaxError();
    }

    @Override
    public String toString(){
        return toPreciseNumber().toString();
    }

    PreciseNumber toPreciseNumber(){
        return (PreciseNumber) new PreciseNumber(value.getNumerator()).divideInternal(new PreciseNumber(value.getDenominator()));
    }
}
