import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class FileScanner {
	/**
	 * Reads all the lines in a text file.	 
	 * @param input filename 
	 * @return an array with one line of text per entry
	 */
	public static ArrayList<String> readLinesFromFile(String fileName) throws FileNotFoundException {
		 File wordFile = new File(fileName);
		 Scanner scanner = new Scanner(new FileReader(wordFile));  //FileReader used instead of File b/c File is not Closeable
		 ArrayList<String>words = new ArrayList<String>(); //Store only the unique words
		 
		 try {
			 while (scanner.hasNextLine()) {
				 String line = scanner.nextLine();
				 line = line.trim();
				 if (line.length() == 0) {
					 continue;
				 }
				 words.add(line.toLowerCase());
			 }
		 } 
		 finally {
			 //make sure the input stream is closed
			 scanner.close();			 
			 
			//Save the list of random words to class' words array
			//this.words = words.toArray(NO_STRINGS);
		 }
		 return words;
	}
}
