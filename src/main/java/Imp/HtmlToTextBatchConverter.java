package Imp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlToTextBatchConverter {

    // Hardcoded input and output folder paths
    private static final String INPUT_FOLDER_PATH = "/Users/kshitiz/Documents/ACC ProjectF/untitled/saved_pages";  // Replace with your HTML folder path
    private static final String OUTPUT_FOLDER_PATH = "/Users/kshitiz/Documents/ACC ProjectF/untitled/text_pages";  // Replace with your output folder path

    public static void main(String[] args) {
        HtmlToTextBatchConverter converter = new HtmlToTextBatchConverter();
        converter.convertHtmlFilesToText(INPUT_FOLDER_PATH, OUTPUT_FOLDER_PATH);
    }

    /**
     * Converts all HTML files in the specified folder to text files.
     *
     * @param inputFolderPath  Path of the folder containing HTML files.
     * @param outputFolderPath Path of the folder to save converted text files.
     */
    public void convertHtmlFilesToText(String inputFolderPath, String outputFolderPath) {
        File inputFolder = new File(inputFolderPath);
        File outputFolder = new File(outputFolderPath);

        // Validate input folder
        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            System.out.println("Invalid input folder path: " + inputFolderPath);
            return;
        }

        // Create output folder if it doesn't exist
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdirs()) {
                System.out.println("Failed to create output folder: " + outputFolderPath);
                return;
            }
        }

        // Get HTML files from the input folder
        File[] htmlFiles = inputFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".html"));

        if (htmlFiles == null || htmlFiles.length == 0) {
            System.out.println("No HTML files found in folder: " + inputFolderPath);
            return;
        }

        // Convert each HTML file to text
        for (File htmlFile : htmlFiles) {
            try {
                convertSingleHtmlToText(htmlFile, outputFolderPath);
            } catch (IOException e) {
                System.err.println("Error processing file: " + htmlFile.getName() + " - " + e.getMessage());
            }
        }

        System.out.println("Conversion completed. Text files saved in: " + outputFolderPath);
    }

    /**
     * Converts a single HTML file to a text file.
     *
     * @param htmlFile         HTML file to be converted.
     * @param outputFolderPath Path of the folder to save the text file.
     * @throws IOException If an I/O error occurs.
     */
    private void convertSingleHtmlToText(File htmlFile, String outputFolderPath) throws IOException {
        // Parse HTML file and extract text content
        Document document = Jsoup.parse(htmlFile, "UTF-8");
        String textContent = document.text();

        // Generate the output text file name
        String outputFileName = htmlFile.getName().replaceAll("(?i)\\.html$", ".txt");
        File textFile = new File(outputFolderPath, outputFileName);

        // Write extracted text to the output file
        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write(textContent);
        }

        System.out.println("Converted: " + htmlFile.getName() + " -> " + textFile.getAbsolutePath());
    }
}
