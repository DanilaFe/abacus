package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.Test;
import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;

import java.util.List;

public class LexerTests {

    @Test
    public void testBasicSuccess() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("abc", 0);
        lexer.register("def", 1);
        List<Match<Integer>> matchedIntegers = lexer.lexAll("abcdefabc", 0, Integer::compare);
        Assert.assertNotNull(matchedIntegers);
        Assert.assertEquals(matchedIntegers.get(0).getType(), Integer.valueOf(0));
        Assert.assertEquals(matchedIntegers.get(1).getType(), Integer.valueOf(1));
        Assert.assertEquals(matchedIntegers.get(2).getType(), Integer.valueOf(0));
    }

    @Test
    public void testBasicFailure() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("abc", 0);
        lexer.register("def", 1);
        Assert.assertNull(lexer.lexAll("abcdefabcz", 0, Integer::compare));
    }

    @Test
    public void testNoPatterns() {
        Lexer<Integer> lexer = new Lexer<>();
        Assert.assertNull(lexer.lexAll("abcdefabc", 0, Integer::compare));
    }

    @Test
    public void testEmptyMatches() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("a?", 0);
        Assert.assertNull(lexer.lexAll("", 0, Integer::compare));
    }

    @Test
    public void testOneOrMore() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("a+", 0);
        List<Match<Integer>> tokens = lexer.lexAll("aaaa", 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
    }

    @Test
    public void testZeroOrMore() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("a*", 0);
        List<Match<Integer>> tokens = lexer.lexAll("aaaa", 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
    }

    @Test
    public void testZeroOrOne() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("a?", 0);
        List<Match<Integer>> tokens = lexer.lexAll("aaaa", 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 4);
    }

    @Test
    public void testGreedyMatching() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("a*a", 0);
        List<Match<Integer>> tokens = lexer.lexAll("aaaa", 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
    }

    @Test
    public void testAnyCharacter() {
        String testString = "abcdef";
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register(".", 0);
        List<Match<Integer>> tokens = lexer.lexAll(testString, 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), testString.length());
        for (int i = 0; i < tokens.size(); i++) {
            Assert.assertEquals(testString.substring(i, i + 1), tokens.get(i).getContent());
        }
    }

    @Test
    public void testBasicGroup() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("(abc)", 0);
        List<Match<Integer>> tokens = lexer.lexAll("abc", 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
        Assert.assertEquals(tokens.get(0).getContent(), "abc");
    }

    @Test
    public void testBasicRangeSuccess() {
        String testString = "abcdef";
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("[a-f]", 0);
        List<Match<Integer>> tokens = lexer.lexAll(testString, 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(testString.length(), tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            Assert.assertEquals(testString.substring(i, i + 1), tokens.get(i).getContent());
        }
    }

    @Test
    public void testBasicRangeFailure() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("[a-f]", 0);
        Assert.assertNull(lexer.lexAll("g", 0, Integer::compare));
    }

    @Test
    public void testGroupAndOperator() {
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("(abc)+", 0);
        List<Match<Integer>> tokens = lexer.lexAll("abcabc", 0, Integer::compare);
        Assert.assertNotNull(tokens);
        Assert.assertEquals(tokens.size(), 1);
    }

}
