package nopointers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Word-Wizard.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setFullScreen(true);
        stage.setTitle("Word Wizards");
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}