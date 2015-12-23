package com.udojava.evalex;

/**
 * Abstract definition of a supported operator. An operator is defined by
 * its name (pattern), precedence and if it is left- or right associative.
 */
public abstract class PureOperator<T extends Number> extends Operator<T, Void> {

    /**
     * Creates a new operator.
     *
     * @param oper
     *            The operator name (pattern).
     * @param precedence
     *            The operators precedence.
     * @param leftAssoc
     *            <code>true</code> if the operator is left associative,
     *            else <code>false</code>.
     */
    public PureOperator(String oper, int precedence, boolean leftAssoc) {
        super(oper, precedence, leftAssoc);
    }

    @Override
    public T eval(T v1, T v2, Void ctx) {
        return eval(v1, v2);
    }

    /**
     * Implementation for this operator.
     *
     * @param v1
     *            Operand 1.
     * @param v2
     *            Operand 2.
     * @return The result of the operation.
     */
    public abstract T eval(T v1, T v2);
}
