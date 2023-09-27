package org.nopointers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.json.simple.JSONObject;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Puzzle {

    //Rank related structures
    String[] ranks = {
            "Student",
            "Apprentice",
            "Adept",
            "Master",
            "Elder Wizard",
            "Sorcerer",
            "Keeper of Words",
            "Keeper of Words",
            "Genius",
            "Word Wizard"
    };
    int[] levels = {
            0,
            2,
            5,
            8,
            15,
            25,
            40,
            50,
            70,
            100
    };

    // Fields of Puzzle Class
    // The 6 optional letters
    private char[] letters;
    // The required letter
    private char requiredLetter;
    private ArrayList<String> validWords;
    private ArrayList<String> guessed;

    private int score;
    private int maxScore;

    // Builder (New puzzle)
    public Puzzle() {
        char[] pangram = Connect.selectPangram();
        this.letters = pangram;
        this.guessed = new ArrayList<String>();
        this.validWords = new ArrayList<String>();
        requiredLetter = selectRequiredLetter();
        validWords = Connect.getWords(letters);

        score = 0;
        maxScore = calculateMaxScore ();
    }

    public Puzzle(char requiredLetter, char[] letters, ArrayList<String> guessed) {
        this.requiredLetter = requiredLetter;
        this.letters = Connect.convertToArray(letters);
        this.guessed = guessed;


        this.validWords = Connect.getWords(letters);

        score = 0;
        maxScore = calculateMaxScore ();
    }

    // Builder using input from user (New puzzle from base)
    public Puzzle(String input) {
        validWords = new ArrayList<>();
        // Take 6 non-requited letters from input
        // Take requiredLetter from input (Have user specify what is required letter?
        // Or just make a system like having the last letter be the requited letter?
        this.letters = Connect.convertToArray(input);
        this.requiredLetter = selectRequiredLetter();
        validWords = Connect.getWords(letters);
        this.guessed = new ArrayList<String>();

        score = 0;
        maxScore = calculateMaxScore ();
    }

    public static Puzzle fromJsonObject(JsonObject jo) {
        String requiredLetter = (String) jo.get("required letter");
        char reql = requiredLetter.charAt(0);
        char[] letters = (char[]) jo.get("letters used").toString().toCharArray();

        ArrayList<String> guessed = (ArrayList<String>)jo.get("Guessed Words");
        return new Puzzle(reql, letters, guessed);

    }

    private ArrayList<Character> findVowels () {
        ArrayList<Character> found = new ArrayList<>();
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};

        for (int i = 0; i < letters.length; ++i) {
            for (char c : vowels) {
                if (letters[i] == c && !found.contains(c)) {
                    found.add(c);
                    break;
                }
            }
        }
        if (found.isEmpty ())
            found.add ('y');
        return found;
    }
    private char selectRequiredLetter () {
        ArrayList<Character> vowels = findVowels();

        Random random = new Random();
        int randInt = random.nextInt (vowels.size ());

        for (int i = 0; i < letters.length; ++i) {
            if (letters[i] != vowels.get (randInt))
                continue;

            char temp = letters[6];
            letters[6] = letters[i];
            letters[i] = temp;
            requiredLetter = letters[6];
            break;
        }
        return letters[6];
    }

    // Helper method to get 6 random letters
    private char[] getSixLetters() {
        ArrayList<Character> pickedLetters = new ArrayList<Character>();
        while (pickedLetters.size() < 6) {
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'a');
            if (pickedLetters.contains(c)) {

            } else {
                pickedLetters.add(c);
            }
        }

        // Converts ArrayList of Characters to array of chars
        char[] array = new char[pickedLetters.size() + 1];
        for (int i = 0; i < pickedLetters.size(); i++) {
            array[i] = pickedLetters.get(i);
        }

        return array;
    }

    // Helper method to get required vowel letter that must be used in all guesses
    private char getRequiredLetter() {
        char[] array = { 'a', 'e', 'i', 'o', 'u' };
        Random r = new Random();
        char c = array[r.nextInt(5)];
        // Need to find way to check if our picked vowel is already in the other 6
        // letters.
        return c;
    }

    // Method to be called on from Show Puzzle Command. Prints out the puzzle
    // letters to user.
    public void showPuzzle() {
        // Print out the seven letters with the required letter in brackets [].
        for (int i = 0; i < letters.length; ++i) {
            if (i == 6)
                System.out.print("[");
            System.out.print(letters[i]);
        }
        System.out.print("]\n");
        System.out.println("Guessed Words: " + guessed.toString());
        System.out.println("                  .'* *.'\n"
                + "               __/_*_*(_\n"
                + "              / _______ \\\n"
                + "             _\\_)/___\\(_/_ \n"
                + "            / _((\\- -/))_ \\\n"
                + "            \\ \\())(-)(()/ /\n"
                + "             ' \\(((()))/ '\n"
                + "            / ' \\)).))/ ' \\\n"
                + "           / _ \\ - | - /_  \\\n"
                + "          (   ( .;''';. .'  )\n"
                + "          _\\\"__ /    )\\ __\"/_\n"
                + "            \\/  \\   ' /  \\/\n"
                + "             .'  '...' ' )\n"
                + "              / /  |  \\ \\\n"
                + "             / .   .   . \\\n"
                + "            /   .     .   \\\n"
                + "           /   /   |   \\   \\\n"
                + "         .'   /    |    '.  '.\n"
                + "     _.-'    /     |     '-. '-._ \n"
                + " _.-'       |      |       '-.  '-. \n"
                + "(________mrf\\____.| .________)____)");
    }

    public String givePuzzle() {
        StringBuilder s = new StringBuilder();
        for (char c : letters) {
            s.append(c);
        }
        return s.toString();
    }

    // Method to be called on from Guess Command. Takes input from user and checks
    // it it is in the
    // list of possible words found in the dictionary.
    public boolean guessWord(String guess) {
        if (validWords.contains(guess)) {
            if (guessed.contains(guess)) {
                System.out.println("Word already found!");
                return false;
            }
            System.out.println("Correct! Word added to guessed words.");
            addCorrectWord(guess);
            showPuzzle();
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
            System.out.println("Incorrect. Does not use required letter.");
            System.out.println("The Required letter is [" + this.requiredLetter + "]");
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
                System.out.println("Not a valid word.");
                return false;
            }

        }

        System.out.println("Not a valid word.");
        return false;

    }

    public void addGuess(String word) {
        guessed.add(word);
    }

    // Method to be called on from Shuffle Command. Shuffles the order of the
    // non-required letters.
    public void shuffleLetters() {
        // We will always keep the required letter at the end of the list.
        for (int i = 0; i < letters.length - 1; ++i) {
            int rand = Math.abs((int) Math.random() % 6);

            if (i == rand)
                continue;

            char temp = letters[i];
            letters[i] = letters[rand];
            letters[rand] = temp;
        }
    }

    public JSONObject toJsonObject() {
        String used = "";
        for (int i = 0; i < letters.length; ++i) {
            used += letters[i];
        }
        JSONObject jo = new JSONObject();
        String reqletter = "" + requiredLetter;
        jo.put("required letter", reqletter);
        jo.put("letters used", used);
        jo.put("Guessed Words", guessed);
        return jo;
    }

    public char[] getLetters() {
        return letters;
    }

    public void setLetters(char[] letters) {
        this.letters = letters;
    }

    public ArrayList<String> getGuessed() {
        return guessed;
    }

    public void setGuessed(ArrayList<String> guessed) {
        this.guessed = guessed;
    }

    public void setRequiredLetter(char requiredLetter) {
        this.requiredLetter = requiredLetter;

    }


    public int getRank () {
        double overallScorePercent = (double) score / maxScore;

        if (overallScorePercent >= 1.0)
            return 9;
        else if (overallScorePercent >= 0.70)
            return 8;
        else if (overallScorePercent >= 0.50)
            return 7;
        else if (overallScorePercent >= 0.40)
            return 6;
        else if (overallScorePercent >= 0.25)
            return 5;
        else if (overallScorePercent >= 0.15)
            return 4;
        else if (overallScorePercent >= 0.08)
            return 3;
        else if (overallScorePercent >= 0.05)
            return 2;
        else if (overallScorePercent >= 0.02)
            return 1;
        else
            return 0;
    }
    public void displayRank () {
        System.out.println ("You have " + score + "pts / " + maxScore + "  |  Rank: " + ranks[getRank()]);
        if (getRank () == 9)
            return;
        System.out.println ("Next rank: " + ranks[getRank() + 1] + " at " + (int) (levels[getRank () + 1] * maxScore / 100) + "pts");
    }
    private void addCorrectWord (String guess) {
        guessed.add(guess);
        int prevRank = getRank ();
        score += calculatePoints(guess);
        if (getRank() != prevRank)
            System.out.println ("Level up!");
        displayRank ();
    }
    private int calculateMaxScore () {
        int total = 0;
        for (String word : validWords) {
            int points = word.length();
            if (points == 4)
                points = 1;
            if (Connect.checkPangram(word))
                points += 7;
            total += points;
        }
        return total;
    }
    private int calculatePoints (String guess) {
        int points = guess.length();
        if (points == 4)
            points = 1;

        if (Connect.checkPangram(guess))
            points += 7;

        return points;
    }
}
