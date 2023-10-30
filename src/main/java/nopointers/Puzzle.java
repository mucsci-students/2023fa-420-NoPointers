package nopointers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

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
        displayRank ();
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

    //private char getRequiredLetter() {
    //char[] array = { 'a', 'e', 'i', 'o', 'u' };
    //Random r = new Random();
    //char c = array[r.nextInt(5)];
    // Need to find way to check if our picked vowel is already in the other 6
    // letters.
    //return c;
    //}
    
    public String print(){
        StringBuilder res = new StringBuilder();
        res.append("Center letter is bold.\n\n");
        res.append("\u001B"+Character.toString(requiredLetter).toUpperCase() + " ");
        for(int i = 0; i < 6; i++){
	        res.append(Character.toString(letters[i]).toUpperCase());
            res.append(" ");
	    }
        res.append("\n");
        res.append("WORDS:" + validWords.size() + ", POINTS: " + Integer.toString(maxScore));//+ var
        res.append(", PANGRAMS: ");
	    res.append(Connect.pangramCount());
        /*
        if(Connect.countPerfectPangrams() > 0){
	    res.append("(" + Connect.countPerfectPangrams() + "Perfect)");
        }
        */
        res.append("\n\n" + buildMatrix(validWords) + "\n");
        res.append("Two letter list:\n");
        res.append(twoLetLst(validWords));
        return res.toString();
    }

    /**
     * This function is a helper function for print.
     *
     * It generates the 2D matrix in the hints tab
     * and returns a string version of the matrix
     *
     * @param lst Arraylist of valid words
     * @return A string of the 2D matrix
     */
    public String buildMatrix(ArrayList<String> lst) {
        String largestString = "";
        for (String str : lst) {
            if (str.length() > largestString.length()) {
                largestString = str;
            }
        }
        String[] lettersArr = firstLet(lst);
        String[][] matrix = new String[lettersArr.length+2][largestString.length()-1];
        int row = matrix.length;
        int col = matrix[0].length;
        int num = 4;
        for(int i = 1; i < col-1; i++){
            matrix[0][i] = String.valueOf(num);
            num++;
        }
        for(int i = 1; i < row-1; i++){
            matrix[i][0] = lettersArr[i-1].toUpperCase();
        }
        matrix = addColonsAndSum(matrix);
        int[][] mat = new int [matrix.length-1][matrix[0].length-1];
        mat = numMatrix(mat,lettersArr,lst);
        matrix = intToStr(matrix, mat);
        matrix[0][0] = "  ";
        return matrixToString(matrix);
    }

    /**
     * This function is a helper function for buildMatrix.
     *
     * The matrix has a constraint where the y-axis of
     * the matrix should only contain the first letter of
     * valid words. So if there is a letter in the puzzle
     * and there isn't a word that starts with that letter
     * then the letter is not in the matrix.
     *
     * This function takes in the valid words and finds all
     * the first letters of the words and returns a set of
     * which letters in the puzzle letters have a valid word
     * that starts with it.
     *
     * @param arr Arraylist of valid words
     * @return A set containing all  the first letters
     * in all  the valid words
     */
    public String[] firstLet(ArrayList<String> arr){
        HashSet<String> set = new HashSet<>();
        for (String str : arr) {
            String fir = Character.toString(str.charAt(0));
            set.add(fir);
        }
        int n = set.size();
        String[] array = new String[n];
        int i = 0;
        for (String x : set)
            array[i++] = x;
        return array;
    }

    /**
     * This function is a helper function for buildMatrix.
     *
     * This function fills in the string matrix with number
     * value for each of the letters and the sizes.
     *
     * @param matrix The string matrix being built.
     * @param strArr The array of the first letter words.
     * @param lst The arraylist of valid words for the puzzle.
     * @return The original matrix with number values
     * filled in.
     */
    public int[][] numMatrix(int[][] matrix,String[] strArr, ArrayList<String> lst){
        //Fills in the number values
        for (String str : lst) {
            int size = str.length();
            int num = 0;
            String fir = Character.toString(str.charAt(0));
            for(int i = 0; i < strArr.length; i++){
                if(fir.equals(strArr[i]))
                    num = i;
            }
            matrix[num][size-4]++;
        }

        //Does the summations and fills the bottom row and column
        int num = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length-1; j++) {
                num+= matrix[i][j];
            }
            matrix[i][matrix[0].length-1] = num;
            num = 0;
        }
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length-1; j++) {
                num+= matrix[j][i];
            }
            matrix[matrix.length-1][i] = num;
            num = 0;
        }
        return matrix;
    }

    /**
     * This function is a helper function for buildMatrix.
     *
     * This function fills adds colons of all the values
     * on the y-axis of the matrix and adds Gamma to the
     * end of the x and y-axis.
     *
     * @param matrix The string matrix being built.
     * @return The original matrix with colons and gamma.
     */
    public String[][] addColonsAndSum(String[][] matrix){
        matrix[matrix.length-1][0] = matrix[0][matrix[0].length-1] = "\u2211";
        for(int i = 1; i < matrix.length; i++){
            matrix[i][0] = matrix[i][0] + ":";
        }
        return matrix;
    }

    /**
     * This function is a helper function for buildMatrix.
     *
     * This function takes the integer matrix with the
     * numerical values and summations and places each
     * entry into the string matrix while converting
     * the entries from int to string
     *
     * @param strMat The string matrix being built.
     * @param intMat The integer matrix
     * @return The original matrix with colons and gamma.
     */
    public String[][] intToStr(String[][] strMat, int[][] intMat){
        for (int i = 1; i < strMat.length; i++) {
            for (int j = 1; j < strMat[0].length; j++) {
                if(intMat[i-1][j-1] == 0){
                    strMat[i][j] = "-";
                }
                else{
                    strMat[i][j] = String.valueOf(intMat[i-1][j-1]);
                }
            }
        }
        return strMat;
    }
    /**
     * This function is a helper function for buildMatrix.
     *
     * This function converts the string matrix that has been
     * built and uses Stringbuiler to turn it into one giant
     * string.
     *
     * @param matrix The string matrix being converted
     * @return The contents of the original string matrix
     * in the form of a string.
     */
    public String matrixToString(String[][] matrix){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                str.append(matrix[i][j] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }
    /**
     * This method is a helper function for print
     *
     * It returns a string of the two letter list
     * such as the one in the example.
     *
     * @param lst The list of valid words
     * @return A string of everything in the hints after
     * the matrix
     */
    public String twoLetLst (ArrayList<String> lst){
        Map<String,Integer> map = new TreeMap<String,Integer>();
        for (String str : lst) {
            String firSec = str.substring(0,2);
            if(!map.containsKey(firSec))
                map.put(firSec,1);
            else{
                int val = map.get(firSec);
                map.replace(firSec, val+1);
            }
        }
        StringBuilder res = new StringBuilder();
        String temp = "";
        for(Map.Entry<String,Integer> iter : map.entrySet()){
            String fir = iter.getKey().substring(0,1);
            if(!fir.equals(temp)){
                res.append("\n");
                temp = fir;
            }
            else{
                res.append(" ");
            }
            res.append(iter.getKey().toString().toUpperCase());
            res.append("-");
            res.append(iter.getValue().toString());
        }
        return res.toString();
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


        // Constructor.
        public Memento (Puzzle puzzle) {
            this.letters = puzzle.letters;
            this.guessed = puzzle.guessed;
            this.requiredLetter = puzzle.requiredLetter;
            this.score = puzzle.score;
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
