import java.util.*;

/**
 * Stores information about a tax lot, or a group of stocks owned
 * which share the same cost.  This is important for calculating taxes
 * and processing shares first-in-first-out.  If taxes were not a concern
 * we would not need this class.
 * 
 * @author Jason A. Covey
 *
 */
public class TaxLot {

	private Calendar m_buy_date; // the date the stock was purchased, used to determine tax rate
	private int m_qty; // how much stock is in this lot
	private double m_cost; // the total cost of the purchase
	
	/**
	 * Sets up the tax lot from a trade and cost
	 * information.
	 * 
	 * @param t the trade describing the buy order
	 * @param cost how much did the trade cost to execute
	 */
	public TaxLot(Trade t, double cost) {
		m_buy_date = t.getDate();
		m_qty = t.getQty();
		m_cost = cost;
	}

	/**
	 * Returns the quantity of stock in the lot
	 * @return the quantity of stock
	 */
	public int getQty() {
		return m_qty;
	}

	/**
	 * Performs the action of selling a stock.  This method returns the after-tax proceeds
	 * of the sale.  It also adjusts the tax lot for any remaining quantity.
	 * 
	 * @param sellingQty how many are you selling
	 * @param stockPrice what price are you selling it at
	 * @param selldate the date you are selling the stock
	 * @return the after-tax proceeds of the sale
	 */
	public double sellStock(int sellingQty, double stockPrice, Calendar selldate) {
		// calculate proceeds and tax
		double ratio = sellingQty * 1.0 / getQty();
		double cost = Math.round(m_cost * ratio * 100.0) / 100.0;
		double proceeds = sellingQty * stockPrice;
		double profit = proceeds - cost;
		double tax = Math.round(profit * calculateTax(selldate) * 100.0) / 100.0;
		
		// reduce the tax lot holdings
		m_qty -= sellingQty;
		m_cost -= cost;
		m_cost = Math.round(m_cost * 100.0) / 100.0;
		
		// return the after-tax proceeds
		double aftertax = Math.round((proceeds - tax) * 100.0) / 100.0;
		return aftertax;
	}

	/**
	 * Calculates the tax rate.  The tax rate is either 20% for stock owned less than one year, or
	 * 10% for stocks owned one year or more.
	 * 
	 * @param selldate the date the sale happens
	 * @return the tax rate of either 10 or 20%
	 */
	private double calculateTax(Calendar selldate) {
		long days = (selldate.getTimeInMillis() - m_buy_date.getTimeInMillis()) / 1000 / 60 / 60 / 24;
		return days >= 365 ? .10 : .20;
	}

}
