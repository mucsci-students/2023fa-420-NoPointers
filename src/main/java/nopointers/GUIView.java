package nopointers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class GUIView {
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
    Button quit = new Button();

    @FXML
    private void handleButtonClick(ActionEvent event) {
        // Get the text from the button that was clicked
        String buttonText = ((Button) event.getSource()).getText();

        // Update the label with the button's text
        input.setText(input.getText() + buttonText);
    }

    public void setErrorText (String input) {
        error.setText(input);
    }


}
