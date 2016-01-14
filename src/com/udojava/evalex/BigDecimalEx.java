/*
 * Copyright 2012 Udo Klimaschewski
 * 
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.udojava.evalex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * BigDecimal expression.
 */
public class BigDecimalEx extends Expression<BigDecimal, MathContext> {

    /**
     * Definition of PI as a constant, can be used in expressions as variable.
     */
    public static final BigDecimal PI = new BigDecimal(
        "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

	/**
	 * Creates a new expression instance from an expression string with a given
	 * default match context of {@link MathContext#DECIMAL32}.
	 * 
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or
	 *            {@code "sin(y)>0 & max(z, 3)>3"}
	 */
	public BigDecimalEx(String expression) {
		this(expression, MathContext.DECIMAL32);
	}

	/**
	 * Creates a new expression instance from an expression string with a given
	 * default match context.
	 * 
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or
	 *            {@code "sin(y)>0 & max(z, 3)>3"}
	 * @param defaultMathContext
	 *            The {@link MathContext} to use by default.
	 */
	public BigDecimalEx(String expression, MathContext defaultMathContext) {
        super(expression, defaultMathContext);

        addOperator(new Operator<BigDecimal, MathContext>("+", 20, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.add(v2, mc);
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("-", 20, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.subtract(v2, mc);
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("*", 30, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.multiply(v2, mc);
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("/", 30, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.divide(v2, mc);
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("%", 30, true) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.remainder(v2, mc);
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("^", 40, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
				/*-
				 * Thanks to Gene Marin:
				 * http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java
				 */
                int signOf2 = v2.signum();
                double dn1 = v1.doubleValue();
                v2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
                BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
                BigDecimal n2IntPart = v2.subtract(remainderOf2);
                BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), mc);
                BigDecimal doublePow = new BigDecimal(Math.pow(dn1,
                    remainderOf2.doubleValue()));

                BigDecimal result = intPow.multiply(doublePow, mc);
                if (signOf2 == -1) {
                    result = BigDecimal.ONE.divide(result, mc.getPrecision(),
                        RoundingMode.HALF_UP);
                }
                return result;
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("&&", 4, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                boolean b1 = !v1.equals(BigDecimal.ZERO);
                boolean b2 = !v2.equals(BigDecimal.ZERO);
                return b1 && b2 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>("||", 2, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                boolean b1 = !v1.equals(BigDecimal.ZERO);
                boolean b2 = !v2.equals(BigDecimal.ZERO);
                return b1 || b2 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>(">", 10, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) == 1 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>(">=", 10, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) >= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>("<", 10, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) == -1 ? BigDecimal.ONE
                    : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>("<=", 10, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>("=", 7, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("==", 7, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addOperator(new Operator<BigDecimal, MathContext>("!=", 7, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) != 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        addOperator(new Operator<BigDecimal, MathContext>("<>", 7, false) {
            @Override
            public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
                return v1.compareTo(v2) != 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addFunction(new Function<BigDecimal, MathContext>("NOT", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                boolean zero = parameters.get(0).compareTo(BigDecimal.ZERO) == 0;
                return zero ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });

        addFunction(new Function<BigDecimal, MathContext>("IF", 3) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                boolean isTrue = !parameters.get(0).equals(BigDecimal.ZERO);
                return isTrue ? parameters.get(1) : parameters.get(2);
            }
        });

        addFunction(new Function<BigDecimal, MathContext>("RANDOM", 0) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.random();
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("SIN", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.sin(Math.toRadians(parameters.get(0)
                    .doubleValue()));
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("COS", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.cos(Math.toRadians(parameters.get(0)
                    .doubleValue()));
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("TAN", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.tan(Math.toRadians(parameters.get(0)
                    .doubleValue()));
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("ASIN", 1) { // added by av
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.toDegrees(Math.asin(parameters.get(0)
                    .doubleValue()));
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("ACOS", 1) { // added by av
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.toDegrees(Math.acos(parameters.get(0)
                    .doubleValue()));
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("ATAN", 1) { // added by av
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.toDegrees(Math.atan(parameters.get(0)
                    .doubleValue()));
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("SINH", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.sinh(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("COSH", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.cosh(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("TANH", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.tanh(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("RAD", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.toRadians(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("DEG", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.toDegrees(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("MAX", -1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                if (parameters.size() == 0) {
                    throw new ExpressionException("MAX requires at least one parameter");
                }
                BigDecimal max = null;
                for (BigDecimal parameter : parameters) {
                    if (max == null || parameter.compareTo(max) > 0) {
                        max = parameter;
                    }
                }
                return max;
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("MIN", -1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                if (parameters.size() == 0) {
                    throw new ExpressionException("MIN requires at least one parameter");
                }
                BigDecimal min = null;
                for (BigDecimal parameter : parameters) {
                    if (min == null || parameter.compareTo(min) < 0) {
                        min = parameter;
                    }
                }
                return min;
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("ABS", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                return parameters.get(0).abs(mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("LOG", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.log(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("LOG10", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                double d = Math.log10(parameters.get(0).doubleValue());
                return new BigDecimal(d, mc);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("ROUND", 2) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                BigDecimal toRound = parameters.get(0);
                int precision = parameters.get(1).intValue();
                return toRound.setScale(precision, mc.getRoundingMode());
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("FLOOR", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                BigDecimal toRound = parameters.get(0);
                return toRound.setScale(0, RoundingMode.FLOOR);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("CEILING", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
                BigDecimal toRound = parameters.get(0);
                return toRound.setScale(0, RoundingMode.CEILING);
            }
        });
        addFunction(new Function<BigDecimal, MathContext>("SQRT", 1) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
				/*
				 * From The Java Programmers Guide To numerical Computing
				 * (Ronald Mak, 2003)
				 */
                BigDecimal x = parameters.get(0);
                if (x.compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO;
                }
                if (x.signum() < 0) {
                    throw new ExpressionException(
                        "Argument to SQRT() function must not be negative");
                }
                BigInteger n = x.movePointRight(mc.getPrecision() << 1)
                    .toBigInteger();

                int bits = (n.bitLength() + 1) >> 1;
                BigInteger ix = n.shiftRight(bits);
                BigInteger ixPrev;

                do {
                    ixPrev = ix;
                    ix = ix.add(n.divide(ix)).shiftRight(1);
                    // Give other threads a chance to work;
                    Thread.yield();
                } while (ix.compareTo(ixPrev) != 0);

                return new BigDecimal(ix, mc.getPrecision());
            }
        });

        setVariable("PI", PI);
        setVariable("TRUE", BigDecimal.ONE);
        setVariable("FALSE", BigDecimal.ZERO);
	}

    @Override
    public BigDecimal round(BigDecimal value, MathContext ctx) {
        return value.round(ctx);
    }

    @Override
    public BigDecimal val(String val, MathContext ctx) {
        return new BigDecimal(val, ctx);
    }

    public BigDecimal evalStripTrailingZeros() {
        return eval().stripTrailingZeros();
    }

    /**
     * Sets the precision for expression evaluation.
     *
     * @param precision
     *            The new precision.
     *
     * @return The expression, allows to chain methods.
     */
    public BigDecimalEx setPrecision(int precision) {
        setContext(new MathContext(precision));
        return this;
    }

    /**
     * Sets the rounding mode for expression evaluation.
     *
     * @param roundingMode
     *            The new rounding mode.
     * @return The expression, allows to chain methods.
     */
    public BigDecimalEx setRoundingMode(RoundingMode roundingMode) {
        setContext(new MathContext(getContext().getPrecision(), roundingMode));
        return this;
    }
}
