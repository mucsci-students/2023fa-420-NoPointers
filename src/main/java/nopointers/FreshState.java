package nopointers;

public class FreshState extends State{
    FreshState(GameState gameState) {
        super(gameState);
    }

    @Override
    public String onGuess() {
        // Changes state to CompletedState as all words have been found.
        gameState.changeState(new CompletedState(gameState));
        return "Congratulations. You have found all of the words.";

    }
}
