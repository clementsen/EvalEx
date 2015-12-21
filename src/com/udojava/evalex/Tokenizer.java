package com.udojava.evalex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Expression tokenizer that allows to iterate over a {@link String}
 * expression token by token. Blank characters will be skipped.
 */
class Tokenizer<T extends Number> implements Iterator<String> {

    /**
     * Actual position in expression string.
     */
    private int pos = 0;

    /**
     * The original input expression.
     */
    private String input;
    /**
     * The previous token or <code>null</code> if none.
     */
    private String previousToken;

    /**
     * All defined operators with name and implementation.
     */
    private Map<String, Operator<T>> operators = new HashMap<>();

    /**
     * Creates a new tokenizer for an expression.
     *
     * @param input
     *            The expression string.
     * @param operators
     */
    public Tokenizer(String input, Map<String, Operator<T>> operators) {
        this.input = input.trim();
        this.operators = operators;
    }

    @Override
    public boolean hasNext() {
        return (pos < input.length());
    }

    /**
     * Peek at the next character, without advancing the iterator.
     *
     * @return The next character or character 0, if at end of string.
     */
    private char peekNextChar() {
        if (pos < (input.length() - 1)) {
            return input.charAt(pos + 1);
        } else {
            return 0;
        }
    }

    @Override
    public String next() {
        StringBuilder token = new StringBuilder();
        if (pos >= input.length()) {
            return previousToken = null;
        }
        char ch = input.charAt(pos);
        while (Character.isWhitespace(ch) && pos < input.length()) {
            ch = input.charAt(++pos);
        }
        if (Character.isDigit(ch)) {
            while ((Character.isDigit(ch) || ch == Constant.decimalSeparator
                                            || ch == 'e' || ch == 'E'
                                            || (ch == Constant.minusSign && token.length() > 0
                                                && ('e'==token.charAt(token.length()-1) || 'E'==token.charAt(token.length()-1)))
                                            || (ch == '+' && token.length() > 0
                                                && ('e'==token.charAt(token.length()-1) || 'E'==token.charAt(token.length()-1)))
                                            ) && (pos < input.length())) {
                token.append(input.charAt(pos++));
                ch = pos == input.length() ? 0 : input.charAt(pos);
            }
        } else if (ch == Constant.minusSign
                && Character.isDigit(peekNextChar())
                && ("(".equals(previousToken) || ",".equals(previousToken)
                        || previousToken == null || operators
                            .containsKey(previousToken))) {
            token.append(Constant.minusSign);
            pos++;
            token.append(next());
        } else if (Character.isLetter(ch) || (ch == '_')) {
            while ((Character.isLetter(ch) || Character.isDigit(ch) || (ch == '_'))
                    && (pos < input.length())) {
                token.append(input.charAt(pos++));
                ch = pos == input.length() ? 0 : input.charAt(pos);
            }
        } else if (ch == '(' || ch == ')' || ch == ',') {
            token.append(ch);
            pos++;
        } else {
            while (!Character.isLetter(ch) && !Character.isDigit(ch)
                    && ch != '_' && !Character.isWhitespace(ch)
                    && ch != '(' && ch != ')' && ch != ','
                    && (pos < input.length())) {
                token.append(input.charAt(pos));
                pos++;
                ch = pos == input.length() ? 0 : input.charAt(pos);
                if (ch == Constant.minusSign) {
                    break;
                }
            }
            if (!operators.containsKey(token.toString())) {
                throw new ExpressionException("Unknown operator '" + token
                        + "' at position " + (pos - token.length() + 1));
            }
        }
        return previousToken = token.toString();
    }

    @Override
    public void remove() {
        throw new ExpressionException("remove() not supported");
    }

    /**
     * Get the actual character position in the string.
     *
     * @return The actual character position.
     */
    public int getPos() {
        return pos;
    }

}
