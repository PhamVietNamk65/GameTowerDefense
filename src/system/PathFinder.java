package system;

import java.awt.Point;
import java.util.ArrayList;

import utils.Constants;

public class PathFinder {

    public static Point[] buildPath(int[][] map) {

        int rows = map.length;
        int cols = map[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Point[][] parent = new Point[rows][cols];

        Point start = findStart(map);
        Point end = findEnd(map);

        if (start == null || end == null) return new Point[0];

        java.util.Queue<Point> queue = new java.util.LinkedList<>();
        queue.add(start);
        visited[start.y][start.x] = true;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.equals(end)) break;

            for (int i = 0; i < 4; i++) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];

                if (isValid(map, visited, nx, ny)) {
                    visited[ny][nx] = true;
                    parent[ny][nx] = p;
                    queue.add(new Point(nx, ny));
                }
            }
        }

        // reconstruct path
        ArrayList<Point> path = new ArrayList<>();
        Point cur = end;

        while (cur != null) {
            path.add(0, new Point(
                cur.x * Constants.Tiles.TILE_SIZE,
                cur.y * Constants.Tiles.TILE_SIZE
            ));
            cur = parent[cur.y][cur.x];
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

    private static Point findEnd(int[][] map) {
        for (int x = map[0].length - 1; x >= 0; x--) {
            for (int y = map.length - 1; y >= 0; y--) {
                if (map[y][x] == 1) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
}