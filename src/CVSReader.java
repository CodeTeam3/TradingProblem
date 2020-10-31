import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CVSReader {

//	public static void main(String[] args) {
//		
//		ArrayList<String> paths = GetMarketData.getFiles();
//		System.out.println(paths.size());
//		
////		for(String p : paths) {
////			System.out.println(p);
////			getContents(p);
////		}
//		
//	}
	
	static ArrayList<String> getContents(String path) {
		String line = "";
		ArrayList<String> data = new ArrayList<String>();
		int counter = 0;
		//	Reads File And Creates ArrayList
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while((line = br.readLine()) != null) {
				String[] values = line.split(",");
				
				//	Symbol, High, Low
				if(counter > 0) {
					String date = formatDate(values[1]);
					data.add(values[0] + " " + date + " " + values[3] + " " + values[4]);
				}
				counter += 1;
				//System.out.println(values[0]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error finding File!");
			e.printStackTrace();
		}  catch (IOException e) {
			System.out.println("Error finding File!");
			e.printStackTrace();
		}
		return data;
	}



	private static String formatDate(String rawDate) {
		String day = rawDate.substring(0, 2);
		String month = rawDate.substring(3, 6);
		
		if(month.contentEquals("Jan")) {
			month = "01";
		} else if(month.contentEquals("Feb")){
			month = "02";
		} else if(month.contentEquals("Mar")){
			month = "03";
		} else if(month.contentEquals("Apr")){
			month = "04";
		} else if(month.contentEquals("May")){
			month = "05";
		} else if(month.contentEquals("Jun")){
			month = "06";
		} else if(month.contentEquals("Jul")){
			month = "07";
		} else if(month.contentEquals("Aug")){
			month = "08";
		} else if(month.contentEquals("Sep")){
			month = "09";
		} else if(month.contentEquals("Oct")){
			month = "10";
		} else if(month.contentEquals("Nov")){
			month = "11";
		} else if(month.contentEquals("Dec")){
			month = "12";
		}
		
		String year = rawDate.substring(7);
		String date = day + month + year;
		return date;
	}
}
