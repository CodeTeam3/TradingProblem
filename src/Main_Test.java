import java.io.IOException;

public class Main_Test {
	
	//	This is to demonstrate the 'shell' of a system this is. 
	//	We will have a predefined list of tickers and they will
	//  have a name and some other values. The example below is 
	//	Apple. Notice first we create an account, and then we 
	//	interact with the account object. It contains a list of
	//	tickers and each ticker has data such as how many we own
	//	and the value of the stock itself. We have calls to update
	//	the values already built in and there is some, although I
	//	can't promise perfect error detection, it needs more testing. 

	public static void main(String[] args) throws IOException {
		
		// Needs UserName + Password
		Account a = new Account("Hi", "There");
		
		//	Test1: Prints positions (none) and then purchases 1 share of apple and prints our positions again.
		a.printPositions();	
		a.buyShares("AAPL", 1);
		a.printPositions();
		
		//	Test2: Update Price of Apple stock, then shell 3 shares going short (-2) shares. 
		a.updateTickerPrice("AAPL", 320.00);
		a.sellShares("AAPL", 3);
		a.printPositions();
		
		//	Test3: Update Price and print, then buy 6 shares (exceeds our buying power) and then reprint. (This is an example of the error handling).
		a.updateTickerPrice("AAPL", 310.00);
		a.printPositions();
		a.buyShares("AAPL", 6);
		a.printPositions();
		
		a.sellShares("AAPL", 5);
		a.printPositions();
		
	}
}
