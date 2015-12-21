package com.udojava.evalex;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Iterator;


public class TestBooleans {

	@Test
	public void testAndTokenizer() {
		BigDecimalEx e = new BigDecimalEx("1&&0");
		Iterator<String> i = e.getExpressionTokenizer();
		
		assertEquals("1", i.next());
		assertEquals("&&", i.next());
		assertEquals("0", i.next());
	}

	@Test
	public void testAndRPN() {
		assertEquals("1 0 &&", new BigDecimalEx("1&&0").toRPN());
	}
	
	@Test
	public void testAndEval() {
		assertEquals("0", new BigDecimalEx("1&&0").eval().toString());
		assertEquals("1", new BigDecimalEx("1&&1").eval().toString());
		assertEquals("0", new BigDecimalEx("0&&0").eval().toString());
		assertEquals("0", new BigDecimalEx("0&&1").eval().toString());
	}
	
	@Test
	public void testOrEval() {
		assertEquals("1", new BigDecimalEx("1||0").eval().toString());
		assertEquals("1", new BigDecimalEx("1||1").eval().toString());
		assertEquals("0", new BigDecimalEx("0||0").eval().toString());
		assertEquals("1", new BigDecimalEx("0||1").eval().toString());
	}
	
	@Test
	public void testCompare() {
		assertEquals("1", new BigDecimalEx("2>1").eval().toString());
		assertEquals("0", new BigDecimalEx("2<1").eval().toString());
		assertEquals("0", new BigDecimalEx("1>2").eval().toString());
		assertEquals("1", new BigDecimalEx("1<2").eval().toString());
		assertEquals("0", new BigDecimalEx("1=2").eval().toString());
		assertEquals("1", new BigDecimalEx("1=1").eval().toString());
		assertEquals("1", new BigDecimalEx("1>=1").eval().toString());
		assertEquals("1", new BigDecimalEx("1.1>=1").eval().toString());
		assertEquals("0", new BigDecimalEx("1>=2").eval().toString());
		assertEquals("1", new BigDecimalEx("1<=1").eval().toString());
		assertEquals("0", new BigDecimalEx("1.1<=1").eval().toString());
		assertEquals("1", new BigDecimalEx("1<=2").eval().toString());
		assertEquals("0", new BigDecimalEx("1=2").eval().toString());
		assertEquals("1", new BigDecimalEx("1=1").eval().toString());
		assertEquals("1", new BigDecimalEx("1!=2").eval().toString());
		assertEquals("0", new BigDecimalEx("1!=1").eval().toString());
	}
	
	@Test
	public void testCompareCombined() {
		assertEquals("1", new BigDecimalEx("(2>1)||(1=0)").eval().toString());
		assertEquals("0", new BigDecimalEx("(2>3)||(1=0)").eval().toString());
		assertEquals("1", new BigDecimalEx("(2>3)||(1=0)||(1&&1)").eval().toString());
	}
	
	@Test
	public void testMixed() {
		assertEquals("0", new BigDecimalEx("1.5 * 7 = 3").eval().toString());
		assertEquals("1", new BigDecimalEx("1.5 * 7 = 10.5").eval().toString());
	}
	
	@Test
	public void testNot() {
		assertEquals("0", new BigDecimalEx("not(1)").eval().toString());
		assertEquals("1", new BigDecimalEx("not(0)").eval().toString());
		assertEquals("1", new BigDecimalEx("not(1.5 * 7 = 3)").eval().toString());
		assertEquals("0", new BigDecimalEx("not(1.5 * 7 = 10.5)").eval().toString());
	}

	@Test
	public void testConstants() {
		assertEquals("1", new BigDecimalEx("TRUE!=FALSE").eval().toString());
		assertEquals("0", new BigDecimalEx("TRUE==2").eval().toString());
		assertEquals("1", new BigDecimalEx("NOT(TRUE)==FALSE").eval().toString());
		assertEquals("1", new BigDecimalEx("NOT(FALSE)==TRUE").eval().toString());
		assertEquals("0", new BigDecimalEx("TRUE && FALSE").eval().toString());
		assertEquals("1", new BigDecimalEx("TRUE || FALSE").eval().toString());
	}

	@Test
	public void testIf() {
		assertEquals("5", new BigDecimalEx("if(TRUE, 5, 3)").eval().toString());
		assertEquals("3", new BigDecimalEx("IF(FALSE, 5, 3)").eval().toString());
		assertEquals("5.35", new BigDecimalEx("If(2, 5.35, 3)").eval().toString());
	}
}
