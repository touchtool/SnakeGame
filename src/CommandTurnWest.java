public class CommandTurnWest extends Command{

    public CommandTurnWest(Head head, int tick) {
        super(head, tick);
    }

    @Override
    public void execute() {
        getHead().turnWest();
    }
}
