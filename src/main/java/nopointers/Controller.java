package nopointers;

import com.google.gson.Gson;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList; 

import javafx.scene.robot.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    //Text area to input a guess
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
    //Button for the required letter
    @FXML
    Button requiredLetter = new Button();
    //Button that generates a new puzzle
    @FXML
    Button newPuzzle = new Button();
    //Button that shuffles the letters
    @FXML
    Button shuffle = new Button();
    //Button to input a guess
    @FXML
    Button guess = new Button();
    //Shows the list of found words
    @FXML
    TextArea foundWords = new TextArea();

    @FXML
    Button saveButton = new Button();

    @FXML
    Button loadButton = new Button();
    //Button to delete a word
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
    //Button for custom input
    @FXML
    Button customButton = new Button();
    //Button that quits the game
    @FXML
    Button quit = new Button();
    //Button the user pressed to show the hints
    @FXML
    Button Screenshot = new Button();

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

    /**
     * Quits the game and presents the scores
     * list if the user's score is a high score
     *
     * @param e
     */
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


    /**
     * Generate a new puzzle and inputs a new high
     * score if user's previous game generated
     * a new high score
     *
     * @param e
     */
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

    /**
     * Deletes a letter from the text box
     *
     * @param e
     */
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

    /**
     * Loads a new puzzle
     *
     * @param e
     */
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

    /**
     * Shuffles the letters in the hive
     *
     * @param e
     */
    public void shuffle(ActionEvent e) {
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

        requiredLetter.setText(String.valueOf(word.charAt(6)));
        score.setText(String.valueOf(gameState.getScore()));
        input.clear();

    }

    /**
     * Saves a user's puzzle
     *
     * @param e
     */
    public void save(ActionEvent e)
    {
        gameState.savePuzzle();
    }

    /**
     * Allows the user to input a custom game
     *
     * @param e
     */
    public void CustomButton (ActionEvent e)
    {
        customButton.setVisible(!customButton.isVisible());
        customInput.setVisible(!customInput.isVisible());
    }



    /**
     * Function that evaluates a user's guess
     *
     * @param e
     */
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
            case INVALID_LETTER -> {
                error.setText("Invalid Letter");
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
     * A javafx function for presenting the hints
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

    @FXML
    private void fileChooser(ActionEvent e) throws IOException {
        Stage stage = (Stage) foundWords.getScene().getWindow();
        WritableImage image = stage.getScene().snapshot(null);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Screenshot");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("Screenshot saved to: " + file.getAbsolutePath());
            } catch (IOException error) {
                error.printStackTrace();
                System.out.println("Saving Error!");
            }
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
     * inputs the user's name into
     * the scores list
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