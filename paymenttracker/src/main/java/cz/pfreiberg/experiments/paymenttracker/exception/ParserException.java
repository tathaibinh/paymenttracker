package cz.pfreiberg.experiments.paymenttracker.exception;

/**
 * Custom exception class for parsing problems.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class ParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParserException(String message) {
		super(message);
	}

}
