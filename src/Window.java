import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Window extends JFrame implements Observer {

    private int size = 500;
    private World world;
    private Renderer renderer;
    private Gui gui;

    List<Command> replays = new ArrayList<Command>();

    public Window() {
        super();
        addKeyListener(new Controller());
        setLayout(new BorderLayout());
        renderer = new Renderer();
        add(renderer, BorderLayout.CENTER);
        gui = new Gui();
        add(gui, BorderLayout.SOUTH);
        world = new World(25);
        world.addObserver(this);
        setSize(size, size);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void update(Observable o, Object arg) {
        renderer.repaint();
        gui.updateTick(world.getTick());

        for (Command c: replays){
            if (c.getTick() == world.getTick()){
                c.execute();
            }
        }
        if(world.isGameOver()) {
            gui.showGameOverLabel();
            gui.enableReplayButton();
        }
    }

    class Renderer extends JPanel {

        public Renderer() {
            setDoubleBuffered(true);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintGrids(g);
            paintHead(g);
            paintFoods(g);
            paintTails(g);
        }

        private void paintGrids(Graphics g) {
            // Background
            g.setColor(Color.black);
            g.fillRect(0, 0, size, size);

            // Lines
            // g.setColor(Color.gray);
            // int perCell = size/world.getSize();
            // for(int i = 0; i < world.getSize(); i++) {
            //     g.drawLine(i * perCell, 0, i * perCell, size);
            //     g.drawLine(0, i * perCell, size, i * perCell);
            // }
        }

        private void paintHead(Graphics g) {
            int perCell = size/world.getSize();
            int x = world.getHead().getX();
            int y = world.getHead().getY();
            g.setColor(Color.green);
            g.fillRect(x * perCell,y * perCell,perCell, perCell);
        }

        private void paintFoods(Graphics g) {
            int perCell = size/world.getSize();
            g.setColor(Color.red);
            for(Food e : world.getFoods()) {
                int x = e.getX();
                int y = e.getY();
                g.fillRect(x * perCell,y * perCell,perCell, perCell);
            }
        }

        private void paintTails(Graphics g) {
            int perCell = size/world.getSize();
            g.setColor(Color.green);
            for(Tails t : world.getTails()) {
                int x = t.getX();
                int y = t.getY();
                g.fillRect(x * perCell,y * perCell,perCell, perCell);
            }
        }
    }

    class Gui extends JPanel {

        private JLabel tickLabel;
        private JButton startButton;
        private JButton replayButton;
        private JLabel gameOverLabel;

        public Gui() {
            setLayout(new FlowLayout());
            tickLabel = new JLabel("Tick: 0");
            add(tickLabel);
            startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    world.start();
                    startButton.setEnabled(false);
                    Window.this.requestFocus();
                }
            });
            add(startButton);
            replayButton = new JButton("Replay");
            replayButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    world.start();
                    replayButton.setEnabled(false);
                    Window.this.requestFocus();
                }
            });
            replayButton.setEnabled(false);
            add(replayButton);
            gameOverLabel = new JLabel("GAME OVER");
            gameOverLabel.setForeground(Color.red);
            gameOverLabel.setVisible(false);
            add(gameOverLabel);
        }

        public void updateTick(int tick) {
            tickLabel.setText("Tick: " + tick);
        }

        public void showGameOverLabel() {
            gameOverLabel.setVisible(true);
        }

        public void enableReplayButton() {
            replayButton.setEnabled(true);
        }
    }

    class Controller extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                Command c = new CommandTurnNorth(world.getHead(), world.getTick());
                c.execute();
                replays.add(c);
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                Command c = new CommandTurnSouth(world.getHead(), world.getTick());
                c.execute();
                replays.add(c);
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                Command c = new CommandTurnWest(world.getHead(), world.getTick());
                c.execute();
                replays.add(c);
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                Command c = new CommandTurnEast(world.getHead(), world.getTick());
                c.execute();
                replays.add(c);
            } else if(e.getKeyCode() == KeyEvent.VK_Z){
                Command c = new CommandTeleport(world.getHead(), world.getTick(), world.getSize());
                c.execute();
                replays.add(c);
            }
        }
    }

    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
    }

}
