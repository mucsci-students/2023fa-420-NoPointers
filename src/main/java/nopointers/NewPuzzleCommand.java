package nopointers;

public class NewPuzzleCommand extends Command {
    public NewPuzzleCommand(Controller controller, GameState gameState) {
        super(controller, gameState);
    }

    @Override
    public boolean execute() {
        backup();
        try {
            gameState.newRandomPuzzle();
        }
        catch (InterruptedException e) {
            System.out.println ("Something went wrong. Please try again.");
        }
        String word = new String(gameState.getLetters());
        controller.setButtons();
        controller.foundWords.clear();
        return true;
    }
}
