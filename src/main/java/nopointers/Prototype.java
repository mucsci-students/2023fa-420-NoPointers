package nopointers;

public abstract class Prototype {
    //public Prototype(Controller controller, GameState gameState) {
        //super(controller, gameState);
    //}
    public Controller x;
    public GameState y;
    private Puzzle.Memento z;

    public Prototype() {

    }

    public Prototype(Prototype target) {
        if (target != null) {
            this.x = target.x;
            this.y = target.y;
            this.z = target.z;
        }
    }

    public abstract Prototype clone();

}
