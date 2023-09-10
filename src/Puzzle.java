import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
public class Puzzle {
	// Fields of Puzzle Class
	// The 6 optional letters
	private ArrayList<Character> letters;
	// The required letter
	private char requiredLetter;
	private String secretWord;
		
	// Builder (New puzzle)
	public Puzzle() {
		// Add 6 random letters to letters array
		this.letters = new ArrayList<>();
		//this.letters = getSixLetters();

		// Insert a random vowel for required letter
		this.requiredLetter = getRequiredLetter();	
	}
		
	// Builder using input from user (New puzzle from base)
	public Puzzle(String input) {
		// Take 6 non-requited letters from input
		this.letters = new ArrayList<>();

		if (validate(input)) {
			letters = null;
			secretWord = null;
			return;
		}

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
	
	//Returns the required letter.
	private char getRequiredLetter() {
		return letters.get (0);
	}
	//Returns the pangram base word.
	private String getWord () {
		return secretWord;
	}
	//Returns the list of unique letters of the puzzle.
	private ArrayList<Character> getLetters () {
		return letters;
	}

	//Helper method to select a required vowel letter that must be used in all guesses
	private char selectRequiredLetter () {
		ArrayList<Character> vowels = findVowels();

		Random random = new Random();
		int randInt = random.nextInt (vowels.size ());

		for (int i = 0; i < letters.size (); ++i) {
			if (!letters.get (i).equals (vowels.get (randInt)))
				continue;

			Collections.swap(letters, 0, i);
			return letters.get (i);
		}
	}
	private ArrayList<Character> findVowels () {
		ArrayList<Character> found = new ArrayList<>();
		char[] vowels = {'a', 'e', 'i', 'o', 'u'};
		for (int i = 0; i < vowels.length; ++i) {
			if (letters.contains (vowels[i]))
				found.add (vowels[i]);
		}
		if (found.isEmpty ())
			found.add ('y');
		return found;
	}
	
	// Method to be called on from Show Puzzle Command. Prints out the puzzle letters to user.
	public void showPuzzle() {
		System.out.print ('[');
		for (int i = 0; i < letters.length (); ++i) {
			if (i == 0)
				System.out.print ('*');

			System.out.print (letters.get(i));

			if (i < 6)
				System.out.print (' ');
		}
		System.out.print (']');
	}
	
	//Method to be called on from Guess Command. Takes input from user and checks it it is in the 
	// list of possible words found in the dictionary.
	public boolean guessWord() {
		// Fill in
		
		// Dummy return for testing purposes.
		return true;
	}

	//The letters of the word will be printed in the following format:
	//    *a b c d e f g
	// '*' signifies the required letter and a-g are letters of the selected pangram.
	private void displayLetters () {
		for (int i = 0; i < letters.length (); ++i) {
			if (i == 0)
				System.out.print ('*');

			System.out.print (letters.get(i));

			if (i < 6)
				System.out.print (' ');
		}
	}
	//Method to be called on from Shuffle Command. Shuffles the order of the non-required letters.
	public void shuffleLetters() {
		for (int i = 0; i < letters.size (); ++i) {
			if (i == 0)
				continue;
			//The required letter must always be the first in the list. So we can randomize indices 1-6
			int rand = Math.abs ((int) Math.random() % 6) + 1;
			Collections.swap(letters, i, rand);
		}
	}
	//Validates whether a word is a valid pangram. If it fails, the instance of the Puzzle object becomes useless.
	private boolean validate (String word) {
		HashSet<Character> letterSet = new HashSet<>();
		//Adding all the letters to a set will result in a list of only unique letters.
		for (int i = 0; i < word.length (); ++i)
			letterSet.add (word.charAt (i));

		if (letterSet.size () != 7)
			return false;

		// Next check if the word exists in the dictionary here
		//  return false;

		for (Character c : letterSet)
			letters.add (c);

		return true;
	}
	
}

