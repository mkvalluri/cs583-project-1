package com.cs583.project1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Algorithm {
	
	private long tCount = 0;
	
	public LinkedHashMap<String, List<FrequentItemSet>> MS_Apriori(List<Set<Long>> T, Map<Long, Float> MS, float SDC) {
		tCount = T.size();
		LinkedHashMap<String, List<FrequentItemSet>> F = new LinkedHashMap<String, List<FrequentItemSet>>();
		
		LinkedHashMap<Long, Float> M = InitSort(MS);
		System.out.println("M: " + M);
		
		List<FrequentItemSet> L = InitPass(M, T);
		
		List<FrequentItemSet> F1 = new ArrayList<FrequentItemSet>(CheckSupValue(L));
		F.put("1-list", F1);		
		int k = 2;
		
		while (F.get((k - 1) + "-list").size() > 0) {
			List<FrequentItemSet> cK = new ArrayList<FrequentItemSet>();
			List<FrequentItemSet> Fk;
			
			if (k == 2) {
				cK = Level2_Candidate_Gen(L, SDC);
			} else {
				cK = MS_Candidate_Gen(F.get(k - 1), SDC);
			}
			
			/*for(Set<Long> t : T) {
				for(FrequentItemSet c : cK) {
					if(t.containsAll(c.getItemSet())) {
						c.actualCount += 1;
					}
					
					if(t.containsAll(c.getTailItemSet())) {
						c.tailCount += 1;
					}
				}
			}
			
			Fk = CheckSupValue(cK);
			F.put((k+1) + "-list", Fk);*/
		}
		
		return F;		
	}
	
	private List<FrequentItemSet> InitPass(LinkedHashMap<Long, Float> M, List<Set<Long>> T) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		float actualMIS = 0; 
		float baseMIS = 0;
		for(Long key:M.keySet() ){
			int keyCount =0;
			if(baseMIS == 0){
				baseMIS = M.get(key);
			}
			for(int i=0;i<tCount;i++){
				if(T.get(i).contains(key)){
					keyCount++;					
				}
			}
			actualMIS = ((float)keyCount)/tCount;
			if(actualMIS >= baseMIS ){
				FrequentItemSet f = new FrequentItemSet(key.toString());
				f.setCount(keyCount);
				f.setMIS(M.get(key));
				returnData.add(f);
			}		
		}	
		printItemSet("L",returnData);
		return returnData;
	}
	
	private List<FrequentItemSet> CheckSupValue(List<FrequentItemSet> L) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		
		for(FrequentItemSet item : L) {
			if( item.getMIS() <= item.getSupport(tCount)){
				returnData.add(item);				
			}
		}
		printItemSet("F1", returnData);
		return returnData;
	}
	
	
	private List<FrequentItemSet> Level2_Candidate_Gen(List<FrequentItemSet> L, float SDC) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		long c = L.size();
		for(int i = 0; i < c; i++) {
			FrequentItemSet l = L.get(i);
			if (l.actualCount / tCount >= l.getMIS()) {
				for (int j = i + 1; j < c; j++) {
					FrequentItemSet h = L.get(j);
					if ((h.actualCount / tCount >= l.getMIS()) && (h.getSupport(tCount) - l.getSupport(tCount) <= SDC)) {
						FrequentItemSet C2 = new FrequentItemSet(l.getItemSet().iterator().next().toString() + "," + h.getItemSet().iterator().next().toString());
						C2.setMIS(l.getMIS());
						returnData.add(C2);
					}
				}
			}
		}
		printItemSet("L2", returnData);
		
		return returnData;
	}
	
	private List<FrequentItemSet> MS_Candidate_Gen(List<FrequentItemSet> Fk, float SDC) {
		
		return null;
	}
	
	private  <K, V extends Comparable<? super V>> LinkedHashMap<K, V> InitSort(Map<K, V> map) {
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
	
	/*private <T> boolean isSubset(Set<T> setA, Set<T> setB) {
	    return setB.containsAll(setA);
	}*/
	
	private void printItemSet(String name, List<FrequentItemSet> itemSet){
		System.out.println(name+":");
		for(FrequentItemSet item: itemSet){
			System.out.println(item.getItemSet());
		}
		
	}
}
