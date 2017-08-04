package org.nwapw.abacus.function;

import org.nwapw.abacus.number.NumberInterface;

/**
 * A function that operates on one or more
 * inputs and returns a single number.
 */
public abstract class Function {

    /**
     * Checks whether the given params will work for the given function.
     *
     * @param params the given params
     * @return true if the params can be used with this function.
     */
    protected abstract boolean matchesParams(NumberInterface[] params);

    /**
     * Internal apply implementation, which already receives appropriately promoted
     * parameters that have bee run through matchesParams
     *
     * @param params the promoted parameters.
     * @return the return value of the function.
     */
    protected abstract NumberInterface applyInternal(NumberInterface[] params);

    /**
     * Function to check, promote arguments and run the function.
     *
     * @param params the raw input parameters.
     * @return the return value of the function, or null if an error occurred.
     */
    public NumberInterface apply(NumberInterface... params) {
        if (!matchesParams(params)) return null;
        return applyInternal(params);
    }

}
