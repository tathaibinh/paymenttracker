package cz.pfreiberg.experiments.paymenttracker.transaction;

import java.math.BigDecimal;

import cz.pfreiberg.experiments.paymenttracker.model.TypeOfOperation;

/**
 * Class representing banking transaction.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 *
 */
public class Transaction {

	private BigDecimal cash;
	private String currency;
	private TypeOfOperation type;

	/**
	 * Auxiliary variable for working with threads.
	 */
	private boolean lastPayment;

	public Transaction() {
		cash = BigDecimal.ZERO;
		type = TypeOfOperation.DEPOSIT;
		lastPayment = false;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public TypeOfOperation getType() {
		return type;
	}

	public void setType(TypeOfOperation type) {
		this.type = type;
	}

	public boolean isLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(boolean lastPayment) {
		this.lastPayment = lastPayment;
	}

	@Override
	public String toString() {
		return "Payment [cash=" + cash + ", currency=" + currency + ", type="
				+ type + ", lastPayment=" + lastPayment + "]";
	}

}
