import java.util.ArrayList;

//Here is where we initialize all tickers. 
//An object is created for the account and every account should
//have access to it's own unique array of tickers. You could create different 
//objects with different tickers, but this is intended to be
//all inclusive (eventually).

public class Tickers {

	static ArrayList<Ticker> tickers = new ArrayList<Ticker>();
	
	public void addTicker(Ticker ticker) {
		tickers.add(ticker);
	}
	
	//	Use to find Ticker
	public Ticker getTicker(String symbol) {
		Ticker t = tickers.get(findTicker(symbol));
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
	
//	public void updateTicker(String symbol, double newPrice) {
//		int index = findTicker(symbol);
//		tickers[index].setPrice(newPrice);
//	}
	public  ArrayList<Ticker> getTickers() {
		return tickers;
	}

	public void clearTickers() {
		tickers.clear();
	}

}
