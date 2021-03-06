package cz.pfreiberg.experiments.paymenttracker.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cz.pfreiberg.experiments.paymenttracker.model.TypeOfOperation;

/**
 * Stores and operates (via Transaction class) with actual bank balances and
 * currency rates.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class TransactionSystem {

	private static final Logger logger = LogManager
			.getLogger(TransactionSystem.class);

	private Map<String, BigDecimal> bankAccounts = new HashMap<>();
	private Map<String, BigDecimal> currencyRates;

	public TransactionSystem() {
		currencyRates = loadCurrencyRates();
	}

	public void makeTransaction(Transaction transaction) {
		BigDecimal cash = bankAccounts.get(transaction.getCurrency());
		if (cash == null) {
			if (transaction.getType() == TypeOfOperation.DEPOSIT) {
				cash = transaction.getCash();
			} else {
				cash = transaction.getCash().negate();
			}
		} else {
			if (transaction.getType() == TypeOfOperation.DEPOSIT) {
				cash = cash.add(transaction.getCash());
			} else {
				cash = cash.subtract(transaction.getCash());
			}
		}
		bankAccounts.put(transaction.getCurrency(), cash);
	}

	public void printBankAccounts() {
		for (String key : bankAccounts.keySet()) {
			BigDecimal value = bankAccounts.get(key);
			if (!value.equals(BigDecimal.ZERO)) {
				if (currencyRates.containsKey(key)) {
					BigDecimal conversion = getCurrencyInUsd(key, value);
					logger.info(key + " " + value + " (USD "
							+ conversion.toString() + ")");
				} else {
					logger.info(key + " " + value);
				}
			}

		}
	}

	public BigDecimal getBalanceForCurrency(String currency) {
		return bankAccounts.get(currency);
	}
	
	private BigDecimal getCurrencyInUsd(String key, BigDecimal value) {
		return value.divide(
				currencyRates.get(key), 3, RoundingMode.HALF_UP);
	}

	private Map<String, BigDecimal> loadCurrencyRates() {
		Map<String, BigDecimal> map = new HashMap<>();
		Properties properties = new Properties();
		try {

			properties.load(TransactionSystem.class
					.getResourceAsStream("/currency_rates.properties"));

		} catch (IOException e) {
			logger.error("Error while loading file with currency rates. Conversions cannot be displayed.");
			logger.debug("Stack trace", e);
			return map;
		}

		@SuppressWarnings("rawtypes")
		Enumeration e = properties.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			map.put(key, new BigDecimal(properties.getProperty(key)));
		}

		return map;
	}
}
