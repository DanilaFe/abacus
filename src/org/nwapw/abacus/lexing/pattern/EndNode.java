package org.nwapw.abacus.lexing.pattern;

public class EndNode<T> extends PatternNode<T> {

    private T patternId;

    public EndNode(T patternId){
        this.patternId = patternId;
    }

    public T getPatternId(){
        return patternId;
    }

}
