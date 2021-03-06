package org.nwapw.abacus.parsing.standard;

import org.nwapw.abacus.exception.TokenizeException;
import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.Match;
import org.nwapw.abacus.lexing.pattern.Pattern;
import org.nwapw.abacus.parsing.Tokenizer;
import org.nwapw.abacus.plugin.PluginListener;
import org.nwapw.abacus.plugin.PluginManager;

import java.util.Comparator;
import java.util.List;

/**
 * A tokenzier that uses the lexer class and registered function and operator
 * names to turn input into tokens in O(n) time.
 */
public class LexerTokenizer implements Tokenizer<Match<TokenType>>, PluginListener {

    /**
     * Comparator used to sort the tokens produced by the lexer.
     */
    protected static final Comparator<TokenType> TOKEN_SORTER = (o1, o2) -> o1.priority - o2.priority;

    /**
     * The lexer instance used to turn strings into matches.
     */
    private Lexer<TokenType> lexer;

    /**
     * Creates a new lexer tokenizer.
     */
    public LexerTokenizer() {
        lexer = new Lexer<TokenType>() {{
            register(" ", TokenType.WHITESPACE);
            register(",", TokenType.COMMA);
            register("[0-9]*(\\.[0-9]+)?", TokenType.NUM);
            register("[a-zA-Z]+", TokenType.VARIABLE);
            register("\\(", TokenType.OPEN_PARENTH);
            register("\\)", TokenType.CLOSE_PARENTH);
        }};
    }

    @Override
    public List<Match<TokenType>> tokenizeString(String string) {
        List<Match<TokenType>> tokens = lexer.lexAll(string, 0, TOKEN_SORTER);
        if(tokens == null) throw new TokenizeException();
        return tokens;
    }

    @Override
    public void onLoad(PluginManager manager) {
        for (String operator : manager.getAllOperators()) {
            lexer.register(Pattern.sanitize(operator), TokenType.OP);
        }
        for (String operator : manager.getAllTreeValueOperators()) {
            lexer.register(Pattern.sanitize(operator), TokenType.TREE_VALUE_OP);
        }
        for (String function : manager.getAllFunctions()) {
            lexer.register(Pattern.sanitize(function), TokenType.FUNCTION);
        }
        for (String function : manager.getAllTreeValueFunctions()) {
            lexer.register(Pattern.sanitize(function), TokenType.TREE_VALUE_FUNCTION);
        }
    }

    @Override
    public void onUnload(PluginManager manager) {
        for (String operator : manager.getAllOperators()) {
            lexer.unregister(Pattern.sanitize(operator), TokenType.OP);
        }
        for (String operator : manager.getAllTreeValueOperators()) {
            lexer.unregister(Pattern.sanitize(operator), TokenType.TREE_VALUE_OP);
        }
        for (String function : manager.getAllFunctions()) {
            lexer.unregister(Pattern.sanitize(function), TokenType.FUNCTION);
        }
        for (String function : manager.getAllTreeValueFunctions()) {
            lexer.unregister(Pattern.sanitize(function), TokenType.TREE_VALUE_FUNCTION);
        }
    }

}
