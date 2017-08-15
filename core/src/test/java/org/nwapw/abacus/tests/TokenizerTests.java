package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.config.Configuration;
import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.function.OperatorType;
import org.nwapw.abacus.lexing.pattern.Match;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.parsing.LexerTokenizer;
import org.nwapw.abacus.plugin.Plugin;
import org.nwapw.abacus.tree.TokenType;

import java.util.List;

public class TokenizerTests {

    private static Abacus abacus = new Abacus(new Configuration(0, "precise", new String[]{}));
    private static LexerTokenizer lexerTokenizer = new LexerTokenizer();
    private static Function subtractFunction = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 2;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return params[0].subtract(params[1]);
        }
    };
    private static Plugin testPlugin = new Plugin(abacus.getPluginManager()) {
        @Override
        public void onEnable() {
            registerOperator("+", new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX,
                    0, subtractFunction));
            registerOperator("-", new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX,
                    0, subtractFunction));
            registerFunction("subtract", subtractFunction);
        }

        @Override
        public void onDisable() {

        }
    };

    private static void assertTokensMatch(List<Match<TokenType>> tokenList, TokenType[] expectedTypes) {
        Assert.assertNotNull(tokenList);
        Assert.assertEquals(tokenList.size(), expectedTypes.length);
        for (int i = 0; i < expectedTypes.length; i++) {
            Assert.assertEquals(expectedTypes[i], tokenList.get(i).getType());
        }
    }

    @BeforeClass
    public static void prepareTests() {
        abacus.getPluginManager().addListener(lexerTokenizer);
        abacus.getPluginManager().addInstantiated(testPlugin);
        abacus.getPluginManager().load();
    }

    @Test
    public void testInteger() {
        assertTokensMatch(lexerTokenizer.tokenizeString("11"), new TokenType[]{TokenType.NUM});
    }

    @Test
    public void testLeadingZeroDecimal() {
        assertTokensMatch(lexerTokenizer.tokenizeString("0.1"), new TokenType[]{TokenType.NUM});
    }

    @Test
    public void testNonLeadingDecimal() {
        assertTokensMatch(lexerTokenizer.tokenizeString(".1"), new TokenType[]{TokenType.NUM});
    }

    @Test
    public void testSimpleChars() {
        TokenType[] types = {
                TokenType.OPEN_PARENTH,
                TokenType.WHITESPACE,
                TokenType.COMMA,
                TokenType.CLOSE_PARENTH
        };
        assertTokensMatch(lexerTokenizer.tokenizeString("( ,)"), types);
    }

    @Test
    public void testFunctionParsing() {
        TokenType[] types = {
                TokenType.FUNCTION,
                TokenType.OPEN_PARENTH,
                TokenType.NUM,
                TokenType.COMMA,
                TokenType.NUM,
                TokenType.CLOSE_PARENTH
        };
        assertTokensMatch(lexerTokenizer.tokenizeString("subtract(1,2)"), types);
    }

    @Test
    public void testOperatorParsing() {
        TokenType[] types = {
                TokenType.NUM,
                TokenType.OP,
                TokenType.NUM
        };
        assertTokensMatch(lexerTokenizer.tokenizeString("1-1"), types);
    }

    @Test
    public void testSanitizedOperators() {
        TokenType[] types = {
                TokenType.NUM,
                TokenType.OP,
                TokenType.NUM
        };
        assertTokensMatch(lexerTokenizer.tokenizeString("1+1"), types);
    }

}