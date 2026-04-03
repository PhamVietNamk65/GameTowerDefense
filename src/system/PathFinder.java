package system;

import java.awt.Point;
import java.util.ArrayList;
import utils.Constants;

public class PathFinder {

    private static final int[] dx = {1, -1, 0, 0};
    private static final int[] dy = {0, 0, 1, -1};

    public static Point[] buildPath(int[][] map) {

        int rows = map.length;
        int cols = map[0].length;

        boolean[][] visited = new boolean[rows][cols];

        Point start = findStart(map);
        if (start == null) return new Point[0];

        ArrayList<Point> path = new ArrayList<>();

        Point current = start;
        visited[current.y][current.x] = true;

        int T = Constants.Tiles.TILE_SIZE;

        while (true) {

            // add vào path (convert sang pixel)
            path.add(new Point(current.x * T, current.y * T));

            Point next = null;

            // tìm ô tiếp theo (chỉ có 1 đường đúng)
            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

                if (isValid(map, visited, nx, ny)) {
                    next = new Point(nx, ny);
                    break;
                }
            }

            // không còn đường → kết thúc
            if (next == null) break;

            current = next;
            visited[current.y][current.x] = true;
        }

        return path.toArray(new Point[0]);
    }

    private static boolean isValid(int[][] map, boolean[][] visited, int x, int y) {
        return y >= 0 && y < map.length &&
               x >= 0 && x < map[0].length &&
               map[y][x] == 1 &&
               !visited[y][x];
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
}