package nopointers;

public class NewPuzzleCommand extends Command implements Cloneable {
    public NewPuzzleCommand(Controller controller, GameState gameState) {
        super(controller, gameState);
    }

    @Override
    public boolean execute() {
        backup();
        try {
            gameState.newRandomPuzzle();
            String word = new String(gameState.getLetters());
            controller.setButtons();
            controller.foundWords.clear();
            return true;
        }
        catch (InterruptedException err) {
            System.out.println("Something went wrong, please try again.");
        }
        return false;
    }

    public Object clone()
}
