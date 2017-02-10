/*******************************
 * Filename: Main.java
 * Description: Implementation of MS-Apriori algorithm
 * with additional conditions.
 * Authors: Murali Valluri (mvallu2@uic.edu), Spoorthi Pendyala (npendy2@uic.edu)
 */

package com.cs583.project1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.LinkedHashSet;

public class Main {
	static String sourceFolder = "";
	static Charset charset = Charset.forName("ISO-8859-1");
	static float SDC = 0;
	static LinkedHashMap<Long, Float> MS = new LinkedHashMap<Long, Float>();
	static List<LinkedHashSet<Long>> T = new ArrayList<LinkedHashSet<Long>>();
	static LinkedHashMap<String, List<FrequentItemSet>> F = new LinkedHashMap<String, List<FrequentItemSet>>();
	static List<Long> MustHave = new ArrayList<Long>();
	static List<LinkedHashSet<Long>> NotBeTogether = new ArrayList<LinkedHashSet<Long>>();

	//Entry point of the program. Steps:
	//Reads input data, configuration data.
	//Call Algorithm.
	//Print the result.
	public static void main(String[] args) {
		Algorithm a = new Algorithm();
		
		sourceFolder = "C:\\input-data";
		if (args.length == 1)
			sourceFolder = args[0];

		ReadInputData();
		ReadConfigurationData();
		F = a.MS_Apriori(T, MS, SDC, NotBeTogether);
		WriteOutputDataToFile();
	}

	//Reads input data.
	public static void ReadInputData() {
		Path data_path = Paths.get(sourceFolder, "input-data.txt");
		try {
			List<String> lines = Files.readAllLines(data_path, charset);

			for (String line : lines) {
				line = line.replaceAll("\\s+", "");
				StringTokenizer st = new StringTokenizer(line, "{}");				
				StringTokenizer stN = new StringTokenizer(st.nextToken(), ",");
				LinkedHashSet<Long> tempSet = new LinkedHashSet<Long>();
				while(stN.hasMoreTokens())
					tempSet.add(Long.valueOf(stN.nextToken()));		
				T.add(tempSet);
			}
			
			//System.out.println("T: " + T);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	//Reads configuration files 
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
				else if(line.startsWith("must-have")) {
					data = line.split(":");
					String[] tempData = data[1].replaceAll("or", ",").split(",");
					for(String s : tempData) {
						MustHave.add(Long.parseLong(s));
					}
				}
				else if(line.startsWith("cannot_be_together")) {
					data = line.split(":");
					String[] tempData = data[1].replaceAll("},", "}-").split("-");
					for(String s : tempData) {
						String[] t1 = s.split("\\{|\\}");
						String[] t2 = t1[1].split(",");
						LinkedHashSet<Long> n = new LinkedHashSet<Long>();
						for(String t : t2) {
							n.add(Long.parseLong(t));
						}
						NotBeTogether.add(n);
						int size = n.size();
						for(int i = size - 1; i > 1; i--) {
							NotBeTogether.addAll(SetOperations.getSubsets(new ArrayList<>(n), i));
						}
					}
				}
			}
			
			//System.out.println("MS: " + MS);
			//System.out.println("SDC: " + SDC);
			//System.out.println("MustHave: " + MustHave);
			//System.out.println("NotBeTogether: " + NotBeTogether);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	//Writes the final output to file and also prints the result in console.
	public static void WriteOutputDataToFile() {
		Boolean flag = true;
		String outputData = "";
		for (String s : F.keySet()) {
			List<FrequentItemSet> tempF = F.get(s);
			outputData += "Frequent " + s + "\n\n";
			int mustHaveCount = 0;
			for (FrequentItemSet f : tempF) {
				Boolean mustHaveFlag = false;
				for(long m: MustHave) {
					if(f.getItemSet().contains(m)) {
						mustHaveFlag = true;
						break;
					}
				}
				if(mustHaveFlag)
				{
					outputData += "\t" + (int)f.getCount() + " : " + f.getItemSet() + "\n";
					if(!flag)
						outputData += "Tailcount = " + (int)f.getTailCount() + "\n";
				}
				else {
					mustHaveCount++;
				}
			}
			if(flag) 
				flag = false;
			
			if ((tempF.size() - mustHaveCount) > 0)
				outputData += "\n\tTotal number of frequent " + s + " = " + (tempF.size() - mustHaveCount) + "\n\n\n";
			else {
				if(Integer.parseInt(s.split("-")[0]) < 10)
					outputData = outputData.substring(0, outputData.lastIndexOf('\n') - 23);
				else 
					outputData = outputData.substring(0, outputData.lastIndexOf('\n') - 22);
			}
		}
		try {
			outputData = outputData.replace("[", "{");
			outputData = outputData.replace("]", "}");
			Path data_path = Paths.get(sourceFolder, "output-patterns.txt");
			Files.write(data_path, outputData.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\n#############################################################\n");
		System.out.println(outputData);
		System.out.println("\n\n#############################################################\n");
	}
}
