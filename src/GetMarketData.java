import java.io.File;
import java.util.ArrayList;

public class GetMarketData {

	//	Had to Use full paths because Windows ='(
	//	Enter your own file paths.
	static String[] FilePaths = {"C:\\\\Users\\\\Master oh Master\\\\Desktop\\\\Code-A-Thon-2020\\\\codeathon-trading-problem\\\\src\\\\stockdata\\\\NASDAQ_2015", 
		                      "C:\\\\Users\\\\Master oh Master\\\\Desktop\\\\Code-A-Thon-2020\\\\codeathon-trading-problem\\\\src\\\\stockdata\\\\NASDAQ_2016",
		                      "C:\\\\Users\\\\Master oh Master\\\\Desktop\\\\Code-A-Thon-2020\\\\codeathon-trading-problem\\\\src\\\\stockdata\\\\NASDAQ_2017",
		                      "C:\\\\Users\\\\Master oh Master\\\\Desktop\\\\Code-A-Thon-2020\\\\codeathon-trading-problem\\\\src\\\\stockdata\\\\NASDAQ_2018",
		                      "C:\\\\Users\\\\Master oh Master\\\\Desktop\\\\Code-A-Thon-2020\\\\codeathon-trading-problem\\\\src\\\\stockdata\\\\NASDAQ_2019", 
		                      "C:\\\\Users\\\\Master oh Master\\\\Desktop\\\\Code-A-Thon-2020\\\\codeathon-trading-problem\\\\src\\\\stockdata\\\\NASDAQ_2020"};
		


	static ArrayList<String> getFiles() {	
		
		ArrayList<String> paths = new ArrayList<String>();
		
		for(String d : FilePaths) {
			File f = new File(d);
			String[] data = f.list();
			for(String a : data) {
				paths.add(d + "\\" + a);
			}
		}
		return paths;
	}
}
