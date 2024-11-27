package Imp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JsonSearch {

    public static void main(String[] args) {
        String jsonFilePath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/outputF.json";
        String vocabularyFilePath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/final.csv";

        // Initialize Word Completion and Spell Checker
        WordCompletion wordCompletion = new WordCompletion();
        SpellCheckerTRIE spellChecker = new SpellCheckerTRIE(vocabularyFilePath);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the word to search (or type 'exit' to quit): ");
            String searchInput = scanner.nextLine().toLowerCase(); // Convert input to lowercase

            if (searchInput.equals("exit")) {
                break;
            }

            // Perform word completion using WordCompletion
            String completedWord = wordCompletion.complete(searchInput);

            if (completedWord != null) {
                System.out.println("Do you mean " + completedWord + "? (yes/no)");
                String confirmation = scanner.nextLine().toLowerCase();

                if (confirmation.equals("yes")) {
                    searchInput = completedWord.toLowerCase(); // Use the completed word for the search
//                    System.out.println("Using completed word: " + searchInput);
                } else {
                    // User said no to the completed word, skip further processing
                    System.out.println("Word not accepted.");
                    continue; // Skip the current iteration
                }
            } else {
                // completedWord is null, proceed directly to spell-check
                System.out.println("Checking word: " + searchInput);
                List<String> suggestions = Collections.singletonList(spellChecker.checkAndSuggest(searchInput, 2));

                searchInput = suggestions.get(0).toLowerCase(); // Use the first suggestion and convert to lowercase

                if (searchInput.equals("no22")) { // Check for specific unwanted output
                    System.out.println("Word not accepted.");
                    continue; // Skip the current iteration and move to the next input
                }

                System.out.println("Using corrected word: " + searchInput);
            }

            try {
                // Search the JSON file using the possibly corrected searchInput
                String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
                JSONArray jsonArray = new JSONArray(jsonContent);

                boolean found = false;
                System.out.println("\nPlans containing the word '" + searchInput + "':");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dataBlock = jsonArray.getJSONObject(i);

                    // Check if the word exists in the data block (case-insensitive)
                    boolean containsWord = containsWordInJson(dataBlock, searchInput);

                    if (containsWord) {
                        found = true;
                        displayJsonContent(dataBlock);
                        System.out.println("------------------------------------");
                    }
                }

                if (!found) {
                    System.out.println("No matching data blocks found.");
                }

            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

    }

    public static boolean containsWordInJson(JSONObject jsonObject, String searchWord) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            // Convert both search word and value to lowercase for case-insensitive comparison
            if (value instanceof String && ((String) value).toLowerCase().contains(searchWord)) {
                return true;
            }

            if (value instanceof JSONObject) {
                if (containsWordInJson((JSONObject) value, searchWord)) {
                    return true;
                }
            } else if (value instanceof JSONArray) {
                if (containsWordInJson((JSONArray) value, searchWord)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsWordInJson(JSONArray jsonArray, String searchWord) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);

            // Convert both search word and value to lowercase for case-insensitive comparison
            if (value instanceof String && ((String) value).toLowerCase().contains(searchWord)) {
                return true;
            }

            if (value instanceof JSONObject) {
                if (containsWordInJson((JSONObject) value, searchWord)) {
                    return true;
                }
            } else if (value instanceof JSONArray) {
                if (containsWordInJson((JSONArray) value, searchWord)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void displayJsonContent(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (key.equals("Image URL")) {
                continue;
            }

            if (value instanceof String) {
                System.out.println(key + ": " + value);
            } else if (value instanceof JSONObject) {
                System.out.println(key + ": ");
                displayJsonContent((JSONObject) value);
            } else if (value instanceof JSONArray) {
                System.out.println(key + ": ");
                displayJsonArray((JSONArray) value);
            } else {
                System.out.println(key + ": " + value);
            }
        }
    }

    public static void displayJsonArray(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);

            if (value instanceof JSONObject && ((JSONObject) value).has("Image URL")) {
                continue;
            }

            if (value instanceof String) {
                System.out.println(value);
            } else if (value instanceof JSONObject) {
                displayJsonContent((JSONObject) value);
            } else if (value instanceof JSONArray) {
                displayJsonArray((JSONArray) value);
            } else {
                System.out.println(value);
            }
        }
    }
}
