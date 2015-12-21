package com.udojava.evalex;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;

/**
 * Abstract definition of a supported expression function. A function is
 * defined by a name, the number of parameters and the actual processing
 * implementation.
 */
public abstract class Function<T extends Number> {
    /**
     * Name of this function.
     */
    private String name;
    /**
     * Number of parameters expected for this function.
     * <code>-1</code> denotes a variable number of parameters.
     */
    private int numParams;

    /**
     * Creates a new function with given name and parameter count.
     *
     * @param name
     *            The name of the function.
     * @param numParams
     *            The number of parameters for this function.
     *            <code>-1</code> denotes a variable number of parameters.
     */
    public Function(String name, int numParams) {
        this.name = name.toUpperCase(Locale.ROOT);
        this.numParams = numParams;
    }

    public String getName() {
        return name;
    }

    public int getNumParams() {
        return numParams;
    }

    public boolean numParamsVaries() {
        return numParams < 0;
    }

    /**
     * Implementation for this function.
     *
     * @param parameters
     *            Parameters will be passed by the expression evaluator as a
     *            {@link List} of {@link BigDecimal} values.
     * @param mc
     * @return The function must return a new {@link BigDecimal} value as a
     *         computing result.
     */
    public abstract T eval(List<T> parameters, MathContext mc);
}
