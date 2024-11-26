package Imp;

import scraper.BellScraper;
import scraper.RogersScraper;

import java.util.Scanner;

public class SearchByName {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input from the user
        System.out.println("Enter a word:");
        String input = scanner.nextLine().toLowerCase();

        // Perform word completion using WordCompletion
        String completedWord = WordCompletion.complete(input);

        if (completedWord != null) {
            System.out.println("Do you mean " + completedWord + "? (yes/no)");
            String confirmation = scanner.nextLine().toLowerCase();

            if (confirmation.equals("yes")) {
                switch (completedWord) {
                    case "bell":
                        System.out.println("Scraping Bell plans...");
                        try {
                            BellScraper.main(new String[0]);  // Call BellScraper.main() to scrape data
                        } catch (Exception e) {
                            System.out.println("Error while scraping Bell plans: " + e.getMessage());
                        }
                        break;
                    case "rogers":
                        System.out.println("Scraping Rogers plans...");
                        try {
                            RogersScraper.main(new String[0]);  // Call RogersScraper.main() to scrape data
                        } catch (Exception e) {
                            System.out.println("Error while scraping Rogers plans: " + e.getMessage());
                        }
                        break;
                    case "telus":
                        System.out.println("Scraping Telus plans...");
                        try {
                            // Uncomment the below line when the TelusScraper class is available
                            // TelusScraper.main(new String[0]);  // Call TelusScraper.main() to scrape data
                        } catch (Exception e) {
                            System.out.println("Error while scraping Telus plans: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("No matching Internet Provider found.");
                }
            } else {
                System.out.println("Plz Enter a Valid Internet Provider Name");
            }
        } else {
            System.out.println("No suggestions found for the input.");
        }

//        scanner.close();
    }
}