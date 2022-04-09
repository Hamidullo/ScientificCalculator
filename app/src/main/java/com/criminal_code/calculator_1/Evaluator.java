package com.criminal_code.calculator_1;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.Constant;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

public class Evaluator  extends AbstractEvaluator<Double> {
    public static final Constant PI = new Constant("pi");
    public static final Constant E = new Constant("e");
    public static final Function CEIL = new Function("ceil", 1);
    public static final Function FLOOR = new Function("floor", 1);
    public static final Function ROUND = new Function("round", 1);
    public static final Function ABS = new Function("abs", 1);
    public static final Function SINE = new Function("sin", 1);
    public static final Function COSINE = new Function("cos", 1);
    public static final Function TANGENT = new Function("tan", 1);
    public static final Function ACOSINE = new Function("acos", 1);
    public static final Function ASINE = new Function("asin", 1);
    public static final Function ATAN = new Function("atan", 1);
    public static final Function SINEH = new Function("sinh", 1);
    public static final Function COSINEH = new Function("cosh", 1);
    public static final Function TANGENTH = new Function("tanh", 1);
    public static final Function MIN = new Function("min", 1, 2147483647);
    public static final Function MAX = new Function("max", 1, 2147483647);
    public static final Function SUM = new Function("sum", 1, 2147483647);
    public static final Function AVERAGE = new Function("avg", 1, 2147483647);
    public static final Function LN = new Function("ln", 1);
    public static final Function LOG = new Function("log", 1);
    public static final Function RANDOM = new Function("random", 0);
    public static final Operator NEGATE;
    public static final Operator NEGATE_HIGH;
    public static final Operator MINUS;
    public static final Operator PLUS;
    public static final Operator MULTIPLY;
    public static final Operator DIVIDE;
    public static final Operator EXPONENT;
    public static final Operator MODULO;
    private static final Operator[] OPERATORS;
    private static final Operator[] OPERATORS_EXCEL;
    private static final Function[] FUNCTIONS;
    private static final Constant[] CONSTANTS;
    private static Parameters DEFAULT_PARAMETERS;
    private static final ThreadLocal<NumberFormat> FORMATTER;

    public static Parameters getDefaultParameters() {
        return getDefaultParameters(DoubleEvaluator.Style.STANDARD);
    }

    public static Parameters getDefaultParameters(DoubleEvaluator.Style style) {
        Parameters result = new Parameters();
        result.addOperators(style == DoubleEvaluator.Style.STANDARD ? Arrays.asList(OPERATORS) : Arrays.asList(OPERATORS_EXCEL));
        result.addFunctions(Arrays.asList(FUNCTIONS));
        result.addConstants(Arrays.asList(CONSTANTS));
        result.addFunctionBracket(BracketPair.PARENTHESES);
        result.addExpressionBracket(BracketPair.PARENTHESES);
        return result;
    }

    private static Parameters getParameters() {
        if (DEFAULT_PARAMETERS == null) {
            DEFAULT_PARAMETERS = getDefaultParameters();
        }

        return DEFAULT_PARAMETERS;
    }

    public Evaluator() {
        this(getParameters());
    }

    public Evaluator(Parameters parameters) {
        super(parameters);
    }

    protected Double toValue(String literal, Object evaluationContext) {
        ParsePosition p = new ParsePosition(0);
        Number result = ((NumberFormat)FORMATTER.get()).parse(literal, p);
        if (p.getIndex() != 0 && p.getIndex() == literal.length()) {
            return result.doubleValue();
        } else {
            throw new IllegalArgumentException(literal + " is not a number");
        }
    }

    protected Double evaluate(Constant constant, Object evaluationContext) {
        if (PI.equals(constant)) {
            return 3.141592653589793D;
        } else {
            return E.equals(constant) ? 2.718281828459045D : (Double)super.evaluate(constant, evaluationContext);
        }
    }

    protected Double evaluate(Operator operator, Iterator<Double> operands, Object evaluationContext) {
        if (!NEGATE.equals(operator) && !NEGATE_HIGH.equals(operator)) {
            if (MINUS.equals(operator)) {
                return (Double)operands.next() - (Double)operands.next();
            } else if (PLUS.equals(operator)) {
                return (Double)operands.next() + (Double)operands.next();
            } else if (MULTIPLY.equals(operator)) {
                return (Double)operands.next() * (Double)operands.next();
            } else if (DIVIDE.equals(operator)) {
                return (Double)operands.next() / (Double)operands.next();
            } else if (EXPONENT.equals(operator)) {
                return Math.pow((Double)operands.next(), (Double)operands.next());
            } else {
                return MODULO.equals(operator) ? (Double)operands.next() % (Double)operands.next() : (Double)super.evaluate(operator, operands, evaluationContext);
            }
        } else {
            return -(Double)operands.next();
        }
    }

    protected Double evaluate(Function function, Iterator<Double> arguments, Object evaluationContext) {
        Double result;
        if (ABS.equals(function)) {
            result = Math.abs((Double)arguments.next());
        } else if (CEIL.equals(function)) {
            result = Math.ceil((Double)arguments.next());
        } else if (FLOOR.equals(function)) {
            result = Math.floor((Double)arguments.next());
        } else if (ROUND.equals(function)) {
            Double arg = (Double)arguments.next();
            if (arg != -1.0D / 0.0 && arg != 1.0D / 0.0) {
                result = (double)Math.round(arg);
            } else {
                result = arg;
            }
        } else if (SINEH.equals(function)) {
            result = Math.sinh((Double)arguments.next());
        } else if (COSINEH.equals(function)) {
            result = Math.cosh((Double)arguments.next());
        } else if (TANGENTH.equals(function)) {
            result = Math.tanh((Double)arguments.next());
        } else if (SINE.equals(function)) {
            result = Math.sin((Double)arguments.next());
        } else if (COSINE.equals(function)) {
            result = Math.cos((Double)arguments.next());
        } else if (TANGENT.equals(function)) {
            result = Math.tan((Double)arguments.next());
        } else if (ACOSINE.equals(function)) {
            result = Math.acos((Double)arguments.next());
        } else if (ASINE.equals(function)) {
            result = Math.asin((Double)arguments.next());
        } else if (ATAN.equals(function)) {
            result = Math.atan((Double)arguments.next());
        } else if (MIN.equals(function)) {
            for(result = (Double)arguments.next(); arguments.hasNext(); result = Math.min(result, (Double)arguments.next())) {
            }
        } else if (MAX.equals(function)) {
            for(result = (Double)arguments.next(); arguments.hasNext(); result = Math.max(result, (Double)arguments.next())) {
            }
        } else if (SUM.equals(function)) {
            for(result = 0.0D; arguments.hasNext(); result = result + (Double)arguments.next()) {
            }
        } else if (AVERAGE.equals(function)) {
            result = 0.0D;

            int nb;
            for(nb = 0; arguments.hasNext(); ++nb) {
                result = result + (Double)arguments.next();
            }

            result = result / (double)nb;
        } else if (LN.equals(function)) {
            result = Math.log((Double)arguments.next());
        } else if (LOG.equals(function)) {
            result = Math.log10((Double)arguments.next());
        } else if (RANDOM.equals(function)) {
            result = Math.random();
        } else {
            result = (Double)super.evaluate(function, arguments, evaluationContext);
        }

        this.errIfNaN(result, function);
        return result;
    }

    private void errIfNaN(Double result, Function function) {
        if (result.equals(0.0D / 0.0)) {
            throw new IllegalArgumentException("Invalid argument passed to " + function.getName());
        }
    }

    static {
        NEGATE = new Operator("-", 1, Operator.Associativity.RIGHT, 3);
        NEGATE_HIGH = new Operator("-", 1, Operator.Associativity.RIGHT, 5);
        MINUS = new Operator("-", 2, Operator.Associativity.LEFT, 1);
        PLUS = new Operator("+", 2, Operator.Associativity.LEFT, 1);
        MULTIPLY = new Operator("*", 2, Operator.Associativity.LEFT, 2);
        DIVIDE = new Operator("/", 2, Operator.Associativity.LEFT, 2);
        EXPONENT = new Operator("^", 2, Operator.Associativity.LEFT, 4);
        MODULO = new Operator("%", 2, Operator.Associativity.LEFT, 2);
        OPERATORS = new Operator[]{NEGATE, MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT, MODULO};
        OPERATORS_EXCEL = new Operator[]{NEGATE_HIGH, MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT, MODULO};
        FUNCTIONS = new Function[]{SINE, COSINE, TANGENT, ASINE, ACOSINE, ATAN, SINEH, COSINEH, TANGENTH,
                MIN, MAX, SUM, AVERAGE, LN, LOG, ROUND, CEIL, FLOOR, ABS, RANDOM};
        CONSTANTS = new Constant[]{PI, E};
        FORMATTER = new ThreadLocal<NumberFormat>() {
            protected NumberFormat initialValue() {
                return NumberFormat.getNumberInstance(Locale.US);
            }
        };
    }

    public static enum Style {
        STANDARD,
        EXCEL;

        private Style() {
        }
    }
}
