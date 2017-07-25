package org.nwapw.abacus.lexing.pattern;

import java.util.ArrayList;
import java.util.Collection;

public class LinkNode<T> extends PatternNode<T> {

    @Override
    public void addInto(Collection<PatternNode<T>> into) {
        addOutputsInto(into);
    }

}
