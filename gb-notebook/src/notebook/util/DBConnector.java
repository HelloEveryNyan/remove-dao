package notebook.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
    public static final String DB_PATH = "db.txt";

    public static void createDB() {
        try {
            File db = new File(DB_PATH);
            if (db.createNewFile()) {
                System.out.println("DB created");
            } else {
                System.out.println("DB already exists");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static List<String> readFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DB_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void saveFile(List<String> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_PATH))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
