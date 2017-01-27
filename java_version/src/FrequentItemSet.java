import java.util.HashSet;
import java.util.Set;

public class FrequentItemSet {
	
	Set<Long> itemSet = new HashSet<Long>();
	public FrequentItemSet(Set<Long> itemSet, long count, long tailCount) {
		super();
		this.itemSet = itemSet;
		this.count = count;
		this.tailCount = tailCount;
	}

	long count = 0;
	long tailCount = 0;
	
	public Set<Long> getItemSet() {
		return itemSet;
	}
	public void setItemSet(Set<Long> itemSet) {
		this.itemSet = itemSet;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getTailCount() {
		return tailCount;
	}
	public void setTailCount(long tailCount) {
		this.tailCount = tailCount;
	}
	
	public float getSupport(long n){
		
		return (count/n);
		
	}
	
	

}
