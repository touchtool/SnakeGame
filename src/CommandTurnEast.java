public class CommandTurnEast extends Command{

    public CommandTurnEast(Head head, int tick) {
        super(head, tick);
    }

    @Override
    public void execute() {
        getHead().turnEast();
    }
}
