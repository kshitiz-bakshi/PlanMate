package scraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CogecoScraper {

    public static void main(String[] args) {
        // Path to the CSV file (replace with the actual path to your file)
        String csvFile = "/Users/kshitiz/Documents/ACC ProjectF/untitled/CogecoPlans_CSV.csv";
        String line;  // Variable to store each line read from the file
        String delimiter = ",";  // Comma is used as the delimiter for the CSV file

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read and print the header (the first line of the CSV)
            String header = br.readLine();
            if (header != null) {
                System.out.println("Columns: " + header);
                printSeparator();
            }

            // Read each subsequent line of the CSV file
            while ((line = br.readLine()) != null) {
                // Skip empty lines or lines with only whitespace
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split the line into an array of values based on the delimiter
                String[] values = line.split(delimiter);

                // Check if the line has the expected number of columns (at least 7)
                if (values.length < 7) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;  // Skip lines that don't meet the column requirement
                }

                // Assign each column value to a variable
                String planType = values[0].trim();    // Plan Type column
                String price = values[1].trim();      // Price column
                String planTitle = values[2].trim();  // Plan Title column
                String planSubtitle = values[3].trim(); // Plan Subtitle column
                String details = values[4].trim();    // Details column
                String wifiModem = values[5].trim();  // Wi-Fi Modem column
                String imageUrl = values[6].trim();   // Image URL column

                // Print the formatted output for the current line
                printFormattedOutput(planType, price, planTitle, planSubtitle, details, wifiModem, imageUrl);
            }
        } catch (IOException e) {
            // Handle errors that occur while reading the file
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    /**
     * Method to print the formatted output for a single plan.
     * @param planType The type of plan (e.g., Internet, Internet + TV).
     * @param price The price of the plan.
     * @param planTitle The title of the plan.
     * @param planSubtitle The subtitle or tagline of the plan.
     * @param details Additional details about the plan (e.g., speed, data allowance).
     * @param wifiModem Information about the Wi-Fi modem.
     * @param imageUrl The URL of the image associated with the plan.
     */
    private static void printFormattedOutput(String planType, String price, String planTitle, String planSubtitle,
                                             String details, String wifiModem, String imageUrl) {
        System.out.println("Plan Type: " + planType);         // Print Plan Type
        System.out.println("Price: " + price);               // Print Price
        System.out.println("Plan Title: " + planTitle);      // Print Plan Title
        System.out.println("Plan Subtitle: " + planSubtitle); // Print Plan Subtitle
        System.out.println("Details: " + details);           // Print Plan Details
        System.out.println("Wi-Fi Modem: " + wifiModem);     // Print Wi-Fi Modem information
        System.out.println("Image URL: " + imageUrl);        // Print Image URL
        printSeparator();  // Add a separator for better readability
    }

    /**
     * Method to print a separator line for better readability in the console.
     */
    private static void printSeparator() {
        System.out.println("------x------x------x------x------");  // Print separator
    }
}
