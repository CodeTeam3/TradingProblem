

public class Ticker {

	String symbol = "";
	double price = 0;
	double shares = 0;
	double equity = 0;
	double averagePrice = 0;
	double high = 0;
	double low = 0;
	//	Constructors
	public Ticker(String symbol, double low, double high) {
		this.symbol = symbol;
		this.low = low;
		this.high = high;
		this.equity = low * shares;
	}
	//	Getters
	public String getSymbol() {
		return symbol;
	}
	public double getAveragePrice() {
		return averagePrice;
	}
	public double getEquity() {
		return equity;
	}
	public double getShares() {
		return shares;
	}
	public double getPrice() {
		return price;
	}
	//	Setters
	public void setPrice(double price) {
		this.price = price;
	}
	//	Buying and selling keeping track of equity and average price 
	public void Buy(double quantity) {
		 double amount = price * quantity;	//	Cash Value of New Position
		//	Going Long
		 if(shares >= 0) {
			 shares += quantity;
			 averagePrice = (equity + amount) / shares;
			 equity += amount;
		 }
		//	Covering Short
		 else if(0 > shares) {
			 //	If Covering the entire position and possibly going Long
			 double newShares = shares + quantity;	//	New share position
			 if(0 > newShares) {
				shares += quantity;
				equity = averagePrice * shares;
			 }
			 else if(newShares > 0) {
				 shares += quantity;
				 equity = price * shares;
				 averagePrice = equity / shares;
			 }
			 else if(newShares == 0) {
				 shares = 0;
				 equity = 0;
				 averagePrice = 0;
			 }
		 }
		//	Check equity for average Price
		if(equity == 0) {
			averagePrice = 0;
		}
	}
	public void Sell(double quantity) {
		double amount = price * quantity;
		//	Closing Long
		if(shares - quantity >= 0){//	Not short
			shares -= quantity;
			equity = shares * averagePrice;
		}
		//	Going Short 
		else if(0 > shares - quantity) {
			if(shares > 0) {
				shares -= quantity;
				equity = shares * price;
				averagePrice = equity / shares;
			}
			else if(0 >= shares) {
				shares -= quantity;
				averagePrice = (equity + amount*-1) / shares;
				equity += amount*-1;
			}
		}
		//	Check equity for average Price
		if(equity == 0) {
			averagePrice = 0;
		}
	}
	
	public double calculateProfit() {
			return price * shares - equity;
	}
}













