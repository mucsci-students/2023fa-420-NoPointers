package nopointers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.event.ActionListener;


public class GUIController implements ActionListener {

    private GameStateModel gameStateModel;
    private GUIView view;




    public GUIController() {

        this.gameStateModel = new GameStateModel();
        this.view = new GUIView();
    }

    public void quit(ActionEvent e)
    {
        System.exit(0);
    }

    public void customPuzzle(ActionEvent e)
    {


        String s = input.getText().trim().toLowerCase();
        if (gameStateModel.newUserPuzzle(s)) {
            setButtons();
            error.setVisible(false);
        }
        else {
            view.error.setText("Not a valid custom puzlle!");
            error.setVisible(true);
        }

    }


    public void NewPuzzle(ActionEvent e)
    {



        gameStateModel.newRandomPuzzle();
        String word = new String(gameStateModel.getLetters());
        setButtons();
        foundWords.clear();
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




    public void delete(ActionEvent e)
    {
        if(input.getLength() > 0)
        {
            input.setText(input.getText().substring(0,input.getLength() - 1));
        }
    }


    public void setButtons()
    {
        String word = new String(gameStateModel.getLetters());
        if(gameStateModel != null)
        {
            l0.setText(String.valueOf(word.charAt(0)));
            l1.setText(String.valueOf(word.charAt(1)));
            l2.setText(String.valueOf(word.charAt(2)));
            l3.setText(String.valueOf(word.charAt(3)));
            l4.setText(String.valueOf(word.charAt(4)));
            l5.setText(String.valueOf(word.charAt(5)));
            requiredLetter.setText(String.valueOf(word.charAt(6)));
            score.setText(String.valueOf(gameStateModel.getScore()));
            input.clear();
            foundWords.clear();

        }
    }

    public void load(ActionEvent e)
    {
        if (gameStateModel.loadPuzzle()) {
            setButtons();
            for(String s : gameStateModel.guessed())
            {
                foundWords.insertText(0, s + "\n");
            }
        }
        else {
            view.setErrorText("No puzzle to load.");
            error.setVisible(true);
        }
    }
    public void shuffle(ActionEvent e)
    {

        gameStateModel.shuffle();
        setButtons();
    }

    public void save(ActionEvent e)
    {
        gameStateModel.savePuzzle();
    }

    public void guess(ActionEvent e)
    {

        
        String guess = input.getText();
        GuessOutcome outcome = gameStateModel.guess(guess);

        switch (outcome) {
            case SUCCESS -> {
                foundWords.insertText(0,input.getText() + "\n");
                score.setText(String.valueOf(gameStateModel.getScore()));
                input.clear();
                int currentRank = gameStateModel.getRank();
                String[] arr = gameStateModel.getRanks();
                rank.setText(arr[gameStateModel.getRank()]);
            }
            case TOO_SHORT -> {
                view.setErrorText("Guess is too short!");
            }
            case EMPTY_INPUT -> {
                view.setErrorText("No puzzle to guess on! Please generate a puzzle.");
            }
            case INCORRECT -> {
                view.setErrorText("Not a valid word.");
            }
            case ALREADY_FOUND -> {
                view.setErrorText("Word already found!");
            }
            case MISSING_REQUIRED -> {
                view.setErrorText("Incorrect. Does not use required letter: " + gameStateModel.requiredLetter());
            }
        }

    }


    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        String command = e.getActionCommand();
        //if (command.equals("New")) {

        //}
        System.out.println(command);
    }
}