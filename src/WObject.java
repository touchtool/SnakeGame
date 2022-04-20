public abstract class WObject {

    private int x;
    private int y;

    private int dx;
    private int dy;
    // Random random = new Random();
    private boolean isNorth=true;
    private boolean isSouth=false;
    private boolean isWest=true;
    private boolean isEast=true;

    public WObject() {
    }

    public WObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void turnNorth() {
        dx = 0;
        dy = -1;
        isEast = true;
        isWest = true;
        isSouth = false;
        isNorth = true;
    }

    public void turnSouth() {
        dx = 0;
        dy = 1;
        isEast = true;
        isWest = true;
        isSouth = true;
        isNorth = false;
    }

    public void turnWest() {
        dx = -1;
        dy = 0;
        isEast = false;
        isWest = true;
        isSouth = true;
        isNorth = true;
    }

    public void turnEast() {
        dx = 1;
        dy = 0;
        isEast = true;
        isWest = false;
        isSouth = true;
        isNorth = true;
    }

    public void move() {
        this.x += dx;
        this.y += dy;
    }

    public void generateFood(int disX, int disY, int tick) {
        if (tick % 2 == 0) {
            this.x = disX + 10;
            this.y = disY + 10;
        } else if (tick % 3 == 0){
            this.x = disX + 20;
            this.y = disY + 20;
        } else if (tick % 5 == 0){
            this.x = disX + 30;
            this.y = disY + 30;
        } else if (tick % 7 == 0){
            this.x = disX + 40;
            this.y = disY + 40;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getdX() {
        return dx;
    }

    public int getdY() {
        return dy;
    }

    public void reset() {
        dx = dy = 0;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isNorth() {
        return isNorth;
    }

    public boolean isSouth() {
        return isSouth;
    }

    public boolean isWest() {
        return isWest;
    }

    public boolean isEast() {
        return isEast;
    }

    public boolean hit(WObject wObj) {
        return x == wObj.x && y == wObj.y;
    }
}
