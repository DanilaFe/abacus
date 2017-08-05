package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.tree.TreeNode;

public class CalculationTests {

    private static Abacus abacus = new Abacus();

    @BeforeClass
    public static void prepareTests(){
        abacus.getPluginManager().addInstantiated(new StandardPlugin(abacus.getPluginManager()));
        abacus.getPluginManager().load();
    }

    private void testOutput(String input, String parseOutput, String output){
        TreeNode parsedTree = abacus.parseString(input);
        Assert.assertNotNull(parsedTree);
        Assert.assertEquals(parsedTree.toString(), parseOutput);
        NumberInterface result = abacus.evaluateTree(parsedTree);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.toString().startsWith(output));
    }

    private void testEvalError(String input, String parseOutput){
        TreeNode parsedTree = abacus.parseString(input);
        Assert.assertNotNull(parsedTree);
        Assert.assertEquals(parsedTree.toString(), parseOutput);
        Assert.assertNull(abacus.evaluateTree(parsedTree));
    }

    @Test
    public void testAddition(){
        testOutput("9.5+10", "(9.5+10)", "19.5");
    }

    @Test
    public void testSubtraction(){
        testOutput("9.5-10", "(9.5-10)", "-0.5");
    }

    @Test
    public void testMultiplication(){
        testOutput("9.5*10", "(9.5*10)", "95");
    }

    @Test
    public void testDivision(){
        testOutput("9.5/2", "(9.5/2)", "4.75");
    }

    @Test
    public void testNegation(){
        testOutput("-9.5", "(9.5)`", "-9.5");
    }

    @Test
    public void testFactorial(){
        testOutput("7!", "(7)!", "5040");
    }

    @Test
    public void testAbs(){
        testOutput("abs(-1)", "abs((1)`)", "1");
        testOutput("abs(1)", "abs(1)", "1");
    }

    @Test
    public void testLn(){
        testEvalError("ln(-1)", "ln((1)`)");
        testOutput("ln2", "ln(2)", "0.6931471805599453094172321214581765680755");
    }

    @Test
    public void testSqrt(){
        testOutput("sqrt0", "sqrt(0)", "0");
        testOutput("sqrt4", "sqrt(4)", "2");
        testOutput("sqrt2", "sqrt(2)", "1.4142135623730950488016887242096980785696");
    }

    @Test
    public void testExp(){
        testOutput("exp0", "exp(0)", "1");
        testOutput("exp1", "exp(1)", "2.718281828459045235360287471352662497757247");
        testOutput("exp300", "exp(300)", "19424263952412559365842088360176992193662086");
        testOutput("exp300", "exp(300)", "19424263952412559365842088360176992193662086");
    }

    @Test
    public void testPow(){
        testOutput("0^2", "(0^2)", "0");
        testOutput("2^0", "(2^0)", "1");
        testOutput("2^1", "(2^1)", "2");
        testOutput("2^-1", "(2^(1)`)", "0.5");
        testOutput("2^300", "(2^300)", "2037035976334486086268445688409378161051468393");
    }

}
