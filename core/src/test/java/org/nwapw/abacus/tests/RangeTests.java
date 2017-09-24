package org.nwapw.abacus.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.config.Configuration;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.number.NumberRange;
import org.nwapw.abacus.number.PreciseNumber;
import org.nwapw.abacus.plugin.standard.StandardPlugin;

import java.util.function.Function;

public class RangeTests {

    private static Abacus abacus = new Abacus(new Configuration( "precise", new String[]{}));
    private static Function<NumberInterface, NumberInterface> naivePromotion = i -> new NaiveNumber((i.toString()));
    private static Function<NumberInterface, NumberInterface> precisePromotion = i -> new PreciseNumber((i.toString()));

    @BeforeClass
    public static void prepareTests() {
        abacus.getPluginManager().addInstantiated(new StandardPlugin(abacus.getPluginManager()));
        abacus.reload();
    }

    public static NumberRange naiveRange(String bottom, String top) {
        return new NaiveNumber(bottom).rangeTo(new NaiveNumber(top)).with(abacus);
    }

    @Test
    public void testNaiveRange(){
        NumberRange range = naiveRange("0", "10");
        Assert.assertTrue(range.getStart().toString().startsWith("0"));
        Assert.assertTrue(range.getEndInclusive().toString().startsWith("10"));
    }

    @Test
    public void testNaiveRangeBelow() {
        NumberRange range = naiveRange("0", "10");
        Assert.assertFalse(range.contains(new NaiveNumber("-10")));
    }

    @Test
    public void testNaiveRangeAbove() {
        NumberRange range = naiveRange("0", "10");
        Assert.assertFalse(range.contains(new NaiveNumber("20")));
    }

    @Test
    public void testNaiveRangeJustWithinBottom() {
        NumberRange range = naiveRange("0", "10");
        Assert.assertTrue(range.contains(new NaiveNumber("0")));
    }

    @Test
    public void testNaiveRangeJustWithinTop() {
        NumberRange range = naiveRange("0", "10");
        Assert.assertTrue(range.contains(new NaiveNumber("10")));
    }

    @Test
    public void testNaiveRangeWithin() {
        NumberRange range = naiveRange("0", "10");
        Assert.assertTrue(range.contains(new NaiveNumber("5")));
    }

    public static void addTestPromotionPaths() {
        StandardPlugin.IMPLEMENTATION_NAIVE.getPromotionPaths().put("precise", precisePromotion);
        StandardPlugin.IMPLEMENTATION_PRECISE.getPromotionPaths().put("naive", naivePromotion);
        abacus.reload();
    }

    public static void removeTestPromotionPaths() {
        StandardPlugin.IMPLEMENTATION_NAIVE.getPromotionPaths().remove("precise");
        StandardPlugin.IMPLEMENTATION_NAIVE.getPromotionPaths().remove("naive");
        abacus.reload();
    }

    @Test
    public void testPromotionWithin() {
        addTestPromotionPaths();
        NumberRange range = naiveRange("0", "10");
        Assert.assertTrue(range.contains(new PreciseNumber("5")));
        removeTestPromotionPaths();
    }

    @Test
    public void testPromotionOutside(){
        addTestPromotionPaths();
        NumberRange range = naiveRange("0","10");
        Assert.assertFalse(range.contains(new PreciseNumber("20")));
        removeTestPromotionPaths();
    }

}
