package org.nwapw.abacus.lexing.pattern;

import java.util.ArrayList;

public class LinkNode<T> extends PatternNode<T> {

    @Override
    public void addInto(ArrayList<PatternNode<T>> into) {
        for(PatternNode<T> node : outputStates){
            node.addInto(into);
        }
    }

}
