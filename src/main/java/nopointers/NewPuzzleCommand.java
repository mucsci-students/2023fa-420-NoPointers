package nopointers;

public class NewPuzzleCommand extends Command {
    public NewPuzzleCommand(Controller controller, GameState gameState) {
        super(controller, gameState);
    }

    @Override
    public boolean execute() {
        backup();
        gameState.newRandomPuzzle();
        String word = new String(gameState.getLetters());
        controller.setButtons();
        controller.foundWords.clear();
        return true;
    }
}
