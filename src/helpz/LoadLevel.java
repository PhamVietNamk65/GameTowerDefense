package helpz;
import java.io.*;
import java.util.*;

import levels.LevelData;

public class LoadLevel {

    public static LevelData LoadLevel(String level) {
        List<int[]> waveList = new ArrayList<>();
        int sDelay = 90; // Giá trị mặc định
        int wDelay = 300;

        try (BufferedReader br = new BufferedReader(new FileReader("res/Map/Level" + level + "/dataMonster.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; // Bỏ qua dòng trống hoặc comment

                // Nếu dòng bắt đầu bằng SET, ta đọc cấu hình delay
                if (line.startsWith("SET_DELAY")) {
                    sDelay = Integer.parseInt(line.split(":")[1].trim());
                    continue;
                }

                // Đọc dữ liệu wave
                String[] values = line.split(",");
                int[] wave = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    wave[i] = Integer.parseInt(values[i].trim());
                }
                waveList.add(wave);
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file map: " + e.getMessage());
            return null;
        }

        return new LevelData(waveList.toArray(new int[0][]), sDelay, wDelay);
    }
}