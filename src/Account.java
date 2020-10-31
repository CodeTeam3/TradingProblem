import java.io.IOException;
import java.util.ArrayList;

public class Account {

	//	Objects 
	Tickers tickers;
	//Logging Information
	int sessionid;
	//	Account details
	String username;
	String password;
	//	Cash Balances
	double starting = 1000.00;	//	Starting Capital
	double cashbalance = starting;	//	Cash Portion of Account Balance
	double stockbalance = 0;	//	Stock Portion of Account Balance	
	double shortToCover = 0;	//	How much Cash it would cost to Cover (is NEGATIVE)
	double shortholding = 0;	//	Cash reserves for short positions	(is NEGATIVE)
	double totalbalance = cashbalance + stockbalance - shortToCover;	
	double totalTax = 0;
	
	//	Constructors
	public Account(String username, String password) throws IOException {
		this.tickers = new Tickers();
		this.username = username;
		this.password = password;
	}
	
	public Account(String username, String password, double starting) throws IOException {
		this.tickers = new Tickers();
		this.username = username;
		this.password = password;
		this.starting = starting;
	}
	// **************************
	//	Get Functions
	
	public String getUser() {
		return username;
	}
	public double getCashBalance() {
		return cashbalance;
	}
	public double getTotalBalance() {
		return totalbalance;
	}
	public double getStockBalance() {
		return stockbalance;
	}

	public double getShortHolding() {
		return shortholding;
	}
	
	public double getShortToCover() {
		return shortToCover;
	}
	
	//	Update, Buy, and Sell Functions
	public void updateAccountBalances() {
		double stockbalanceTEMP = 0;
		double shortholdingTEMP = 0;
		double shortToCoverTEMP = 0;
		for(Ticker t : tickers.getTickers()) {
			if(t.getEquity() > 0) {
				stockbalanceTEMP += t.getHigh() * t.getShares();
			}
			else if(0 > t.getEquity()) {
				shortholdingTEMP += t.getEquity();
				shortToCoverTEMP += t.getLow() * t.getShares();
			}
		}
		stockbalance = stockbalanceTEMP;
		shortholding = shortholdingTEMP;
		shortToCover = shortToCoverTEMP;
		double shortValue = shortholding + (shortholding - shortToCover);
		totalbalance = stockbalance + cashbalance - shortValue;
	}
	public void buyShares(String symbol, double sharesToBuy) {
		Ticker t = tickers.getTicker(symbol);	//	Getting the ticker
		double cashValue = t.getLow() * sharesToBuy;	//	Get's the cash value of the new position then checks if we have enough cash before ordering
		//	TO BUY
		if(t.getShares() >= 0 && sharesToBuy > 0) {
			if(cashValue > cashbalance) {
				System.out.println("\nError: Insufficient funds to purchase >>>   " + symbol + ": +" + sharesToBuy + "\n");
				return;
			} else {
				cashbalance -= cashValue;
				t.Buy(sharesToBuy);
			}
		} 
		//	If I am short
//		if(0 > t.getShares()) {
//			double newShares = sharesToBuy + t.getShares();
//			if((newShares * t.getPrice()) > cashbalance + (t.getShares()*-1* t.getPrice())) {
//				System.out.println("\nError: Insufficient funds to purchase >>>   " + symbol + ": " + sharesToBuy + "\n");
//				return;
//			}
//			if(0 > newShares) {
//				//	New Cash = what I paid + Loss/Profit
//				//	New cash = (what I'm buying x myAveragePrice) + (the shares I'm buying x the margin between the price I sold and am buying at(profits))
//				cashbalance += sharesToBuy * t.averagePrice + sharesToBuy * (t.getAveragePrice() - t.getPrice());
//				t.Buy(sharesToBuy);
//			}
//			else {
//				cashbalance += (t.getShares() * t.averagePrice * -1) + t.getShares() * -1 * (t.getAveragePrice() - t.getPrice());
//				t.Buy(sharesToBuy);
//				cashbalance -= t.getShares() * t.getPrice();
//			}
//
//		}
		updateAccountBalances();
		//printPositions();
	}
	public void sellShares(String symbol, double sharesToSell) {
		Ticker t = tickers.getTicker(symbol);	//	Getting the ticker
		double newShares = t.getShares() - sharesToSell; // Calculating the different between current shares and the ones we are going to sell 
		//	If We are selling and not going short(below zero shares)
		if(newShares >= 0) {
			double taxes = doTaxes(t, sharesToSell);
			cashbalance +=  t.getHigh() * sharesToSell - taxes; 
			t.Sell(sharesToSell);
		}
		//	If We are going short
//		if(0 > newShares) {
//			//	Check for enough money to short
//			if(t.getPrice()*newShares*(-1) > cashbalance) {
//				System.out.println("\nError: Insufficient funds to sell >>>   " + symbol + ": -" + sharesToSell + "\n");
//				return;
//			}
//			//	Anything above 0 shares, we are selling and adding back into our cash. Basically closing our long position.
//			if(t.getShares() > 0) {
//				cashbalance += t.getPrice() * t.getShares();
//			}
//			t.Sell(sharesToSell);
//			cashbalance += t.getPrice() * t.getShares();
//		}
		//	Update and Print
		updateAccountBalances();
		//printPositions();
	}
	//	Print Information
	public void printPositions() {
		int c = 1;
		updateAccountBalances();
		if(stockbalance != 0 || shortToCover != 0) {
			System.out.println("        Current Positions");
			System.out.println("    SYM   Shares     Low      High   Avg Price  Market Val     Cost     Profit/Loss   %");
			for(Ticker t : tickers.getTickers()) {
				if(t.getShares() == 0) {
					continue;
				}
				System.out.println(c + ". [" + 		// 	Symbol - Shares - Price - Avg Price - Total - Cost - %change
						String.format("%-4s", t.getSymbol()) + "|" + 
						String.format("%7s",String.format("%,.2f", t.getShares())) + 
						"| $" + String.format("%7s", String.format("%,.2f",t.getLow())) + 
						"| $" + String.format("%7s", String.format("%,.2f",t.getHigh())) + 
						"| $" + String.format("%7s", String.format("%,.2f", t.getAveragePrice())) +
						"| $" + String.format("%10s", String.format("%,.2f", t.getShares() * t.getHigh())) + 
						"| $" + String.format("%10s", String.format("%,.2f", t.getEquity())) +
						"| $" + String.format("%7s", String.format("%,.2f", t.calculateProfit())) +
						"| %" + String.format("%5s", String.format("%,.2f", (t.calculateProfit() / Math.abs(t.getEquity()) * 100))) +
						"]");
				c++;
			}
			System.out.println();
		}
		System.out.println("Cash:  $" + String.format("%,.2f" ,getCashBalance()) + "\nStock: $" + 
					String.format("%,.2f", getStockBalance()) +"\nShort: $" + String.format("%,.2f" ,getShortToCover()) +
					"\nTotal Value: $" + String.format("%,.2f", getTotalBalance()));
		
		System.out.println("_________________________________________");
	}
	
//	public void updateTickerPrice(String symbol, double newPrice) {
//		tickers.updateTicker(symbol, newPrice);
//	}
	
	public  ArrayList<Ticker> getMyTickers() {
		return tickers.getTickers();
	}
	
	public void clearTickers(){
		tickers.clearTickers();
	}
	

	private double doTaxes(Ticker t, double quantity) {
		if(t.getHigh() > t.getAveragePrice()) {
			double taxes = (t.getHigh() - t.getLow()) * quantity * .2;
			System.out.println("\nYou paid $" + String.format("%,.2f", taxes) + " in taxes.\n");
			totalTax += taxes;
			return taxes;
		}
		return 0;
	}
	
	public void addTicker(Ticker ticker) {
		tickers.addTicker(ticker);
	}

}

















