package org.nwapw.abacus.lexing.pattern;

import java.util.ArrayList;
import java.util.HashSet;

public class PatternNode<T> {

    protected HashSet<PatternNode<T>> outputStates;

    public PatternNode(){
        outputStates = new HashSet<>();
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
