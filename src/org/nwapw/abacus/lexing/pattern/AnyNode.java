package org.nwapw.abacus.lexing.pattern;

public class AnyNode<T> extends PatternNode<T> {

    @Override
    public boolean matches(char other) {
        return true;
    }

}
