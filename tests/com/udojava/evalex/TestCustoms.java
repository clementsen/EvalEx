package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class TestCustoms {

	@Test
	public void testCustomOperator() {
		BigDecimalEx e = new BigDecimalEx("2.1234 >> 2");
		
		e.addOperator(new Operator<BigDecimal>(">>", 30, true) {
			@Override
			public BigDecimal eval(BigDecimal v1, BigDecimal v2, MathContext mc) {
				return v1.movePointRight(v2.toBigInteger().intValue());
			}
		});
		
		assertEquals("212.34", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunction() {
		BigDecimalEx e = new BigDecimalEx("2 * average(12,4,8)");
		e.addFunction(new Function<BigDecimal>("average", 3) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
				BigDecimal sum = parameters.get(0).add(parameters.get(1)).add(parameters.get(2));
				return sum.divide(new BigDecimal(3));
			}
		});
		
		assertEquals("16", e.eval().toPlainString());
	}
	
	@Test
	public void testCustomFunctionVariableParameters() {
		BigDecimalEx e = new BigDecimalEx("2 * average(12,4,8,2,9)");
		e.addFunction(new Function<BigDecimal>("average", -1) {
			@Override
			public BigDecimal eval(List<BigDecimal> parameters, MathContext mc) {
				BigDecimal sum = new BigDecimal(0);
				for (BigDecimal parameter : parameters) {
					sum = sum.add(parameter);
				}
				return sum.divide(new BigDecimal(parameters.size()));
			}
		});
		
		assertEquals("14", e.eval().toPlainString());
	}

}
