package nopointers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class Controller {
    @FXML
    Button b;
    Scanner scanner;
    private Puzzle puzzle;

    public void NewPuzzle(ActionEvent e)
    {
        b.setText("Generatinsg");
        Puzzle p = new Puzzle();

    }
   public void newPuzzle(ActionEvent e) {

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