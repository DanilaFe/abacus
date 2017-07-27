package org.nwapw.abacus.tree;

import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;

import java.util.*;

/**
 * An abstract class that represents an expression tree node.
 */
public abstract class TreeNode {

    public abstract <T> T reduce(Reducer<T> reducer);

}
