package nopointers;

public abstract class CommandFactory {
    // Creates the desired Command
    public static Command getCommand(CommandType type, Controller controller, GameState gameState) {
       if (CommandType.NewPuzzle == type) {
           return new NewPuzzleCommand(controller, gameState);
       }
       else {
           return null;
       }
    }

}
