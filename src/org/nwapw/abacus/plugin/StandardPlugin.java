package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;

import java.util.function.BiFunction;

/**
 * The plugin providing standard functions such as addition and subtraction to
 * the calculator.
 */
public class StandardPlugin extends Plugin {

    public StandardPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public void load() {
        registerFunction("+", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length >= 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                NumberInterface sum = params[0];
                for(int i = 1; i < params.length; i++){
                    sum = sum.add(params[i]);
                }
                return sum;
            }
        });

        registerFunction("-", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].subtract(params[1]);
            }
        });

        registerFunction("*", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length >= 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                NumberInterface product = params[0];
                for(int i = 1; i < params.length; i++){
                    product = product.multiply(params[i]);
                }
                return product;
            }
        });

        registerFunction("/", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].divide(params[1]);
            }
        });

        registerFunction("!", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                if(params[0].signum() == 0){
                    return (new NaiveNumber(1)).promoteTo(params[0].getClass());
                }
                NumberInterface factorial = params[0];
                NumberInterface multiplier = params[0];
                //It is necessary to later prevent calls of factorial on anything but non-negative integers.
                while((multiplier = multiplier.subtract(NaiveNumber.ONE.promoteTo(multiplier.getClass()))).signum() == 1){
                    factorial = factorial.multiply(multiplier);
                }
                return factorial;
            }
        });

        registerFunction("exp", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return sumSeries(params[0], StandardPlugin.this::getExpSeriesTerm, getNTermsExp(getMaxError(params[0]), params[0]));
            }
        });
    }

    /**
     * Returns the nth term of the Taylor series (centered at 0) of e^x
     * @param n the term required (n >= 0).
     * @param x the real number at which the series is evaluated.
     * @return
     */
    private NumberInterface getExpSeriesTerm(int n, NumberInterface x){
        return x.intPow(n).divide(this.getFunction("!").apply((new NaiveNumber(n)).promoteTo(x.getClass())));
    }

    /**
     * Returns the number of terms needed to evaluate the exponential function (at x)
     * such that the error is at most maxError.
     * @param maxError Maximum error permissible (This should probably be positive.)
     * @param x where the function is evaluated.
     * @return
     */
    private int getNTermsExp(NumberInterface maxError, NumberInterface x){
        //We need n such that x^(n+2) <= (n+1)! * maxError
        //The variables LHS and RHS refer to the above inequality.
        int n = 0;
        NumberInterface LHS = x.intPow(2), RHS = maxError;
        while(LHS.compareTo(RHS) > 0){
            n++;
            LHS = LHS.multiply(x);
            RHS = RHS.multiply(new NaiveNumber(n).promoteTo(RHS.getClass()));
        }
        return n;
    }


    /**
     * Returns a partial sum of a series whose terms are given by the nthTermFunction, evaluated at x.
     * @param x the value at which the series is evaluated.
     * @param nthTermFunction the function that returns the nth term of the series, in the format term(x, n).
     * @param n the number of terms in the partial sum.
     * @return the value of the partial sum that has the same class as x.
     */
    private NumberInterface sumSeries(NumberInterface x, BiFunction<Integer, NumberInterface, NumberInterface> nthTermFunction, int n){
        NumberInterface sum = NaiveNumber.ZERO.promoteTo(x.getClass());
        for(int i = 0; i <= n; i++){
            sum = sum.add(nthTermFunction.apply(i, x));
        }
        return sum;
    }

    /**
     * Returns the maximum error based on the precision of the class of number.
     * @param number Any instance of the NumberInterface in question (should return an appropriate precision).
     * @return
     */
    private NumberInterface getMaxError(NumberInterface number){
        return (new NaiveNumber(10)).promoteTo(number.getClass()).intPow(-number.precision());
    }

}
