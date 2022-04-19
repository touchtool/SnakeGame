public class CommandTurnNorth extends Command{

    public CommandTurnNorth(Head head, int tick) {
        super(head, tick);
    }

    @Override
    public void execute() {
        getHead().turnNorth();
    }
}
