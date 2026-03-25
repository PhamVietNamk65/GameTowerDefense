package levels;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadLevel {

    // Load map từ file txt
    public static int[][] loadLevel(String path) {
        int[][] map = null;

        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            // Đọc dòng đầu: width height
            String firstLine = br.readLine();
            String[] size = firstLine.split(" ");

            int cols = Integer.parseInt(size[0]);
            int rows = Integer.parseInt(size[1]);

            map = new int[rows][cols];

            // Đọc từng dòng map
            for (int y = 0; y < rows; y++) {
                String line = br.readLine();
                String[] tokens = line.split(",");

                for (int x = 0; x < cols; x++) {
                    map[y][x] = Integer.parseInt(tokens[x]);
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}