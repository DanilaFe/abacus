package org.nwapw.abacus.number;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * A number that uses a BigDecimal to store its value,
 * leading to infinite possible precision.
 */
public class PreciseNumber extends NumberInterface {

    /**
     * The number one.
     */
    public static final PreciseNumber ONE = new PreciseNumber(BigDecimal.ONE);
    /**
     * The number zero.
     */
    public static final PreciseNumber ZERO = new PreciseNumber(BigDecimal.ZERO);
    /**
     * The number ten.
     */
    public static final PreciseNumber TEN = new PreciseNumber(BigDecimal.TEN);

    /**
     * The number of extra significant figures kept in calculations before rounding for output.
     */
    private static int numExtraInternalSigFigs = 15;

    /**
     * MathContext that is used when rounding a number prior to output.
     */
    private static MathContext outputContext = new MathContext(50);

    /**
     * MathContext that is actually used in calculations.
     */
    private static MathContext internalContext = new MathContext(outputContext.getPrecision() + numExtraInternalSigFigs);

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
        return internalContext.getPrecision();
    }

    @Override
    public NumberInterface multiplyInternal(NumberInterface multiplier) {
        return new PreciseNumber(this.value.multiply(((PreciseNumber) multiplier).value));
    }

    @Override
    public NumberInterface divideInternal(NumberInterface divisor) {
        return new PreciseNumber(value.divide(((PreciseNumber) divisor).value, internalContext));
    }

    @Override
    public NumberInterface addInternal(NumberInterface summand) {
        return new PreciseNumber(value.add(((PreciseNumber) summand).value));
    }

    @Override
    public NumberInterface subtractInternal(NumberInterface subtrahend) {
        return new PreciseNumber(value.subtract(((PreciseNumber) subtrahend).value));
    }

    @Override
    public NumberInterface intPowInternal(int exponent) {
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
    public NumberInterface ceilingInternal() {
        String str = value.toPlainString();
        int decimalIndex = str.indexOf('.');
        if (decimalIndex != -1) {
            return this.floor().add(ONE);
        }
        return this;
    }

    @Override
    public NumberInterface floorInternal() {
        String str = value.toPlainString();
        int decimalIndex = str.indexOf('.');
        if (decimalIndex != -1) {
            NumberInterface floor = new PreciseNumber(str.substring(0, decimalIndex));
            if (signum() == -1) {
                floor = floor.subtract(ONE);
            }
            return floor;
        }
        return this;
    }

    @Override
    public NumberInterface fractionalPartInternal() {
        return this.subtractInternal(floorInternal());
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public NumberInterface negateInternal() {
        return new PreciseNumber(value.negate());
    }

    @Override
    public String toString() {
        return value.round(outputContext).toString();
    }

    @Override
    public NumberInterface getMaxError() {
        return new PreciseNumber(value.ulp()).multiplyInternal(TEN.intPowInternal(value.precision() - internalContext.getPrecision()));
    }
}
