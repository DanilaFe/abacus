package org.nwapw.abacus.function;

import org.nwapw.abacus.tree.Reducer;

/**
 * A slightly more specific Applicable that requires a reducer
 * to be passed to it along with the parameters.
 * @param <T> the type of the input arguments.
 * @param <O> the return type of the application.
 * @param <R> the required type of the reducer.
 */
public abstract class ReducerApplicable<T, O, R> extends Applicable<T, O> {

    @Override
    protected final O applyInternal(T[] params) {
        return null;
    }

    @Override
    public final O apply(T... params) {
        return null;
    }

    /**
     * Applies this applicable to the given arguments, and reducer.
     * @param reducer the reducer to use in the application.
     * @param args the arguments to apply to.
     * @return the result of the application.
     */
    public abstract O applyWithReducerInternal(Reducer<R> reducer, T[] args);

    /**
     * Applies this applicable to the given arguments, and reducer,
     * if the arguments and reducer are compatible with this applicable.
     * @param reducer the reducer to use in the application.
     * @param args the arguments to apply to.
     * @return the result of the application, or null if the arguments are incompatible.
     */
    public O applyWithReducer(Reducer<R> reducer, T...args) {
        if(!matchesParams(args) || reducer == null) return null;
        return applyWithReducerInternal(reducer, args);
    }

}
