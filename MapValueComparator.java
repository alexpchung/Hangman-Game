import java.util.Comparator;
import java.util.Map;

/*
 * A comparator function for comparing the values of a hashmap
 */

public class MapValueComparator implements Comparator<Object> {
	Map<Character, Integer> base;
	public MapValueComparator(Map<Character, Integer> base)
	{
		this.base = base;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		
		if ((Integer)base.get(arg0) < (Integer)base.get(arg1))
		{
			return 1;
		} else if ((Integer)base.get(arg0) == (Integer)base.get(arg1))
		{
			return 0;
		} else {
			return -1;
		}
	}
	
	
}
