package nopointers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class Controller {
    @FXML
    private Scanner scanner;
    private Puzzle puzzle;

    public void Begin(ActionEvent e)
    {
    System.out.println("Begin");
    }
   public void newPuzzle(ActionEvent e) {
        System.out.println("Generating New Puzzle...");
        time();
        System.out.println("\nNew Puzzle Generated!");
    }

    public void text()
    {
        Text text = new Text();
        text.setText("ABCDEF");


    }
    void time() {
        for (int i = 0; i < 100; ++i) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                System.out.print("\u001b[1000D");
                System.out.flush();
                TimeUnit.MILLISECONDS.sleep(1);
                System.out.print((i + 1) + "%");
                System.out.flush();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}