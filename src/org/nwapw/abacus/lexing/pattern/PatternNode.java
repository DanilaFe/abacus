package org.nwapw.abacus.lexing.pattern;

import java.util.ArrayList;

public class PatternNode<T> {

    protected ArrayList<PatternNode<T>> outputStates;

    public PatternNode(){
        outputStates = new ArrayList<>();
    }

    public boolean matches(char other){
        return false;
    }

    public char range(){
        return '\0';
    }

    public void addInto(ArrayList<PatternNode<T>> into){
        into.add(this);
    }

}
