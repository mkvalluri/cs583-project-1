import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
public class MSApriori {
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String path_name_input = "D:\\coursework\\Data mining\\input-data.txt";
		String path_name_mis = "D:\\coursework\\Data mining\\parameter-file.txt";
		
		String line = null;
		String line_mis = null;
		
		ArrayList<HashSet<Long>> T = new ArrayList<HashSet<Long>>();
		ArrayList<FrequentItemSet> F = new ArrayList<FrequentItemSet>();
		HashMap<Integer,Float> MIS = new HashMap<Integer,Float>();
		
		try {
			FileReader fileReader = new FileReader(path_name_input);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileReader fileReader_mis = new FileReader(path_name_mis);
			BufferedReader bufferedReader_mis = new BufferedReader(fileReader_mis);
			
			while((line = bufferedReader.readLine())!=null ){
				HashSet<Long> set = new HashSet<Long>();
				String l = line.substring(1, line.length()-1);
				l = l.replaceAll(" ", "");
				System.out.println(l);
				StringTokenizer st = new StringTokenizer(l,",");
				while(st.hasMoreTokens())					
					set.add(Long.parseLong(st.nextToken()));
				System.out.println(set.size());
				T.add(set);
			}
			
			System.out.println(T);
			
			while((line_mis = bufferedReader_mis.readLine())!=null){
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

	}

}
