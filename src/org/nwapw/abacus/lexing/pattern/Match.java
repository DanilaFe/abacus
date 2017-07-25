package org.nwapw.abacus.lexing.pattern;

public class Match<T> {

    private int from;
    private int to;
    private T type;

    public Match(int from, int to, T type){
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public T getType() {
        return type;
    }
}
