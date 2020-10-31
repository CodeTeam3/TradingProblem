import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

/**
 * This class is a singleton which gets information about stock pricing
 * from the data files.
 * 
 * @author Jason A. Covey
 *
 */
public class StockPriceDatabase {

	private static StockPriceDatabase m_instance; // the only instance of the class allowed
	
	private String[] m_current_file; // the current file being worked with
	private Calendar m_current_date; // the date which corresponds to the current file
	
	/**
	 * Main constructor which does nothing.  It's only purpose here is to be marked private
	 * to implement the singleton pattern.
	 */
	private StockPriceDatabase() {
		
	}

	/**
	 * Gets the instance of the database.
	 * @return the only instance of the database
	 */
	public static StockPriceDatabase getInstance() {
		// Set it up first if we haven't already
		if (m_instance == null) {
			m_instance = new StockPriceDatabase();
		}
		
		return m_instance;
	}

	/**
	 * Gets the total value of a trade (quantity * price)
	 * @param t the trade to value
	 * @return the total value of the trade
	 */
	public double getValue(Trade t) {
		return Math.round(t.getQty() * getPrice(t) * 100) / 100;
	}

	/**
	 * Gets the price of a stock on a day
	 * @param t the trade with date and symbol information
	 * @return the price of the stock on the date
	 */
	public double getPrice(Trade t) {
		getDayData(t.getDate());
		
		// data files can be inefficient, we just have to search until we
		// find the correct symbol
		for (String line : m_current_file) {
			if (line.startsWith(t.getSymbol() + ",")) {
				String parts[] = line.split(",");
				String priceWanted = t.getPrice();
				if (priceWanted.equals("OPEN")) {
					return Double.parseDouble(parts[2]);
				}
				if (priceWanted.equals("HIGH")) {
					return Double.parseDouble(parts[3]);
				}
				if (priceWanted.equals("LOW")) {
					return Double.parseDouble(parts[4]);
				}
				if (priceWanted.equals("CLOSE")) {
					return Double.parseDouble(parts[5]);
				}
			}
		}

		// the price wasn't found so give back nonsense
		if (t.getAction().equals("BUY")) {
			return Double.MAX_VALUE;
		} else {
			return 0.00;
		}		
	}
	
	/**
	 * Gets data for the day from teh file system.
	 */
	private void getDayData(Calendar d) {
		if (!d.equals(m_current_date)) {
			String year = d.get(Calendar.YEAR)+"";
			String month = td(d.get(Calendar.MONTH) + 1);
			String day = td(d.get(Calendar.DAY_OF_MONTH));
			String filename = "C:\\Users\\Master oh Master\\Desktop\\Code-A-Thon-2020\\codeathon-trading-problem\\src\\stockdata\\NASDAQ_" + year + "/NASDAQ_" + year + month + day + ".csv";
			
			m_current_date = d;
			m_current_file = readFile(filename, StandardCharsets.UTF_8).split("[\\r\\n]+");
		}
	}

	/**
	 * A simple method to left pad a number to two wide with a "0" if necessary.
	 * @param i the number to pad
	 * @return a string at least two wide padded on the left with "0"
	 */
	private String td(int i) {
		if (i < 10) {
			return "0" + i;
		}
		return i+"";
	}
	
	/**
	 * A method to read an entire file
	 * @param path the path of the file
	 * @param encoding the character set used by the file
	 * @return
	 */
	private String readFile(String path, Charset encoding)
	{
		byte[] encoded = new byte[] {};
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encoded, encoding);
	}

}
