package com.cs583.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Algorithm {
	
	private long tCount = 0;
	
	public Map<String, List<FrequentItemSet>> MS_Apriori(List<Set<Long>> T, Map<Long, Float> MS, float SDC) {
		tCount = T.size();
		Map<String, List<FrequentItemSet>> F = new HashMap<String, List<FrequentItemSet>>();
		
		Map<Long, Float> M = InitSort(MS);
		System.out.println("M: " + M);
		
		List<Set<Long>> L = InitPass(M, T);
		
		/*List<FrequentItemSet> F1 = CheckSupValue(L);
		F.put("1-list", F1);
		System.out.println(M);
		
		int k = 2;
		
		while (F.get(k - 1).size() > 0) {
			List<FrequentItemSet> cK = new ArrayList<FrequentItemSet>();
			List<FrequentItemSet> Fk;
			
			if (k == 1) {
				cK = Level2_Candidate_Gen(L, SDC);
			} else {
				cK = MS_Candidate_Gen(F.get(k - 1), SDC);
			}
			
			for(Set<Long> t : T) {
				for(FrequentItemSet c : cK) {
					if(t.containsAll(c.getItemSet())) {
						c.count += 1;
					}
					
					if(t.containsAll(c.getTailItemSet())) {
						c.tailCount += 1;
					}
				}
			}
			
			Fk = CheckSupValueByFrequentItemSet(cK);
			F.put((k+1) + "-list", Fk);
		}*/
		
		return F;		
	}
	
	private List<Set<Long>> InitPass(Map<Long, Float> MS, List<Set<Long>> T) {
		List<Set<Long>> returnData = new ArrayList<Set<Long>>();
		
		
		
		return null;
	}
	
	private List<FrequentItemSet> CheckSupValue(List<Set<Long>> L) {
		
		return null;
	}
	
	private List<FrequentItemSet> CheckSupValueByFrequentItemSet(List<FrequentItemSet> Ck) {
		
		return null;
	}
	
	
	private List<FrequentItemSet> Level2_Candidate_Gen(List<Set<Long>> L, float SDC) {
		
		return null;
	}
	
	private List<FrequentItemSet> MS_Candidate_Gen(List<FrequentItemSet> Fk, float SDC) {
		
		return null;
	}
	
	private  <K, V extends Comparable<? super V>> Map<K, V> InitSort(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue())
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
	
	private static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
	    return setB.containsAll(setA);
	}
}
