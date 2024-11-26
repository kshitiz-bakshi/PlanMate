package Imp;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingUtility {

    private static final String LOG_FILE = "session_log.txt";

    public static void log(String taskDescription, String output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("Timestamp: " + timestamp);
            writer.newLine();
            writer.write("Task: " + taskDescription);
            writer.newLine();
            writer.write("Output: " + output);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}
