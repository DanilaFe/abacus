package org.nwapw.abacus.tree;

import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;

public class NumberNode extends TreeNode {

    private NumberInterface number;

    public NumberNode(){
        number = null;
    }

    public NumberNode(double value){
        number = new NaiveNumber(value);
    }

    public NumberNode(String value){
        this(Double.parseDouble(value));
    }

    public NumberInterface getNumber() {
        return number;
    }
}
