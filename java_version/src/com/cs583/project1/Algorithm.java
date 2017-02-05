package com.cs583.project1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class Algorithm {
	
	private float tCount = 0;
	LinkedHashMap<Long, Float> M = new LinkedHashMap<Long,Float>();
	LinkedHashMap<Long, Float> Sup = new LinkedHashMap<Long,Float>();
	
	public LinkedHashMap<String, List<FrequentItemSet>> MS_Apriori(List<LinkedHashSet<Long>> T, Map<Long, Float> MS, float SDC) {
		tCount = T.size();
		LinkedHashMap<String, List<FrequentItemSet>> F = new LinkedHashMap<String, List<FrequentItemSet>>();
		
		M = InitSort(MS);
		System.out.println("M: " + M);
		
		List<FrequentItemSet> L = InitPass(M, T);
		
		List<FrequentItemSet> F1 = new ArrayList<FrequentItemSet>(CheckSupValue(L));
		printItemSet("F1", F1);
		F.put("1-list", F1);		
		int k = 2;
		
		while (F.get((k - 1) + "-list").size() > 0) {
			List<FrequentItemSet> cK = new ArrayList<FrequentItemSet>();
			List<FrequentItemSet> Fk;
			
			if (k == 2) {
				cK = Level2_Candidate_Gen(F1, SDC);
			} else {
				cK = MS_Candidate_Gen(F.get((k - 1) + "-list"), SDC);
			}
			
			if(cK.size() > 0) {				
				for(LinkedHashSet<Long> t : T) {
					for(FrequentItemSet c : cK) {
						if(t.containsAll(c.getItemSet())) {
							c.actualCount += 1;
						}
						
						/*if(t.containsAll(c.getTailItemSet())) {
							c.tailCount += 1;
						}*/
					}
				}
				Fk = CheckSupValue(cK);
				printItemSet("F" + k, Fk);
				F.put((k) + "-list", Fk);
				k++;				
			}
			else {
				break;
			}
		}
		
		return F;		
	}
	
	private List<FrequentItemSet> InitPass(LinkedHashMap<Long, Float> M, List<LinkedHashSet<Long>> T) {
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
			actualMIS = keyCount / tCount;
			if(actualMIS >= baseMIS ){
				FrequentItemSet f = new FrequentItemSet(key.toString());
				f.setCount(keyCount);
				f.setMIS(M.get(key));
				Sup.put(key, actualMIS);
				returnData.add(f);
			}		
		}	
		printItemSet("L", returnData);
		return returnData;
	}
	
	private List<FrequentItemSet> CheckSupValue(List<FrequentItemSet> L) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		
		for(FrequentItemSet item : L) {
			if( item.getMIS() <= item.getSupport(tCount)){
				returnData.add(item);				
			}
		}
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
					if ((h.actualCount / tCount >= l.getMIS()) && (Math.abs(h.getSupport(tCount) - l.getSupport(tCount)) <= SDC)) {
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
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		int n = Fk.size();
		
		for(int i=0; i<n; i++) {
			LinkedHashSet<Long> f1 = new LinkedHashSet<>(Fk.get(i).getItemSet());
			int subSetSize = f1.size();
			Long lastItemf1 = Fk.get(i).getItemSet().stream().skip(subSetSize-1).findFirst().get();	
			f1.remove(lastItemf1);
			
			for(int j = i + 1; j < n; j++) {
				Long lastItemf2 = Fk.get(j).getItemSet().stream().skip(subSetSize-1).findFirst().get();
				LinkedHashSet<Long> f2 = new LinkedHashSet<>(Fk.get(j).getItemSet());				
				f2.remove(lastItemf2);
				
				if(f1.equals(f2) && Math.abs(Sup.get(lastItemf1) - Sup.get(lastItemf2))<=SDC){
						LinkedHashSet<Long> c = new LinkedHashSet<Long>(Fk.get(i).getItemSet());
						c.add(lastItemf2);					
						boolean addToCk = true;
						List<LinkedHashSet<Long>> tempSubSets = SetOperations.getSubsets(new ArrayList<>(c),subSetSize);
						
						for(LinkedHashSet<Long> s:tempSubSets){
							Long c1 = c.iterator().next();
							Long c2 = c.stream().skip(1).findFirst().get();
							if(s.contains(c1)||(M.get(c1).equals(M.get(c2)))){
								if(!Fk.contains(s)){
									addToCk = false;
								}
							}
						}
						
						if(addToCk) {
							FrequentItemSet f = new FrequentItemSet();
							f.setItemSet(c);
							returnData.add(f);							
						}
						
				}
			}
		}
		return returnData;
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
	
	private void printItemSet(String name, List<FrequentItemSet> itemSet){
		System.out.println(name+":");
		for(FrequentItemSet item: itemSet){
			System.out.println(item.getItemSet());
		}
		
	}
}
