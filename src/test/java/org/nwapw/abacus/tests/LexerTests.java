package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.Test;
import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;

import java.util.List;

public class LexerTests {

    @Test
    public void testBasicSuccess(){
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("abc", 0);
        lexer.register("def", 1);
        List<Match<Integer>> matchedIntegers = lexer.lexAll("abcdefabc", 0, Integer::compare);
        Assert.assertEquals(matchedIntegers.get(0).getType(), Integer.valueOf(0));
        Assert.assertEquals(matchedIntegers.get(1).getType(), Integer.valueOf(1));
        Assert.assertEquals(matchedIntegers.get(2).getType(), Integer.valueOf(0));
    }

    @Test
    public void testBasicFailure(){
        Lexer<Integer> lexer = new Lexer<>();
        lexer.register("abc", 0);
        lexer.register("def", 1);
        Assert.assertNull(lexer.lexAll("abcdefabcz", 0, Integer::compare));
    }

}
