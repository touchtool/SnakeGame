
public class CommandTeleport extends Command{

    private int size;

    public CommandTeleport(Head head, int tick, int size) {
        super(head, tick);
        this.size  = size;
    }

    @Override
    public void execute() {
        getHead().reset();
        getHead().setPosition(this.size/2, this.size/2);
    }
}
