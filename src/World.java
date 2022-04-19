import java.util.Observable;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import javax.swing.plaf.TableUI;

public class World extends Observable {

    private int tick;
    private int size;

    private Player player;

    private int tailsCount = 2;
    private List<Tails> tails;
    private List<Tails> tailsStart;

    private Thread thread;
    private boolean notOver;
    private long delayed = 500;
    private int enemyCount = 10;
    private Enemy [] enemies;
    private Enemy [] enemiesStart;

    public World(int size) {
        this.size = size;
        tick = 0;
        player = new Player(size/2, size/2);
        tails = new ArrayList<Tails>();
        tailsStart = new ArrayList<Tails>();
        tails.add(new Tails(player.getX(), player.getY()+1));
        tailsStart.add(new Tails(player.getX(), player.getY()+1));
        for(int i = 1; i < tailsCount; i++) {
            int x = tails.get(i-1).getX();
            int y = tails.get(i-1).getY() + 1;
            tails.add(new Tails(x, y));
            tailsStart.add(new Tails(x, y));
        }

        enemies = new Enemy[enemyCount];
        enemiesStart = new Enemy[enemyCount];
        Random random = new Random();
        for(int i = 0; i < enemies.length; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            enemies[i] = new Enemy(x, y);
            enemiesStart[i] = new Enemy(x, y);
        }
        // enemies[enemies.length] = new Enemy((size/2), (size/2)+2);
    }

    public void start() {
        player.reset();
        tails.removeAll(tails);
        player.turnNorth();
        player.setPosition(size/2, size/2);
        tails.add(new Tails(player.getX(), player.getY()+1));
        for(int i = 0; i < tails.size()-1; i++) {
            tails.get(i).setPosition(tailsStart.get(i).getX(), tailsStart.get(i).getY());
        }
        for(int i = 0; i < enemies.length; i++) {
            enemies[i].setPosition(enemiesStart[i].getX(), enemiesStart[i].getY());
        }
        tick = 0;
        notOver = true;
        thread = new Thread() {
            @Override
            public void run() {
                while(notOver) {
                    tick++;
                    int x = player.getX();
                    int y = player.getY();
                    player.move();
                    for(int i = tails.size()-1; i > 0; i--) {
                        tails.get(i).setPosition(tails.get(i-1).getX(), tails.get(i-1).getY());
                    }
                    tails.get(0).setPosition(x, y);
                    checkCollisions();
                    checkEat();
                    setChanged();
                    notifyObservers();
                    waitFor(delayed);
                }
            }
        };
        thread.start();
    }

    private void checkCollisions() {
        for(Tails t : tails) {
            if(player.hit(t)) {
                notOver = false;
            }
        }
    }

    private void checkEat() {
        for(Enemy e : enemies) {
            if(player.hit(e)) {
                int x = tails.get(tails.size()-1).getX();
                int y = tails.get(tails.size()-1).getY();
                if (player.getdX() != 0) {
                    x = x + player.getdX()*(-1);
                }else if (player.getdY() != 0) {
                    y = y + player.getdY()*(-1);
                }
                tails.add(new Tails(x, y));
            }
        }
    }

    private void waitFor(long delayed) {
        try {
            Thread.sleep(delayed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getTick() {
        return tick;
    }

    public int getSize() {
        return size;
    }

    public Player getPlayer() {
        return player;
    }

    public void turnPlayerNorth() {
        player.turnNorth();
    }

    public void turnPlayerSouth() {
        player.turnSouth();
    }

    public void turnPlayerWest() {
        player.turnWest();
    }

    public void turnPlayerEast() {
        player.turnEast();
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public List<Tails> getTails() {
        return tails;
    }

    public boolean isGameOver() {
        return !notOver;
    }
}
