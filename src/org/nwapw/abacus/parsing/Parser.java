package org.nwapw.abacus.parsing;

import org.nwapw.abacus.tree.TreeNode;

import java.util.List;

/**
 * An itnerface that provides the ability to convert a list of tokens
 * into a parse tree.
 * @param <T> the type of tokens accepted by this parser.
 */
public interface Parser<T> {

    /**
     * Constructs a tree out of the given tokens.
     * @param tokens the tokens to construct a tree from.
     * @return the constructed tree, or null on error.
     */
    public TreeNode constructTree(List<T> tokens);
}
