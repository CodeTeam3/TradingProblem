import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/**
 * The main program of the Stock Data Judging Program. This program takes a list
 * of trades from either a file or a command line argument. It then performs a
 * first-pass validation of the trades and places all valid trades into a queue
 * for execution. It then sets up the brokerage account and processes the
 * trades.
 * 
 * @author Jason A. Covey
 */
public class StockDataJudgingProgram {
	/*******************************************
	 * DEBUG FLAG SET THIS TO TRUE TO SEE DEBUG INFORMATION
	 */
	public static boolean DEBUG = false;

	/**
	 * END DEBUG FLAG
	 *******************************************/

	/**
	 * The main method of the program. This processes the file and calculates the
	 * dollar value of the account.
	 * 
	 * @param args may contain the trade file as the first item of the array
	 */
	public static void main(String[] args) {

		double score = 0.00; // variable to hold the score

		try {
			String tradesFile = "C:\\Users\\Master oh Master\\Desktop\\Code-A-Thon-2020\\Test\\trading-problem\\src\\stock_test.txt";

			// Get the location of the trades file either by user or by command line
			// argument
//			if (args.length == 0) {
//				System.out.println("Enter filename: ");
//				Scanner input = new Scanner(System.in);
//				tradesFile = input.next();
//				input.close();
//			} else {
//				tradesFile = args[0];
//			}

			ArrayList<String> str_trades = readAllTrades(tradesFile); // holds the trades in the file
			ArrayList<Trade> trades = new ArrayList<Trade>(); // a place to store the trades before processing
			TreeSet<String> uniqueStockTracker = new TreeSet<String>(); // a way to ensure unique date/symbol
																		// combinations
			// we need to ensure that the file ascends in date only, this helps track that
			Calendar previousDate = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
			previousDate.setTimeInMillis(0L);

			// validate and add all trades to the queue
			// any trade which does not validate will be thrown out but processing will
			// continue
			for (String trade : str_trades) {

				try {
					// Trade String Format:
					// ACTION DATE SYMBOL QTY PRICE
					
					String[] parts = trade.split(" ");

					if (parts.length != 5) {
						throw new Exception("Too many tokens in trade string!");
					}

					// Validate the action
					String action = parts[0];
					if (!action.equals("BUY") && !action.equals("SELL")) {
						throw new Exception("Invalid trade action!");
					}

					// Validate the date
					String ds = parts[1];
					int month = Integer.parseInt(ds.substring(0, 2));
					int day = Integer.parseInt(ds.substring(2, 4));
					int year = Integer.parseInt(ds.substring(4));
					Calendar date = new GregorianCalendar(year, month - 1, day, 0, 0, 0);

					if (date.after(previousDate) || date.equals(previousDate)) {
						previousDate = date;
					} else {
						throw new Exception("Invalid Date");
					}

					// Symbol isn't validated here. If a symbol is invalid then incorrect
					// prices will be returned.
					String symbol = parts[2];

					// Quantity isn't validated other than requiring it to be a number.
					int qty = Integer.parseInt(parts[3]);

					// Validate that buy and sell have appropriate prices
					String price = parts[4];
					if (action.equals("BUY")) {
						if (!price.equals("OPEN") && !price.equals("LOW")) {
							throw new Exception("Invalid Price");
						}
					}

					if (action.equals("SELL")) {
						if (!price.equals("CLOSE") && !price.equals("HIGH")) {
							throw new Exception("Invalid Price");
						}
					}

					// Validate that this trade is unique for the stock symbol and day
					String key = ds + symbol;
					if (uniqueStockTracker.contains(key)) {
						throw new Exception("You may not perform multiple trades on the same stock in the same day.");
					} else {
						uniqueStockTracker.add(key);
					}

					// if it passes all tests, then add the trade to the list
					Trade t = new Trade(action, date, symbol, qty, price, trade);
					trades.add(t);
				} catch (Exception ex) {
						System.err.println(ex.getMessage());
						System.err.println("Error processing trade: " + trade);
				}
			}

			BrokerageAccount ba = new BrokerageAccount(1000.00);
			ba.processTrades(trades);
			score = ba.getCashBalance();
		} catch (Exception ex) {
			// something happened we didn't expect
			score = 0.00;
		}

		// Print out the score
		System.out.printf("%.2f", score);

	}

	/**
	 * A method to read the trade list file and return the results as an ArrayList.
	 * 
	 * @param tradesFile the filename of the file containing the trade list
	 * @return an ArrayList containing the trades in the file
	 * @throws FileNotFoundException 
	 */
	private static ArrayList<String> readAllTrades(String tradesFile) throws FileNotFoundException {
		ArrayList<String> linesToRead = new ArrayList<String>();
		try {
			// Need to cast the return result to an ArrayList
			Path path = Paths.get(tradesFile);
			linesToRead = (ArrayList<String>) Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			if (DEBUG) {
				System.out.println("There was a problem getting to the file!");
			}
		}
		return linesToRead;
	}

}
