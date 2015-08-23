package cz.pfreiberg.experiments.paymenttracker.system;

import cz.pfreiberg.experiments.paymenttracker.transaction.TransactionSystem;

/**
 * Provides a listing of current balances.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class Printer implements Runnable {

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
			System.out.print("Closing balance:\n");
			paymentSystem.printBankAccounts();
		}
	}

}
