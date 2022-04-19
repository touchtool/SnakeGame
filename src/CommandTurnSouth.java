public class CommandTurnSouth extends Command{

    public CommandTurnSouth(Head head, int tick) {
        super(head, tick);
    }

    @Override
    public void execute() {
        getHead().turnSouth();
    }
}
