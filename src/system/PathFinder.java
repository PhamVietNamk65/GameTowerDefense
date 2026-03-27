package system;

import java.awt.Point;
import java.util.ArrayList;

import utils.Constants;

public class PathFinder {

    public static Point[] buildPath(int[][] map) {

        ArrayList<Point> path = new ArrayList<>();

        int rows = map.length;
        int cols = map[0].length;

        // tìm điểm bắt đầu (tile đầu tiên = 1)
        Point start = findStart(map);

        if (start == null) return new Point[0];

        int x = start.x;
        int y = start.y;

        boolean[][] visited = new boolean[rows][cols];

        while (true) {

            path.add(new Point(x * Constants.Tiles.TILE_SIZE * 2 , y * Constants.Tiles.TILE_SIZE * 2));
            visited[y][x] = true;

            // tìm hướng tiếp theo
            if (isValid(map, visited, x + 1, y)) x++;
            else if (isValid(map, visited, x - 1, y)) x--;
            else if (isValid(map, visited, x, y + 1)) y++;
            else if (isValid(map, visited, x, y - 1)) y--;
            else break;
        }

        return path.toArray(new Point[0]);
    }

    private static Point findStart(int[][] map) {
        for (int x = 0; x < map[0].length; x++) {
            for (int y = 0; y < map.length; y++) {
                if (map[y][x] == 1) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static boolean isValid(int[][] map, boolean[][] visited, int x, int y) {
        return y >= 0 && y < map.length &&
               x >= 0 && x < map[0].length &&
               map[y][x] == 1 &&
               !visited[y][x];
    }
}