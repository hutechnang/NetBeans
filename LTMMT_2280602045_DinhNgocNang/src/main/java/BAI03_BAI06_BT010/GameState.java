package BAI03_BAI06_BT010;

import java.io.Serializable;
import java.awt.Rectangle;
import java.util.ArrayList;

// ================== Car ==================
class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public int x, y;
    public int colorId;
    public boolean isAlive = true;
    public boolean isFinished = false;

    public Car(int id, int x, int y, int colorId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.colorId = colorId;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 20, y, 40, 45);
    }
}

// ================== Obstacle ==================
class Obstacle implements Serializable {
    private static final long serialVersionUID = 1L;

    public int x, y, width, height;
    public int shapeType; // 1 = circle, 2 = trapezoid

    public Obstacle(int x, int y, int w, int h, int type) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.shapeType = type;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

// ================== GameState ==================
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public Car[] cars;
    public ArrayList<Obstacle> obstacles;
    public String status = "WAITING";

    public GameState() {
        obstacles = new ArrayList<>();
    }
}
