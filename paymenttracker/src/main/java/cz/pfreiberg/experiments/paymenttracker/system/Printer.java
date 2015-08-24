package cz.pfreiberg.experiments.paymenttracker.system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cz.pfreiberg.experiments.paymenttracker.transaction.TransactionSystem;

/**
 * Provides a listing of current balances.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class Printer implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(Printer.class);

	private TransactionSystem paymentSystem;

	public Printer(TransactionSystem paymentSystem) {
		this.paymentSystem = paymentSystem;
	}

	public void run() {
		try {
			while (true) {
				paymentSystem.printBankAccounts();
				Thread.sleep(60000);
			}
		} catch (InterruptedException e) {
			logger.info("Closing balance:\n");
			paymentSystem.printBankAccounts();
		}
	}

}
