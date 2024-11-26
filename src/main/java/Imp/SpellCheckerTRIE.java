package Imp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class TrieNode1 {
    Map<Character, TrieNode1> childNodesMap;
    boolean endOfWordFlag;

    public TrieNode1() {
        childNodesMap = new HashMap<>();
        endOfWordFlag = false;
    }
}

class Trie1 {
    private TrieNode1 rootNode;

    public Trie1() {
        rootNode = new TrieNode1();
    }

    public void insertWordIntoTrie(String wordToInsert) {
        TrieNode1 currentNode = rootNode;

        for (char character : wordToInsert.toLowerCase().toCharArray()) {
            currentNode.childNodesMap.putIfAbsent(character, new TrieNode1());
            currentNode = currentNode.childNodesMap.get(character);
        }

        currentNode.endOfWordFlag = true;
    }

    public boolean searchWordInTrie(String wordToSearch) {
        TrieNode1 currentNode = rootNode;

        for (char character : wordToSearch.toLowerCase().toCharArray()) {
            currentNode = currentNode.childNodesMap.get(character);
            if (currentNode == null) return false;
        }

        return currentNode.endOfWordFlag;
    }

    public TrieNode1 getRootNode() {
        return rootNode;
    }
}

public class SpellCheckerTRIE {
    private Trie1 trieStructure;

    public SpellCheckerTRIE(String filePath) {
        trieStructure = new Trie1();
        loadVocabularyFromCSVFile(filePath);
    }

    private void loadVocabularyFromCSVFile(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String lineFromFile;
            while ((lineFromFile = bufferedReader.readLine()) != null) {
                for (String singleWord : lineFromFile.trim().split("\\s+")) {
                    if (!singleWord.isEmpty()) {
                        trieStructure.insertWordIntoTrie(singleWord.trim().toLowerCase());
                    }
                }
            }
        } catch (IOException exception) {
            System.err.println("Error reading vocabulary file: " + exception.getMessage());
        }
    }

    public String checkAndSuggest(String wordToCheck, int maximumDistance) {
        // If the word exists in the trie, return it directly
        if (trieStructure.searchWordInTrie(wordToCheck)) {
            return wordToCheck;  // Return the word as it is already correct
        } else {
            List<String> suggestions = findSuggestionsForWord(wordToCheck, maximumDistance);

            if (!suggestions.isEmpty()) {
                // Ask the user for confirmation of the suggestion
                System.out.print("Do you mean " + suggestions + "? (yes/no): ");
                Scanner scanner = new Scanner(System.in);
                String confirmation = scanner.nextLine().toLowerCase();

                // Keep prompting until the user enters a valid input ("yes" or "no")
                while (!confirmation.equals("yes") && !confirmation.equals("no")) {
                    System.out.print("Please enter a valid input (yes/no): ");
                    confirmation = scanner.nextLine().toLowerCase();
                }

                // If the user confirms "yes", use the suggestion
                if (confirmation.equals("yes")) {
                    String completedWord = suggestions.get(0);  // Use the first suggestion for search
                    System.out.println("Using completed word: " + completedWord);
                    return completedWord;  // Return the corrected word
                } else {
                    System.out.println("No changes made.");
                    return wordToCheck;  // Return the original word if no change
                }
            } else {
                System.out.println("No suggestions found.");
                return wordToCheck;  // Return the original word if no suggestions
            }
        }
    }

    private int calculateEditDistance(String firstWord, String secondWord) {
        int[][] distanceMatrix = new int[firstWord.length() + 1][secondWord.length() + 1];

        for (int i = 0; i <= firstWord.length(); i++) {
            for (int j = 0; j <= secondWord.length(); j++) {
                if (i == 0) {
                    distanceMatrix[i][j] = j;
                } else if (j == 0) {
                    distanceMatrix[i][j] = i;
                } else if (firstWord.charAt(i - 1) == secondWord.charAt(j - 1)) {
                    distanceMatrix[i][j] = distanceMatrix[i - 1][j - 1];
                } else {
                    distanceMatrix[i][j] = 1 + Math.min(
                            distanceMatrix[i - 1][j],
                            Math.min(distanceMatrix[i][j - 1], distanceMatrix[i - 1][j - 1])
                    );
                }
            }
        }
        return distanceMatrix[firstWord.length()][secondWord.length()];
    }

    private List<String> findSuggestionsForWord(String wordToSuggest, int maximumDistanceAllowed) {
        List<String> suggestionList = new ArrayList<>();
        traverseTrieAndSuggest(trieStructure.getRootNode(), "", wordToSuggest, maximumDistanceAllowed, suggestionList);
        suggestionList.sort(Comparator.comparingInt(s -> calculateEditDistance(wordToSuggest, s)));  // Sort by closest match
        return suggestionList;
    }

    private void traverseTrieAndSuggest(TrieNode1 currentNode, String currentWord, String targetWord, int maxDistance, List<String> suggestions) {
        if (currentNode.endOfWordFlag && calculateEditDistance(currentWord, targetWord) <= maxDistance) {
            suggestions.add(currentWord);
        }
        for (Map.Entry<Character, TrieNode1> entry : currentNode.childNodesMap.entrySet()) {
            traverseTrieAndSuggest(entry.getValue(), currentWord + entry.getKey(), targetWord, maxDistance, suggestions);
        }
    }
}
