package cz.pfreiberg.experiments.paymenttracker.system;

import java.math.BigDecimal;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

import cz.pfreiberg.experiments.paymenttracker.model.TypeOfOperation;
import cz.pfreiberg.experiments.paymenttracker.transaction.Transaction;
import cz.pfreiberg.experiments.paymenttracker.transaction.TransactionSystem;

/**
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class TransactionSystemTest {
	
	TransactionSystem transactionSystem;

	@Before 
	public void initTransactionSystem() {
		transactionSystem = new TransactionSystem();
	}
	
	@Test
	public void makeTransaction_PositiveBalance_Put_True() {
		final String currency = "USD";
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(50), currency, TypeOfOperation.DEPOSIT));
		
		Assert.assertEquals(50L, transactionSystem.getBalanceForCurrency(currency).longValue());
	}
	
	@Test
	public void makeTransaction_PositiveBalance_Add_True() {
		final String currency = "USD";
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(50), currency, TypeOfOperation.DEPOSIT));
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(50), currency, TypeOfOperation.DEPOSIT));
		
		Assert.assertEquals(100L, transactionSystem.getBalanceForCurrency(currency).longValue());
	}
	
	@Test
	public void makeTransaction_NegativeBalance_Put_True() {
		final String currency = "USD";
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(50), currency, TypeOfOperation.WITHDRAW));
		
		Assert.assertEquals(-50L, transactionSystem.getBalanceForCurrency(currency).longValue());
	}
	
	@Test
	public void makeTransaction_ZeroBalanceFromPositive_True() {
		final String currency = "USD";
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(50), currency, TypeOfOperation.DEPOSIT));
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(50), currency, TypeOfOperation.WITHDRAW));
		
		Assert.assertEquals(0L, transactionSystem.getBalanceForCurrency(currency).longValue());
	}
	
	@Test
	public void makeTransaction_ZeroBalanceFromNegative_True() {
		final String currency = "USD";
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(150), currency, TypeOfOperation.WITHDRAW));
		transactionSystem.makeTransaction(createTransaction(new BigDecimal(150), currency, TypeOfOperation.DEPOSIT));
		
		Assert.assertEquals(0L, transactionSystem.getBalanceForCurrency(currency).longValue());
	}
	
	private Transaction createTransaction(BigDecimal cash, String currency, TypeOfOperation type) {
		Transaction transaction = new Transaction();
		transaction.setCash(cash);
		transaction.setCurrency(currency);
		transaction.setType(type);
		return transaction;
	}

}
