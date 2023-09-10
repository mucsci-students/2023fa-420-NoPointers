import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
public class Puzzle {
	// Fields of Puzzle Class
	// The 6 optional letters
	private char[] letters;
	// The required letter
	private char requiredLetter;
		
	// Builder (New puzzle)
	public Puzzle() {
		// Add 6 random letters to letters array
		this.letters = getSixLetters();	
		// Insert a random vowel for required letter
		this.requiredLetter = getRequiredLetter();	
	}
		
	// Builder using input from user (New puzzle from base)
	public Puzzle(String input) {
		// Take 6 non-requited letters from input
			
		// Take requiredLetter from input (Have user specify what is required letter?
		// Or just make a system like having the last letter be the requited letter?
			
	}
	
	// Helper method to get 6 random letters
	private char[] getSixLetters( ) {
		ArrayList<Character> pickedLetters = new ArrayList<Character>();
		while (pickedLetters.size() < 6) {
			Random r = new Random();
			char c = (char)(r.nextInt(26) + 'a');
			if (pickedLetters.contains(c)) {
				
			}
			else {
				pickedLetters.add(c);
			}
		}
		// Converts ArrayList of Characters to array of chars
		char[] array = new char[pickedLetters.size()];
		for(int i = 0; i < pickedLetters.size(); i++) {
		    array[i] = pickedLetters.get(i);
		}
		
		return array;  
	}
	
	// Helper method to get required vowel letter that must be used in all guesses
	private char getRequiredLetter() {
		char[] array = {'a', 'e', 'i', 'o', 'u'};
		Random r = new Random();
		char c = array[r.nextInt(5)];
		// Need to find way to check if our picked vowel is already in the other 6 letters.
		return c;
	}
	
	// Method to be called on from Show Puzzle Command. Prints out the puzzle letters to user.
	public void showPuzzle() {
		// Fill in
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(letters);
		stringBuilder.append("[");
		stringBuilder.append(requiredLetter);
		stringBuilder.append("]");
		//Print out the seven letters with the required letter in brackets [].
		System.out.println(stringBuilder.toString()); 
	}
	
	//Method to be called on from Guess Command. Takes input from user and checks it it is in the 
	// list of possible words found in the dictionary.
	public boolean guessWord() {
		// Fill in
		
		// Dummy return for testing purposes.
		return true;
	}
	
	//Method to be called on from Shuffle Command. Shuffles the order of the non-required letters.
	public void shuffleLetters() {
		// Fill in.
	}
	
}

