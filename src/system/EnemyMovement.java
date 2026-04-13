package system;

import entity.monster.EnemyState;
import entity.monster.Monster;
import entity.trap.Wall;
import java.awt.Point;
import levels.Level;
import utils.Constants;
import static utils.Constants.Monsters.*;

public class EnemyMovement {

    private Point[] path;

    public EnemyMovement(Point[] path) {
        this.path = path;
    }

public void move(Monster m, Level level) {
    if (m.getPathIndex() >= path.length) {
        m.reachEnd();
        return;
    }

    if (m.getState() == EnemyState.DYING) return;

    int tileSize = Constants.Tiles.TILE_SIZE;
    float speed = GetSpeed(m.getEnemyType());

    Point targetTile = path[m.getPathIndex()];
    float finalTargetX = targetTile.x;
    float finalTargetY = targetTile.y;

    int tileX = targetTile.x / tileSize;
    int tileY = targetTile.y / tileSize;

    boolean isWallAhead = (m.getEnemyType() != BEE && level.hasWall(tileX, tileY));

    if (isWallAhead) {

        Point prevTile = path[Math.max(0, m.getPathIndex() - 1)];
        int dirX = targetTile.x - prevTile.x;
        int dirY = targetTile.y - prevTile.y;

        if (dirX > 0) finalTargetX = targetTile.x - tileSize;  
        else if (dirX < 0) finalTargetX = targetTile.x + tileSize; 
        else if (dirY < 0) finalTargetY = targetTile.y + tileSize;
        else if (dirY > 0) finalTargetY = targetTile.y - tileSize + 20;
    }

    float dx = finalTargetX - m.getX();
    float dy = finalTargetY - m.getY();
    float distance = (float) Math.sqrt(dx * dx + dy * dy);

    if (distance > speed) {
        float moveX = (dx / distance) * speed;
        float moveY = (dy / distance) * speed;
        m.setPos(m.getX() + moveX, m.getY() + moveY);
        
        updateDirection(m, dx, dy);
    } else {
        m.setPos(finalTargetX, finalTargetY);

        if (isWallAhead) {
            if (m.getState() != EnemyState.ATTACK) {
                m.setTargetWall(level.getWallAt(tileX, tileY));
                m.setState(EnemyState.ATTACK);
            }
        } else {
            m.nextPath();
        }
    }
}

private void updateDirection(Monster m, float dx, float dy) {
    if (Math.abs(dx) > Math.abs(dy)) {
        m.setDirectionInt(dx > 0 ? Constants.Direction.RIGHT : Constants.Direction.LEFT);
    } else if (Math.abs(dy) > 0) {
        m.setDirectionInt(dy > 0 ? Constants.Direction.DOWN : Constants.Direction.UP);
    }
}

    public Point getStartPoint() {
        return path[0];
    }
}