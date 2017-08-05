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
    
}
