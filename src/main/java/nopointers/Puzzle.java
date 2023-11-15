package nopointers;

import java.io.File;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Random;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.event.ActionEvent;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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


    private ArrayList<String> guessed;

    // The required letter

    private char requiredLetter;


    private ArrayList<String> validWords;


    private int score;

    @SerializedName(value = "maxPoints")
    @Expose (serialize = true, deserialize = true)
    private int maxScore;
    private Database database;

    // Builder (New puzzle)
    public Puzzle() {
        database = Database.getInstance();

        this.letters = database.selectPangram();
        this.guessed = new ArrayList<>();
        this.validWords = new ArrayList<>();
        requiredLetter = selectRequiredLetter();
        validWords = database.getWords(letters);

        score = 0;
        maxScore = calculateMaxScore ();

    }

    public Puzzle(char requiredLetter, char[] letters, ArrayList<String> guessed) {
        database = Database.getInstance();
        this.requiredLetter = requiredLetter;
        this.letters = database.convertToArray(letters);
        this.guessed = guessed;


        this.validWords = database.getWords(letters);

        score = 0;
        maxScore = calculateMaxScore ();
    }

    //Build puzzle with a selected required letter. Used only for test purposes.
    public Puzzle(String baseWord, char requiredLetter) {
        database = Database.getInstance();
        letters = database.convertToArray(baseWord);

        int requiredIndex = 0;
        for (int i = 0; i < 7; ++i) {
            if (letters[i] != requiredLetter)
                continue;

            requiredIndex = i;
            break;
        }
        this.requiredLetter = letters[requiredIndex];
        letters[requiredIndex] = letters[6];
        letters[6] = requiredLetter;

        this.guessed = new ArrayList<String>();
        this.validWords = new ArrayList<String>();

        validWords = database.getWords(letters);

        score = 0;
        maxScore = calculateMaxScore ();
    }

    // Builder using input from user (New puzzle from base)
    public Puzzle(String input) {
        database = Database.getInstance();
        validWords = new ArrayList<>();
        // Take 6 non-requited letters from input
        // Take requiredLetter from input Have user specify what is required letter?
        // Or just make a system like having the last letter be the requited letter?
        this.letters = database.convertToArray(input);

        this.requiredLetter = selectRequiredLetter();
        validWords = database.getWords(letters);
        this.guessed = new ArrayList<>();

        score = 0;
        maxScore = calculateMaxScore ();
    }

    private ArrayList<Character> findVowels () {
        ArrayList<Character> found = new ArrayList<>();
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};

        for (char letter : letters) {
            for (char c : vowels) {
                if (letter == c && !found.contains(c)) {
                    found.add(c);
                    break;
                }
            }
        }
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

    // Method to be called on from Guess Command. Takes input from user and checks
    //  it is in the
    // list of possible words found in the dictionary.
    public GuessOutcome guessWord(String guess) {
        if (validWords.contains(guess)) {
            if (guessed.contains(guess)) {

                return GuessOutcome.ALREADY_FOUND;
            }

            addCorrectWord(guess);
            return GuessOutcome.SUCCESS;
        }
        boolean foundRequired = false;
        for (int i = 0; i < guess.length(); ++i) {
            if (guess.charAt(i) == requiredLetter) {
                foundRequired = true;
                break;
            }
        }
        if (!foundRequired) {
            return GuessOutcome.MISSING_REQUIRED;
        }
        return GuessOutcome.INCORRECT;
    }

    public char getRequiredLetter() {
        return this.requiredLetter;
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
    }
    private int calculateMaxScore () {
        int total = 0;
        for (String word : validWords) {
            int points = word.length();
            if (points == 4)
                points = 1;
            if (database.checkPangram(word))
                points += 7;
            total += points;
        }
        return total;
    }
    private int calculatePoints (String guess) {
        int points = guess.length();
        if (points == 4)
            points = 1;

        if (database.checkPangram(guess))
            points += 7;

        return points;
    }

    public String[] getRanks() {
        return ranks;
    }

    /*public void setRanks(String[] ranks) {
        this.ranks = ranks;
    }

    public int[] getLevels() {
        return levels;
    }

    public void setLevels(int[] levels) {
        this.levels = levels;
    }*/

    public ArrayList<String> getValidWords() {
        return validWords;
    }
    /*
    public void setValidWords(ArrayList<String> validWords) {
        this.validWords = validWords;
    }*/

    public int getScore() {
        return score;
    }
    /*
    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }*/

    public char[] getLetters() {
        return letters;
    }

    /*
    public void setLetters(char[] letters) {
        this.letters = letters;
    }*/

    public ArrayList<String> getGuessed() {
        return guessed;
    }

    /*
    public void setGuessed(ArrayList<String> guessed) {
        this.guessed = guessed;
    }

    public void setMaxScore(int maxScore) {this.maxScore = maxScore;}

    public void setRequiredLetter(char requiredLetter) {
        this.requiredLetter = requiredLetter;

    }*/

    public double getScorePercent () {
        return (double) score / maxScore;
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
    // Changes current puzzle's fields to be of those stored in the saved Memento
    public void restoreFromMemento(Memento memento) {
        letters = memento.getLetters();
        guessed = memento.getGuessed();
        requiredLetter = memento.getRequiredLetter();
        score = memento.getScore();
    }
    // Save fields of current puzzle to a Memento
    public Memento saveToMemento() {
        Memento saved = new Memento(this);
        return saved;
    }
    // Memento implementation. Used to save/load details of puzzle to/from a json file.
    public class Memento {

        @SerializedName(value = "baseWord")
        @Expose (serialize = true, deserialize = true)
        private char[] letters;

        @SerializedName(value = "foundWords")
        @Expose (serialize = true, deserialize = true)
        private ArrayList<String> guessed;

        // The required letter
        @SerializedName(value = "requiredLetter")
        @Expose (serialize = true, deserialize = true)
        private char requiredLetter;


        @SerializedName(value = "playerPoints")
        @Expose (serialize = true, deserialize = true)
        private int score;

        @SerializedName(value = "wordlist")
        @Expose (serialize = true, deserialize = true)
        private ArrayList<String> validWords;

        @SerializedName(value = "author")
        @Expose (serialize = true, deserialize = true)
        private String author;
        // Constructor.
        public Memento (Puzzle puzzle) {
            this.letters = puzzle.letters;
            this.guessed = puzzle.guessed;
            this.requiredLetter = puzzle.requiredLetter;
            this.score = puzzle.score;
            this.validWords = puzzle.validWords;
            this.author = "no-pointers";
        }

        public char getRequiredLetter() {
            return requiredLetter;
        }

        public ArrayList<String> getGuessed() {
            return guessed;
        }

        public char[] getLetters() {
            return letters;
        }

        public int getScore() {
            return score;
        }

        public String toGSONObject()
        {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(this);
            return json;
        }


    }

}