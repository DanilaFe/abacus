package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.config.Configuration;
import org.nwapw.abacus.exception.DomainException;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.standard.StandardPlugin;
import org.nwapw.abacus.tree.nodes.TreeNode;

public class CalculationTests {

    private static Abacus abacus = new Abacus(new Configuration( "precise", new String[]{}));

    @BeforeClass
    public static void prepareTests() {
        abacus.getPluginManager().addInstantiated(new StandardPlugin(abacus.getPluginManager()));
        abacus.reload();
    }

    private void testOutput(String input, String parseOutput, String output) {
        TreeNode parsedTree = abacus.parseString(input);
        Assert.assertEquals(parsedTree.toString(), parseOutput);
        NumberInterface result = abacus.evaluateTree(parsedTree).getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.toString().startsWith(output));
    }

    private void testDomainException(String input, String parseOutput) {
        TreeNode parsedTree = abacus.parseString(input);
        Assert.assertEquals(parsedTree.toString(), parseOutput);
        try {
            abacus.evaluateTree(parsedTree);
            Assert.fail("Function did not throw DomainException.");
        } catch (DomainException e){ }
    }

    @Test
    public void testAddition() {
        testOutput("9.5+10", "(9.5+10)", "19.5");
    }

    @Test
    public void testSubtraction() {
        testOutput("9.5-10", "(9.5-10)", "-0.5");
    }

    @Test
    public void testMultiplication() {
        testOutput("9.5*10", "(9.5*10)", "95");
    }

    @Test
    public void testDivision() {
        testOutput("9.5/2", "(9.5/2)", "4.75");
    }

    @Test
    public void testNegation() {
        testOutput("-9.5", "(9.5)`", "-9.5");
    }

    @Test
    public void testFactorial() {
        testOutput("7!", "(7)!", "5040");
    }

    @Test
    public void testAbs() {
        testOutput("abs(-1)", "abs((1)`)", "1");
        testOutput("abs(1)", "abs(1)", "1");
    }

    @Test
    public void testLn() {
        testDomainException("ln(-1)", "ln((1)`)");
        testOutput("ln2", "ln(2)", "0.6931471805599453094172321214581765680755");
    }

    @Test
    public void testSqrt() {
        testOutput("sqrt0", "sqrt(0)", "0");
        testOutput("sqrt4", "sqrt(4)", "2");
        testOutput("sqrt2", "sqrt(2)", "1.4142135623730950488016887242096980785696");
    }

    @Test
    public void testExp() {
        testOutput("exp0", "exp(0)", "1");
        testOutput("exp1", "exp(1)", "2.718281828459045235360287471352662497757247");
        testOutput("exp300", "exp(300)", "1.9424263952412559365842088360176992193662086");
        testOutput("exp(-500)", "exp((500)`)", "7.1245764067412855315491573771227552469277568");
    }

    @Test
    public void testPow() {
        testOutput("0^2", "(0^2)", "0");
        testOutput("2^0", "(2^0)", "1");
        testOutput("2^1", "(2^1)", "2");
        testOutput("2^-1", "(2^(1)`)", "0.5");
        testOutput("2^50", "(2^50)", "112589990684262");
        testOutput("7^(-sqrt2*17)", "(7^((sqrt(2)*17))`)", "4.81354609155297814551845300063563");
        testDomainException("0^0", "(0^0)");
        testDomainException("(-13)^.9999", "((13)`^.9999)");
    }

}
