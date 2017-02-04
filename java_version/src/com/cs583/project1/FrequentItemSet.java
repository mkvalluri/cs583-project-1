package com.cs583.project1;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class FrequentItemSet {
	
	Set<Long> itemSet;
	
	long actualCount;
	
	long tailCount;
	
	public FrequentItemSet(String items) {
		itemSet = new HashSet<Long>();
		actualCount = 0;
		tailCount = 0;
		
		StringTokenizer st = new StringTokenizer(items, ",");
		while(st.hasMoreTokens())
		   itemSet.add(Long.valueOf(st.nextToken()));		
	}
	
	public Set<Long> getItemSet() {
		return itemSet;
	}
	
	public Set<Long> getTailItemSet() {
		return itemSet.stream()
				   .skip(1)
				   .collect(Collectors.toSet());
	}
	
	public void setCount(long count) {
		this.actualCount = count;
	}
	
	public long getCount() {
		return actualCount;
	}
	
	public void setTailCount(long tailCount) {
		this.tailCount = tailCount;
	}
	
	public long getTailCount() {
		return tailCount;
	}
	
	public float getSupport(long transactionCount) {
		return actualCount / transactionCount;
	}
}
