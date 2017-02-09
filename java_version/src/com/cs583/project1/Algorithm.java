/*******************************
 * Filename: Algorithm.java
 * Description: Implementation of MS-Apriori algorithm
 * with additional conditions.
 * Authors: Murali Valluri (mvallu2@uic.edu), Spoorthi Pendyala (npendy2@uic.edu)
 */


package com.cs583.project1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class Algorithm {

	private float tCount = 0;
	LinkedHashMap<Long, Float> M = new LinkedHashMap<Long, Float>();
	LinkedHashMap<Long, Float> Sup = new LinkedHashMap<Long, Float>();
	List<LinkedHashSet<Long>> NBT;

	//Entry point of the algorithm.
	public LinkedHashMap<String, List<FrequentItemSet>> MS_Apriori(List<LinkedHashSet<Long>> T, Map<Long, Float> MS,
			float SDC, List<LinkedHashSet<Long>> NotBeTogether) {
		tCount = T.size();
		LinkedHashMap<String, List<FrequentItemSet>> F = new LinkedHashMap<String, List<FrequentItemSet>>();
		NBT = new ArrayList<LinkedHashSet<Long>>(NotBeTogether);

		M = InitSort(MS);
		//System.out.println("M: " + M);

		List<FrequentItemSet> L = InitPass(M, T);

		List<FrequentItemSet> F1 = new ArrayList<FrequentItemSet>(CheckSupValue(L));
		//printItemSet("F1", F1);
		F.put("1-itemsets", F1);
		int k = 2;

		while (F.get((k - 1) + "-itemsets").size() > 0) {
			List<FrequentItemSet> cK = new ArrayList<FrequentItemSet>();
			List<FrequentItemSet> Fk;

			if (k == 2) {
				cK = Level2_Candidate_Gen(L, SDC);
			} else {
				cK = MS_Candidate_Gen(F.get((k - 1) + "-itemsets"), SDC);
			}

			if (cK.size() > 0) {
				for (LinkedHashSet<Long> t : T) {
					for (FrequentItemSet c : cK) {
						if (t.containsAll(c.getItemSet())) {
							c.actualCount += 1;
						}

						if (t.containsAll(c.getTailItemSet())) {
							c.tailCount += 1;
						}
					}
				}
				Fk = CheckSupValue(cK);
				if(Fk != null && Fk.size() > 0) {
					//printItemSet("F" + k, Fk);
					F.put((k) + "-itemsets", Fk);	
					k++;
				}
				else {
					break;
				}
			} else {
				break;
			}
		}

		return F;
	}

	//Initial pass where we generate 1-itemsets. Steps include retrieving count of each
	//item and then making sure the support is less than min(MIS(i1), MIS(i2)..(MIS(in))
	private List<FrequentItemSet> InitPass(LinkedHashMap<Long, Float> M, List<LinkedHashSet<Long>> T) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		float actualMIS = 0;
		float baseMIS = 0;
		for (Long key : M.keySet()) {
			int keyCount = 0;
			for (int i = 0; i < tCount; i++) {
				if (T.get(i).contains(key)) {
					keyCount++;
				}
			}
			actualMIS = (float) keyCount / tCount;

			if (baseMIS == 0) {
				if (actualMIS >= M.get(key)) {			
					baseMIS = M.get(key);
				}
				else {
					continue;
				}
			}
			
			if (actualMIS >= baseMIS) {
				FrequentItemSet f = new FrequentItemSet(key.toString());
				f.setCount(keyCount);
				f.setMIS(M.get(key));
				Sup.put(key, actualMIS);
				returnData.add(f);
			}
		}
		//printItemSet("L", returnData);
		return returnData;
	}

	//Used to check if the support of the itemset is greater than 
	//the MIS value of the item set. 
	private List<FrequentItemSet> CheckSupValue(List<FrequentItemSet> Items) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();

		for (FrequentItemSet item : Items) {
			//System.out.println("Item: " + item.getItemSet() + "\t Support: " + item.getSupport(tCount) + "\t MIS: " + item.getMIS());
			if (item.getMIS() <= item.getSupport(tCount)) {
				returnData.add(item);
			}
		}
		return returnData;
	}

	//Used to generate 2-itemsets. Additional parameter SDC is used 
	//to make sure that the support difference between 2 items in an
	//itemset is less than or equal to SDC.
	private List<FrequentItemSet> Level2_Candidate_Gen(List<FrequentItemSet> L, float SDC) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		long c = L.size();
		for (int i = 0; i < c; i++) {
			FrequentItemSet l = L.get(i);
			if (l.getSupport(tCount) >= l.getMIS()) {
				for (int j = i + 1; j < c; j++) {
					FrequentItemSet h = L.get(j);
					if ((h.getSupport(tCount) >= l.getMIS())
							&& (Math.abs(h.getSupport(tCount) - l.getSupport(tCount)) <= SDC)) {
						FrequentItemSet C2 = new FrequentItemSet(l.getItemSet().iterator().next().toString() + ","
								+ h.getItemSet().iterator().next().toString());
						C2.setMIS(l.getMIS());

						if (!CheckForNotBeTogether(C2.getItemSet())) {
							returnData.add(C2);
						}
					}
				}
			}
		}
		//printItemSet("L2", returnData);

		return returnData;
	}

	//Used to generate n-itemsets where n > 2. This function generates
	//(n+1)-itemsets and then checks for few more conditions.
	private List<FrequentItemSet> MS_Candidate_Gen(List<FrequentItemSet> Fk, float SDC) {
		List<FrequentItemSet> returnData = new ArrayList<FrequentItemSet>();
		int n = Fk.size();

		for (int i = 0; i < n; i++) {
			LinkedHashSet<Long> f1 = new LinkedHashSet<>(Fk.get(i).getItemSet());
			int subSetSize = f1.size();
			Long lastItemf1 = f1.stream().skip(subSetSize - 1).findFirst().get();
			f1.remove(lastItemf1);

			for (int j = i + 1; j < n; j++) {
				LinkedHashSet<Long> f2 = new LinkedHashSet<>(Fk.get(j).getItemSet());
				Long lastItemf2 = f2.stream().skip(subSetSize - 1).findFirst().get();
				f2.remove(lastItemf2);

				if (f1.equals(f2) && Math.abs(Sup.get(lastItemf1) - Sup.get(lastItemf2)) <= SDC) {
					LinkedHashSet<Long> c = new LinkedHashSet<Long>(Fk.get(i).getItemSet());
					c.add(lastItemf2);
					boolean addToCk = true;
					List<LinkedHashSet<Long>> tempSubSets = SetOperations.getSubsets(new ArrayList<>(c), subSetSize);

					for (LinkedHashSet<Long> s : tempSubSets) {
						Long c1 = c.iterator().next();
						Long c2 = c.stream().skip(1).findFirst().get();
						FrequentItemSet tempfItemSet = new FrequentItemSet();
						tempfItemSet.setItemSet(s);

						if (s.contains(c1) || (M.get(c1).equals(M.get(c2)))) {
							if (Fk.contains(s)) {
								addToCk = false;
							}
						}
					}

					if (CheckForNotBeTogether(c)) {
						addToCk = false;
					}

					if (addToCk) {
						FrequentItemSet f = new FrequentItemSet();
						f.setItemSet(c);
						f.setMIS(M.get(c.iterator().next()));
						returnData.add(f);
					}

				}
			}
		}
		//printItemSet("C", returnData);
		return returnData;
	}

	//This function checks if the itemset generates have items
	//which can't be together.
	private Boolean CheckForNotBeTogether(LinkedHashSet<Long> d) {
		for (LinkedHashSet<Long> nbt : NBT) {
			if (d.containsAll(nbt)) {
				return true;
			}
		}
		return false;
	}

	//This is used to sort items in a map based on value.
	private <K, V extends Comparable<? super V>> LinkedHashMap<K, V> InitSort(Map<K, V> map) {
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	//This is used to print itemsets.
	private void printItemSet(String name, List<FrequentItemSet> itemSet) {
		System.out.println(name + ":");
		for (FrequentItemSet item : itemSet) {
			System.out.println(item.getItemSet());
		}
	}
}
