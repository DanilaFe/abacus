package org.nwapw.abacus.lexing.pattern;

public class ValueNode<T> extends PatternNode<T> {

    private char value;

    public ValueNode(char value){
        this.value = value;
    }

    @Override
    public boolean matches(char other) {
        return other == value;
    }

    @Override
    public char range() {
        return value;
    }
}
