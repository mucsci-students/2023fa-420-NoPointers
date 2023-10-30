package nopointers;

public abstract class Command {
    public Controller controller;
    public GameState gameState;
    //public Puzzle puzzle;
    private Puzzle.Memento memento;

    Command (Controller controller, GameState gameState) {
        this.controller = controller;
        this.gameState = gameState;

    }

    void backup() {

        memento = gameState.getMemento();
    }

    public void undo() {
        gameState.restoreFromMemento(memento);
    }
    public abstract boolean execute();
}
