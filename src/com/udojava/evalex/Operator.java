package com.udojava.evalex;

/**
 * Abstract definition of a supported operator. An operator is defined by
 * its name (pattern), precedence and if it is left- or right associative.
 */
public abstract class Operator<T extends Number, C> {
    /**
     * This operators name (pattern).
     */
    private String oper;
    /**
     * Operators precedence.
     */
    private int precedence;
    /**
     * Operator is left associative.
     */
    private boolean leftAssoc;

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
    public Operator(String oper, int precedence, boolean leftAssoc) {
        this.oper = oper;
        this.precedence = precedence;
        this.leftAssoc = leftAssoc;
    }

    public String getOper() {
        return oper;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isLeftAssoc() {
        return leftAssoc;
    }

    /**
     * Implementation for this operator.
     *
     * @param v1
     *            Operand 1.
     * @param v2
     *            Operand 2.
     * @param ctx
     *            Context
     * @return The result of the operation.
     */
    public abstract T eval(T v1, T v2, C ctx);
}
