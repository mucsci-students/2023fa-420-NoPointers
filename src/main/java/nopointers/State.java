package nopointers;

public abstract class State {
    GameState gameState;
    // State constructor
    State(GameState gameState) {
        this.gameState = gameState;
    }

    public abstract String onGuess();
}
