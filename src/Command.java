public abstract class Command {
    private Head head;
    private int tick;

    public Command(Head head, int tick) {
        this.head = head;
        this.tick = tick;
    }

    public abstract void execute();

    public Head getHead() {
        return head;
    }

    public int getTick() {
        return tick;
    }

}