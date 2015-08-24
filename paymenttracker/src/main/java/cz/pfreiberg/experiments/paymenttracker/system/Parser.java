package cz.pfreiberg.experiments.paymenttracker.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cz.pfreiberg.experiments.paymenttracker.exception.ParserException;
import cz.pfreiberg.experiments.paymenttracker.model.TypeOfOperation;
import cz.pfreiberg.experiments.paymenttracker.transaction.Transaction;

/**
 * Class parses the input file, and each line is converted (if possible) to the
 * Transaction.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class Parser {

	private static final Logger logger = LogManager.getLogger(Parser.class);

	private BufferedReader br;
	private String path;
	
	private final String CHARSET = "UTF-8";
	private final int CURRENCY_LENGTH = 3;
	
	public Parser() {}

	public Parser(String path) throws FileNotFoundException, IOException {
		File file = new File(path);
		br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), CHARSET));
		this.path = path;
	}

	public Transaction getNextTransaction() throws ParserException {
		String loadedRow = getRow();
		return parseRow(loadedRow);
	}

	private String getRow() {
		String currentRow = "";
		try {
			currentRow = br.readLine();
		} catch (IOException e) {
			logger.error("Error while loading transaction.");
			logger.debug("Stack trace", e);
		}
		return currentRow;
	}

	public Transaction parseRow(String row) throws ParserException {
		Transaction transaction = new Transaction();

		if (row == null) {
			transaction.setLastPayment(true);
			return transaction;
		}

		StringBuilder sbCurrency = new StringBuilder();
		StringBuilder sbCash = new StringBuilder();
		boolean parsingCash = false;
		for (int i = 0; i < row.length(); i++) {

			char actualCharacter = getActualCharacter(row, i);

			if (isBlankSpace(actualCharacter)) {
				continue;
			} else if (isPartOfDigit(actualCharacter)) {
				sbCash.append(actualCharacter);
				parsingCash = true;
			} else if (isNegativeSign(actualCharacter)) {
				transaction.setType(TypeOfOperation.WITHDRAW);
			} else if (isPartOfCurrency(sbCurrency, parsingCash,
					actualCharacter)) {
				sbCurrency.append(actualCharacter);
			} else {
				throw new ParserException("Row with transaction is not valid: "
						+ row);
			}

		}

		String cash = sbCash.toString();
		if (isNumeric(cash)) {
			transaction.setCash(new BigDecimal(cash));
		} else {
			String message = "Row with transaction is not valid: " + row
					+ ". Cash is not a valid number.";
			throw new ParserException(message);
		}

		String currency = sbCurrency.toString().toUpperCase();
		if (currency.length() == CURRENCY_LENGTH) {
			transaction.setCurrency(currency);
		} else {
			String message = "Row with transaction is not valid: " + row
					+ ". Currency must be only 3 letter alphabetic word.";
			throw new ParserException(message);
		}
		transaction.setCurrency(currency);

		return transaction;
	}

	private char getActualCharacter(String row, int position) {
		return row.charAt(position);
	}

	private boolean isBlankSpace(char actualCharacter) {
		return actualCharacter == ' ';
	}

	private boolean isPartOfDigit(char actualCharacter) {
		return Character.isDigit(actualCharacter) || actualCharacter == '.';
	}

	private boolean isNegativeSign(char actualCharacter) {
		return actualCharacter == '-';
	}

	private boolean isPartOfCurrency(StringBuilder sbCurrency,
			boolean parsingCash, char actualCharacter) {
		return sbCurrency.length() < CURRENCY_LENGTH && !parsingCash
				&& Character.isAlphabetic(actualCharacter);
	}

	private boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	public String getPath() {
		return path;
	}

}
