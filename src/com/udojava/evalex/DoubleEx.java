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

import java.util.List;

/**
 * Double expression.
 */
public class DoubleEx extends Expression<Double,Void> {

	/**
	 * Creates a new expression instance from an expression string with a given
	 * default match context.
	 *
	 * @param expression
	 *            The expression. E.g. <code>"2.4*sin(3)/(2-4)"</code> or
	 *            <code>"sin(y)>0 & max(z, 3)>3"</code>
	 */
	public DoubleEx(String expression) {
        super(expression, null, (v,mc) -> new Double(v), 0D);

        addOperator(new PureOperator<Double>("+", 20, true) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1 + v2;
            }
        });
        addOperator(new PureOperator<Double>("-", 20, true) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1 - v2;
            }
        });
        addOperator(new PureOperator<Double>("*", 30, true) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1 * v2;
            }
        });
        addOperator(new PureOperator<Double>("/", 30, true) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1 / v2;
            }
        });
        addOperator(new PureOperator<Double>("%", 30, true) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1 % v2;
            }
        });
        addOperator(new PureOperator<Double>("^", 40, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return Double.longBitsToDouble(
                    Double.doubleToRawLongBits(v1) ^ Double.doubleToRawLongBits(v2));
            }
        });
        addOperator(new PureOperator<Double>("&&", 4, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                boolean b1 = !v1.equals(0D);
                boolean b2 = !v2.equals(0D);
                return b1 && b2 ? 1D : 0D;
            }
        });

        addOperator(new PureOperator<Double>("||", 2, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                boolean b1 = !v1.equals(0D);
                boolean b2 = !v2.equals(0D);
                return b1 || b2 ? 1D : 0D;
            }
        });

        addOperator(new PureOperator<Double>(">", 10, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) == 1 ? 1D : 0D;
            }
        });

        addOperator(new PureOperator<Double>(">=", 10, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) >= 0 ? 1D : 0D;
            }
        });

        addOperator(new PureOperator<Double>("<", 10, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) == -1 ? 1D
                    : 0D;
            }
        });

        addOperator(new PureOperator<Double>("<=", 10, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) <= 0 ? 1D : 0D;
            }
        });

        addOperator(new PureOperator<Double>("=", 7, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) == 0 ? 1D : 0D;
            }
        });
        addOperator(new PureOperator<Double>("==", 7, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) == 0 ? 1D : 0D;
            }
        });

        addOperator(new PureOperator<Double>("!=", 7, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) != 0 ? 1D : 0D;
            }
        });
        addOperator(new PureOperator<Double>("<>", 7, false) {
            @Override
            public Double eval(Double v1, Double v2) {
                return v1.compareTo(v2) != 0 ? 1D : 0D;
            }
        });

        addFunction(new PureFunction<Double>("NOT", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                boolean zero = parameters.get(0).compareTo(0D) == 0;
                return zero ? 1D : 0D;
            }
        });

        addFunction(new PureFunction<Double>("IF", 3) {
            @Override
            public Double eval(List<Double> parameters) {
                boolean isTrue = !parameters.get(0).equals(0D);
                return isTrue ? parameters.get(1) : parameters.get(2);
            }
        });

        addFunction(new PureFunction<Double>("RANDOM", 0) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.random();
            }
        });
        addFunction(new PureFunction<Double>("SIN", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.sin(Math.toRadians(parameters.get(0)));
            }
        });
        addFunction(new PureFunction<Double>("COS", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.cos(Math.toRadians(parameters.get(0)));
            }
        });
        addFunction(new PureFunction<Double>("TAN", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.tan(Math.toRadians(parameters.get(0)));
            }
        });
        addFunction(new PureFunction<Double>("ASIN", 1) { // added by av
            @Override
            public Double eval(List<Double> parameters) {
                return Math.toDegrees(Math.asin(parameters.get(0)));
            }
        });
        addFunction(new PureFunction<Double>("ACOS", 1) { // added by av
            @Override
            public Double eval(List<Double> parameters) {
                return Math.toDegrees(Math.acos(parameters.get(0)));
            }
        });
        addFunction(new PureFunction<Double>("ATAN", 1) { // added by av
            @Override
            public Double eval(List<Double> parameters) {
                return Math.toDegrees(Math.atan(parameters.get(0)));
            }
        });
        addFunction(new PureFunction<Double>("SINH", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.sinh(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("COSH", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.cosh(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("TANH", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.tanh(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("RAD", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.toRadians(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("DEG", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.toDegrees(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("MAX", -1) {
            @Override
            public Double eval(List<Double> parameters) {
                if (parameters.size() == 0) {
                    throw new ExpressionException("MAX requires at least one parameter");
                }
                Double max = null;
                for (Double parameter : parameters) {
                    if (max == null || parameter.compareTo(max) > 0) {
                        max = parameter;
                    }
                }
                return max;
            }
        });
        addFunction(new PureFunction<Double>("MIN", -1) {
            @Override
            public Double eval(List<Double> parameters) {
                if (parameters.size() == 0) {
                    throw new ExpressionException("MIN requires at least one parameter");
                }
                Double min = null;
                for (Double parameter : parameters) {
                    if (min == null || parameter.compareTo(min) < 0) {
                        min = parameter;
                    }
                }
                return min;
            }
        });
        addFunction(new PureFunction<Double>("ABS", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.abs(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("LOG", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.log(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("LOG10", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.log10(parameters.get(0));
            }
        });
        addFunction(new PureFunction<Double>("ROUND", 2) {
            @Override
            public Double eval(List<Double> parameters) {
                Double toRound = parameters.get(0);
                int precision = parameters.get(1).intValue();
                return round(toRound, precision);
            }
        });
        addFunction(new PureFunction<Double>("FLOOR", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                Double toRound = parameters.get(0);
                return Math.floor(toRound);
            }
        });
        addFunction(new PureFunction<Double>("CEILING", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                Double toRound = parameters.get(0);
                return Math.ceil(toRound);
            }
        });
        addFunction(new PureFunction<Double>("SQRT", 1) {
            @Override
            public Double eval(List<Double> parameters) {
                return Math.sqrt(parameters.get(0));
            }
        });

        setVariable("PI", Math.PI);
        setVariable("TRUE", 1D);
        setVariable("FALSE", 0D);
	}

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public Double round(Double value, Void ctx) {
        return value;
    }

    @Override
    public Double stripTrailingZeros(Double value) {
        return value;
    }

}
