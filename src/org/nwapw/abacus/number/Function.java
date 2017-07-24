package org.nwapw.abacus.number;

import java.util.HashMap;

public abstract class Function {

    private static final HashMap<Class<? extends Number>, Integer> priorityMap =
            new HashMap<Class<? extends Number>, Integer>() {{
                put(NaiveNumber.class, 0);
            }};

    protected abstract boolean matchesParams(Number[] params);
    protected abstract Number applyInternal(Number[] params);

    public Number apply(Number...params) {
        if(!matchesParams(params)) return null;
        return applyInternal(params);
    }

}
