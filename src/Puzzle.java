import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;

import org.json.simple.JSONObject;

import com.github.cliftonlabs.json_simple.JsonObject;
public class Puzzle {
	
	// Fields of Puzzle Class
	// The 6 optional letters
	private char[] letters;
	private ArrayList<String> validWords;
	// The required letter
	private char requiredLetter;
	private ArrayList<String> guessed;
		
	// Builder (New puzzle)
	public Puzzle() {
		validWords = new ArrayList<>();
		// Add 6 random letters to letters array
		this.letters = getSixLetters();	
		
		// Insert a random vowel for required letter
		this.requiredLetter = getRequiredLetter();	
		
		letters[6] = requiredLetter;
		
		guessed = new ArrayList<>();
		validWords = Connect.getWords(letters);
		//showPuzzle ();
		//System.out.println (validWords);
	}

		public Puzzle(char requiredLetter, char[] letters, ArrayList<String> guessed) {
		this.requiredLetter = requiredLetter;
		this.letters = letters;
		this.guessed = guessed;
	}

		public static Puzzle fromJsonObject(JsonObject jo) {
		String requiredLetter =  (String) jo.get("required letter");
		char reql = requiredLetter.charAt(0);
		char[] letters = (char[]) jo.get("letters used").toString().toCharArray();
		
		
		ArrayList<String> guessed = (ArrayList<String>) jo.get("Guessed Words");
		return new Puzzle(reql, letters, guessed);

	}
		
	// Builder using input from user (New puzzle from base)
	public Puzzle(String input) {
		// Take 6 non-requited letters from input
			
		// Take requiredLetter from input (Have user specify what is required letter?
		// Or just make a system like having the last letter be the requited letter?
			
	}
	
	
	    public void addGuess(String word) {
        guessed.add(word);
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
		char[] array = new char[pickedLetters.size() + 1];
		for(int i = 0; i < pickedLetters.size(); i++) {
		    array[i] = pickedLetters.get(i);
		}
		array[6] = '.';
		
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
		//Print out the seven letters with the required letter in brackets [].
		for (int i = 0; i < letters.length; ++i) {
			if (i == 6)
				System.out.print("[");
			System.out.print(letters[i]);
		}
		System.out.print("]\n");
	}
	
	//Method to be called on from Guess Command. Takes input from user and checks it it is in the 
	// list of possible words found in the dictionary.
	public boolean guessWord (String guess) {
		if (validWords.contains (guess)) {
			if (guessed.contains(guess)) {
				System.out.println ("Word already found!");
				return false;
			}
			System.out.println ("Correct! Word added to guessed words.");
			guessed.add(guess);
			return true;
		}
		boolean foundRequired = false;
		for (int i = 0; i < guess.length(); ++i) {
			if (guess.charAt(i) == requiredLetter) {
				foundRequired = true;
				break;
			}
		}
		if (!foundRequired) {
			System.out.println ("Incorrect. Does not use required letter.");
			return false;
		}
		
		for (int i = 0; i < letters.length; ++i) {
			boolean found = false;
			for (int j = 0; j < guess.length(); ++j) {
				if (guess.charAt(j) == letters[i]) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println ("Bad letters");
				return false;
			}
			
		}
		
		System.out.println ("Not a valid word.");
		return false;
		// Dummy return for testing purposes.
		
	}

		public JSONObject toJsonObject() {
		String used = "";
		for (int i = 0; i < letters.length; ++i) {
			used += letters[i];
		}
		JSONObject jo = new JSONObject();
		String reqletter = ""+requiredLetter;
		jo.put("required letter", reqletter);
		jo.put("letters used", used);
		jo.put("Guessed Words", guessed);
		return jo;
	}
	
	//Method to be called on from Shuffle Command. Shuffles the order of the non-required letters.
	public void shuffleLetters() {
		// We will always keep the required letter at the end of the list.
		for (int i = 0; i < letters.length - 1; ++i) {
			int rand = Math.abs ((int) Math.random() % 6);
			
			if (i == rand)
				continue;
			
			char temp = letters[i];
			letters[i] = letters[rand];
			letters[rand] = temp;
		}
	}
	
}

