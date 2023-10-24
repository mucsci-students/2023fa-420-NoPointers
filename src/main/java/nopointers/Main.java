package nopointers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        // New Stuff
        GameStateModel model = new GameStateModel();
        CLIView cliView = new CLIView();
        CLIController cliController = new CLIController(model, cliView);

    }

    public static void commandLine()
    {
        CLIController c = new CLIController();
        Platform.exit();
    }
}