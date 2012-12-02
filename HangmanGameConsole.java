import java.io.*;
import java.util.*;

public class HangmanGameConsole {
	private int totalScore;
	private int gamesWon;
	private int gamesLost;
	private String wordFileName; //file containing the dictionary
	private ArrayList<String>words = new ArrayList<String>(); //words found in the dictionary
	private Map<Integer, TreeMap<Character, Integer>> letterCallingOrder = new HashMap<Integer, TreeMap<Character, Integer>>(); //letter frequency map per word length
	
	public HangmanGameConsole(String fileName) {
		this.totalScore = 0; 
		this.wordFileName = fileName;
		this.gamesWon = 0;
		this.gamesLost = 0;
	}
	
	public void retrieveWordList()
	{
		try {
			this.words = FileScanner.readLinesFromFile(this.wordFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	/*
	 * For each word length, rank the letter frequency with the highest one first.
	 * Used for guessing first letter when no information is available
	 */
	public void rankLettersByWordLength()
	{
		Map<Integer, HashMap<Character, Integer>> wordLengthMap = new HashMap<Integer, HashMap<Character, Integer>>();
		
		for (String word : words)
		{
			Integer wordLength = word.length();
					
			HashMap<Character, Integer> letterCountMap = new HashMap<Character, Integer>();
			if (wordLengthMap.containsKey(wordLength))
			{
				//Retrieve from existing list
				letterCountMap = wordLengthMap.get(wordLength);
			} 
			
			//Scan word and count each letter
			for (int i = 0; i < wordLength; i++)
			{
				Character c = word.charAt(i);
				
				if (letterCountMap.containsKey(c))
				{
					Integer count = letterCountMap.get(c);
					count += 1;
					letterCountMap.put(c, count);
				} else
				{
					Integer count = 1;
					letterCountMap.put(c, count);
				}
			}
			//Add back to the word length Map
			wordLengthMap.put(wordLength, letterCountMap);
		}
		
		//Sort the alphabet in order of its frequency for each word length
		Iterator<Map.Entry<Integer, HashMap<Character, Integer>>> it = wordLengthMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, HashMap<Character, Integer>> pairs = it.next();
	        MapValueComparator mvc = new MapValueComparator(pairs.getValue());
	        TreeMap<Character, Integer> sorted_map = new TreeMap<Character, Integer>(mvc);
	        sorted_map.putAll(pairs.getValue());
	        //System.out.println(pairs.getKey() + " = " + sorted_map);
	        it.remove(); 
	        
	        //Put the sorted map into letterCallingOrder Map
	        letterCallingOrder.put(pairs.getKey(), sorted_map);
	    }
	}
	
	/*
	 *Generate a set of random words 
	 */
	public List<String> getRandomWords(int numGames)
	{
		List<String> wordsForGame = new LinkedList<String>();
		for (int i = 0; i < numGames; i++) {
			wordsForGame.add(this.nextRandomWord());
		}
		return wordsForGame;
	}
	
	/*
	 * Pick a random word from the list
	 * @return word string
	 */
	public String nextRandomWord() {
		Random rand = new Random();
		return words.get(rand.nextInt(this.words.size()));
	}
	
	/*
	 * Run the given strategy for the given game, then returns the score
	 * @param game, strategy
	 * @return a single game score
	 */
	public int run(HangmanGame game, GuessingStrategy strategy) {
		
		//Get the most frequent letter for secret word length
		Integer wordLength = game.getSecretWordLength();
		TreeMap<Character, Integer> callOrderMap = letterCallingOrder.get(wordLength);
		Character mostFreqChar = callOrderMap.firstKey();
		
		//Guess the first letter
		Guess firstGuess = new GuessLetter(mostFreqChar);
		firstGuess.makeGuess(game);
		//Keep Guessing
		while (game.gameStatus() == HangmanGame.Status.KEEP_GUESSING){
			Guess guess = strategy.nextGuess(game);
			
			if (guess != null) {
				guess.makeGuess(game);
			} else {
				System.out.println("no guess made");
				break; 
			}
		}
		return game.currentScore();
	}
	
	public void play(int numGames, int maxGuesses)
	{
		//Retrieve the word lists from text file		
		this.retrieveWordList();			
		//Measure letter frequency and rank them for each word length
		this.rankLettersByWordLength();			
		//List of words for the game
		List<String> wordsForGame = this.getRandomWords(numGames);
		
		HangmanGuessingStrategy strategy = new HangmanGuessingStrategy(this.words);
		int wordCounter = 0;		
		for (String word : wordsForGame)
		{
			wordCounter++;
			HangmanGame game = new HangmanGame(word, maxGuesses);
			int gameScore = this.run(game, strategy);
			//Game Won or Lost
			if (game.gameStatus() == HangmanGame.Status.GAME_WON)
			{
				this.gamesWon++;
			} else
			{
				this.gamesLost++;
			}
			this.totalScore += gameScore;
			System.out.printf("%d) Secret word=%s; ", wordCounter, word.toUpperCase());
			System.out.printf("Game So Far=" + game.toString());
			System.out.println();
		}
	}
	
	/*
	 * Check if the provide string is a number
	 */
	public static boolean isNumeric(String str)
	{
		for(char c : str.toCharArray())
		{
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String wordListFileName = "words.txt";		
		int numGames;
		if (args.length > 0 && isNumeric(args[0]))
		{
			numGames = Integer.parseInt(args[0]);
		} else
		{
			numGames = 15;
		}
		int maxGuesses = 5;
		
		//Start-time
		final long startTime = System.currentTimeMillis(); 
		final long endTime;
		
		//Instantiate the console that will run multiple Hangman games
		HangmanGameConsole console = new HangmanGameConsole(wordListFileName);
		try
		{						
			//Start the game
			console.play(numGames, maxGuesses);
		} finally 
		{
			endTime = System.currentTimeMillis();
		}
		
		final long duration = endTime - startTime;
		System.out.println("------------------------");
		System.out.printf("Games Won: %d, Games Lost: %d", console.gamesWon, console.gamesLost);
		System.out.println();
		System.out.printf("Total Score: %2d", console.totalScore);
		System.out.println();
		System.out.printf("Execution Time (sec): %.2f", (float)duration/(float)1000);
		System.out.println();
	}

}
