package org.nwapw.abacus.function.applicable

/**
 * A class that can be applied to arguments.
 *
 * Applicable is a class that represents something that can be applied to one or more
 * arguments of the same type, and returns a single value from that application.
 * @param <T> the type of the parameters passed to this applicable.
 * @param <O> the return type of the applicable.
 */
interface Applicable<in T : Any, out O : Any> {

    /**
     * Checks if the given applicable can be used with the given parameters.
     * @param params the parameter array to verify for compatibility.
     * @return whether the array can be used with applyInternal.
     */
    fun matchesParams(params: Array<out T>): Boolean

    /**
     * Applies the applicable object to the given parameters,
     * without checking for compatibility.
     * @param params the parameters to apply to.
     * @return the result of the application.
     */
    fun applyInternal(params: Array<out T>): O?

    /**
     * If the parameters can be used with this applicable, returns
     * the result of the application of the applicable to the parameters.
     * Otherwise, returns null.
     * @param params the parameters to apply to.
     * @return the result of the operation, or null if parameters do not match.
     */
    fun apply(vararg params: T): O? {
        if (!matchesParams(params)) return null
        return applyInternal(params)
    }

}