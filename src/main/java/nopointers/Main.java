package nopointers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Puzzle puzzle = new Puzzle();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GUI2.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Word Wizards");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        if(args != null) {
            if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
                commandLine();
            } else {
                launch(args);
            }
        }

    }

    public static void commandLine()
    {
        CLI c = new CLI();

        Platform.exit();
    }
}