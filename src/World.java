import java.util.Observable;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import javax.swing.plaf.TableUI;

public class World extends Observable {

    private int tick;
    private int size;

    private Head head;

    private int tailsCount = 2;
    private List<Tails> tails;
    private List<Tails> tailsStart;

    private Thread thread;
    private boolean notOver;
    private long delayed = 200;
    private int foodCount = 10;
    private Food [] foods;
    private Food [] foodsStart;

    public World(int size) {
        this.size = size;
        tick = 0;
        head = new Head(size/2, size/2);
        tails = new ArrayList<Tails>();
        tailsStart = new ArrayList<Tails>();
        tails.add(new Tails(head.getX(), head.getY()+1));
        tailsStart.add(new Tails(head.getX(), head.getY()+1));
        for(int i = 1; i < tailsCount; i++) {
            int x = tails.get(i-1).getX();
            int y = tails.get(i-1).getY() + 1;
            tails.add(new Tails(x, y));
            tailsStart.add(new Tails(x, y));
        }

        foods = new Food[foodCount];
        foodsStart = new Food[foodCount];
        Random random = new Random();
        for(int i = 0; i < foods.length; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            foods[i] = new Food(x, y);
            foodsStart[i] = new Food(x, y);
        }
    }

    public void start() {
        head.reset();
        tails.removeAll(tails);
        head.setPosition(size/2, size/2);
        head.turnNorth();
        for(int i = 0; i < tailsStart.size(); i++) {
            tails.add(new Tails(tailsStart.get(i).getX(), tailsStart.get(i).getY()));
        }
        for(int i = 0; i < foods.length; i++) {
            foods[i].setPosition(foodsStart[i].getX(), foodsStart[i].getY());
        }
        tick = 0;
        notOver = true;
        thread = new Thread() {
            @Override
            public void run() {
                while(notOver) {
                    tick++;
                    int x = head.getX();
                    int y = head.getY();
                    head.move();
                    for(int i = tails.size()-1; i > 0; i--) {
                        tails.get(i).setPosition(tails.get(i-1).getX(), tails.get(i-1).getY());
                    }
                    tails.get(0).setPosition(x, y);
                    checkCollisions();
                    checkBorder();
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
            if(head.hit(t)) {
                notOver = false;
            }
        }
    }

    private void checkBorder() {
        if (head.getX() < 0){
            head.setPosition(size-1, head.getY());
        } else if (head.getX() > size-1) {
            head.setPosition(0, head.getY());
        } else if (head.getY() < 0) {
            head.setPosition(head.getX(), size-1);
        } else if (head.getY() > size-1) {
            head.setPosition(head.getX(), 0);
        }
    }

    private void checkEat() {
        for(Food e : foods) {
            if(head.hit(e)) {
                int x = tails.get(tails.size()-1).getX();
                int y = tails.get(tails.size()-1).getY();
                // if (head.getdX() != 0) {
                //     x = x + head.getdX()*(-1);
                // }else if (head.getdY() != 0) {
                //     y = y + head.getdY()*(-1);
                // }
                tails.add(new Tails(x, y));
                Random random = new Random();
                e.setPosition(random.nextInt(size), random.nextInt(size));
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

    public Head getHead() {
        return head;
    }

    public void turnHeadNorth() {
        head.turnNorth();
    }

    public void turnHeadSouth() {
        head.turnSouth();
    }

    public void turnHeadWest() {
        head.turnWest();
    }

    public void turnHeadEast() {
        head.turnEast();
    }

    public Food[] getFoods() {
        return foods;
    }

    public List<Tails> getTails() {
        return tails;
    }

    public boolean isGameOver() {
        return !notOver;
    }
}
