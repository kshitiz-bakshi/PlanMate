package scraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BellScraper {
    public static void main(String[] args) {
        String csvFile = "/Users/kshitiz/Documents/ACC ProjectF/untitled/bell_plans (1).csv"; // Replace with the path to your CSV file
        String line;
        String delimiter = ","; // Comma is used as the delimiter

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip the header line
            br.readLine();

            // Read and print each data row
            while ((line = br.readLine()) != null) {
                // Skip empty lines or lines with only whitespace
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(delimiter);

                // Ensure the line has the expected number of columns
                if (values.length < 4) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  // Skip lines that don't have enough columns
                }

                // Assuming the CSV has columns in the order: Plan, Price, Data Allowance, and Features
                String plan = values[0].trim();
                String price = values[1].trim();
                String dataAllowance = values[2].trim();
                String features = values[3].trim();

                // Print the formatted output
                printFormattedOutput(plan, price, dataAllowance, features);
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    // Method to print the formatted output
    private static void printFormattedOutput(String plan, String price, String dataAllowance, String features) {
        System.out.println("Plan: " + plan);
        System.out.println("Price: " + price);
        System.out.println("Data Allowance: " + dataAllowance);
        System.out.println("Features: " + features);
        printSeparator();

    }

    // Method to print a separator line between each entry
    private static void printSeparator() {
        System.out.println("------x------x------x------x------");
    }
}
