package nopointers;

public class CompletedState extends State{
    CompletedState(GameState gameState) {
        super(gameState);
        // Changes setDone to true as the puzzle is completed.
        gameState.setDone(true);
    }

    @Override
    public String onGuess() {
        return "You have already completed the Puzzle. Click the new puzzle button to start a new challenge.";
    }

}
