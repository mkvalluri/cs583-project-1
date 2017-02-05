package com.cs583.project1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Main {
	static String sourceFolder = "";
	static Charset charset = Charset.forName("ISO-8859-1");
	static float SDC = 0;
	static LinkedHashMap<Long, Float> MS = new LinkedHashMap<Long, Float>();
	static List<TreeSet<Long>> T = new ArrayList<TreeSet<Long>>();
	static LinkedHashMap<String, List<FrequentItemSet>> F = new LinkedHashMap<String, List<FrequentItemSet>>();

	public static void main(String[] args) {
		//System.out.println(args[0]);
		Algorithm a = new Algorithm();
		
		sourceFolder = "C:\\input-data";
		if (args.length == 1)
			sourceFolder = args[0];

		ReadInputData();
		ReadConfigurationData();
		F = a.MS_Apriori(T, MS, SDC);
		WriteOutputData();
	}

	public static void ReadInputData() {
		Path data_path = Paths.get(sourceFolder, "input-data.txt");
		try {
			List<String> lines = Files.readAllLines(data_path, charset);

			for (String line : lines) {
				line = line.replaceAll("\\s+", "");
				StringTokenizer st = new StringTokenizer(line, "{}");				
				StringTokenizer stN = new StringTokenizer(st.nextToken(), ",");
				TreeSet<Long> tempSet = new TreeSet<Long>();
				while(stN.hasMoreTokens())
					tempSet.add(Long.valueOf(stN.nextToken()));		
				T.add(tempSet);
			}
			
			System.out.println("T: " + T);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void ReadConfigurationData() {
		Path config_path = Paths.get(sourceFolder, "parameter-file.txt");
		try {
			List<String> lines = Files.readAllLines(config_path, charset);
			for (String line : lines) {
				line = line.replaceAll("\\s+", "");
				String data[];
				
				if(line.startsWith("MIS")) {
					data = line.split("=|\\(|\\)");
					MS.put(Long.parseLong(data[1]), Float.parseFloat(data[3]));
				}
				else if(line.startsWith("SDC")) {					
					data = line.split("=");
					SDC = Float.parseFloat(data[1]);
				}				
			}
			
			System.out.println("MS: " + MS);
			System.out.println("SDC: " + SDC);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void WriteOutputData() {
		System.out.println("F: " + F);
	}
}
