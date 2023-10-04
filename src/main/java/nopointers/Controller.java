package nopointers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;



public class Controller {
    private Puzzle puzzle;

    @FXML
    Button newpuzz = new Button();

    @FXML
    Button guessBox = new Button();

    @FXML
    TextArea guessed = new TextArea();

    @FXML
    Label pdislay = new Label();

    @FXML
    TextField gfield = new TextField();

    @FXML
    Label requiredLetter = new Label();



    public void NewPuzzle(ActionEvent e)
    {
        puzzle = new Puzzle();
        if(puzzle != null)
        {
            char[] arr = puzzle.getLetters();
            String word = new String(arr);

            pdislay.setText(word.substring(0,5));
            requiredLetter.setText(String.valueOf(word.charAt(6)));
        }
    }

    public void save(ActionEvent e)
    {
        if(puzzle != null)
        {
            puzzle.
        }
    }

    public void shuffle(ActionEvent e)
    {
        if(puzzle != null)
        {
            char[] arr = puzzle.getLetters();
            String word = new String(arr);
            puzzle.shuffleLetters();
            pdislay.setText(word.substring(0,5));
        }
    }

    public void guess(ActionEvent e)
    {
        if(puzzle != null)
        {
            String guess = gfield.getText();
            if(puzzle.guessWord(guess))
            {
                guessed.insertText(0,guess + ",");
                gfield.clear();
            }

        }
    }



}