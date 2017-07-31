package org.nwapw.abacus.tree;

import org.nwapw.abacus.number.NumberInterface;

/**
 * A node implementation that represents a single number.
 */
public class NumberNode extends TreeNode {

    /**
     * The number that is represented by this number node.
     */
    private NumberInterface number;

    /**
     * Creates a number node with no number.
     */
    public NumberNode() {
        number = null;
    }

    /**
     * Creates a new number node with the given double value.
     *
     * @param newNumber the number for which to create a number node.
     */
    public NumberNode(NumberInterface newNumber) {
        this.number = newNumber;
    }

    /**
     * Gets the number value of this node.
     *
     * @return the number value of this node.
     */
    public NumberInterface getNumber() {
        return number;
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        return reducer.reduceNode(this);
    }

    @Override
    public String toString() {
        return number != null ? number.toString() : "null";
    }
}
