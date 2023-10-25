import java.util.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

//If anyone is reading this please send help
// S.O.S.
// I can't take it anymore
public class Hints {

    public Hints(){

    }
    /**
     * Method that return the string version of the text
     * in the hints button. Uses a lot of string building
     * and calls many helper functions. Read descriptions to
     * understand how everything works.
     *
     *
     * @return A string containing the entire contents on the
     * hints tab.
     */
    public String print(ArrayList<String> lst, String[] arr){
        StringBuilder res = new StringBuilder();
        res.append("Center letter is bold.\n\n");
        res.append("WORDS:" + lst.size() + ",POINTS: ");//+ var
        res.append("PANGRAMS: ");
        /*
        if(/* There is a perfect pangram print this text*//*){
            res+= "(" + var + "Perfect)";
        }
        */
        res.append("\n\n" + buildMatrix(lst, arr) + "\n");
        res.append("Two letter list:\n\n");
        res.append(twoLetLst(lst));
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
    public String buildMatrix(ArrayList<String> lst, String[] arr) {
        String largestString = "";
        for (String str : lst) {
            if (str.length() > largestString.length()) {
                largestString = str;
            }
        }
        String[] lettersArr = firstLet(lst);
        String[][] matrix = new String[lettersArr.length+1][largestString.length()-1];
        int row = matrix.length;
        int col = matrix[0].length;
        int num = 4;
        for(int i = 1; i < row; i++){
            matrix[0][i] = String.valueOf(num);
            num++;
        }
        for(int i = 1; i < col; i++){
            matrix[i][0] = lettersArr[i-1];
        }
        matrix = addColonsAndSum(matrix);
        int[][] mat = new int [matrix.length-1][matrix[0].length-1];
        mat = numMatrix(mat,lettersArr,lst);
        matrix = intToStr(matrix, mat);
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
        HashSet<String> set = new HashSet<String>();
        for (String str : arr) {
            String fir = Character.toString(str.charAt(0));
            set.add(fir);
        }
        int n = set.size();
        String array[] = new String[n];
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
            matrix[size-3][num+1]++;
        }
        //Does the summations and fills the bottom row and column
        int num = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length-1; j++) {
                num+= matrix[i][j];
            }
            matrix[i][matrix[0].length] = num;
            num = 0;
        }
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length-1; j++) {
                num+= matrix[i][j];
            }
            matrix[i][matrix.length] = num;
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
        matrix[matrix.length][0] = matrix[0][matrix[0].length] = "\u2211";
        for(int i = 0; i < matrix[0].length; i++){
            matrix[0][i] = matrix[0][i] + ":";
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
     * @param intMat
     * @return The original matrix with colons and gamma.
     */
    public String[][] intToStr(String[][] strMat, int[][] intMat){
        for (int i = 0; i < strMat.length; i++) {
            for (int j = 1; j < strMat[0].length; j++) {
                strMat[i][j] = String.valueOf(intMat[i-1][j-1]);
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
        Map<String,Integer> map = new HashMap<String,Integer>();
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
            String fir = iter.getKey().substring(0);
            if(fir != temp){
                res.append("\n");
                temp = fir;
            }
            else{
                res.append(" ");
            }
            res.append(iter.getKey());
            res.append("-");
            res.append(iter.getValue().toString());
        }
        return res.toString();
    }
}
