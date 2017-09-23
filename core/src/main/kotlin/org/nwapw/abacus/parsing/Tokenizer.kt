package org.nwapw.abacus.parsing

/**
 * Converter from string to tokens.
 *
 * Interface that converts a string into a list
 * of tokens of a certain type.
 *
 * @param <T> the type of the tokens produced.
 */
interface Tokenizer<out T> {

    /**
     * Converts a string into tokens.
     *
     * @param string the string to convert.
     * @return the list of tokens, or null on error.
     */
    fun tokenizeString(string: String):  List<T>

}
