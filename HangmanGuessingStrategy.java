import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/*
 * Weight guesses towards the most information gain
 * by maximizing the entropy of the result set 
 * ( https://en.wikipedia.org/wiki/Entropy_(information_theory) ).
 * To calculate this, take the sum of -p * log(p) for all patterns of the guessed letter, 
 * where p is the proportion of words in the candidate set that have this pattern.
 * 
 * Language Models - http://www.scribd.com/doc/50891038/7/Shannon-game-Word-Prediction
 * 
 */
public class HangmanGuessingStrategy implements GuessingStrategy {
	
	private final Map<Integer, List<String>> dictionaryByLength;
	
	public HangmanGuessingStrategy(List<String> words)
	{
		dictionaryByLength = divideWordsByLength(words);
	}
	
	/*
	 * Create a hashTable with the length of words as key and its corresponding list of words
	 * as value
	 * @param words list
	 * @return a map of words bucketed by the word character length 
	 */
	private static Map<Integer, List<String>> divideWordsByLength(List<String> words) {
		Map<Integer, List<String>> dictionary = new HashMap<Integer, List<String>>();
		
		for (String word : words)
		{
			Integer wordLength = word.length();
			if (dictionary.containsKey(wordLength))
			{
				//Add to existing list of certain length
				dictionary.get(wordLength).add(word);
			} else 
			{
				//Create a new list for this length
				List<String> newWordList = new LinkedList<String>();
				newWordList.add(word);
				dictionary.put(wordLength, newWordList);
			}
		}
		return dictionary;
	}
	
	/*
	 * Get a list of word candidates
	 *@param current HangmanGame instance
	 *@return a list of remaining possible matching words  
	 */
	private List<String> screenWordCandidates(HangmanGame game)
	{
		//Screen candidates based on the current game's secret word
		Integer length = game.getSecretWordLength();
		List<String> wordList = dictionaryByLength.get(length);		
		//Current game so far
		char[] wordGuessedSoFar = game.getGuessedSoFar().toCharArray();		
		//All guessed letters
		Set<Character> allGuessedLetters = game.getAllGuessedLetters();		
		//Screen word candidate list by guessed letters
		wordList = HangmanGuessingStrategy.screenByLetter(wordList, allGuessedLetters, wordGuessedSoFar);
		//All guessed words
		Set<String> incorrectlyGuessedWords = game.getIncorrectlyGuessedWords();		
		//Screen word candidate list with previously guessed words
		wordList = HangmanGuessingStrategy.screenByWord(wordList, incorrectlyGuessedWords);				
		return wordList; 
	}
	
	/*
	 * A helper function for method screen to determine if a word can be the mystery word
	 * @param the word under examination
	 * @param the word guessed so far
	 * @param all previously guessed letter
	 * @param the symbol that represents the mystery letter
	 * @return True or False 
	 */
	private static boolean isCandidate(String word, char[] wordGuessedSoFar, Set<Character> allGuessedLetters, char mysteryLetter)
	{
		boolean pass = true;	
		
		wordLoop: for (int i = 0; i < word.length(); i++)
		{
			if (wordGuessedSoFar[i] == mysteryLetter)
			{
				for (Character c : allGuessedLetters)
				{
					if (c.charValue() == Character.toUpperCase(word.charAt(i)))
					{
						pass = false;
						break wordLoop;
					}
				}
					
			} else {
				if (wordGuessedSoFar[i] == Character.toUpperCase(word.charAt(i)))
				{
					continue;
				} else {
					pass = false;
					break wordLoop;
				}
				
			}
		}
		return pass;
	}

	/*
	 * A helper function for method screenWordCandidates to check word candidates base
	 * on previously guessed letters
	 * @param a list of words
	 * @param all previously guessed letters
	 * @param current game board with correctly guessed letters in position
	 * @return a modified list of words   
	 */
	private static List<String> screenByLetter(List<String> wordList,
			Set<Character> allGuessedLetters, char[] wordGuessedSoFar) {
		//Modified List with all possible candidates
		List<String> screened = new LinkedList<String>();
		
		//Hangman game mystery character
		char mysteryLetter = HangmanGame.MYSTERY_LETTER;
		
		for (String word : wordList)
		{
			if (isCandidate(word, wordGuessedSoFar, allGuessedLetters, mysteryLetter)) 
			{
				screened.add(word);
			}
		}
		return screened;
	}
	
	/*
	 * A helper function for method screenWordCandidates to check word candidates base
	 * on previously guessed words
	 * @param a list of words
	 * @param all wrongly guessed words 
	 * @return a modified list of words   
	 */
	private static List<String> screenByWord(List<String> wordList,
			Set<String> incorrectlyGuessedWords) {
		List<String> screened = new LinkedList<String>();
		
		for (String word : wordList) 
		{			
			if (incorrectlyGuessedWords.contains(word.toUpperCase())) {
				//do not include
				continue;
			} else {
				screened.add(word);
			}
		}
		return screened;
	}	
	
	/*
	 * Find all the positions of mystery letters in the current game
	 * @param current game board with missing letters
	 * @param mystery letter symbol
	 * @return an array of positions with missing letters
	 */
	private static List<Integer> getEmptyLetterPositions(char[] wordGuessedSoFar, char mysteryLetter)
	{
		List<Integer> emptyList = new ArrayList<Integer>();
		
		for (int i = 0; i < wordGuessedSoFar.length; i++)
		{
			if (wordGuessedSoFar[i] == mysteryLetter)
			{
				emptyList.add(i);				
			}
		}
		return emptyList;
	}
	
	/*
	 * Determine the remaining possible letters to make a guess 
	 * @param list of all possible matching words
	 * @param list of letter positions with missing letters
	 * @param list of already guessed letters
	 * @return a set of all possible guessing letters
	 */
	private static Set<Character> getLetterCandidates(List<String> wordCandidates, List<Integer>emptyLetterPositions, Set<Character>allGuessedLetters)
	{
		Set<Character> letterCandidates = new HashSet<Character>();
		wordLoop: for (String word : wordCandidates)
		{
			for (int i : emptyLetterPositions)
			{				
				char currentChar = word.charAt(i);
				//Skip if the letter has already been guessed
				if (allGuessedLetters.contains(currentChar))
				{
					continue;
				}
				letterCandidates.add(new Character(currentChar));
				//Assuming words are all in English with only alphabet letters
				if (letterCandidates.size() == 26)
				{
					break wordLoop; 
				}
			}
		}
		return letterCandidates;
	}

	@Override
	public Guess nextGuess(HangmanGame game) {
		//find the list of remaining possible word answers
		List<String> wordCandidates = screenWordCandidates(game);
		
		assert(wordCandidates.size() > 0);
		
		//Guess word if there's only one to choose from
		if (wordCandidates.size() == 1)
		{
			return new GuessWord(wordCandidates.get(0));
		}
		
		//Current game so far
		char[] wordGuessedSoFar = game.getGuessedSoFar().toCharArray();
		//Hangman game mystery character
		char mysteryLetter = HangmanGame.MYSTERY_LETTER;
		//Empty letter positions
		List<Integer> emptyLetterPositions = getEmptyLetterPositions(wordGuessedSoFar, mysteryLetter);
		//All guessed letters
		Set<Character> allGuessedLetters = game.getAllGuessedLetters();
		//Define the unique letters among the wordCandidates		
		Set<Character> letterCandidates = getLetterCandidates(wordCandidates, emptyLetterPositions, allGuessedLetters);
		
		//Calculate for the Maximum information score
		//Letter:
		double maxEntropy = Double.NEGATIVE_INFINITY; //A  constant holding the negative infinity of type double.
		char maxChar = 0;
		// Find the letter guess with highest information content
		for (Character c : letterCandidates)
		{
			Map<String, List<String>> freqMap = buildFrequencyMap(c.charValue(), emptyLetterPositions, wordCandidates);
			//Entropy Calculation
			double entropy = Entropy.calculateEntropy(freqMap, wordCandidates.size());
			
			//Save the maximum entropy and the character
			if (entropy > maxEntropy)
			{ 
				maxEntropy = entropy;
				maxChar = c;
			}
		}		
		//Word: 
		double maxWordEntropy = Entropy.calculateRandomSelectEntropy(wordCandidates.size());
		
		//Compare the max. entropy to randomly picking a word in the remaining word candidates  
		if (maxWordEntropy >= maxEntropy)
		{
			//Pick any random word from the list
			Random rand = new Random();
			int i = rand.nextInt(wordCandidates.size());		
			assert(i >= 0 && i < wordCandidates.size());
			return new GuessWord(wordCandidates.get(i));
		} else {			
			return new GuessLetter(maxChar);
		}
	}

	/*
	 * Create a hashtable for combinations of letter positions that matches the single alphabet
	 * based on the remaining word candidates.  
	 * @param alphabet to match
	 * @param positions of mystery letters
	 * @param a list of word candidates
	 * @return a hashtable of lists of words that correspond to the combination of matching letter
	 * positions 
	 */
	private Map<String, List<String>> buildFrequencyMap(char c, List<Integer> emptyLetterPositions, List<String>wordCandidates) {
		//A hashTable to count the number of occurrence for each guessing letter
		Map<String, List<String>> freqMap = new HashMap<String, List<String>>();
		for (String word : wordCandidates)
		{
			//Create groups of word candidates and index them by a unique hashkey
			String mapKey = createMapKey(c, word, emptyLetterPositions);
			
			if (freqMap.containsKey(mapKey)) 
			{
				freqMap.get(mapKey).add(word);
			} else {
				List<String> newMapValue = new LinkedList<String>();
				newMapValue.add(word);
				freqMap.put(mapKey, newMapValue);
			}			
		}		
		
		return freqMap;
	}

	/*
	 * Create the signature for the combination of letter positions that matches the single
	 * alphabet for each word.  A helper function to method buildFrequencyMap.
	 * Each unique signature represents a signal state for the guessing letter.
	 * @param alphabet to match
	 * @param the current word under examination
	 * @param positions of mystery letters
	 * @return a signature based on the matching alphabet  
	 */
	private String createMapKey(char c, String word, List<Integer>emptyLetterPositions) {
		//Only go through the letter positions with missing letters
		char[] keyCharArray = new char[emptyLetterPositions.size()];
		int index = 0;
		//Create a unique hash key based on matching letters
		for (int i : emptyLetterPositions)
		{				
			if (c == word.charAt(i))
			{
				keyCharArray[index] = c;
			} else {
				keyCharArray[index] = '_';
			}
			index++;
		}
		String mapKey = new String(keyCharArray);
		return mapKey;
	}

}
