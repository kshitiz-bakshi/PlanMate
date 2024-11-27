package Imp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Node class representing each character in the Trie
class Auto {
    Map<Character, Auto> children = new HashMap<>();
    boolean isEndOfWord = false;
}

// Trie class for managing word insertion and prefix search
class Trie {
    private Auto root;

    // Initialization of the Trie
    public Trie() {
        root = new Auto();
    }

    // Insert a word into the Trie
    public void insert(String word) {
        Auto node = root;
        for (char c : word.toLowerCase().toCharArray()) {
            node.children.putIfAbsent(c, new Auto());
            node = node.children.get(c);
        }
        node.isEndOfWord = true;
    }

    // Find the node corresponding to the end of the prefix
    private Auto findNode(String prefix) {
        Auto node = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(c)) {
                return null;
            }
            node = node.children.get(c);
        }
        return node;
    }

    // Perform DFS to collect words
    private void dfs(Auto node, String prefix, List<String> words) {
        if (node.isEndOfWord) {
            words.add(prefix);
        }
        for (Map.Entry<Character, Auto> entry : node.children.entrySet()) {
            dfs(entry.getValue(), prefix + entry.getKey(), words);
        }
    }

    // Get all words that start with the given prefix
    public List<String> getWordsWithPrefix(String prefix) {
        List<String> words = new ArrayList<>();
        Auto prefixNode = findNode(prefix);
        if (prefixNode != null) {
            dfs(prefixNode, prefix.toLowerCase(), words);
        }
        return words;
    }
}

public class WordCompletion {

    private static Trie trie = new Trie(); // Shared Trie instance

    // Static block to load vocabulary when the class is loaded
    static {
        String filePath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/final.csv";  // Adjust the path to your CSV file
        String filePath1 = "/Users/kshitiz/Documents/ACC ProjectF/untitled/words.txt";
        loadVocabulary(filePath);  // Load the vocabulary into the Trie
        loadVocabulary(filePath1);
    }

    // Method to load vocabulary from a file into the Trie
    private static void loadVocabulary(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int wordCount = 0;  // Counter to keep track of how many words are loaded
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                // Split the line into individual words using commas or whitespace as delimiters
                for (String word : line.split("[,\\s]+")) {
                    word = word.trim().toLowerCase();  // Trim whitespace and convert to lowercase
                    if (!word.isEmpty()) {  // Only insert non-empty words
                        trie.insert(word);
                        wordCount++;  // Increment the count of words added
//                        System.out.printf("Inserted word #%d: %s%n", wordCount, word);  // Debug output with word count
                    }
                }
            }
//            System.out.println("Completed loading vocabulary. Total words loaded: " + wordCount);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Method to complete a word based on the given prefix
    public static String complete(String prefix) {
        List<String> words = trie.getWordsWithPrefix(prefix.toLowerCase());
        return words.isEmpty() ? null : words.get(0);  // Return the first match or null if no match found
    }
}
