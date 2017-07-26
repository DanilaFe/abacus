package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;

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

        System.out.println(getExpSeriesTerm(4, new NaiveNumber(3)));
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

}
