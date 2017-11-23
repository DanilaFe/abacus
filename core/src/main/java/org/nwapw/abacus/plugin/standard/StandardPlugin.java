package org.nwapw.abacus.plugin.standard;

import org.jetbrains.annotations.NotNull;
import org.nwapw.abacus.context.MutableEvaluationContext;
import org.nwapw.abacus.context.PluginEvaluationContext;
import org.nwapw.abacus.function.Documentation;
import org.nwapw.abacus.function.DocumentationType;
import org.nwapw.abacus.function.interfaces.NumberFunction;
import org.nwapw.abacus.function.interfaces.NumberOperator;
import org.nwapw.abacus.function.interfaces.TreeValueOperator;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.number.standard.NaiveNumber;
import org.nwapw.abacus.number.standard.PreciseNumber;
import org.nwapw.abacus.plugin.NumberImplementation;
import org.nwapw.abacus.plugin.Plugin;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.standard.operator.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The plugin providing standard functions such as addition and subtraction to
 * the calculator.
 */
public class StandardPlugin extends Plugin {

    /**
     * The set operator.
     */
    public static final TreeValueOperator OP_SET = new OperatorSet();
    /**
     * The define operator.
     */
    public final TreeValueOperator OP_DEFINE = new OperatorDefine();
    /**
     * The addition operator, +
     */
    public static final NumberOperator OP_ADD = new OperatorAdd();
    /**
     * The subtraction operator, -
     */
    public static final NumberOperator OP_SUBTRACT = new OperatorSubtract();
    /**
     * The negation operator, -
     */
    public static final NumberOperator OP_NEGATE = new OperatorNegate();
    /**
     * The multiplication operator, *
     */
    public static final NumberOperator OP_MULTIPLY = new OperatorMultiply();
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
            MutableEvaluationContext dummyContext = new MutableEvaluationContext(null, this, null);
            NumberInterface C = FUNCTION_SQRT.apply(dummyContext, new PreciseNumber("10005")).multiply(new PreciseNumber("426880"));
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
                        .multiply(new PreciseNumber((12 * i + 2) + ""))
                        .multiply(new PreciseNumber((12 * i + 6) + ""))
                        .multiply(new PreciseNumber((12 * i + 10) + ""))
                        .divide(new PreciseNumber(Math.pow(i + 1, 3) + ""));
                L = L.add(lSummand);
                X = X.multiply(xMultiplier);
                sum = sum.add(M.multiply(L).divide(X));
            }
            return C.divide(sum);
        }
    };
    /**
     * The division operator, /
     */
    public static final NumberOperator OP_DIVIDE = new OperatorDivide();
    /**
     * The factorial operator, !
     */
    public static final NumberOperator OP_FACTORIAL = new OperatorFactorial();
    /**
     * The permutation operator.
     */
    public static final NumberOperator OP_NPR = new OperatorNpr();
    /**
     * The combination operator.
     */
    public static final NumberOperator OP_NCR = new OperatorNcr();
    /**
     * The absolute value function, abs(-3) = 3
     */
    public static final NumberFunction FUNCTION_ABS = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return params[0].multiply(context.getInheritedNumberImplementation().instanceForString(Integer.toString(params[0].signum())));
        }
    };
    /**
     * The natural log function.
     */
    public static final NumberFunction FUNCTION_LN = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1 && params[0].compareTo(context.getInheritedNumberImplementation().instanceForString("0")) > 0;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberImplementation implementation = context.getInheritedNumberImplementation();
            NumberInterface param = params[0];
            NumberInterface one = implementation.instanceForString("1");
            int powersOf2 = 0;
            while (FUNCTION_ABS.apply(context, param.subtract(one)).compareTo(implementation.instanceForString(".1")) >= 0) {
                if (param.subtract(one).signum() == 1) {
                    param = param.divide(implementation.instanceForString("2"));
                    powersOf2++;
                    if (param.subtract(one).signum() != 1) {
                        break;
                        //No infinite loop for you.
                    }
                } else {
                    param = param.multiply(implementation.instanceForString("2"));
                    powersOf2--;
                    if (param.subtract(one).signum() != -1) {
                        break;
                        //No infinite loop for you.
                    }
                }
            }
            return getLog2(context.getInheritedNumberImplementation(), param).multiply(implementation.instanceForString(Integer.toString(powersOf2))).add(getLogPartialSum(context, param));
        }

        /**
         * Returns the partial sum of the Taylor series for logx (around x=1).
         * Automatically determines the number of terms needed based on the precision of x.
         *
         * @param context
         * @param x value at which the series is evaluated. 0 < x < 2. (x=2 is convergent but impractical.)
         * @return the partial sum.
         */
        private NumberInterface getLogPartialSum(PluginEvaluationContext context, NumberInterface x) {
            NumberImplementation implementation = context.getInheritedNumberImplementation();
            NumberInterface maxError = x.getMaxError();
            x = x.subtract(implementation.instanceForString("1")); //Terms used are for log(x+1).
            NumberInterface currentNumerator = x, currentTerm = x, sum = x;
            int n = 1;
            while (FUNCTION_ABS.apply(context, currentTerm).compareTo(maxError) > 0) {
                n++;
                currentNumerator = currentNumerator.multiply(x).negate();
                currentTerm = currentNumerator.divide(implementation.instanceForString(Integer.toString(n)));
                sum = sum.add(currentTerm);
            }
            return sum;
        }

        /**
         * Returns natural log of 2 to the required precision of the class of number.
         * @param number a number of the same type as the return type. (Used for precision.)
         * @return the value of log(2) with the appropriate precision.
         */
        private NumberInterface getLog2(NumberImplementation implementation, NumberInterface number) {
            NumberInterface maxError = number.getMaxError();
            //NumberInterface errorBound = implementation.instanceForString("1");
            //We'll use the series \sigma_{n >= 1) ((1/3^n + 1/4^n) * 1/n)
            //In the following, a=1/3^n, b=1/4^n, c = 1/n.
            //a is also an error bound.
            NumberInterface a = implementation.instanceForString("1"), b = a, c;
            NumberInterface sum = implementation.instanceForString("0");
            NumberInterface one = implementation.instanceForString("1");
            int n = 0;
            while (a.compareTo(maxError) >= 1) {
                n++;
                a = a.divide(implementation.instanceForString("3"));
                b = b.divide(implementation.instanceForString("4"));
                c = one.divide(implementation.instanceForString(Integer.toString(n)));
                sum = sum.add(a.add(b).multiply(c));
            }
            return sum;
        }
    };
    /**
     * Gets a random number smaller or equal to the given number's integer value.
     */
    public static final NumberFunction FUNCTION_RAND_INT = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return context.getInheritedNumberImplementation().instanceForString(Long.toString(Math.round(Math.random() * params[0].floor().intValue())));
        }
    };
    /**
     * The caret / pow operator, ^
     */
    public static final NumberOperator OP_CARET = new OperatorCaret();
    /**
     * The square root function.
     */
    public static final NumberFunction FUNCTION_SQRT = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return OP_CARET.apply(context, params[0], context.getInheritedNumberImplementation().instanceForString(".5"));
        }
    };
    private static final HashMap<NumberImplementation, ArrayList<NumberInterface>> FACTORIAL_LISTS = new HashMap<>();
    /**
     * The exponential function, exp(1) = e^1 = 2.71...
     */
    public static final NumberFunction FUNCTION_EXP = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberImplementation implementation = context.getInheritedNumberImplementation();
            NumberInterface maxError = params[0].getMaxError();
            int n = 0;
            if (params[0].signum() < 0) {
                NumberInterface[] negatedParams = {params[0].negate()};
                return implementation.instanceForString("1").divide(applyInternal(context, negatedParams));
            } else {
                //We need n such that x^(n+1) * 3^ceil(x) <= maxError * (n+1)!.
                //right and left refer to lhs and rhs in the above inequality.
                NumberInterface sum = implementation.instanceForString("1");
                NumberInterface nextNumerator = params[0];
                NumberInterface left = params[0].multiply(implementation.instanceForString("3").intPow(params[0].ceiling().intValue())), right = maxError;
                do {
                    sum = sum.add(nextNumerator.divide(factorial(implementation, n + 1)));
                    n++;
                    nextNumerator = nextNumerator.multiply(params[0]);
                    left = left.multiply(params[0]);
                    NumberInterface nextN = implementation.instanceForString(Integer.toString(n + 1));
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
     * The sine function (the argument is interpreted in radians).
     */
    public final NumberFunction functionSin = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberImplementation implementation = context.getInheritedNumberImplementation();
            NumberInterface pi = piFor(params[0].getClass());
            NumberInterface twoPi = pi.multiply(implementation.instanceForString("2"));
            NumberInterface theta = getSmallAngle(context, params[0], pi);
            //System.out.println(theta);
            if (theta.compareTo(pi.multiply(implementation.instanceForString("1.5"))) >= 0) {
                theta = theta.subtract(twoPi);
            } else if (theta.compareTo(pi.divide(implementation.instanceForString("2"))) > 0) {
                theta = pi.subtract(theta);
            }
            //System.out.println(theta);
            return sinTaylor(context, theta);
        }
    };
    /**
     * The cosine function (the argument is in radians).
     */
    public final NumberFunction functionCos = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return functionSin.apply(context, piFor(params[0].getClass()).divide(context.getInheritedNumberImplementation().instanceForString("2"))
                    .subtract(params[0]));
        }
    };
    /**
     * The tangent function (the argument is in radians).
     */
    public final NumberFunction functionTan = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return functionSin.apply(context, params[0]).divide(functionCos.apply(context, params[0]));
        }
    };
    /**
     * The secant function (the argument is in radians).
     */
    public final NumberFunction functionSec = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return context.getInheritedNumberImplementation().instanceForString("1").divide(functionCos.apply(context, params[0]));
        }
    };
    /**
     * The cosecant function (the argument is in radians).
     */
    public final NumberFunction functionCsc = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return context.getInheritedNumberImplementation().instanceForString("1").divide(functionSin.apply(context, params[0]));
        }
    };
    /**
     * The cotangent function (the argument is in radians).
     */
    public final NumberFunction functionCot = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return functionCos.apply(context, params[0]).divide(functionSin.apply(context, params[0]));
        }
    };

    /**
     * The arcsine function (return type in radians).
     */
    public final NumberFunction functionArcsin = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1
                    && FUNCTION_ABS.apply(context, params[0]).compareTo(context.getInheritedNumberImplementation().instanceForString("1")) <= 0;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberImplementation implementation = context.getInheritedNumberImplementation();
            if (FUNCTION_ABS.apply(context, params[0]).compareTo(implementation.instanceForString(".8")) >= 0) {
                NumberInterface[] newParams = {FUNCTION_SQRT.apply(context, implementation.instanceForString("1").subtract(params[0].multiply(params[0])))};
                return piFor(params[0].getClass()).divide(implementation.instanceForString("2"))
                        .subtract(applyInternal(context, newParams)).multiply(implementation.instanceForString(Integer.toString(params[0].signum())));
            }
            NumberInterface currentTerm = params[0], sum = currentTerm,
                    multiplier = currentTerm.multiply(currentTerm), summandBound = sum.getMaxError().multiply(implementation.instanceForString("1").subtract(multiplier)),
                    power = currentTerm, coefficient = implementation.instanceForString("1");
            int exponent = 1;
            while (FUNCTION_ABS.apply(context, currentTerm).compareTo(summandBound) > 0) {
                exponent += 2;
                power = power.multiply(multiplier);
                coefficient = coefficient.multiply(implementation.instanceForString(Integer.toString(exponent - 2)))
                        .divide(implementation.instanceForString(Integer.toString(exponent - 1)));
                currentTerm = power.multiply(coefficient).divide(implementation.instanceForString(Integer.toString(exponent)));
                sum = sum.add(currentTerm);
            }
            return sum;
        }
    };

    /**
     * The arccosine function.
     */
    public final NumberFunction functionArccos = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1 && FUNCTION_ABS.apply(context, params[0]).compareTo(context.getInheritedNumberImplementation().instanceForString("1")) <= 0;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return piFor(params[0].getClass()).divide(context.getInheritedNumberImplementation().instanceForString("2"))
                    .subtract(functionArcsin.apply(context, params));
        }
    };

    /**
     * The arccosecant function.
     */
    public final NumberFunction functionArccsc = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1 && FUNCTION_ABS.apply(context, params[0]).compareTo(context.getInheritedNumberImplementation().instanceForString("1")) >= 0;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberInterface[] reciprocalParamArr = {context.getInheritedNumberImplementation().instanceForString("1").divide(params[0])};
            return functionArcsin.apply(context, reciprocalParamArr);
        }
    };

    /**
     * The arcsecant function.
     */
    public final NumberFunction functionArcsec = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1 && FUNCTION_ABS.apply(context, params[0]).compareTo(context.getInheritedNumberImplementation().instanceForString("1")) >= 0;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberInterface[] reciprocalParamArr = {context.getInheritedNumberImplementation().instanceForString("1").divide(params[0])};
            return functionArccos.apply(context, reciprocalParamArr);
        }
    };

    /**
     * The arctangent function.
     */
    public final NumberFunction functionArctan = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            NumberImplementation implementation = context.getInheritedNumberImplementation();
            if (params[0].signum() == -1) {
                NumberInterface[] negatedParams = {params[0].negate()};
                return applyInternal(context, negatedParams).negate();
            }
            if (params[0].compareTo(implementation.instanceForString("1")) > 0) {
                NumberInterface[] reciprocalParams = {implementation.instanceForString("1").divide(params[0])};
                return piFor(params[0].getClass()).divide(implementation.instanceForString("2"))
                        .subtract(applyInternal(context, reciprocalParams));
            }
            if (params[0].compareTo(implementation.instanceForString("1")) == 0) {
                return piFor(params[0].getClass()).divide(implementation.instanceForString("4"));
            }
            if (params[0].compareTo(implementation.instanceForString(".9")) >= 0) {
                NumberInterface[] newParams = {params[0].multiply(implementation.instanceForString("2"))
                        .divide(implementation.instanceForString("1").subtract(params[0].multiply(params[0])))};
                return applyInternal(context, newParams).divide(implementation.instanceForString("2"));
            }
            NumberInterface currentPower = params[0], currentTerm = currentPower, sum = currentTerm,
                    maxError = params[0].getMaxError(), multiplier = currentPower.multiply(currentPower).negate();
            int n = 1;
            while (FUNCTION_ABS.apply(context, currentTerm).compareTo(maxError) > 0) {
                n += 2;
                currentPower = currentPower.multiply(multiplier);
                currentTerm = currentPower.divide(implementation.instanceForString(Integer.toString(n)));
                sum = sum.add(currentTerm);
            }
            return sum;
        }
    };

    /**
     * The arccotangent function. Range: (0, pi).
     */
    public final NumberFunction functionArccot = new NumberFunction() {
        @Override
        public boolean matchesParams(PluginEvaluationContext context, NumberInterface[] params) {
            return params.length == 1;
        }

        @Override
        public NumberInterface applyInternal(PluginEvaluationContext context, NumberInterface[] params) {
            return piFor(params[0].getClass()).divide(context.getInheritedNumberImplementation().instanceForString("2"))
                    .subtract(functionArctan.apply(context, params));
        }
    };

    public StandardPlugin(PluginManager manager) {
        super(manager);
    }

    /**
     * A factorial function that uses memoization for each number class; it efficiently
     * computes factorials of non-negative integers.
     *
     * @param implementation type of number to return.
     * @param n              non-negative integer.
     * @return a number of numClass with value n factorial.
     */
    synchronized public static NumberInterface factorial(NumberImplementation implementation, int n) {
        if (!FACTORIAL_LISTS.containsKey(implementation)) {
            FACTORIAL_LISTS.put(implementation, new ArrayList<>());
            FACTORIAL_LISTS.get(implementation).add(implementation.instanceForString("1"));
            FACTORIAL_LISTS.get(implementation).add(implementation.instanceForString("1"));
        }
        ArrayList<NumberInterface> list = FACTORIAL_LISTS.get(implementation);
        if (n >= list.size()) {
            while (list.size() < n + 16) {
                list.add(list.get(list.size() - 1).multiply(implementation.instanceForString(Integer.toString(list.size()))));
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
    private static NumberInterface sinTaylor(PluginEvaluationContext context, NumberInterface x) {
        NumberInterface power = x, multiplier = x.multiply(x).negate(), currentTerm, sum = x;
        NumberInterface maxError = x.getMaxError();
        int n = 1;
        do {
            n += 2;
            power = power.multiply(multiplier);
            currentTerm = power.divide(factorial(context.getInheritedNumberImplementation(), n));
            sum = sum.add(currentTerm);
        } while (FUNCTION_ABS.apply(context, currentTerm).compareTo(maxError) > 0);
        return sum;
    }

    /**
     * Returns an equivalent angle in the interval [0, 2pi)
     *
     * @param phi an angle (in radians).
     * @return theta in [0, 2pi) that differs from phi by a multiple of 2pi.
     */
    private static NumberInterface getSmallAngle(PluginEvaluationContext context, NumberInterface phi, NumberInterface pi) {
        NumberInterface twoPi = pi.multiply(context.getInheritedNumberImplementation().instanceForString("2"));
        NumberInterface theta = FUNCTION_ABS.apply(context, phi).subtract(twoPi
                .multiply(FUNCTION_ABS.apply(context, phi).divide(twoPi).floor())); //Now theta is in [0, 2pi).
        if (phi.signum() < 0) {
            theta = twoPi.subtract(theta);
        }
        return theta;
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

        registerTreeValueOperator("=", OP_SET);
        registerTreeValueOperator(":=", OP_DEFINE);

        registerOperator("nPr", OP_NPR);
        registerOperator("nCr", OP_NCR);

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

        registerFunction("arcsin", functionArcsin);
        registerFunction("arccos", functionArccos);
        registerFunction("arctan", functionArctan);
        registerFunction("arcsec", functionArcsec);
        registerFunction("arccsc", functionArccsc);
        registerFunction("arccot", functionArccot);

        registerFunction("random_int", FUNCTION_RAND_INT);

        registerDocumentation(new Documentation("abs", "Absolute Value", "Finds the distance " +
                "from zero of a number.", "Given a number, this function finds the distance form " +
                "zero of a number, effectively turning negative numbers into positive ones.\n\n" +
                "Example: abs(-2) -> 2", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("exp", "Exponentiate", "Brings e to the given power.",
                "This function evaluates e to the power of the given value, and is the inverse " +
                        "of the natural logarithm.\n\n" +
                        "Example: exp(1) -> 2.718...", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("ln", "Natural Logarithm", "Gets the natural " +
                "logarithm of the given value.", "The natural logarithm of a number is " +
                "the power that e has to be brought to to be equal to the number.\n\n" +
                "Example: ln(2.718) -> 1", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("sqrt", "Square Root", "Finds the square root " +
                "of the number.", "A square root a of a number is defined as the non-negative a such that a times a is equal " +
                "to that number.\n\n" +
                "Example: sqrt(4) -> 2", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("sin", "Sine", "Computes the sine of the given angle, " +
                "in radians.", "Example: sin(pi/6) -> 0.5", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("cos", "Cosine", "Computes the cosine of the given angle, " +
                "in radians.", "Example: cos(pi/6) -> 0.866... (the exact result is sqrt(3)/2)", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("tan", "Tangent", "Computes the tangent of the given angle, " +
                "in radians.", "Example: tan(pi/6) -> 0.577... (the exact result is 1/sqrt(3))", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("sec", "Secant", "Computes the secant of the given angle, " +
                "in radians.", "Example: sec(pi/6) -> 1.154... (the exact result is 2/sqrt(3))", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("csc", "Cosecant", "Computes the cosecant of the given angle, " +
                "in radians.", "Example: csc(pi/6) -> 2", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("cot", "Cotangent", "Computes the cotangent of the given angle, " +
                "in radians.", "Example: cot(pi/6) -> 1.732... (the exact result is sqrt(3))", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("random_int", "Random Integer", "Generates a random integer [0, n].",
                "Generates a pseudorandom number using the standard JVM random mechanism, keeping it less than or " +
                        "equal to the given number.\n\n" +
                        "Example: random_int(5) -> 4\n" +
                        "random_int(5) -> 3\n" +
                        "random_int(5) -> 3\n", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("arcsin", "Arcsine", "Computes the arcsine of x. (The result is in radians.)",
                "Example: arcsin(0.5) -> 0.523... (the exact result is pi/6)", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("arccos", "Arccosine", "Computes the arccosine of x. (The result is in radians.)",
                "Example: arccos(0.5) -> 1.047... (the exact result is pi/3)", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("arctan", "Arctangent", "Computes the arctangent of x. (The result is in radians.)",
                "Example: arctan(1) -> 0.785... (the exact result is pi/4)", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("arcsec", "Arcsecant", "Computes the arcsecant of x. (The result is in radians.)",
                "Example: arcsec(2) -> 1.047... (the exact result is pi/3)", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("arccsc", "Arccosecant", "Computes the arcscosecant of x. (The result is in radians.)",
                "Example: arccsc(2) -> 0.523... (the exact result is pi/6)", DocumentationType.FUNCTION));
        registerDocumentation(new Documentation("arccot", "Arccotangent", "Computes the arccotangent of x. (The result is in radians," +
                " in the range (0, pi).)",
                "Example: arccot(0) -> 1.570... (the exact result is pi/2)", DocumentationType.FUNCTION));
    }

    @Override
    public void onDisable() {
        FACTORIAL_LISTS.clear();
    }
}
