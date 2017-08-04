package org.nwapw.abacus.plugin;

import org.nwapw.abacus.number.NumberInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class NumberImplementation {

    private Class<? extends NumberInterface> implementation;
    private Map<String, Function<NumberInterface, NumberInterface>> promotionPaths;
    private int priority;

    public NumberImplementation(Class<? extends NumberInterface> implementation, int priority){
        this.implementation = implementation;
        this.priority = priority;
        promotionPaths = new HashMap<>();
    }

    public abstract NumberInterface instanceForString(String string);
    public abstract NumberInterface instanceForPi();

}
