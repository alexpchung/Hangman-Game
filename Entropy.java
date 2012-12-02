import java.util.List;
import java.util.Map;

/*
 * Computes the entropy of an array of tokens (each unique hashkey in provided map)
 * The entropy calculation algorithm is derived from O'Reilly PHP Dev Center:
 * http://onlamp.com/pub/a/php/2005/01/06/entropy.html
 */
public class Entropy {
	
	/*
	 * Calcuate the entropy of all different hash groups of this alphabet
	 * @param hashTable of a single alphabet with different groups of words based on 
	 * the locations of mystery letters that matched to the single alphabet.
	 * @param total number of word candidates
	 * @return shannon entropy for this alphabet
	 */
	public static double calculateEntropy(Map<String, List<String>> freqMap, int numEvents)
	{
		double entropy = 0;
		
		for (List<String> mapValueList : freqMap.values())
		{
			double mapKeyProbs = (double) mapValueList.size() / (double) numEvents;
			entropy += (mapKeyProbs * log2(mapKeyProbs));
		}
		//convert to positive value 
		entropy *= -1; 
		return entropy;
	}
	
	/*
	 * Calculate the entropy of a single random selection from word candidates
	 * There are two events with probability p and q where q=(1-p) since the sum of the probabilities adds up to 1
	 * @param total number of word candidates
	 * @return entropy score of a single selection
	 */
	public static double calculateRandomSelectEntropy(int numEvents)
	{
		double entropy = 0;
		
		double pOne = (double) 1 / (double) numEvents;
		
		entropy = -1 * ( (pOne * log2(pOne)) + ((1 - pOne) * log2(1 - pOne)) );
		return entropy;
	}
	
	public static double log2(double num)
	{
		return (Math.log(num) / Math.log(2));
	}
}
