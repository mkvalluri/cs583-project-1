/*******************************
 * Filename: SetOperations.java
 * Description: Implementation of MS-Apriori algorithm
 * with additional conditions.
 * Authors: Murali Valluri (mvallu2@uic.edu), Spoorthi Pendyala (npendy2@uic.edu)
 */

package com.cs583.project1;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;

public class SetOperations {
	
	private static void getSubsets(List<Long> superSet, int k, int idx, LinkedHashSet<Long> current,List<LinkedHashSet<Long>> solution) {
	    if (current.size() == k) {
	        solution.add(new LinkedHashSet<>(current));
	        return;
	    }
	    
	    if (idx == superSet.size()) 
	    	return;
	    
	    Long x = superSet.get(idx);
	    current.add(x);
	    getSubsets(superSet, k, idx+1, current, solution);
	    current.remove(x);
	    getSubsets(superSet, k, idx+1, current, solution);
	}

	public static List<LinkedHashSet<Long>> getSubsets(List<Long> superSet, int k) {
	    List<LinkedHashSet<Long>> res = new ArrayList<>();
	    getSubsets(superSet, k, 0, new LinkedHashSet<Long>(), res);
	    return res;
	}
}
