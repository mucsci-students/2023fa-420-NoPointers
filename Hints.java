public class Hints {
    public static void main(String args){

    }

    /**
     * Method that return the string version of the text
     * in the hints button
     *
     */
    public String print(){
        String res = "";
        res += "Center letter is bold.\n\n";

        res += "WORDS:" + var + ",POINTS: " + var;
        res += "PANAGRAMS: " + var;
        if(/* There is a perfect panagram print this text*/){
            res+= "(" + var + "Perfect)";
        }
        res += "\n";
        res += buildMatrix() + "\n";
        res += "Two letter list:\n\n";

    }
    /**
     * This function is a helper function for print
     * it generates the 2D matrix in the hints tab
     * and returns a string version of the matrix
     *
     * @param validwords Arraylist of valid words
     * @return A string of the 2D matrix
     */
    public String buildMatrix() {

        for (String str : validWords) {
            if (str.length() > largestString.length()) {
                largestString = str;
            }
        }
        String[][] matrix = new String[7][largestString.length()-1];


    }
}
