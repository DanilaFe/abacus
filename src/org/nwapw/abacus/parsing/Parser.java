package org.nwapw.abacus.parsing;

import org.nwapw.abacus.tree.TreeNode;

import java.util.List;

public interface Parser<T> {

    public TreeNode constructTree(List<T> tokens);
}
