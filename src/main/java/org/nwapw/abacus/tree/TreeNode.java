package org.nwapw.abacus.tree;

import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;

import java.util.*;

/**
 * An abstract class that represents an expression tree node.
 */
public abstract class TreeNode {

    /**
     * The function that reduces a tree to a single vale.
     * @param reducer the reducer used to reduce the tree.
     * @param <T> the type the reducer produces.
     * @return the result of the reduction, or null on error.
     */
    public abstract <T> T reduce(Reducer<T> reducer);

}