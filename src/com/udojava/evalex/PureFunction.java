package com.udojava.evalex;

import java.util.List;

/**
 * Abstract definition of a supported expression function. A function is
 * defined by a name, the number of parameters and the actual processing
 * implementation.
 */
public abstract class PureFunction<T extends Number> extends Function<T, Void> {

    /**
     * Creates a new function with given name and parameter count.
     *
     * @param name
     *            The name of the function.
     * @param numParams
     *            The number of parameters for this function.
     *            <code>-1</code> denotes a variable number of parameters.
     */
    public PureFunction(String name, int numParams) {
        super(name, numParams);
    }

    @Override
    public T eval(List<T> parameters, Void ctx) {
        return eval(parameters);
    }

    /**
     * Implementation for this function.
     *
     * @param parameters
     *            Parameters will be passed by the expression evaluator as a
     *            {@link List} of values.
     * @return The function must return a new value as a
     *         computing result.
     */
    public abstract T eval(List<T> parameters);

}
