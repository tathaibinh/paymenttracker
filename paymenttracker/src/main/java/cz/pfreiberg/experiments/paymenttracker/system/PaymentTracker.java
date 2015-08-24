package cz.pfreiberg.experiments.paymenttracker.system;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class. Program takes none, or one argument with the file path. The
 * program is terminated after typing 'quit' to the console.
 * 
 * @author Petr Freiberg (freibergp@gmail.com)
 * 
 */
public class PaymentTracker {

	private static final Logger logger = LogManager
			.getLogger(PaymentTracker.class);

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		if (args.length == 0) {
			Controller controller = new Controller();
			controller.run(false);
		} else if (args.length == 1) {
			String pathToFile = args[0];
			try {
				Controller controller = new Controller(pathToFile);
				controller.run(true);
			} catch (FileNotFoundException e) {
				logger.fatal("Input file was not found, or can't be read.");
				logger.debug("Stack trace", e);
			} catch (IOException e) {
				logger.fatal("Fatal error during reading input file.");
				logger.debug("Stack trace", e);
			}
		} else {
			String errorMessage = "Program accepts one parameter with file path, or none parameter at all.";
			logger.fatal(errorMessage);
		}

	}

}
