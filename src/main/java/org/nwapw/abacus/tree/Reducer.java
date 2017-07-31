package org.nwapw.abacus.tree;

/**
 * Interface used to reduce a tree into a single value.
 *
 * @param <T> the value to reduce into.
 */
public interface Reducer<T> {

    /**
     * Reduces the given tree into a single value of type T.
     *
     * @param node     the node being passed in to be reduced.
     * @param children the already-reduced children of this node.
     * @return the resulting value from the reduce.
     */
    public T reduceNode(TreeNode node, Object... children);

}
