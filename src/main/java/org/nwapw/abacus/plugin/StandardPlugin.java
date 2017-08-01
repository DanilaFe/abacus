package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.function.OperatorType;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.number.PreciseNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiFunction;

/**
 * The plugin providing standard functions such as addition and subtraction to
 * the calculator.
 */
public class StandardPlugin extends Plugin {

    private static HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>> factorialLists = new HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>>();

    /**
     * The addition operator, +
     */
    public static final Operator OP_ADD = new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 0, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length >= 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface sum = params[0];
            for (int i = 1; i < params.length; i++) {
                sum = sum.add(params[i]);
            }
            return sum;
        }
    });
    /**
     * The subtraction operator, -
     */
    public static final Operator OP_SUBTRACT = new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 0, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 2;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return params[0].subtract(params[1]);
        }
    });
    /**
     * The multiplication operator, *
     */
    public static final Operator OP_MULTIPLY = new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 1, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length >= 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface product = params[0];
            for (int i = 1; i < params.length; i++) {
                product = product.multiply(params[i]);
            }
            return product;
        }
    });
    /**
     * The division operator, /
     */
    public static final Operator OP_DIVIDE = new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 1, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length >= 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface product = params[0];
            for (int i = 1; i < params.length; i++) {
                product = product.multiply(params[i]);
            }
            return product;
        }
    });
    /**
     * The factorial operator, !
     */
    public static final Operator OP_FACTORIAL = new Operator(OperatorAssociativity.RIGHT, OperatorType.UNARY_POSTFIX, 0, new Function() {
        //private HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>> storedList = new HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>>();
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            if (params[0].signum() == 0) {
                return (new NaiveNumber(1)).promoteTo(params[0].getClass());
            }
            NumberInterface factorial = params[0];
            NumberInterface multiplier = params[0];
            //It is necessary to later prevent calls of factorial on anything but non-negative integers.
            while ((multiplier = multiplier.subtract(NaiveNumber.ONE.promoteTo(multiplier.getClass()))).signum() == 1) {
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
    /**
     * The caret / pow operator, ^
     */
    public static final Operator OP_CARET = new Operator(OperatorAssociativity.RIGHT, OperatorType.BINARY_INFIX, 2, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 2;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return FUNCTION_EXP.apply(FUNCTION_LN.apply(params[0]).multiply(params[1]));
        }
    });
    /**
     * The absolute value function, abs(-3) = 3
     */
    public static final Function FUNCTION_ABS = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return params[0].multiply((new NaiveNumber(params[0].signum())).promoteTo(params[0].getClass()));
        }
    };
    /**
     * The exponential function, exp(1) = e^1 = 2.71...
     */
    public static final Function FUNCTION_EXP = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface maxError = getMaxError(params[0]);
            int n = 0;
            if(params[0].signum() <= 0){
                NumberInterface currentTerm = NaiveNumber.ONE.promoteTo(params[0].getClass()), sum = currentTerm;
                while(FUNCTION_ABS.apply(currentTerm).compareTo(maxError) > 0){
                    n++;
                    currentTerm = currentTerm.multiply(params[0]).divide((new NaiveNumber(n)).promoteTo(params[0].getClass()));
                    sum = sum.add(currentTerm);
                }
                return sum;
            }
            else{
                //We need n such that x^(n+1) * 3^ceil(x) <= maxError * (n+1)!.
                //right and left refer to lhs and rhs in the above inequality.
                NumberInterface sum = NaiveNumber.ONE.promoteTo(params[0].getClass());
                NumberInterface nextNumerator = params[0];
                NumberInterface left = params[0].multiply((new NaiveNumber(3)).promoteTo(params[0].getClass()).intPow(params[0].ceiling())), right = maxError;
                do{
                    sum = sum.add(nextNumerator.divide(factorial(params[0].getClass(), n+1)));
                    n++;
                    nextNumerator = nextNumerator.multiply(params[0]);
                    left = left.multiply(params[0]);
                    NumberInterface nextN = (new NaiveNumber(n+1)).promoteTo(params[0].getClass());
                    right = right.multiply(nextN);
                    //System.out.println(left + ", " + right);
                }
                while(left.compareTo(right) > 0);
                System.out.println(n+1);
                return sum;
            }
        }
    };
    /**
     * The natural log function.
     */
    public static final Function FUNCTION_LN = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface param = params[0];
            int powersOf2 = 0;
            while (FUNCTION_ABS.apply(param.subtract(NaiveNumber.ONE.promoteTo(param.getClass()))).compareTo((new NaiveNumber(0.1)).promoteTo(param.getClass())) >= 0) {
                if (param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() == 1) {
                    param = param.divide(new NaiveNumber(2).promoteTo(param.getClass()));
                    powersOf2++;
                    if (param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() != 1) {
                        break;
                        //No infinite loop for you.
                    }
                } else {
                    param = param.multiply(new NaiveNumber(2).promoteTo(param.getClass()));
                    powersOf2--;
                    if (param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() != 1) {
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
        private NumberInterface getLogPartialSum(NumberInterface x) {
            NumberInterface maxError = getMaxError(x);
            x = x.subtract(NaiveNumber.ONE.promoteTo(x.getClass())); //Terms used are for log(x+1).
            NumberInterface currentNumerator = x, currentTerm = x, sum = x;
            int n = 1;
            while (FUNCTION_ABS.apply(currentTerm).compareTo(maxError) > 0) {
                n++;
                currentNumerator = currentNumerator.multiply(x).negate();
                currentTerm = currentNumerator.divide(new NaiveNumber(n).promoteTo(x.getClass()));
                sum = sum.add(currentTerm);
            }
            return sum;
        }

        /**
         * Returns natural log of 2 to the required precision of the class of number.
         * @param number a number of the same type as the return type. (Used for precision.)
         * @return the value of log(2) with the appropriate precision.
         */
        private NumberInterface getLog2(NumberInterface number) {
            NumberInterface maxError = getMaxError(number);
            //NumberInterface errorBound = (new NaiveNumber(1)).promoteTo(number.getClass());
            //We'll use the series \sigma_{n >= 1) ((1/3^n + 1/4^n) * 1/n)
            //In the following, a=1/3^n, b=1/4^n, c = 1/n.
            //a is also an error bound.
            NumberInterface a = (new NaiveNumber(1)).promoteTo(number.getClass()), b = a, c = a;
            NumberInterface sum = NaiveNumber.ZERO.promoteTo(number.getClass());
            int n = 0;
            while (a.compareTo(maxError) >= 1) {
                n++;
                a = a.divide((new NaiveNumber(3)).promoteTo(number.getClass()));
                b = b.divide((new NaiveNumber(4)).promoteTo(number.getClass()));
                c = NaiveNumber.ONE.promoteTo(number.getClass()).divide((new NaiveNumber(n)).promoteTo(number.getClass()));
                sum = sum.add(a.add(b).multiply(c));
            }
            return sum;
        }
    };
    /**
     * The square root function.
     */
    public static final Function FUNCTION_SQRT = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return OP_CARET.getFunction().apply(params[0], ((new NaiveNumber(0.5)).promoteTo(params[0].getClass())));
        }
    };

    public StandardPlugin(PluginManager manager) {
        super(manager);
    }

    /**
     * Returns a partial sum of a series whose terms are given by the nthTermFunction, evaluated at x.
     *
     * @param x               the value at which the series is evaluated.
     * @param nthTermFunction the function that returns the nth term of the series, in the format term(x, n).
     * @param n               the number of terms in the partial sum.
     * @return the value of the partial sum that has the same class as x.
     */
    private static NumberInterface sumSeries(NumberInterface x, BiFunction<Integer, NumberInterface, NumberInterface> nthTermFunction, int n) {
        NumberInterface sum = NaiveNumber.ZERO.promoteTo(x.getClass());
        for (int i = 0; i <= n; i++) {
            sum = sum.add(nthTermFunction.apply(i, x));
        }
        return sum;
    }

    /**
     * Returns the maximum error based on the precision of the class of number.
     *
     * @param number Any instance of the NumberInterface in question (should return an appropriate precision).
     * @return the maximum error.
     */
    private static NumberInterface getMaxError(NumberInterface number) {
        return (new NaiveNumber(10)).promoteTo(number.getClass()).intPow(-number.getMaxPrecision());
    }

    @Override
    public void onEnable() {
        registerNumber("naive", NaiveNumber.class);
        registerNumber("precise", PreciseNumber.class);

        registerOperator("+", OP_ADD);
        registerOperator("-", OP_SUBTRACT);
        registerOperator("*", OP_MULTIPLY);
        registerOperator("/", OP_DIVIDE);
        registerOperator("^", OP_CARET);
        registerOperator("!", OP_FACTORIAL);

        registerFunction("abs", FUNCTION_ABS);
        registerFunction("exp", FUNCTION_EXP);
        registerFunction("ln", FUNCTION_LN);
        registerFunction("sqrt", FUNCTION_SQRT);
    }

    @Override
    public void onDisable() {

    }

    public static NumberInterface factorial(Class<? extends NumberInterface> numberClass, int n){
        if(!factorialLists.containsKey(numberClass)){
            factorialLists.put(numberClass, new ArrayList<NumberInterface>());
            factorialLists.get(numberClass).add(NaiveNumber.ONE.promoteTo(numberClass));
            factorialLists.get(numberClass).add(NaiveNumber.ONE.promoteTo(numberClass));
        }
        ArrayList<NumberInterface> list = factorialLists.get(numberClass);
        if(n >= list.size()){
            while(list.size() < n + 16){
                list.add(list.get(list.size()-1).multiply(new NaiveNumber(list.size()).promoteTo(numberClass)));
            }
        }
        return list.get(n);
    }

}
