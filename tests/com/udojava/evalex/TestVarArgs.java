package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class TestVarArgs {

	@Test
	public void testSimple() {
		BigDecimalEx e = new BigDecimalEx("max(1)");
		assertEquals("1", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(4,8)");
		assertEquals("8", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(12,4,8)");
		assertEquals("12", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(12,4,8,16,32)");
		assertEquals("32", e.eval().toPlainString());
	}

	@Test
	public void testNested() {
		BigDecimalEx e = new BigDecimalEx("max(1,2,max(3,4,5,max(9,10,3,4,5),8),7)");
		assertEquals("10", e.eval().toPlainString());
	}
	
	@Test
	public void testZero() {
		BigDecimalEx e = new BigDecimalEx("max(0)");
		assertEquals("0", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(0,3)");
		assertEquals("3", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(2,0,-3)");
		assertEquals("2", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(-2,0,-3)");
		assertEquals("0", e.eval().toPlainString());
		
		e = new BigDecimalEx("max(0,0,0,0)");
		assertEquals("0", e.eval().toPlainString());
	}
	
	@Test
	public void testError() {
		String err = "";
		BigDecimalEx e = new BigDecimalEx("max()");
		try {
			e.eval();
		} catch (ExpressionException ex) {
			err = ex.getMessage();
		}
		assertEquals("MAX requires at least one parameter", err);
	}

	@Test
	public void testCustomFunction1() {
		BigDecimalEx e = new BigDecimalEx("3 * AVG(2,4)");
		e.addFunction(new Function<BigDecimal, MathContext>("AVG", -1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
				if (parameters.size() == 0) {
					throw new ExpressionException("AVG requires at least one parameter");
				}
				BigDecimal avg = new BigDecimal(0);
				for (BigDecimal parameter : parameters) {
						avg = avg.add(parameter);
				}
				return avg.divide(new BigDecimal(parameters.size()));
			}
		});
		
		assertEquals("9", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunction2() {
		BigDecimalEx e = new BigDecimalEx("4 * AVG(2,4,6,8,10,12)");
		e.addFunction(new Function<BigDecimal, MathContext>("AVG", -1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
				if (parameters.size() == 0) {
					throw new ExpressionException("AVG requires at least one parameter");
				}
				BigDecimal avg = new BigDecimal(0);
				for (BigDecimal parameter : parameters) {
						avg = avg.add(parameter);
				}
				return avg.divide(new BigDecimal(parameters.size()));
			}
		});
		
		assertEquals("28", e.eval().toPlainString());
	}
}
