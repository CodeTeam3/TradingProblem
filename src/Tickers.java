

//Here is where we initialize all tickers. 
//An object is created for the account and every account should
//have access to it's own unique array of tickers. You could create different 
//objects with different tickers, but this is intended to be
//all inclusive (eventually).

public class Tickers {
	
	static Ticker AAPL = new Ticker("AAPL", 310.00);
	static Ticker SPY = new Ticker("SPY", 305.00);
	static Ticker TSLA = new Ticker("TSLA", 850.00);
	static Ticker WMT = new Ticker("WMT", 130.00);
	static Ticker NCLH = new Ticker("NCLH", 15.00);
	
	static Ticker[] tickers = {AAPL, TSLA, WMT, SPY, NCLH};
	
	//	Use to find Ticker
	public Ticker getTicker(String symbol) {
		Ticker t = tickers[findTicker(symbol)];
		return t;
	}
	//	Returns index, to find ticker use above ^
	public int findTicker(String symbol) {
		int c = 0;
		for(Ticker t : tickers) {
			if(symbol.contentEquals(t.getSymbol())) {
				return c;
			}
			c++;
		}
		return -1;
	}
	
	public void updateTicker(String symbol, double newPrice) {
		int index = findTicker(symbol);
		tickers[index].setPrice(newPrice);
	}
	public Ticker[] getTickers() {
		return tickers;
	}


}
