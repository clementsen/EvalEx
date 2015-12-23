package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestNested {

	@Test
	public void testNestedVars() {
		String x = "1";
		String y = "2";
		String z = "2*x + 3*y";
		String a = "2*x + 4*z";

		BigDecimalEx e = new BigDecimalEx(a);
		e.with("x", x);
		e.with("y", y);
		e.with("z", z);

		assertEquals("34", e.eval().toString());
	}

	@Test
	public void testReplacements() {
		Expression e = new BigDecimalEx("3+a+aa+aaa").with("a", "1*x")
				.with("aa", "2*x").with("aaa", "3*x").with("x", "2");
		assertEquals("15", e.eval().toString());
	}

    @Test
    public void testReuseExpression() {
        String a = "2*x + 4*y";

        BigDecimalEx e = new BigDecimalEx(a);
        e.with("x", "2");
        e.with("y", "3");

        assertEquals("16", e.eval().toString());

        e.with("x", "3");
        e.with("y", "2");

        assertEquals("14", e.eval().toString());
    }
}
