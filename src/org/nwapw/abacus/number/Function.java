package org.nwapw.abacus.number;

public abstract class Function {

    protected abstract boolean matchesParams(Number[] params);
    protected abstract Number applyInternal(Number[] params);

    public Number apply(Number...params) {
        if(!matchesParams(params)) return null;
        return applyInternal(params);
    }

}
