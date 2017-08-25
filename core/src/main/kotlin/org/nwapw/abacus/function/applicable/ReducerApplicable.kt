package org.nwapw.abacus.function.applicable

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
interface ReducerApplicable<in T: Any, out O: Any, in R: Any> : Applicable<T, O> {

    override fun applyInternal(params: Array<out T>): O? {
        return null
    }

    override fun apply(vararg params: T): O? {
        return null
    }

    /**
     * Applies this applicable to the given arguments, and reducer.
     * @param reducer the reducer to use in the application.
     * @param args the arguments to apply to.
     * @return the result of the application.
     */
    fun applyWithReducerInternal(reducer: Reducer<R>, params: Array<out T>): O?
    /**
     * Applies this applicable to the given arguments, and reducer,
     * if the arguments and reducer are compatible with this applicable.
     * @param reducer the reducer to use in the application.
     * @param args the arguments to apply to.
     * @return the result of the application, or null if the arguments are incompatible.
     */
    fun applyWithReducer(reducer: Reducer<R>, vararg params: T): O? {
        if(!matchesParams(params)) return null
        return applyWithReducerInternal(reducer, params)
    }

}