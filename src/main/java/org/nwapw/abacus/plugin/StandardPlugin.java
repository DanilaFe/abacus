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

    /**
     * Stores objects of NumberInterface with integer values for reuse.
     */
    private final static HashMap<Class<? extends NumberInterface>, HashMap<Integer, NumberInterface>> integerValues = new HashMap<>();

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
     * The negation operator, -
     */
    public static final Operator OP_NEGATE = new Operator(OperatorAssociativity.LEFT, OperatorType.UNARY_PREFIX, 0, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return params[0].negate();
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
            return params.length == 2 && params[1].compareTo(NaiveNumber.ZERO.promoteTo(params[1].getClass())) != 0;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return params[0].divide(params[1]);
        }
    });
    /**
     * The factorial operator, !
     */
    public static final Operator OP_FACTORIAL = new Operator(OperatorAssociativity.RIGHT, OperatorType.UNARY_POSTFIX, 0, new Function() {
        //private HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>> storedList = new HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>>();
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1
                    && params[0].fractionalPart().compareTo(NaiveNumber.ZERO.promoteTo(params[0].getClass())) == 0
                    && params[0].signum() >= 0;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            if (params[0].signum() == 0) {
                return fromInt(params[0].getClass(), 1);
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
     * The natural log function.
     */
    public static final Function FUNCTION_LN = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1 && params[0].compareTo(NaiveNumber.ZERO.promoteTo(params[0].getClass())) > 0;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface param = params[0];
            int powersOf2 = 0;
            while (FUNCTION_ABS.apply(param.subtract(NaiveNumber.ONE.promoteTo(param.getClass()))).compareTo(new NaiveNumber(0.1).promoteTo(param.getClass())) >= 0) {
                if (param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() == 1) {
                    param = param.divide(fromInt(param.getClass(), 2));
                    powersOf2++;
                    if (param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() != 1) {
                        break;
                        //No infinite loop for you.
                    }
                } else {
                    param = param.multiply(fromInt(param.getClass(), 2));
                    powersOf2--;
                    if (param.subtract(NaiveNumber.ONE.promoteTo(param.getClass())).signum() != -1) {
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
            //NumberInterface errorBound = fromInt(number.getClass(), 1);
            //We'll use the series \sigma_{n >= 1) ((1/3^n + 1/4^n) * 1/n)
            //In the following, a=1/3^n, b=1/4^n, c = 1/n.
            //a is also an error bound.
            NumberInterface a = fromInt(number.getClass(), 1), b = a, c = a;
            NumberInterface sum = NaiveNumber.ZERO.promoteTo(number.getClass());
            int n = 0;
            while (a.compareTo(maxError) >= 1) {
                n++;
                a = a.divide(fromInt(number.getClass(), 3));
                b = b.divide(fromInt(number.getClass(), 4));
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
    /**
     * The implementation for double-based naive numbers.
     */
    public static final NumberImplementation IMPLEMENTATION_NAIVE = new NumberImplementation(NaiveNumber.class, 0) {
        @Override
        public NumberInterface instanceForString(String string) {
            return new NaiveNumber(string);
        }

        @Override
        public NumberInterface instanceForPi() {
            return new NaiveNumber(Math.PI);
        }
    };
    /**
     * The implementation for the infinite-precision BigDecimal.
     */
    public static final NumberImplementation IMPLEMENTATION_PRECISE = new NumberImplementation(PreciseNumber.class, 0) {
        @Override
        public NumberInterface instanceForString(String string) {
            return new PreciseNumber(string);
        }

        @Override
        public NumberInterface instanceForPi() {
            NumberInterface C = FUNCTION_SQRT.apply(new PreciseNumber("10005")).multiply(new PreciseNumber("426880"));
            NumberInterface M = PreciseNumber.ONE;
            NumberInterface L = new PreciseNumber("13591409");
            NumberInterface X = M;
            NumberInterface sum = L;
            int termsNeeded = C.getMaxPrecision() / 13 + 1;

            NumberInterface lSummand = new PreciseNumber("545140134");
            NumberInterface xMultiplier = new PreciseNumber("262537412")
                    .multiply(new PreciseNumber("1000000000"))
                    .add(new PreciseNumber("640768000"))
                    .negate();
            for (int i = 0; i < termsNeeded; i++) {
                M = M
                        .multiply(new NaiveNumber(12 * i + 2).promoteTo(PreciseNumber.class))
                        .multiply(new NaiveNumber(12 * i + 6).promoteTo(PreciseNumber.class))
                        .multiply(new NaiveNumber(12 * i + 10).promoteTo(PreciseNumber.class))
                        .divide(new NaiveNumber(Math.pow(i + 1, 3)).promoteTo(PreciseNumber.class));
                L = L.add(lSummand);
                X = X.multiply(xMultiplier);
                sum = sum.add(M.multiply(L).divide(X));
            }
            return C.divide(sum);
        }
    };
    private static final HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>> FACTORIAL_LISTS = new HashMap<>();
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
            if (params[0].signum() <= 0) {
                NumberInterface currentTerm = NaiveNumber.ONE.promoteTo(params[0].getClass()), sum = currentTerm;
                while (FUNCTION_ABS.apply(currentTerm).compareTo(maxError) > 0) {
                    n++;
                    currentTerm = currentTerm.multiply(params[0]).divide((new NaiveNumber(n)).promoteTo(params[0].getClass()));
                    sum = sum.add(currentTerm);
                }
                return sum;
            } else {
                //We need n such that x^(n+1) * 3^ceil(x) <= maxError * (n+1)!.
                //right and left refer to lhs and rhs in the above inequality.
                NumberInterface sum = NaiveNumber.ONE.promoteTo(params[0].getClass());
                NumberInterface nextNumerator = params[0];
                NumberInterface left = params[0].multiply(fromInt(params[0].getClass(), 3).intPow(params[0].ceiling().intValue())), right = maxError;
                do {
                    sum = sum.add(nextNumerator.divide(factorial(params[0].getClass(), n + 1)));
                    n++;
                    nextNumerator = nextNumerator.multiply(params[0]);
                    left = left.multiply(params[0]);
                    NumberInterface nextN = (new NaiveNumber(n + 1)).promoteTo(params[0].getClass());
                    right = right.multiply(nextN);
                    //System.out.println(left + ", " + right);
                }
                while (left.compareTo(right) > 0);
                //System.out.println(n+1);
                return sum;
            }
        }
    };
    /**
     * The caret / pow operator, ^
     */
    public static final Operator OP_CARET = new Operator(OperatorAssociativity.RIGHT, OperatorType.BINARY_INFIX, 2, new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 2
                    && !(params[0].compareTo(NaiveNumber.ZERO.promoteTo(params[0].getClass())) == 0
                    && params[1].compareTo(NaiveNumber.ZERO.promoteTo(params[1].getClass())) == 0)
                    && !(params[0].signum() == -1 && params[1].fractionalPart().compareTo(NaiveNumber.ZERO.promoteTo(params[1].getClass())) != 0);
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            if (params[0].compareTo(NaiveNumber.ZERO.promoteTo(params[0].getClass())) == 0)
                return NaiveNumber.ZERO.promoteTo(params[0].getClass());
            else if (params[1].compareTo(NaiveNumber.ZERO.promoteTo(params[0].getClass())) == 0)
                return NaiveNumber.ONE.promoteTo(params[1].getClass());
            //Detect integer bases:
            if(params[0].fractionalPart().compareTo(fromInt(params[0].getClass(), 0)) == 0
                    && FUNCTION_ABS.apply(params[0]).compareTo(fromInt(params[0].getClass(), Integer.MAX_VALUE)) < 0
                    && FUNCTION_ABS.apply(params[1]).compareTo(fromInt(params[1].getClass(), 1)) >= 0){
                NumberInterface[] newParams = {params[0], params[1].fractionalPart()};
                return params[0].intPow(params[1].floor().intValue()).multiply(applyInternal(newParams));
            }
            return FUNCTION_EXP.apply(FUNCTION_LN.apply(FUNCTION_ABS.apply(params[0])).multiply(params[1]));
        }
    });
    /**
     * The sine function (the argument is interpreted in radians).
     */
    public final Function functionSin = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            NumberInterface pi = getPi(params[0].getClass());
            NumberInterface twoPi = pi.multiply(fromInt(pi.getClass(), 2));
            NumberInterface theta = getSmallAngle(params[0], pi);
            //System.out.println(theta);
            if (theta.compareTo(pi.multiply(new NaiveNumber(1.5).promoteTo(twoPi.getClass()))) >= 0) {
                theta = theta.subtract(twoPi);
            } else if (theta.compareTo(pi.divide(fromInt(pi.getClass(), 2))) > 0) {
                theta = pi.subtract(theta);
            }
            //System.out.println(theta);
            return sinTaylor(theta);
        }
    };
    /**
     * The cosine function (the argument is in radians).
     */
    public final Function functionCos = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return functionSin.apply(getPi(params[0].getClass()).divide(fromInt(params[0].getClass(), 2))
                    .subtract(params[0]));
        }
    };
    /**
     * The tangent function (the argument is in radians).
     */
    public final Function functionTan = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return functionSin.apply(params[0]).divide(functionCos.apply(params[0]));
        }
    };
    /**
     * The secant function (the argument is in radians).
     */
    public final Function functionSec = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return NaiveNumber.ONE.promoteTo(params[0].getClass()).divide(functionCos.apply(params[0]));
        }
    };
    /**
     * The cosecant function (the argument is in radians).
     */
    public final Function functionCsc = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return NaiveNumber.ONE.promoteTo(params[0].getClass()).divide(functionSin.apply(params[0]));
        }
    };
    /**
     * The cotangent function (the argument is in radians).
     */
    public final Function functionCot = new Function() {
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            return functionCos.apply(params[0]).divide(functionSin.apply(params[0]));
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
        return fromInt(number.getClass(), 10).intPow(-number.getMaxPrecision());
    }

    /**
     * A factorial function that uses memoization for each number class; it efficiently
     * computes factorials of non-negative integers.
     *
     * @param numberClass type of number to return.
     * @param n           non-negative integer.
     * @return a number of numClass with value n factorial.
     */
    public static NumberInterface factorial(Class<? extends NumberInterface> numberClass, int n) {
        if (!FACTORIAL_LISTS.containsKey(numberClass)) {
            FACTORIAL_LISTS.put(numberClass, new ArrayList<>());
            FACTORIAL_LISTS.get(numberClass).add(NaiveNumber.ONE.promoteTo(numberClass));
            FACTORIAL_LISTS.get(numberClass).add(NaiveNumber.ONE.promoteTo(numberClass));
        }
        ArrayList<NumberInterface> list = FACTORIAL_LISTS.get(numberClass);
        if (n >= list.size()) {
            while (list.size() < n + 16) {
                list.add(list.get(list.size() - 1).multiply(new NaiveNumber(list.size()).promoteTo(numberClass)));
            }
        }
        return list.get(n);
    }

    /**
     * Returns the value of the Taylor series for sin (centered at 0) at x.
     *
     * @param x where the series is evaluated.
     * @return the value of the series
     */
    private static NumberInterface sinTaylor(NumberInterface x) {
        NumberInterface power = x, multiplier = x.multiply(x).negate(), currentTerm = x, sum = x;
        NumberInterface maxError = getMaxError(x);
        int n = 1;
        do {
            n += 2;
            power = power.multiply(multiplier);
            currentTerm = power.divide(factorial(x.getClass(), n));
            sum = sum.add(currentTerm);
        } while (FUNCTION_ABS.apply(currentTerm).compareTo(maxError) > 0);
        return sum;
    }

    /**
     * Returns an equivalent angle in the interval [0, 2pi)
     *
     * @param phi an angle (in radians).
     * @return theta in [0, 2pi) that differs from phi by a multiple of 2pi.
     */
    private static NumberInterface getSmallAngle(NumberInterface phi, NumberInterface pi) {
        NumberInterface twoPi = pi.multiply(new NaiveNumber("2").promoteTo(phi.getClass()));
        NumberInterface theta = FUNCTION_ABS.apply(phi).subtract(twoPi
                .multiply(FUNCTION_ABS.apply(phi).divide(twoPi).floor())); //Now theta is in [0, 2pi).
        if (phi.signum() < 0) {
            theta = twoPi.subtract(theta);
        }
        return theta;
    }

    /**
     * Returns a number of class numType with value n.
     * @param numType class of number to return.
     * @param n value of returned number.
     * @return numClass instance with value n.
     */
    private static NumberInterface fromInt(Class<? extends NumberInterface> numType, int n){
        if(!integerValues.containsKey(numType)){
            integerValues.put(numType, new HashMap<>());
        }
        if(!integerValues.get(numType).containsKey(n)){
            integerValues.get(numType).put(n, new NaiveNumber(n).promoteTo(numType));
        }
        return integerValues.get(numType).get(n);
    }

    @Override
    public void onEnable() {
        registerNumberImplementation("naive", IMPLEMENTATION_NAIVE);
        registerNumberImplementation("precise", IMPLEMENTATION_PRECISE);

        registerOperator("+", OP_ADD);
        registerOperator("-", OP_SUBTRACT);
        registerOperator("`", OP_NEGATE);
        registerOperator("*", OP_MULTIPLY);
        registerOperator("/", OP_DIVIDE);
        registerOperator("^", OP_CARET);
        registerOperator("!", OP_FACTORIAL);

        registerFunction("abs", FUNCTION_ABS);
        registerFunction("exp", FUNCTION_EXP);
        registerFunction("ln", FUNCTION_LN);
        registerFunction("sqrt", FUNCTION_SQRT);
        registerFunction("sin", functionSin);
        registerFunction("cos", functionCos);
        registerFunction("tan", functionTan);
        registerFunction("sec", functionSec);
        registerFunction("csc", functionCsc);
        registerFunction("cot", functionCot);
    }

    @Override
    public void onDisable() {

    }
}
