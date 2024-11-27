package Imp;

import java.io.*;
import java.util.*;

public class FileSearchRanker {

    // Hardcoded directory path
    private static final String DIRECTORY_PATH = "/Users/kshitiz/Documents/ACC ProjectF/untitled/text_pages";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word to search:");
        String searchWord = scanner.nextLine().trim().toLowerCase();

        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory path: " + DIRECTORY_PATH);
            return;
        }

        Map<String, Integer> fileWordFrequency = new HashMap<>();

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                int frequency = countWordFrequency(file, searchWord);
                fileWordFrequency.put(file.getName(), frequency);
            }
        }

        // Sort files by frequency in descending order
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(fileWordFrequency.entrySet());
        sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Print results
        System.out.println("\nFiles ranked by frequency of the word \"" + searchWord + "\":");
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " occurrences");
        }
    }

    private static int countWordFrequency(File file, String word) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\W+");
                for (String w : words) {
                    if (w.equals(word)) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getName());
            e.printStackTrace();
        }

        return count;
    }
}