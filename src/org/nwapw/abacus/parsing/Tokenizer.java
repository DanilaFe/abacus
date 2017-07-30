package org.nwapw.abacus.parsing;

import java.util.List;

public interface Tokenizer<T> {

    public List<T> tokenizeString(String string);

}
