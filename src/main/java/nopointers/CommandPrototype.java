package nopointers;

public class CommandPrototype extends Prototype{


    public CommandPrototype() {

    }
    public CommandPrototype(CommandPrototype target) {
        super(target);

    }

    @Override
    public Prototype clone() {
        return new CommandPrototype(this);
    }
}
