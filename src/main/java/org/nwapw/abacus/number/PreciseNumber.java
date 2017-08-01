package org.nwapw.abacus.number;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PreciseNumber implements NumberInterface {

    /**
     * The number one.
     */
    static final PreciseNumber ONE = new PreciseNumber(BigDecimal.ONE);
    /**
     * The number zero.
     */
    static final PreciseNumber ZERO = new PreciseNumber(BigDecimal.ZERO);
    /**
     * The number ten.
     */
    static final PreciseNumber TEN = new PreciseNumber(BigDecimal.TEN);

    /**
     * The value of the PreciseNumber.
     */
    BigDecimal value;

    /**
     * Constructs a precise number from the given string.
     *
     * @param string a string representation of the number meeting the same conditions
     *               as the BidDecimal(String) constructor.
     */
    public PreciseNumber(String string) {
        value = new BigDecimal(string);
    }

    /**
     * Constructs a precise number from the given BigDecimal.
     *
     * @param value a BigDecimal object representing the value of the number.
     */
    public PreciseNumber(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int getMaxPrecision() {
        return 65;
    }

    @Override
    public NumberInterface multiply(NumberInterface multiplier) {
        return new PreciseNumber(this.value.multiply(((PreciseNumber) multiplier).value));
    }

    @Override
    public NumberInterface divide(NumberInterface divisor) {
        return new PreciseNumber(value.divide(((PreciseNumber) divisor).value, this.getMaxPrecision(), RoundingMode.HALF_UP));
    }

    @Override
    public NumberInterface add(NumberInterface summand) {
        return new PreciseNumber(value.add(((PreciseNumber) summand).value));
    }

    @Override
    public NumberInterface subtract(NumberInterface subtrahend) {
        return new PreciseNumber(value.subtract(((PreciseNumber) subtrahend).value));
    }

    @Override
    public NumberInterface intPow(int exponent) {
        if (exponent == 0) {
            return PreciseNumber.ONE;
        }
        boolean takeReciprocal = exponent < 0;
        exponent = Math.abs(exponent);
        NumberInterface power = this;
        for (int currentExponent = 1; currentExponent < exponent; currentExponent++) {
            power = power.multiply(this);
        }
        if (takeReciprocal) {
            power = PreciseNumber.ONE.divide(power);
        }
        return power;
    }

    @Override
    public int compareTo(NumberInterface number) {
        return value.compareTo(((PreciseNumber) number).value);
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    public int ceiling() {
        return (int) Math.ceil(value.doubleValue());
    }

    @Override
    public int floor() {
        return (int) Math.floor(value.doubleValue());
    }

    @Override
    public NumberInterface negate() {
        return new PreciseNumber(value.negate());
    }

    @Override
    public NumberInterface promoteTo(Class<? extends NumberInterface> toClass) {
        if (toClass == this.getClass()) {
            return this;
        }
        return null;
    }

    @Override
    public String toString() {
        BigDecimal rounded = value.setScale(getMaxPrecision() - 15, RoundingMode.HALF_UP);
        return rounded.stripTrailingZeros().toPlainString();
    }
}
