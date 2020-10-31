import java.util.*;

/**
 * Represents a brokerage account. This class maintains the cash balance, the
 * quantities and lots of stock owned, and processes buy and sell trades.
 * 
 * @author Jason A. Covey
 */
public class BrokerageAccount {

	private double m_cash_balance; // tracks the cash balance
	private TreeMap<String, ArrayList<TaxLot>> m_stock; // tracks the stock ownership
	private StockPriceDatabase m_database; // a reference to the data files with pricing information

	/**
	 * Sets up a new brokerage account with an initial deposit.
	 * 
	 * @param initialDeposit the initial deposit.
	 */
	public BrokerageAccount(double initialDeposit) {
		// initialize member variables
		m_cash_balance = initialDeposit;
		m_stock = new TreeMap<String, ArrayList<TaxLot>>();
		m_database = StockPriceDatabase.getInstance();
	}

	/**
	 * Returns the cash balance.
	 * 
	 * @return the cash balance
	 */
	public double getCashBalance() {
		return m_cash_balance;
	}

	/**
	 * Processes a list of trades.
	 * 
	 * @param trades the trades to process
	 */
	public void processTrades(ArrayList<Trade> trades) {
		// TODO Auto-generated method stub
		for (Trade t : trades) {
			if (t.isBuy()) {
				processBuyTrade(t);
			} else {
				processSellTrade(t);
			}
		}
	}

	/**
	 * Processes sell trades
	 * 
	 * @param t the sell trade to process
	 */
	private void processSellTrade(Trade t) {
		if (m_stock.containsKey(t.getSymbol())) {
			// if we own the stock, then get the tax lots
			ArrayList<TaxLot> lots = m_stock.get(t.getSymbol());

			// ensure we have enough quantity to make the trade
			int total_qty = lots.stream().map(tl -> tl.getQty()).reduce(0, Integer::sum);
			if (t.getQty() <= total_qty) {

				// get the price
				double stockPrice = m_database.getPrice(t);

				// reduce the remaining quantity as you liquidate tax lots
				int remaining_qty = t.getQty();
				while (remaining_qty > 0) {

					// try to sell the maximum of the remaining quantity and the amount we have left
					// to sell
					TaxLot tl = lots.get(0);
					int sellingQty = tl.getQty() < remaining_qty ? tl.getQty() : remaining_qty;

					// increase our cash balance by the after-tax proceeds of this sale
					// decrease the remaining quantity we have left to sell
					double proceeds = tl.sellStock(sellingQty, stockPrice, t.getDate());
					m_cash_balance += proceeds;
					m_cash_balance = Math.round(m_cash_balance * 100.0) / 100.0;
					remaining_qty -= sellingQty;

					// print out trade information
					if (StockDataJudgingProgram.DEBUG) {
						System.out.println("Processed: " + t);
						System.out.println("Proceeds: " + proceeds);
						System.out.println("New Balance: " + m_cash_balance);
						System.out.println();
					}

					// clean up empty tax lots and empty stock positions
					if (tl.getQty() == 0) {
						lots.remove(0);
					}

					if (lots.isEmpty()) {
						m_stock.remove(t.getSymbol());
					}
				}
			} else {
				if (StockDataJudgingProgram.DEBUG) {
					System.err.printf("Not enough stock owned\nTotal Quantity: %d\nTrade: %s\n\n", total_qty, t);
				}
			}
		} else {
			if (StockDataJudgingProgram.DEBUG) {
				System.err.printf("Stock not owned\nTrade: %s\n\n", t);
			}
		}
	}

	/**
	 * Processes buy trades
	 * 
	 * @param t the trade to process
	 */
	private void processBuyTrade(Trade t) {
		// get the cost of performing the trade
		double cost = m_database.getValue(t);

		// validate that there is enough cash to make the trade
		if (cost <= m_cash_balance) {

			// make the trade and add a new tax lot to the stock ownership
			m_cash_balance -= cost;
			if (!m_stock.containsKey(t.getSymbol())) {
				m_stock.put(t.getSymbol(), new ArrayList<TaxLot>());
			}
			m_stock.get(t.getSymbol()).add(new TaxLot(t, cost));

			// print out trade information
			if (StockDataJudgingProgram.DEBUG) {
				System.out.println("Processed: " + t);
				System.out.println("Cost: " + cost);
				System.out.println("New Balance: " + m_cash_balance);
				System.out.println();
			}

		} else {
			if (StockDataJudgingProgram.DEBUG) {
				System.err.printf("Not enough cash on hand\nBalance: %f\nCost %s\nTrade: %s\n\n", m_cash_balance, cost, t);
			}
		}
	}
}
