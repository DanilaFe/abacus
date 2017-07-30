package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.Test;
import org.nwapw.abacus.lexing.pattern.Match;
import org.nwapw.abacus.parsing.LexerTokenizer;
import org.nwapw.abacus.tree.TokenType;

import java.util.List;

public class TokenizerTests {

    private LexerTokenizer lexerTokenizer = new LexerTokenizer();

    @Test
    public void testInteger(){
        List<Match<TokenType>> tokens = lexerTokenizer.tokenizeString("11");
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
        Assert.assertEquals(tokens.get(0).getType(), TokenType.NUM);
    }

    @Test
    public void testLeadingZeroDecimal(){
        List<Match<TokenType>> tokens = lexerTokenizer.tokenizeString("0.1");
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
        Assert.assertEquals(tokens.get(0).getType(), TokenType.NUM);
    }

    @Test
    public void testNonLeadingDecimal(){
        List<Match<TokenType>> tokens = lexerTokenizer.tokenizeString(".1");
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
        Assert.assertEquals(tokens.get(0).getType(), TokenType.NUM);
    }

    @Test
    public void testSimpleChars(){
        List<Match<TokenType>> tokens = lexerTokenizer.tokenizeString("( ,)");
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 4);
        Assert.assertEquals(tokens.get(0).getType(), TokenType.OPEN_PARENTH);
        Assert.assertEquals(tokens.get(1).getType(), TokenType.WHITESPACE);
        Assert.assertEquals(tokens.get(2).getType(), TokenType.COMMA);
        Assert.assertEquals(tokens.get(3).getType(), TokenType.CLOSE_PARENTH);
    }

}
