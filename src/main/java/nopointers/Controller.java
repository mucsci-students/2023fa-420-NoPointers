package nopointers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;


public class Controller {
    public Puzzle puzzle;

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

    @FXML
    Button guess = new Button();

    @FXML
    TextArea foundWords = new TextArea();

    @FXML
    Button saveButton = new Button();

    @FXML
    Button loadButton = new Button();
    @FXML
    Button deleteButton = new Button();

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

    public void delete(ActionEvent e)
    {
        if(input.getLength() > 0)
        {
            input.setText(input.getText().substring(0,input.getLength() - 1));
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
            input.clear();
        }
    }

    public void load(ActionEvent e)
    {
        String home = System.getProperty("user.home");
        Path path = Paths.get(home).resolve("puzzle.json");
        try {
            Gson gson = new Gson();
            String json = new String(Files.readAllBytes(path));
            puzzle = gson.fromJson(json, Puzzle.class);
            setButtons();
        } catch (IOException err) {
            System.out.println("No Save Found");
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

    public void save(ActionEvent e)
    {
        if(puzzle != null)
        {
         String s = new String(puzzle.toGSONObject());
         String home = System.getProperty("user.home");

         System.out.println(s);
            try {
                Files.write(Paths.get(home).resolve("puzzle.json"), s.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException error) {
                throw new RuntimeException(error);
            }
        }
    }

    public void guess(ActionEvent e)
    {
        if(puzzle != null)
        {
            if(puzzle.guessWord(input.getText()))
            {
                foundWords.insertText(0,input.getText() + "\n");

                input.clear();
            }

        }
    }



}