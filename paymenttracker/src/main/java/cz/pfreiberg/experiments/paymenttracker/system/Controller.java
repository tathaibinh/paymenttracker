package cz.pfreiberg.experiments.paymenttracker.system;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cz.pfreiberg.experiments.paymenttracker.exception.ParserException;
import cz.pfreiberg.experiments.paymenttracker.transaction.Transaction;
import cz.pfreiberg.experiments.paymenttracker.transaction.TransactionSystem;

/**
 * Controller provides proper cooperation of all threads, ensuring the proper
 * functioning of the program.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class Controller {

	private static final Logger logger = LogManager.getLogger(Controller.class);

	private final int BUFFER_SIZE = 10000;

	private Parser parser;
	private TransactionSystem transactionSystem;
	private BlockingQueue<Transaction> queue;

	public Controller() {
		queue = new ArrayBlockingQueue<>(BUFFER_SIZE);
		transactionSystem = new TransactionSystem();
		parser = new Parser();
	}

	public Controller(String path) throws FileNotFoundException, IOException {
		this();
		parser = new Parser(path);
	}

	public void run(boolean withFile) {
		Printer printer = new Printer(transactionSystem);
		Consumer consumer = new Consumer(queue);

		Thread printerThread = new Thread(printer);
		Thread producerThread = null;
		Thread consumerThread = new Thread(consumer);

		printerThread.start();
		if (withFile) {
			logger.info("Parsing user input and file on path: "
					+ parser.getPath());
			Producer producer = new Producer(queue);
			producerThread = new Thread(producer);
			producerThread.start();
		} else {
			logger.info("Waiting for parsing user input.");
		}
		consumerThread.start();
		consoleReader();

		if (withFile) {
			if (producerThread.isAlive()) {
				producerThread.interrupt();
			}
		}

		try {
			consumerThread.join();
		} catch (InterruptedException e) {
			logger.error(
					"When executing transactions from a file an error appeared.",
					e);
		}

		if (printerThread.isAlive()) {
			printerThread.interrupt();
		}

	}

	private class Producer implements Runnable {

		private BlockingQueue<Transaction> queue;

		public Producer(BlockingQueue<Transaction> queue) {
			this.queue = queue;
		}

		public void run() {
			Transaction payment = null;
			try {
				while (true) {
					try {
						payment = parser.getNextTransaction();
					} catch (ParserException e) {
						logger.error("Error during parsing.", e);
						continue;
					}

					if (payment.isLastPayment()) {
						break;
					}

					queue.put(payment);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	private class Consumer implements Runnable {

		private BlockingQueue<Transaction> queue;

		public Consumer(BlockingQueue<Transaction> queue) {
			this.queue = queue;
		}

		public void run() {
			Transaction payment = null;
			while (true) {
				try {
					payment = queue.take();

					if (payment.isLastPayment()) {
						break;
					}

					transactionSystem.makeTransaction(payment);

				} catch (InterruptedException e) {
					logger.error(
							"Error during storing payment: "
									+ payment.toString(), e);
					Thread.currentThread().interrupt();
				}
			}

		}

	}

	private void consoleReader() {

		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));
		Transaction payment;
		while (true) {
			try {
				String line = console.readLine();

				if (line.equals("quit")) {
					payment = new Transaction();
					payment.setLastPayment(true);
					queue.put(payment);
					break;
				}

				try {
					payment = (parser.parseRow(line));
				} catch (ParserException e) {
					logger.error("Error during parsing.", e);
					continue;
				}

				queue.put(payment);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
