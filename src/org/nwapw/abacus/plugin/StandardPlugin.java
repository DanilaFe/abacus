package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.HashMap;
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
    public void onEnable() {
        registerOperator("+", new Operator(OperatorAssociativity.LEFT, 0, new Function() {
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
        }));

        registerOperator("-", new Operator(OperatorAssociativity.LEFT, 0, new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].subtract(params[1]);
            }
        }));

        registerOperator("*", new Operator(OperatorAssociativity.LEFT, 1, new Function() {
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
        }));

        registerOperator("/", new Operator(OperatorAssociativity.LEFT, 1, new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].divide(params[1]);
            }
        }));

        registerOperator("^", new Operator(OperatorAssociativity.RIGHT, 2, new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return StandardPlugin.this.getFunction("exp").apply(StandardPlugin.this.getFunction("ln").apply(params[0]).multiply(params[1]));
            }
        }));

        registerFunction("!", new Function() {
            //private HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>> storedList = new HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>>();
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
                /*if(!storedList.containsKey(params[0].getClass())){
                    storedList.put(params[0].getClass(), new ArrayList<NumberInterface>());
                    storedList.get(params[0].getClass()).add(NaiveNumber.ONE.promoteTo(params[0].getClass()));
                    storedList.get(params[0].getClass()).add(NaiveNumber.ONE.promoteTo(params[0].getClass()));
                }*/
            }
        });

        registerFunction("abs", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].multiply((new NaiveNumber(params[0].signum())).promoteTo(params[0].getClass()));
            }
        });

        registerFunction("exp", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                boolean takeReciprocal = params[0].signum() == -1;
                params[0] = StandardPlugin.this.getFunction("abs").apply(params[0]);
                NumberInterface sum = sumSeries(params[0], StandardPlugin.this::getExpSeriesTerm, getNTermsExp(getMaxError(params[0]), params[0]));
                if(takeReciprocal){
                    sum = NaiveNumber.ONE.promoteTo(sum.getClass()).divide(sum);
                }
                return sum;
            }
        });

        registerFunction("ln", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                NumberInterface param = params[0];
                int powersOf2 = 0;
                while(StandardPlugin.this.getFunction("abs").apply(param.subtract(NaiveNumber.ONE.promoteTo(param.getClass()))).compareTo((new NaiveNumber(0.1)).promoteTo(param.getClass())) >= 0){
                    if(param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() == 1) {
                        param = param.divide(new NaiveNumber(2).promoteTo(param.getClass()));
                        powersOf2++;
                        if(param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() != 1) {
                            break;
                            //No infinite loop for you.
                        }
                    }
                    else {
                        param = param.multiply(new NaiveNumber(2).promoteTo(param.getClass()));
                        powersOf2--;
                        if(param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() != 1) {
                            break;
                            //No infinite loop for you.
                        }
                    }
                }
                return getLog2(param).multiply((new NaiveNumber(powersOf2)).promoteTo(param.getClass())).add(getLogPartialSum(param));
            }

            /**
             * Returns the partial sum of the Taylor series for logx (around x=1).
             * Automatically determines the number of terms needed based on the precision of x.
             * @param x value at which the series is evaluated. 0 < x < 2. (x=2 is convergent but impractical.)
             * @return the partial sum.
             */
            private NumberInterface getLogPartialSum(NumberInterface x){
                NumberInterface maxError = StandardPlugin.this.getMaxError(x);
                x = x.subtract(NaiveNumber.ONE.promoteTo(x.getClass())); //Terms used are for log(x+1).
                NumberInterface currentTerm = x, sum = x;
                int n = 1;
                while(StandardPlugin.this.getFunction("abs").apply(currentTerm).compareTo(maxError) > 0){
                    n++;
                    currentTerm = currentTerm.multiply(x).multiply((new NaiveNumber(n-1)).promoteTo(x.getClass())).divide((new NaiveNumber(n)).promoteTo(x.getClass())).negate();
                    sum = sum.add(currentTerm);
                }
                return sum;
            }

            /**
             * Returns natural log of 2 to the required precision of the class of number.
             * @param number a number of the same type as the return type. (Used for precision.)
             * @return the value of log(2) with the appropriate precision.
             */
            private NumberInterface getLog2(NumberInterface number){
                NumberInterface maxError = StandardPlugin.this.getMaxError(number);
                //NumberInterface errorBound = (new NaiveNumber(1)).promoteTo(number.getClass());
                //We'll use the series \sigma_{n >= 1) ((1/3^n + 1/4^n) * 1/n)
                //In the following, a=1/3^n, b=1/4^n, c = 1/n.
                //a is also an error bound.
                NumberInterface a = (new NaiveNumber(1)).promoteTo(number.getClass()), b = a, c = a;
                NumberInterface sum = NaiveNumber.ZERO.promoteTo(number.getClass());
                int n = 0;
                while(a.compareTo(maxError) >= 1){
                    n++;
                    a = a.divide((new NaiveNumber(3)).promoteTo(number.getClass()));
                    b = b.divide((new NaiveNumber(4)).promoteTo(number.getClass()));
                    c = NaiveNumber.ONE.promoteTo(number.getClass()).divide((new NaiveNumber(n)).promoteTo(number.getClass()));
                    sum = sum.add(a.add(b).multiply(c));
                }
                return sum;
            }
        });

        registerFunction("sqrt", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return StandardPlugin.this.getFunction("pow").apply(params[0], (new NaiveNumber(0.5)));
            }
        });
    }

    @Override
    public void onDisable() {

    }

    /**
     * Returns the nth term of the Taylor series (centered at 0) of e^x
     * @param n the term required (n >= 0).
     * @param x the real number at which the series is evaluated.
     * @return the nth term of the series.
     */
    private NumberInterface getExpSeriesTerm(int n, NumberInterface x){
        return x.intPow(n).divide(this.getFunction("!").apply((new NaiveNumber(n)).promoteTo(x.getClass())));
    }

    /**
     * Returns the number of terms needed to evaluate the exponential function (at x)
     * such that the error is at most maxError.
     * @param maxError Maximum error permissible (This should probably be positive.)
     * @param x where the function is evaluated.
     * @return the number of terms needed to evaluated the exponential function.
     */
    private int getNTermsExp(NumberInterface maxError, NumberInterface x) {
        //We need n such that |x^(n+1)| <= (n+1)! * maxError
        //The variables LHS and RHS refer to the above inequality.
        int n = 0;
        x = this.getFunction("abs").apply(x);
        NumberInterface LHS = x, RHS = maxError;
        while (LHS.compareTo(RHS) > 0) {
            n++;
            LHS = LHS.multiply(x);
            RHS = RHS.multiply(new NaiveNumber(n + 1).promoteTo(RHS.getClass()));
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
     * @return the maximum error.
     */
    private NumberInterface getMaxError(NumberInterface number){
        return (new NaiveNumber(10)).promoteTo(number.getClass()).intPow(-number.precision());
    }

}
