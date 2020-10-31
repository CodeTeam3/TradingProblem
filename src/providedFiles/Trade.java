import java.util.*;

/**
 * Stores information about a trade.  This class does very little beyond that.
 * 
 * @author Jason A. Covey
 */
public class Trade {

	private String m_action; // Action: BUY or SELL
	private Calendar m_date; // the date of the trade
	private String m_symbol; // the stock symbol (unvalidated)
	private int m_qty; // the quantity to move
	private String m_price; // for BUY: OPEN or LOW, for SELL: CLOSE or HIGH
	private String m_trade_string; // store the string used to create it off as a nice toString
	
	/**
	 * The main constructor which takes in the building blocks of a trade.
	 * @param action Action: BUY or SELL
	 * @param date the date of the trade
	 * @param symbol the stock symbol (unvalidated)
	 * @param qty the quantity to move
	 * @param price for BUY: OPEN or LOW, for SELL: CLOSE or HIGH
	 * @param trade store the string used to create it off as a nice toString
	 */
	public Trade(String action, Calendar date, String symbol, int qty, String price, String trade) {
		m_action = action;
		m_date = date;
		m_symbol = symbol;
		m_qty = qty;
		m_price = price;
		m_trade_string = trade;
	}

	
	public String getAction() {
		return m_action;
	}

	public Calendar getDate() {
		return m_date;
	}

	public String getSymbol() {
		return m_symbol;
	}

	public int getQty() {
		return m_qty;
	}

	public String getPrice() {
		return m_price;
	}

	public boolean isBuy() {
		return getAction().equals("BUY");
	}

	public String toString() {
		return m_trade_string;
	}

}
