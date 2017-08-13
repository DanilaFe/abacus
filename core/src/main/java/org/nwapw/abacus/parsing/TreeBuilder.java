package org.nwapw.abacus.parsing;

import org.nwapw.abacus.tree.TreeNode;

import java.util.List;

/**
 * TreeBuilder class used to piece together a Tokenizer and
 * Parser of the same kind. This is used to essentially avoid
 * working with any parameters at all, and the generics
 * in this class are used only to ensure the tokenizer and parser
 * are of the same type.
 *
 * @param <T> the type of tokens created by the tokenizer and used by the parser.
 */
public class TreeBuilder<T> {

    /**
     * The tokenizer used to convert a string into tokens.
     */
    private Tokenizer<T> tokenizer;
    /**
     * The parser used to parse a list of tokens into a tree.
     */
    private Parser<T> parser;

    /**
     * Create a new Tree Builder with the given tokenizer and parser
     *
     * @param tokenizer the tokenizer to turn strings into tokens
     * @param parser    the parser to turn tokens into a tree
     */
    public TreeBuilder(Tokenizer<T> tokenizer, Parser<T> parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    /**
     * Parse the given string into a tree.
     *
     * @param input the string to parse into a tree.
     * @return the resulting tree.
     */
    public TreeNode fromString(String input) {
        List<T> tokens = tokenizer.tokenizeString(input);
        if (tokens == null) return null;
        return parser.constructTree(tokens);
    }

}
