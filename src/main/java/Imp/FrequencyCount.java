package Imp;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FrequencyCount {

    public static void main(String[] args) {
        // Specify the folder containing the .txt files
        String folderPath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/text_pages"; // Replace with the actual folder path

        // Create a File object for the folder
        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            System.out.println("The specified path is not a directory.");
            return;
        }

        // Get a list of all .txt files in the folder
        File[] txtFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (txtFiles == null || txtFiles.length == 0) {
            System.out.println("No .txt files found in the specified folder.");
            return;
        }

        // Ask the user for the word to search
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the word to search: ");
        String wordToSearch = scanner.nextLine().toLowerCase();

        // A map to store the frequency count for each file
        Map<String, Integer> fileWordFrequency = new HashMap<>();

        // Process each .txt file
        for (File file : txtFiles) {
            try {
                // Read the content of the file
                List<String> lines = readFile(file);

                // Count the occurrences of the word in this file
                int wordCount = countWordOccurrences(lines, wordToSearch);

                // Store the result in the map
                fileWordFrequency.put(file.getName(), wordCount);

            } catch (IOException e) {
                System.out.println("Error reading file " + file.getName() + ": " + e.getMessage());
            }
        }

        // Print the frequency count for each file
        System.out.println("\nWord Frequency Count:");
        fileWordFrequency.forEach((fileName, count) -> {
            System.out.println(fileName + ":\n " + count + " occurrences of \"" + wordToSearch + "\"");
        });
    }

    // Method to read the content of a file and return it as a list of lines
    private static List<String> readFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    // Method to count occurrences of a word in a list of lines
    private static int countWordOccurrences(List<String> lines, String word) {
        int count = 0;
        for (String line : lines) {
            // Split the line into words and count matches
            String[] words = line.toLowerCase().split("\\W+"); // Split by non-word characters
            count += Arrays.stream(words)
                    .filter(w -> w.equals(word))
                    .count();
        }
        return count;
    }
}

