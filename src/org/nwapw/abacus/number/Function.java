package org.nwapw.abacus.number;

import java.util.HashMap;

public abstract class Function {

    private static final HashMap<Class<? extends NumberInterface>, Integer> priorityMap =
            new HashMap<Class<? extends NumberInterface>, Integer>() {{
                put(NaiveNumber.class, 0);
            }};

    protected abstract boolean matchesParams(NumberInterface[] params);
    protected abstract NumberInterface applyInternal(NumberInterface[] params);

    public NumberInterface apply(NumberInterface...params) {
        if(!matchesParams(params)) return null;
        return applyInternal(params);
    }

}
