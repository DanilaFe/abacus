package org.nwapw.abacus.parsing

import org.nwapw.abacus.tree.nodes.TreeNode

/**
 * Class to combine a [Tokenizer] and a [Parser]
 *
 * TreeBuilder class used to piece together a Tokenizer and
 * Parser of the same kind. This is used to essentially avoid
 * working with any parameters at all, and the generics
 * in this class are used only to ensure the tokenizer and parser
 * are of the same type.
 *
 * @param <T> the type of tokens created by the tokenizer and used by the parser.
 */
class TreeBuilder<T>(private val tokenizer: Tokenizer<T>, private val parser: Parser<T>) {

    /**
     * Parses the given [string] into a tree.
     *
     * @param string the string to parse into a tree.
     * @return the resulting tree.
     */
    fun fromString(string: String): TreeNode = parser.constructTree(tokenizer.tokenizeString(string))

}