package com.udojava.evalex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import java.math.RoundingMode;


public class TestEval {
	
	@Test
	public void testInvalidExpressions1() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("12 18 2");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many numbers or variables", err);
	}

	@Test
	public void testInvalidExpressions2() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("(12)(18)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many numbers or variables", err);
	}

	@Test
	public void testInvalidExpressions3() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("12 + * 18");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Too many operators or functions at: +", err);
	}

	@Test
	public void testInvalidExpressions4() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}

		assertEquals("Empty expression", err);
	}
	
	@Test
	public void testWrongBrackets1() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("2*3(5*3)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing operator at character position 4", err);
	}
	
	@Test
	public void testWrongBrackets2() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("2*(3((5*3)))");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Missing operator at character position 5", err);
	}
	
	@Test
	public void testBrackets() {
		assertEquals("3", new BigDecimalEx("(1+2)").eval().toPlainString());
		assertEquals("3", new BigDecimalEx("((1+2))").eval().toPlainString());
		assertEquals("3", new BigDecimalEx("(((1+2)))").eval().toPlainString());
		assertEquals("9", new BigDecimalEx("(1+2)*(1+2)").eval().toPlainString());
		assertEquals("10", new BigDecimalEx("(1+2)*(1+2)+1").eval().toPlainString());
		assertEquals("12", new BigDecimalEx("(1+2)*((1+2)+1)").eval().toPlainString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testUnknow1() {
		assertEquals("", new BigDecimalEx("7#9").eval().toPlainString());
	}
	
	@Test(expected = RuntimeException.class)
	public void testUnknow2() {
		assertEquals("", new BigDecimalEx("123.6*-9.8-7#9").eval().toPlainString());
	}
	
	@Test
	public void testSimple() {
		assertEquals("3", new BigDecimalEx("1+2").eval().toPlainString());
		assertEquals("2", new BigDecimalEx("4/2").eval().toPlainString());
		assertEquals("5", new BigDecimalEx("3+4/2").eval().toPlainString());
		assertEquals("3.5", new BigDecimalEx("(3+4)/2").eval().toPlainString());
		assertEquals("7.98", new BigDecimalEx("4.2*1.9").eval().toPlainString());
		assertEquals("2", new BigDecimalEx("8%3").eval().toPlainString());
		assertEquals("0", new BigDecimalEx("8%2").eval().toPlainString());
	}
	
	@Test
	public void testPow() {
		assertEquals("16", new BigDecimalEx("2^4").eval().toPlainString());
		assertEquals("256", new BigDecimalEx("2^8").eval().toPlainString());
		assertEquals("9", new BigDecimalEx("3^2").eval().toPlainString());
		assertEquals("6.25", new BigDecimalEx("2.5^2").eval().toPlainString());
		assertEquals("28.34045", new BigDecimalEx("2.6^3.5").eval().toPlainString());
	}
	
	@Test
	public void testSqrt() {
		assertEquals("4", new BigDecimalEx("SQRT(16)").evalStripTrailingZeros().toPlainString());
		assertEquals("1.4142135", new BigDecimalEx("SQRT(2)").eval().toPlainString());
		assertEquals("1.41421356237309504880168872420969807856967187537694807317667973799073247846210703885038753432764157273501384623091229702492483605", new BigDecimalEx("SQRT(2)").setPrecision(128).eval().toPlainString());
		assertEquals("2.2360679", new BigDecimalEx("SQRT(5)").eval().toPlainString());
		assertEquals("99.3730345", new BigDecimalEx("SQRT(9875)").eval().toPlainString());
		assertEquals("2.3558437", new BigDecimalEx("SQRT(5.55)").eval().toPlainString());
		assertEquals("0", new BigDecimalEx("SQRT(0)").eval().toPlainString());
	}
	
	@Test
	public void testFunctions() {
		assertNotSame("1.5", new BigDecimalEx("Random()").eval().toPlainString());
		assertEquals("0.400349", new BigDecimalEx("SIN(23.6)").evalStripTrailingZeros().toPlainString());
		assertEquals("8", new BigDecimalEx("MAX(-7,8)").eval().toPlainString());
		assertEquals("5", new BigDecimalEx("MAX(3,max(4,5))").eval().toPlainString());
		assertEquals("9.6", new BigDecimalEx("MAX(3,max(MAX(9.6,-4.2),Min(5,9)))").eval().toPlainString());
		assertEquals("2.302585", new BigDecimalEx("LOG(10)").eval().toPlainString());
	}

	@Test
	public void testExpectedParameterNumbers() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("Random(1)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Function Random expected 0 parameters, got 1", err);

		try {
			BigDecimalEx expression = new BigDecimalEx("SIN(1, 6)");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("Function SIN expected 1 parameters, got 2", err);
	}

	@Test
	public void testVariableParameterNumbers() {
		String err = "";
		try {
			BigDecimalEx expression = new BigDecimalEx("min()");
			expression.eval();
		} catch (ExpressionException e) {
			err = e.getMessage();
		}
		assertEquals("MIN requires at least one parameter", err);

		assertEquals("1", new BigDecimalEx("min(1)").eval().toPlainString());
		assertEquals("1", new BigDecimalEx("min(1, 2)").eval().toPlainString());
		assertEquals("1", new BigDecimalEx("min(1, 2, 3)").eval().toPlainString());
		assertEquals("3", new BigDecimalEx("max(3, 2, 1)").eval().toPlainString());
		assertEquals("9", new BigDecimalEx("max(3, 2, 1, 4, 5, 6, 7, 8, 9, 0)").eval().toPlainString());
	}

	@Test
	public void testExtremeFunctionNesting() {
		assertNotSame("1.5", new BigDecimalEx("Random()").eval().toPlainString());
		assertEquals("0.0002791281", new BigDecimalEx("SIN(SIN(COS(23.6)))").eval().toPlainString());
		assertEquals("-4", new BigDecimalEx("MIN(0, SIN(SIN(COS(23.6))), 0-MAX(3,4,MAX(0,SIN(1))), 10)").eval().toPlainString());
	}

	@Test
	public void testTrigonometry() {
		assertEquals("0.5", new BigDecimalEx("SIN(30)").evalStripTrailingZeros().toPlainString());
		assertEquals("0.8660254", new BigDecimalEx("cos(30)").eval().toPlainString());
		assertEquals("0.5773503", new BigDecimalEx("TAN(30)").eval().toPlainString());
		assertEquals("5343237000000", new BigDecimalEx("SINH(30)").eval().toPlainString());
		assertEquals("5343237000000", new BigDecimalEx("COSH(30)").eval().toPlainString());
		assertEquals("1", new BigDecimalEx("TANH(30)").eval().toPlainString());
		assertEquals("0.5235988", new BigDecimalEx("RAD(30)").eval().toPlainString());
		assertEquals("1718.873", new BigDecimalEx("DEG(30)").eval().toPlainString());
		
	}
	
	@Test
	public void testMinMaxAbs() {
		assertEquals("3.78787", new BigDecimalEx("MAX(3.78787,3.78786)").eval().toPlainString());
		assertEquals("3.78787", new BigDecimalEx("max(3.78786,3.78787)").eval().toPlainString());
		assertEquals("3.78786", new BigDecimalEx("MIN(3.78787,3.78786)").eval().toPlainString());
		assertEquals("3.78786", new BigDecimalEx("Min(3.78786,3.78787)").eval().toPlainString());
		assertEquals("2.123", new BigDecimalEx("aBs(-2.123)").eval().toPlainString());
		assertEquals("2.123", new BigDecimalEx("abs(2.123)").eval().toPlainString());
	}
	
	@Test
	public void testRounding() {
		assertEquals("3.8", new BigDecimalEx("round(3.78787,1)").eval().toPlainString());
		assertEquals("3.788", new BigDecimalEx("round(3.78787,3)").eval().toPlainString());
		assertEquals("3.734", new BigDecimalEx("round(3.7345,3)").eval().toPlainString());
		assertEquals("-3.734", new BigDecimalEx("round(-3.7345,3)").eval().toPlainString());
		assertEquals("-3.79", new BigDecimalEx("round(-3.78787,2)").eval().toPlainString());
		assertEquals("123.79", new BigDecimalEx("round(123.78787,2)").eval().toPlainString());
		assertEquals("3", new BigDecimalEx("floor(3.78787)").eval().toPlainString());
		assertEquals("4", new BigDecimalEx("ceiling(3.78787)").eval().toPlainString());
		assertEquals("-3", new BigDecimalEx("floor(-2.1)").eval().toPlainString());
		assertEquals("-2", new BigDecimalEx("ceiling(-2.1)").eval().toPlainString());
	}
	
	@Test
	public void testMathContext() {
        BigDecimalEx e = null;
		e = new BigDecimalEx("2.5/3").setPrecision(2);
		assertEquals("0.83", e.eval().toPlainString());
		
		e = new BigDecimalEx("2.5/3").setPrecision(3);
		assertEquals("0.833", e.eval().toPlainString());
		
		e = new BigDecimalEx("2.5/3").setPrecision(8);
		assertEquals("0.83333333", e.eval().toPlainString());
		
		e = new BigDecimalEx("2.5/3").setRoundingMode(RoundingMode.DOWN);
		assertEquals("0.8333333", e.eval().toPlainString());
		
		e = new BigDecimalEx("2.5/3").setRoundingMode(RoundingMode.UP);
		assertEquals("0.8333334", e.eval().toPlainString());
	}
	
}
