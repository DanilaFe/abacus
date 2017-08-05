package org.nwapw.abacus.number;

/**
 * An interface used to represent a number.
 */
public abstract class NumberInterface {

    /**
     * Check if the thread was interrupted and
     * throw an exception to end the computation.
     */
    private static void checkInterrupted(){
        if(Thread.currentThread().isInterrupted())
            throw new ComputationInterruptedException();
    }
    /**
     * The maximum precision to which this number operates.
     *
     * @return the precision.
     */
    public abstract int getMaxPrecision();

    /**
     * Multiplies this number by another, returning
     * a new number instance.
     *
     * @param multiplier the multiplier
     * @return the result of the multiplication.
     */
    protected abstract NumberInterface multiplyInternal(NumberInterface multiplier);

    public final NumberInterface multiply(NumberInterface multiplier){
        checkInterrupted();
        return multiplyInternal(multiplier);
    }

    /**
     * Divides this number by another, returning
     * a new number instance.
     *
     * @param divisor the divisor
     * @return the result of the division.
     */
    protected abstract NumberInterface divideInternal(NumberInterface divisor);

    public final NumberInterface divide(NumberInterface divisor){
        checkInterrupted();
        return divideInternal(divisor);
    }

    /**
     * Adds this number to another, returning
     * a new number instance.
     *
     * @param summand the summand
     * @return the result of the summation.
     */
    protected abstract NumberInterface addInternal(NumberInterface summand);

    public final NumberInterface add(NumberInterface summand){
        checkInterrupted();
        return addInternal(summand);
    }

    /**
     * Subtracts another number from this number,
     * a new number instance.
     *
     * @param subtrahend the subtrahend.
     * @return the result of the subtraction.
     */
    protected abstract NumberInterface subtractInternal(NumberInterface subtrahend);

    public final NumberInterface subtract(NumberInterface subtrahend){
        checkInterrupted();
        return subtractInternal(subtrahend);
    }

    /**
     * Returns a new instance of this number with
     * the sign flipped.
     *
     * @return the new instance.
     */
    protected abstract NumberInterface negateInternal();

    public final NumberInterface negate(){
        checkInterrupted();
        return negateInternal();
    }

    /**
     * Raises this number to an integer power.
     *
     * @param exponent the exponent to which to take the number.
     * @return the resulting value.
     */
    protected abstract NumberInterface intPowInternal(int exponent);

    public final NumberInterface intPow(int exponent){
        checkInterrupted();
        return intPowInternal(exponent);
    }

    /**
     * Compares this number to another.
     *
     * @param number the number to compare to.
     * @return same as Integer.compare();
     */
    public abstract int compareTo(NumberInterface number);

    /**
     * Same as Math.signum().
     *
     * @return 1 if this number is positive, -1 if this number is negative, 0 if this number is 0.
     */
    public abstract int signum();

    /**
     * Returns the least integer greater than or equal to the number.
     *
     * @return the least integer >= the number, if int can hold the value.
     */
    protected abstract NumberInterface ceilingInternal();

    public final NumberInterface ceiling(){
        checkInterrupted();
        return ceilingInternal();
    }

    /**
     * Return the greatest integer less than or equal to the number.
     *
     * @return the greatest int >= the number, if int can hold the value.
     */
    protected abstract NumberInterface floorInternal();

    public final NumberInterface floor(){
        checkInterrupted();
        return floorInternal();
    }

    /**
     * Returns the fractional part of the number.
     *
     * @return the fractional part of the number.
     */
    protected abstract NumberInterface fractionalPartInternal();

    public final NumberInterface fractionalPart(){
        checkInterrupted();
        return fractionalPartInternal();
    }

    /**
     * Returns the integer representation of this number, discarding any fractional part,
     * if int can hold the value.
     *
     * @return
     */
    public abstract int intValue();

    /**
     * Promotes this class to another number class.
     *
     * @param toClass the class to promote to.
     * @return the resulting new instance.
     */
    protected abstract NumberInterface promoteToInternal(Class<? extends NumberInterface> toClass);

    public final NumberInterface promoteTo(Class<? extends NumberInterface> toClass) {
        checkInterrupted();
        return promoteToInternal(toClass);
    }

}
