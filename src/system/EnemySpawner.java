package system;


import java.awt.Point;

import entity.monster.*;

import static utils.Constants.Monsters.*;

public class EnemySpawner {

    private Point[] path;

    public EnemySpawner(Point[] path) {
        this.path = path;
    }

    public Monster spawn(int type) {

        int x = path[0].x;
        int y = path[0].y;

        switch (type) {
            case ORC:
                return new Orc(x, y, 0);
            case BEE:
                return new Bee(x, y, 0);
            case SLIME:
                return new Slime(x, y, 0);
            case WOLF:
                return new Wolf(x, y, 0);
        }

        return null;
    }
}