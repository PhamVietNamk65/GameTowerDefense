package system;

import java.awt.Point;

import entity.*;
import utils.Constants;

public class EnemySpawner {

    private Point[] levelPath;

    public EnemySpawner(Point[] levelPath) {
        this.levelPath = levelPath;
    }

    public Monster spawn(int type) {

        int x = levelPath[0].x;
        int y = levelPath[0].y;

        return switch (type) {
            case Constants.Monsters.ORC -> new Orc(x, y, 0);
            case Constants.Monsters.BEE -> new Bee(x, y, 0);
            case Constants.Monsters.SLIME -> new Slime(x, y, 0);
            case Constants.Monsters.WOLF -> new Wolf(x, y, 0);
            default -> null;
        };
    }
}