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
import javafx.scene.image.WritableImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Controller {

    private GameState gameState;
    private CommandHistory history = new CommandHistory();

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

    @FXML
    Text score = new Text();

    @FXML
    TextArea help = new TextArea();

    @FXML
    Button custom = new Button();

    @FXML
    Label error = new Label();

    @FXML
    Text rank = new Text();

    @FXML
    TextField customInput = new TextField();

    @FXML
    Button customButton = new Button();

    @FXML
    Button quit = new Button();
    //Button the user pressed to show the hints
    @FXML
    Button hintsButton = new Button();
    //Text ares that presents the hint
    @FXML
    TextArea hintsBox = new TextArea();
    //Text area that presents the scores
    @FXML
    TextArea scoresBox = new TextArea();
    //Text field where user enters their name
    @FXML
    TextField enterName = new TextField();
    //Button the user presses to enter their name
    @FXML
    Button enter = new Button();

    Button g = new Button();
    Controller controller = this;


    public Controller() {
        //this.gameState = new GameState();
        GameState.GameStateBuilder builder = new GameState.GameStateBuilder(Database.getInstance());
        this.gameState = builder.build();
    }

    public void quit(ActionEvent e)
    {
        if(gameState.newScore()){
            scoresf(e);
        }
        else{
            System.exit(0);
        }
    }

    public void customPuzzle(ActionEvent e)
    {


        String s = customInput.getText().trim().toLowerCase();
        if (gameState.newUserPuzzle(s)) {
            setButtons();
            error.setVisible(false);
            customInput.clear();
        }
        else {
            error.setText("Not a valid custom puzzle!");
            error.setVisible(true);
        }

    }



    public void NewPuzzle(ActionEvent e)
    {
        // Create a new puzzle via the New button with a NewPuzzle command.
        String word = new String(gameState.getLetters());
        if(!l0.getText().equals(String.valueOf(word.charAt(0)))){
            executeCommand(new NewPuzzleCommand(this, gameState));
        }
        else if(scoresBox.isVisible()){
            scoresBox.setVisible(false);
        }
        else{
            scoresf(e);
            executeCommand(new NewPuzzleCommand(this, gameState));
        }




    }

    public void setHelp(ActionEvent e)
    {
        help.setText("Welcome To Word Wizards Press New to generate a puzzle or create your own with by pressing Custom!\n\n" +
                " In order to input your guess you can click the orbs or type your guess in the text box! \n\n" +
                "If you need a new perspective you can shuffle the puzzle as well." +
                "\n\n♦The Puzzle You must use the required word in the pangram at least once in your guess. \n\n" +
                "Your word guess must be greater than 4 letters. You can only use the letters in the generated puzzle. \n\n" +
                "Words longer than the minimum 4 letters will be awarded bonus points among other criteria. \n" +
                "Your guess MUST be a valid word to get points.");
        if(!help.isVisible())
        {
            help.setVisible(true);
        }
        else
        {
            help.setVisible(false);
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
        String word = new String(gameState.getLetters());
        if(gameState != null)
        {
            l0.setText(String.valueOf(word.charAt(0)));
            l1.setText(String.valueOf(word.charAt(1)));
            l2.setText(String.valueOf(word.charAt(2)));
            l3.setText(String.valueOf(word.charAt(3)));
            l4.setText(String.valueOf(word.charAt(4)));
            l5.setText(String.valueOf(word.charAt(5)));
            requiredLetter.setText(String.valueOf(word.charAt(6)));
            score.setText(String.valueOf(gameState.getScore()));
            input.clear();
            foundWords.clear();

        }
    }

    public void load(ActionEvent e)
    {
        if (gameState.loadPuzzle()) {
            setButtons();
            for(String s : gameState.guessed())
            {
                foundWords.insertText(0, s + "\n");
            }
        }
        else {
            error.setText("No puzzle to load.");
            error.setVisible(true);
        }
    }
    public void shuffle(ActionEvent e)
    {
        if (gameState == null)
            return;

        gameState.shuffle();

        String word = new String (gameState.getLetters());
        l0.setText(String.valueOf(word.charAt(0)));
        l1.setText(String.valueOf(word.charAt(1)));
        l2.setText(String.valueOf(word.charAt(2)));
        l3.setText(String.valueOf(word.charAt(3)));
        l4.setText(String.valueOf(word.charAt(4)));
        l5.setText(String.valueOf(word.charAt(5)));

        input.clear();
    }

    public void save(ActionEvent e)
    {
        gameState.savePuzzle();
    }

    public void CustomButton (ActionEvent e)
    {
        customButton.setVisible(!customButton.isVisible());
        customInput.setVisible(!customInput.isVisible());
    }

    public void ConfirmButton (ActionEvent e)
    {

    }

    public void guess(ActionEvent e)
    {

        
        String guess = input.getText();
        GuessOutcome outcome = gameState.guess(guess);

        switch (outcome) {
            case SUCCESS -> {
                foundWords.insertText(0,input.getText() + "\n");
                score.setText(String.valueOf(gameState.getScore()));
                input.clear();
                int currentRank = gameState.getRank();
                String[] arr = gameState.getRanks();
                rank.setText(arr[gameState.getRank()]);
            }
            case TOO_SHORT -> {
                error.setText("Guess is too short!");
            }
            case EMPTY_INPUT -> {
                error.setText("No puzzle to guess on! Please generate a puzzle.");
            }
            case INCORRECT -> {
                error.setText("Not a valid word.");
            }
            case ALREADY_FOUND -> {
                error.setText("Word already found!");
            }
            case MISSING_REQUIRED -> {
                error.setText("Incorrect. Does not use required letter: " + gameState.requiredLetter());
            }
            case PUZZLE_COMPLETED -> {
                // Uses the onGuess() function from either FreshState or Completed State.
                // When a user successfully enters the last word, FreshState's text will be returned.
                // Subsequent attempts to guess words will cause CompletedState's text to appear.
                error.setText(gameState.getState().onGuess());
            }
        }

    }
    // Executes Command. Pushes command onto command history stack.
    private void executeCommand (Command command) {
        if (command.execute()) {
            history.push(command);
        }
    }
    /**
     * A javafx function for
     *
     * @param e
     */
    public void hintsf(ActionEvent e){
        if(hintsBox.isVisible()){
            hintsBox.setVisible(false);
            return;
        }
        String res = gameState.hints();
        hintsBox.setText(gameState.hints());
        hintsBox.setVisible(true);
    }

    // Undo previous command. Reverts to previous state.
        private void undo() {
        if (history.isEmpty()) {
            return;
        }
        Command command = history.pop();
        if (command != null) {
            command.undo();
        }
    }

    /**
     * A javafx function for the textbox
     * that presents the scoresbox
     *
     * @param e
     */
    public void scoresf(ActionEvent e){
        if(scoresBox.isVisible()){
            scoresBox.setVisible(false);
        }
        enterName.setVisible(true);
        enter.setVisible(true);
    }

    /**
     * A javafx function that
     *
     *
     * @param e
     */
    public void enterf(ActionEvent e){
        enterName.setVisible(false);
        enter.setVisible(false);
        gameState.addScore(enterName.getText());
        enterName.setText("");
        String res = gameState.printScore();
        scoresBox.setText(res);
        scoresBox.setVisible(true);

    }
}