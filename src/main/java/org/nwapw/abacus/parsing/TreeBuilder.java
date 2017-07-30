package org.nwapw.abacus.parsing;

import org.nwapw.abacus.tree.TreeNode;

import java.util.List;

public class TreeBuilder<T> {

    private Tokenizer<T> tokenizer;
    private Parser<T> parser;

    public TreeBuilder(Tokenizer<T> tokenizer, Parser<T> parser){
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    public TreeNode fromString(String input){
        List<T> tokens = tokenizer.tokenizeString(input);
        if(tokens == null) return null;
        return parser.constructTree(tokens);
    }

}
