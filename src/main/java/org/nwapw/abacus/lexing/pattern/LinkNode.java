package org.nwapw.abacus.lexing.pattern;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A node that is used as structural glue in pattern compilation.
 * @param <T> the type that's used to tell which pattern this node belongs to.
 */
public class LinkNode<T> extends PatternNode<T> {

    @Override
    public void addInto(Collection<PatternNode<T>> into) {
        if(!into.contains(this)) {
            into.add(this);
            addOutputsInto(into);
        }
    }

}
