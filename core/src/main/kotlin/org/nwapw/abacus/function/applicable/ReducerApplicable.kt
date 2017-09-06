package org.nwapw.abacus.function.applicable

import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.tree.Reducer

/**
 * Applicable that requires a reducer.
 *
 * ReducerApplicable slightly more specific Applicable that requires a reducer
 * to be passed to it along with the parameters.
 * @param <T> the type of the input arguments.
 * @param <O> the return type of the application.
 * @param <R> the required type of the reducer.
 */
interface ReducerApplicable<in T : Any, out O : Any, in R : Any> {

    /**
     * Checks if this applicable can be applied to the
     * given parameters.
     * @param params the parameters to check.
     */
    fun matchesParams(implementation: NumberImplementation, params: Array<out T>): Boolean

    /**
     * Applies this applicable to the given arguments, and reducer.
     * @param reducer the reducer to use in the application.
     * @param params the arguments to apply to.
     * @return the result of the application.
     */
    fun applyWithReducerInternal(implementation: NumberImplementation, reducer: Reducer<R>, params: Array<out T>): O?

    /**
     * Applies this applicable to the given arguments, and reducer,
     * if the arguments and reducer are compatible with this applicable.
     * @param reducer the reducer to use in the application.
     * @param params the arguments to apply to.
     * @return the result of the application, or null if the arguments are incompatible.
     */
    fun applyWithReducer(implementation: NumberImplementation, reducer: Reducer<R>, vararg params: T): O? {
        if (!matchesParams(implementation, params)) return null
        return applyWithReducerInternal(implementation, reducer, params)
    }

}