/*******************************
 * Filename: FrequentItemSet.java
 * Description: Implementation of MS-Apriori algorithm
 * with additional conditions.
 * Authors: Murali Valluri (mvallu2@uic.edu), Spoorthi Pendyala (npendy2@uic.edu)
 */

package com.cs583.project1;

import java.util.StringTokenizer;
import java.util.LinkedHashSet;

public class FrequentItemSet {
	
	LinkedHashSet<Long> itemSet;
	
	float actualCount;
	
	float tailCount;
	
	float MIS;
	
	public FrequentItemSet() {
		itemSet = new LinkedHashSet<Long>();
		actualCount = 0;
		tailCount = 0;
		MIS = 0;
	}
	
	public FrequentItemSet(String items) {
		itemSet = new LinkedHashSet<Long>();
		actualCount = 0;
		tailCount = 0;
		MIS = 0;
		
		StringTokenizer st = new StringTokenizer(items, ",");
		while(st.hasMoreTokens())
		   itemSet.add(Long.valueOf(st.nextToken()));		
	}
	
	public void setItemSet(LinkedHashSet<Long> itemSet) {
		this.itemSet = itemSet;
	}
	
	public LinkedHashSet<Long> getItemSet() {
		return itemSet;
	}
	
	public LinkedHashSet<Long> getTailItemSet() {
		LinkedHashSet<Long> l = new LinkedHashSet<>(itemSet);		
		if (!l.isEmpty())
			  l.remove(l.iterator().next());
		return l;
	}
	
	public void setCount(float count) {
		this.actualCount = count;
	}
	
	public float getCount() {
		return actualCount;
	}
	
	public void setTailCount(float tailCount) {
		this.tailCount = tailCount;
	}
	
	public float getTailCount() {
		return tailCount;
	}
	
	public void setMIS(float MIS) {
		this.MIS = MIS;
	}
	
	public float getMIS() {
		return MIS;
	}
	
	public float getSupport(float transactionCount) {
		return actualCount / transactionCount;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
		    return true;
		}	
		if (!(obj instanceof FrequentItemSet)) {
		    return false;
		}	
		FrequentItemSet other = (FrequentItemSet) obj;
		return itemSet.equals(other.getItemSet());
	}
}
