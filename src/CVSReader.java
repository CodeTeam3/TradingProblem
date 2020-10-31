import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
					String[] d = values[1].split("-");
					String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		        		String date = d[0] + "0" + Arrays.asList(months).indexOf(d[1]) + d[2];
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
}
