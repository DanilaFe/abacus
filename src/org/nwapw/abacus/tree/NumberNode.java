package org.nwapw.abacus.tree;

import org.nwapw.abacus.number.NaiveNumber;
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
    public NumberNode(){
        number = null;
    }

    /**
     * Creates a new number node with the given double value.
     * @param value the value to use.
     */
    public NumberNode(double value){
        number = new NaiveNumber(value);
    }

    /**
     * Creates a new number node with the given string value, converted
     * to a double.
     * @param value the value.
     */
    public NumberNode(String value){
        this(Double.parseDouble(value));
    }

    /**
     * Gets the number value of this node.
     * @return the number value of this node.
     */
    public NumberInterface getNumber() {
        return number;
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        return reducer.reduceNode(this);
    }
}
