package org.nwapw.abacus.lexing.pattern;

public class RangeNode<T> extends PatternNode<T> {

    private char from;
    private char to;

    public RangeNode(char from, char to){
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean matches(char other) {
        return other >= from && other <= to;
    }

}
