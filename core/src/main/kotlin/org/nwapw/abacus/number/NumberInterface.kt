package org.nwapw.abacus.number

import org.nwapw.abacus.exception.ComputationInterruptedException
import org.nwapw.abacus.number.range.NumberRangeBuilder

abstract class NumberInterface: Comparable<NumberInterface> {

    /**
     * Check if the thread was interrupted and
     * throw an exception to end the computation.
     */
    private fun checkInterrupted(){
        if(Thread.currentThread().isInterrupted)
            throw ComputationInterruptedException()
    }

    /**
     * Returns the integer representation of this number, discarding any fractional part,
     * if int can hold the value.
     *
     * @return the integer value of this number.
     */
    abstract fun intValue(): Int
    /**
     * Same as Math.signum().
     *
     * @return 1 if this number is positive, -1 if this number is negative, 0 if this number is 0.
     */
    abstract fun signum(): Int

    /**
     * The maximum precision to which this number operates.
     */
    abstract val maxPrecision: Int
    /**
     * Returns the smallest error this instance can tolerate depending
     * on its precision and value.
     */
    abstract val maxError: NumberInterface

    /**
     * Adds this number to another, returning
     * a new number instance.
     *
     * @param summand the summand
     * @return the result of the summation.
     */
    abstract fun addInternal(summand: NumberInterface): NumberInterface
    /**
     * Subtracts another number from this number,
     * a new number instance.
     *
     * @param subtrahend the subtrahend.
     * @return the result of the subtraction.
     */
    abstract fun subtractInternal(subtrahend: NumberInterface): NumberInterface
    /**
     * Multiplies this number by another, returning
     * a new number instance.
     *
     * @param multiplier the multiplier
     * @return the result of the multiplication.
     */
    abstract fun multiplyInternal(multiplier: NumberInterface): NumberInterface
    /**
     * Divides this number by another, returning
     * a new number instance.
     *
     * @param divisor the divisor
     * @return the result of the division.
     */
    abstract fun divideInternal(divisor: NumberInterface): NumberInterface
    /**
     * Returns a new instance of this number with
     * the sign flipped.
     *
     * @return the new instance.
     */
    abstract fun negateInternal(): NumberInterface
    /**
     * Raises this number to an integer power.
     *
     * @param exponent the exponent to which to take the number.
     * @return the resulting value.
     */
    abstract fun intPowInternal(pow: Int): NumberInterface
    /**
     * Returns the least integer greater than or equal to the number.
     *
     * @return the least integer greater or equal to the number, if int can hold the value.
     */
    abstract fun ceilingInternal(): NumberInterface
    /**
     * Return the greatest integer less than or equal to the number.
     *
     * @return the greatest integer smaller or equal the number.
     */
    abstract fun floorInternal(): NumberInterface
    /**
     * Returns the fractional part of the number.
     *
     * @return the fractional part of the number.
     */
    abstract fun fractionalPartInternal(): NumberInterface

    /**
     * Adds this number to another, returning
     * a new number instance. Also, checks if the
     * thread has been interrupted, and if so, throws
     * an exception.
     *
     * @param summand the summand
     * @return the result of the summation.
     */
    fun add(summand: NumberInterface): NumberInterface {
        checkInterrupted()
        return addInternal(summand)
    }

    /**
     * Subtracts another number from this number,
     * a new number instance. Also, checks if the
     * thread has been interrupted, and if so, throws
     * an exception.
     *
     * @param subtrahend the subtrahend.
     * @return the result of the subtraction.
     */
    fun subtract(subtrahend: NumberInterface): NumberInterface {
        checkInterrupted()
        return subtractInternal(subtrahend)
    }

    /**
     * Multiplies this number by another, returning
     * a new number instance. Also, checks if the
     * thread has been interrupted, and if so, throws
     * an exception.
     *
     * @param multiplier the multiplier
     * @return the result of the multiplication.
     */
    fun multiply(multiplier: NumberInterface): NumberInterface {
        checkInterrupted()
        return multiplyInternal(multiplier)
    }

    /**
     * Divides this number by another, returning
     * a new number instance. Also, checks if the
     * thread has been interrupted, and if so, throws
     * an exception.
     *
     * @param divisor the divisor
     * @return the result of the division.
     */
    fun divide(divisor: NumberInterface): NumberInterface {
        checkInterrupted()
        return divideInternal(divisor)
    }

    /**
     * Returns a new instance of this number with
     * the sign flipped. Also, checks if the
     * thread has been interrupted, and if so, throws
     * an exception.
     *
     * @return the new instance.
     */
    fun negate(): NumberInterface {
        checkInterrupted()
        return negateInternal()
    }

    /**
     * Raises this number to an integer power. Also, checks if the
     * thread has been interrupted, and if so, throws
     * an exception.
     *
     * @param exponent the exponent to which to take the number.
     * @return the resulting value.
     */
    fun intPow(exponent: Int): NumberInterface {
        checkInterrupted()
        return intPowInternal(exponent)
    }

    /**
     * Returns the least integer greater than or equal to the number.
     * Also, checks if the thread has been interrupted, and if so, throws
     * an exception.
     *
     * @return the least integer bigger or equal to the number.
     */
    fun ceiling(): NumberInterface {
        checkInterrupted()
        return ceilingInternal()
    }

    /**
     * Return the greatest integer less than or equal to the number.
     * Also, checks if the thread has been interrupted, and if so, throws
     * an exception.
     *
     * @return the greatest int smaller than or equal to the number.
     */
    fun floor(): NumberInterface {
        checkInterrupted()
        return floorInternal()
    }

    /**
     * Returns the fractional part of the number, specifically x - floor(x).
     * Also, checks if the thread has been interrupted,
     * and if so, throws an exception.
     *
     * @return the fractional part of the number.
     */
    fun fractionalPart(): NumberInterface {
        checkInterrupted()
        return fractionalPartInternal()
    }

    /**
     * Returns a NumberRangeBuilder object, which is used to create a range.
     * The reason that this returns a builder and not an actual range is that
     * the NumberRange needs to promote values passed to it, which
     * requires an abacus instance.
     * @param other the value at the bottom of the range.
     * @return the resulting range builder.
     */
    operator fun rangeTo(other: NumberInterface) = NumberRangeBuilder(this, other)

    /**
     * Plus operator overloaded to allow "nice" looking math.
     * @param other the value to add to this number.
     * @return the result of the addition.
     */
    operator fun plus(other: NumberInterface) = add(other)
    /**
     * Minus operator overloaded to allow "nice" looking math.
     * @param other the value to subtract to this number.
     * @return the result of the subtraction.
     */
    operator fun minus(other: NumberInterface) = subtract(other)
    /**
     * Times operator overloaded to allow "nice" looking math.
     * @param other the value to multiply this number by.
     * @return the result of the multiplication.
     */
    operator fun times(other: NumberInterface) = multiply(other)
    /**
     * Divide operator overloaded to allow "nice" looking math.
     * @param other the value to divide this number by.
     * @return the result of the division.
     */
    operator fun div(other: NumberInterface) = divide(other)
    /**
     * The plus operator.
     * @return this number.
     */
    operator fun unaryPlus() = this
    /**
     * The minus operator.
     * @return the negative of this number.
     */
    operator fun unaryMinus() = negate()

}