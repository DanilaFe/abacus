package org.nwapw.abacus.plugin;

import org.nwapw.abacus.number.NumberInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A class that holds data about a number implementation.
 */
public abstract class NumberImplementation {

    /**
     * The list of paths through which this implementation can be promoted.
     */
    protected Map<Class<? extends NumberInterface>, Function<NumberInterface, NumberInterface>> promotionPaths;
    /**
     * The implementation class for this implementation.
     */
    private Class<? extends NumberInterface> implementation;
    /**
     * The priority of converting into this number implementation.
     */
    private int priority;

    /**
     * Creates a new number implementation with the given data.
     *
     * @param implementation the implementation class.
     * @param priority       the priority, higher -> more likely to be converted into.
     */
    public NumberImplementation(Class<? extends NumberInterface> implementation, int priority) {
        this.implementation = implementation;
        this.priority = priority;
        promotionPaths = new HashMap<>();
    }

    /**
     * Gets the list of all promotion paths this implementation can take.
     *
     * @return the map of documentation paths.
     */
    public final Map<Class<? extends NumberInterface>, Function<NumberInterface, NumberInterface>> getPromotionPaths() {
        return promotionPaths;
    }

    /**
     * Gets the implementation class used by this implementation.
     *
     * @return the implementation class.
     */
    public final Class<? extends NumberInterface> getImplementation() {
        return implementation;
    }

    /**
     * Gets the priority of this number implementation.
     *
     * @return the priority.
     */
    public final int getPriority() {
        return priority;
    }

    /**
     * Abstract function to create a new instance from a string.
     *
     * @param string the string to create a number from.
     * @return the resulting number.
     */
    public abstract NumberInterface instanceForString(String string);

    /**
     * Get the instance of pi with the given implementation.
     *
     * @return pi
     */
    public abstract NumberInterface instanceForPi();

}
