import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FindBestTrade {
	
	static ArrayList<String> paths = GetMarketData.getFiles();
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public static void main(String[] args) throws IOException {
		
		ArrayList<String> data = getBestTrades();
		String toWrite = "";
		for(String l : data) {
			toWrite += l + "\n";
		}
		FileWriter fw = new FileWriter("C:\\Users\\Master oh Master\\Desktop\\Code-A-Thon-2020\\codeathon-trading-problem\\src\\biggestgainers.txt");
		fw.write(toWrite);
		fw.close();
	}

	private static ArrayList<String> getBestTrades() {
		ArrayList<String> bestTrades = new ArrayList<String>();
		int counter = 0;
		while(counter < paths.size() - 1) {
			if(counter + 1 == paths.size()) {
				break;
			}
			String best = getBestTrade(counter);
			bestTrades.add(best);
			counter++;
		}
		return bestTrades;
	}

	private static String getBestTrade(int counter) {
		
		ArrayList<String> d1 = CVSReader.getContents(paths.get(counter));
		ArrayList<String> d2 = CVSReader.getContents(paths.get(counter + 1));
		ArrayList<String> percentChg = new ArrayList<String>();
		
		for(String t : d1) {
			String[] pieces = t.split(" ");
			String symbol = pieces[0];
			double low = Double.parseDouble(pieces[3]);
			
			for(String s : d2) {
				String[] d2pieces = s.split(" ");
				String d2symbol = d2pieces[0];
				if(symbol.contentEquals(d2symbol)) {
					double high = Double.parseDouble(d2pieces[2]);
					double pchng = (high - low)/low * 100;
					String newEntry = symbol + " " + pieces[1] + " " + df2.format(pchng) +" " + low + " " + high;
					percentChg.add(newEntry);
				}
			}
		}//	End
		double biggestGain = 0;
		String bestSymbol = "";
		String bhigh = "";
		String blow = "";
		String date = "";
		for(String e : percentChg) {
			String[] j = e.split(" ");
			double chg = Double.parseDouble(j[2]);
			if(chg > biggestGain) {
				bestSymbol = j[0];
				date = j[1];
				biggestGain = chg;
				blow = j[3];
				bhigh = j[4];
			}
		}// End
		String bestOfDay = bestSymbol + " " + date + " " + biggestGain + "% " + bhigh + " "+ blow;
		//System.out.println(bestOfDay);
		return bestOfDay;
	}
}

