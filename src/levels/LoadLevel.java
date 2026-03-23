package levels;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class LoadLevel {

    public static int[][] loadLevelCSV(String fileName){
        int[][] data = null;
        try (InputStream is = LoadLevel.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

        // Đọc tất cả dòng trước
        java.util.List<int[]> rows = new java.util.ArrayList<>();

        String line;
        while((line = br.readLine()) != null){

            String[] tokens = line.split(",");

            int[] row = new int[tokens.length];

            for(int i = 0; i < tokens.length; i++){
                row[i] = Integer.parseInt(tokens[i].trim());
            }

            rows.add(row);
        }

        data = new int[rows.size()][];
        for(int i = 0; i < rows.size(); i++){
            data[i] = rows.get(i);
        }

    } catch (Exception e){
        System.out.println("Lỗi load CSV: " + fileName);
        e.printStackTrace();
    }

    return data;
}
}
