package org.nwapw.abacus.number;

public abstract class Function {

    public int arity;

    protected abstract Number applyInternal(Number...params);

    public Number apply(Number...params) throws IllegalArgumentException {
        if(params.length != arity)
            throw new IllegalArgumentException("Invalid number of arguments: Function takes "
                    + arity + ", " + params.length + " provided.");
        return applyInternal(params);
    }

}
