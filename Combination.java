import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * This class was not used at the end.  
 * It was originally written to build a bi-gram table.
 */


/*
 * Index of Combinations in Lexicographical Order (Buckles Algorithm 515)
 * Algorithm is borrowed from Sourabh Bhat (heySourabh@gmail.com)
 * Assuming the incoming characters are all unique
 */
public class Combination
{
    public static List<char[]> getCombinations(int num, String s)
    {        
    	//Sort the letters alphabetically
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        String sortedS = new String(chars);
        
        int outOf = sortedS.length() - 1;
        int[][] combinations = getCombinations(num, outOf);
        List<char[]> charComboList = new LinkedList<char[]>();
        
        for (int i = 0; i < combinations.length; i++)
        {        	
        	char[] charCombo = new char[num];
        	int index = 0;
            for (int j = 0; j < combinations[i].length; j++)
            {
                charCombo[index] = sortedS.charAt(combinations[i][j]);
                index++;
            }
            charComboList.add(charCombo);
        }
                
        return charComboList;
    }

    private static int[][] getCombinations(int num, int outOf)
    {
        int possibilities = get_nCr(outOf, num);
        int[][] combinations = new int[possibilities][num];
        int arrayPointer = 0;

        int[] counter = new int[num];

        for (int i = 0; i < num; i++)
        {
            counter[i] = i;
        }
        breakLoop: while (true)
        {
            // Initializing part
            for (int i = 1; i < num; i++)
            {
                if (counter[i] >= outOf - (num - 1 - i))
                    counter[i] = counter[i - 1] + 1;
            }

            // Testing part
            for (int i = 0; i < num; i++)
            {
                if (counter[i] < outOf)
                {
                    continue;
                } else
                {
                    break breakLoop;
                }
            }

            // Innermost part
            combinations[arrayPointer] = counter.clone();
            arrayPointer++;

            // Incrementing part
            counter[num - 1]++;
            for (int i = num - 1; i >= 1; i--)
            {
                if (counter[i] >= outOf - (num - 1 - i))
                    counter[i - 1]++;
            }
        }

        return combinations;
    }

    private static int get_nCr(int n, int r)
    {
        if(r > n)
        {
            throw new ArithmeticException("r is greater then n");
        }
        long numerator = 1;
        long denominator = 1;
        for (int i = n; i >= r + 1; i--)
        {
            numerator *= i;
        }
        for (int i = 2; i <= n - r; i++)
        {
            denominator *= i;
        }

        return (int) (numerator / denominator);
    }
}


//Build indices combination for calculating frequency table
/* int n = 2;		
 * Single Letter is used because calculation of information gain is
 * evaluated after every single letter guess.
 *  		
List<char[]> charCombos = new LinkedList<char[]>();
charCombos = Combination.getCombinations(1, letterCandidates.toString());				
for (char[] combo : charCombos)
{
	buildFrequencyTable(combo, wordCandidates);
}
*/