import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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

	public static void main(String[] args) throws IOException, InterruptedException {
		
		// Needs UserName + Password
		Account a = new Account("Hi", "There");
		
		// Should be the path to the biggestgainers.txt
		File file = new File("C:\\Users\\Master oh Master\\Desktop\\Code-A-Thon-2020\\codeathon-trading-problem\\src\\biggestgainers.txt");
		Scanner scan = new Scanner(file);
		while(scan.hasNext()) {
			try {
				String[] data = scan.nextLine().split(" ");
				Ticker New = new Ticker(data[0], Double.parseDouble(data[4]), Double.parseDouble(data[3])); // Symbol, Low, High
				double toBuy =  Math.floor(a.getCashBalance()/Double.parseDouble(data[3]));
				a.addTicker(New);
				a.printPositions();
				a.buyShares(data[0], toBuy);
				a.printPositions();
				a.sellShares(data[0], toBuy);
			}catch(Exception e) {
				System.out.println("Error in main");
			}

		}
		a.printPositions();
	}
}
