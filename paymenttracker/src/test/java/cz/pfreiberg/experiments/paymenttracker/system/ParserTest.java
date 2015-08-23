package cz.pfreiberg.experiments.paymenttracker.system;

import org.junit.Before;
import org.junit.Test;

import cz.pfreiberg.experiments.paymenttracker.exception.ParserException;

/**
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class ParserTest {

	Parser parser;

	@Before
	public void initParser() {
			parser = new Parser();
	}

	@Test(expected = ParserException.class)
	public void parseRow_TooLongCurrency_ExceptionTrue() throws ParserException {
		parser.parseRow("USDD 900");
	}
	
	@Test(expected = ParserException.class)
	public void parseRow_TooShortCurrency_ExceptionTrue() throws ParserException {
		parser.parseRow("UD 900");
	}
	
	@Test(expected = ParserException.class)
	public void parseRow_NoCurrency_ExceptionTrue() throws ParserException {
		parser.parseRow("900");
	}
	
	@Test(expected = ParserException.class)
	public void parseRow_InvalidCash_ExceptionTrue() throws ParserException {
		parser.parseRow("US 900D");
	}
	
	@Test(expected = ParserException.class)
	public void parseRow_InvalidCashDecimal_ExceptionTrue() throws ParserException {
		parser.parseRow("USD 9..00");
	}
	
	@Test
	public void parseRow_ValidCash_ExceptionFalse() throws ParserException {
		parser.parseRow("USD 900");
	}
	
	@Test
	public void parseRow_ValidCashDecimal_ExceptionFalse() throws ParserException {
		parser.parseRow("USD 900.5");
	}
	
	@Test
	public void parseRow_ManyBlankSpacesBetween_ExceptionFalse() throws ParserException {
		parser.parseRow("  USD    900.5   ");
	}
	
	public void parseRow_Reversed_ExceptionFalse() throws ParserException {
		parser.parseRow("900 USD");
	}

}
