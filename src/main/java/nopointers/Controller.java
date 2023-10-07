package nopointers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;



import java.util.ArrayList;


public class Controller {
    private Puzzle puzzle;

    @FXML
    TextField input = new TextField();

    @FXML
    Button l0 = new Button();

    @FXML
    Button l1 = new Button();

    @FXML
    Button l2 = new Button();

    @FXML
    Button l3 = new Button();

    @FXML
    Button l4 = new Button();

    @FXML
    Button l5 = new Button();

    @FXML
    Button requiredLetter = new Button();

    @FXML
    Button newPuzzle = new Button();

    @FXML
    Button shuffle = new Button();
    public void NewPuzzle(ActionEvent e)
    {

        puzzle = new Puzzle();
        String word = new String(puzzle.getLetters());
        if(puzzle != null)
        {
          setButtons();
        }
    }


    @FXML
    private void handleButtonClick(ActionEvent event) {
        // Get the text from the button that was clicked
        String buttonText = ((Button) event.getSource()).getText();

        // Update the label with the button's text
        input.setText(input.getText() + buttonText);
    }


    public void save(ActionEvent e)
    {
        if(puzzle != null)
        {

        }
    }
    public void setButtons()
    {
        String word = new String(puzzle.getLetters());
        if(puzzle != null)
        {
            l0.setText(String.valueOf(word.charAt(0)));
            l1.setText(String.valueOf(word.charAt(1)));
            l2.setText(String.valueOf(word.charAt(2)));
            l3.setText(String.valueOf(word.charAt(3)));
            l4.setText(String.valueOf(word.charAt(4)));
            l5.setText(String.valueOf(word.charAt(5)));
            requiredLetter.setText(String.valueOf(word.charAt(6)));
        }
    }
    public void shuffle(ActionEvent e)
    {
        if(puzzle != null)
        {
            puzzle.shuffleLetters();
            setButtons();
        }
    }

    public void guess(ActionEvent e)
    {
        if(puzzle != null)
        {
            if(puzzle.guessWord(input.getText()))
            {
                System.out.println("W");
            }

        }
    }



}