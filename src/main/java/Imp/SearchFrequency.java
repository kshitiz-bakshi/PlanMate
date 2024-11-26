package Imp;

import java.util.*;

public class SearchFrequency {

    public static void main(String[] args) {
        // HashMap to keep track of search frequency of words
        Map<String, Integer> searchFrequency = new HashMap<>();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Ask the user for the word to search
            System.out.print("Enter the word to search (or type 'exit' to quit): ");
            String wordToSearch = scanner.nextLine().toLowerCase();

            if (wordToSearch.equals("exit")) {
                System.out.println("Exiting the Search Frequency Feature.");
                break;
            }

            // Update search frequency for the word
            searchFrequency.put(wordToSearch, searchFrequency.getOrDefault(wordToSearch, 0) + 1);

            // Print the search frequency map
            System.out.println("\nSearch Frequency:");
            searchFrequency.forEach((word, frequency) -> {
                System.out.println(word + ": searched " + frequency + " times");
            });

            // Print top 5 searches
            System.out.println("\nTop 5 Searches:");
            searchFrequency.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .limit(5)
                    .forEach(entry -> System.out.println(entry.getKey() + ": searched " + entry.getValue() + " times"));
        }
    }
}
