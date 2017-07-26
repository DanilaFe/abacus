package org.nwapw.abacus.lexing.pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * A base class for a pattern node. Provides all functions
 * necessary for matching, and is constructed by a Pattern instance
 * from a string.
 * @param <T> the type that's used to tell which pattern this node belongs to.
 */
public class PatternNode<T> {

    /**
     * The set of states to which the lexer should continue
     * should this node be correctly matched.
     */
    protected HashSet<PatternNode<T>> outputStates;

    /**
     * Creates a new pattern node.
     */
    public PatternNode(){
        outputStates = new HashSet<>();
    }

    /**
     * Determines whether the current input character can
     * be matched by this node.
     * @param other the character being matched.
     * @return true if the character can be matched, false otherwise.
     */
    public boolean matches(char other){
        return false;
    }

    /**
     * If this node can be used as part of a range, returns that value.
     * @return a NULL terminator if this character cannot be converted
     * into a range bound, or the appropriate range bound if it can.
     */
    public char range(){
        return '\0';
    }

    /**
     * Adds this node in a collection of other nodes.
     * @param into the collection to add into.
     */
    public void addInto(Collection<PatternNode<T>> into){
        into.add(this);
    }

    /**
     * Adds the node's children into a collection of other nodes.
     * @param into the collection to add into.
     */
    public void addOutputsInto(Collection<PatternNode<T>> into){
        outputStates.forEach(e -> e.addInto(into));
    }

}
