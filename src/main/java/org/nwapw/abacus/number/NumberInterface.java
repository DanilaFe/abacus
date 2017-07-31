package org.nwapw.abacus.number;

/**
 * An interface used to represent a number.
 */
public interface NumberInterface {

    /**
     * The maximum precision to which this number operates.
     *
     * @return the precision.
     */
    int getMaxPrecision();

    /**
     * Multiplies this number by another, returning
     * a new number instance.
     *
     * @param multiplier the multiplier
     * @return the result of the multiplication.
     */
    NumberInterface multiply(NumberInterface multiplier);

    /**
     * Divides this number by another, returning
     * a new number instance.
     *
     * @param divisor the divisor
     * @return the result of the division.
     */
    NumberInterface divide(NumberInterface divisor);

    /**
     * Adds this number to another, returning
     * a new number instance.
     *
     * @param summand the summand
     * @return the result of the summation.
     */
    NumberInterface add(NumberInterface summand);

    /**
     * Subtracts another number from this number,
     * a new number instance.
     *
     * @param subtrahend the subtrahend.
     * @return the result of the subtraction.
     */
    NumberInterface subtract(NumberInterface subtrahend);

    /**
     * Returns a new instance of this number with
     * the sign flipped.
     *
     * @return the new instance.
     */
    NumberInterface negate();

    /**
     * Raises this number to an integer power.
     *
     * @param exponent the exponent to which to take the number.
     * @return the resulting value.
     */
    NumberInterface intPow(int exponent);

    /**
     * Compares this number to another.
     *
     * @param number the number to compare to.
     * @return same as Integer.compare();
     */
    int compareTo(NumberInterface number);

    /**
     * Same as Math.signum().
     *
     * @return 1 if this number is positive, -1 if this number is negative, 0 if this number is 0.
     */
    int signum();

    /**
     * Promotes this class to another number class.
     *
     * @param toClass the class to promote to.
     * @return the resulting new instance.
     */
    NumberInterface promoteTo(Class<? extends NumberInterface> toClass);

}
