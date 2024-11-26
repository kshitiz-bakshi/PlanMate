package Imp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PageRank {

    private static final double DAMPING_FACTOR = 0.85;
    private static final int MAX_ITERATIONS = 100;
    private static final double TOLERANCE = 0.0001;
    private static final int MAX_PAGES = 40;

    // Map to store pages and their respective outbound links
    private Map<String, Set<String>> pages = new HashMap<>();
    private Map<String, Double> pageRank = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for folder path
//        System.out.print("Enter the folder path containing .txt files: ");
        String folderPath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/text_pages";

        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.out.println("The provided path is not a folder.");
            return;
        }

        // Initialize PageRank
        PageRank pageRanker = new PageRank();
        pageRanker.loadPagesFromFolder(folder);

        // Calculate PageRank
        pageRanker.calculatePageRank();

        // Print the final PageRank of each page
        pageRanker.printPageRanks();

        scanner.close();
    }

    // Load pages and links from txt files in the folder
    private void loadPagesFromFolder(File folder) throws IOException {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                loadPageLinks(file);
            }
        }
    }

    // Load a single page's outbound links
    private void loadPageLinks(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        String pageName = file.getName();
        Set<String> links = new HashSet<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                links.add(line); // Add link (assumed to be the name of the page being linked to)
            }
        }

        // Store the page and its links
        pages.put(pageName, links);
        pageRank.put(pageName, 1.0); // Initialize page rank

        scanner.close();
    }

    // Calculate PageRank using the iterative method
    private void calculatePageRank() {
        int numPages = pages.size();
        double dampingTerm = (1.0 - DAMPING_FACTOR) / numPages;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Map<String, Double> newPageRank = new HashMap<>();
            double maxChange = 0;

            for (String page : pages.keySet()) {
                double rankSum = 0.0;

                // For each incoming link to the current page
                for (Map.Entry<String, Set<String>> entry : pages.entrySet()) {
                    String sourcePage = entry.getKey();
                    Set<String> links = entry.getValue();

                    if (links.contains(page)) {
                        rankSum += pageRank.get(sourcePage) / links.size();
                    }
                }

                // Calculate the new PageRank for this page
                double newRank = dampingTerm + DAMPING_FACTOR * rankSum;
                newPageRank.put(page, newRank);

                // Track the maximum change in rank to check for convergence
                maxChange = Math.max(maxChange, Math.abs(newRank - pageRank.get(page)));
            }

            // Update the page rank with new values
            pageRank.putAll(newPageRank);

            // If the change is small enough, stop the iteration
            if (maxChange < TOLERANCE) {
                break;
            }
        }
    }

    // Print the final PageRank values
    private void printPageRanks() {
        System.out.println("PageRank results:");
        List<Map.Entry<String, Double>> pageRankList = new ArrayList<>(pageRank.entrySet());

        // Sort the pages by their PageRank in descending order
        pageRankList.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // Print each page's name and its PageRank score
        for (Map.Entry<String, Double> entry : pageRankList) {
            int score = (int) Math.round(entry.getValue()); // Convert to integer score
            System.out.println("PageRanking [pageName=" + entry.getKey() + ", score=" + score + "]");
        }
    }
}
