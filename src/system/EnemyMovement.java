package system;

import entity.Monster;
import entity.EnemyState;

import java.awt.Point;

import static utils.Constants.Monsters.*;

public class EnemyMovement {

    private Point[] path;

    public EnemyMovement(Point[] path) {
        this.path = path;
    }

    public void move(Monster m) {

        if (m.getState() == EnemyState.DEATH)
            return;

        if (m.getPathIndex() >= path.length) {
            m.reachEnd();
            return;
        }

        float targetX = path[m.getPathIndex()].x + m.getxOffset();
        float targetY = path[m.getPathIndex()].y + m.getyOffset();

        float dx = targetX - m.getX();
        float dy = targetY - m.getY();

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float speed = GetSpeed(m.getEnemyType());

        if (distance <= speed) {
            m.setPos(targetX, targetY);
            m.nextPath();
            return;
        }

        float moveX = (dx / distance) * speed;
        float moveY = (dy / distance) * speed;

        m.updateDirection(moveX, moveY);

        m.setPos(m.getX() + moveX,m.getY() + moveY);
}

    public Point getStartPoint() {
        return path[0];
    }
}