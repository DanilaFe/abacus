package org.nwapw.abacus.parsing;

import java.util.List;

/**
 * Interface that provides the ability to convert a string into a list of tokens.
 * @param <T> the type of the tokens produced.
 */
public interface Tokenizer<T> {

    /**
     * Converts a string into tokens.
     * @param string the string to convert.
     * @return the list of tokens, or null on error.
     */
    public List<T> tokenizeString(String string);

}
