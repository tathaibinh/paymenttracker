# paymenttracker

Clone
-----
git clone https://github.com/pfreiberg/paymenttracker.git

Build
-----
Apache Maven: 3.2.1

Java: 1.7

Go to project folder ("folder-where-you-clone\paymenttracker\paymenttracker") and build with "mvn clean install". Output will be in target folder.

Run
-----
java -jar target\paymenttracker-0.0.1-SNAPSHOT.jar

or 

java -jar target\paymenttracker-0.0.1-SNAPSHOT.jar file-path

Details about program
-----
Program accept one argument with file path, or none argument at all. You can pass the argument when running JAR file via console.

Program works with up to four threads - producer (only if file-path is specified), consumer, listing the console and processing user input from the console.

All payments are stored in HashMap<String, BigDecimal> and are printed every one minute. Each loaded row with payment is verified whether it's valid.

Program includes Javadoc, JUnit (tests run at build) and log4j.

Logging
-----
The program uses granularity in logging and logging to console and file.

Stack trace is logged on the DEBUG level. For the purpose of the demonstration of the program is debug printed to the console, but in normal operation would be logged only to the file (ordinary users are freed from details).

Bonus
-----
Program contains "currency_rates.properties" with currency rates. File can be edited easily, so more currency rates could be added. In case of frequent changes, it would be better to add another parameter, so user would specify the file path with actual courses.
