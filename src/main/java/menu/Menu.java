package menu;

import Imp.*;
import scraper.BellScraper;
import scraper.CogecoScraper;
import scraper.RogersScraper;
import scraper.Shaw.MainShaw;
import scraper.StarlinkScraper;

import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Menu {

    public static void main(String[] args) throws InterruptedException {
        Menu menu = new Menu();
        menu.showMenu();
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println(); // Print 50 blank lines
        }
    }

    // Method to display the menu and process user's choice
    public void showMenu() throws InterruptedException {
        String planMate =
                """
                 ██████  ██╗      █████╗ ███╗   ██╗       ███╗   ███╗ █████╗ ████████╗███████╗
                ██╔   ██ ██║     ██╔══██╗████╗  ██║       ████╗ ████║██╔══██╗╚══██╔══╝██╔════╝
                ██║█████ ██║     ███████║██╔██╗ ██║█████╗ ██╔████╔██║███████║   ██║   █████╗  
                ██║      ██║     ██╔══██║██║╚██╗██║╚════╝ ██║╚██╔╝██║██╔══██║   ██║   ██╔══╝  
                ██║      ███████╗██║  ██║██║ ╚████║       ██║ ╚═╝ ██║██║  ██║   ██║   ███████╗
                ╚═╝      ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝       ╚═╝     ╚═╝╚═╝  ╚═╝   ╚═╝   ╚══════╝
                                                                                      
                        ╔══════════════════════════════════════════════╗               
                        ║       ~ PLAN YOUR INTERNET WITH PRECISION ~  ║
                        ║        ~ HELP IN SAVINGS, ALWAYS! ~          ║
                        ╚══════════════════════════════════════════════╝               
                """;

        System.out.println(planMate);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                // Displaying the menu options
                System.out.println("1. Get Bell Internet Plans");
                System.out.println("2. Get Rogers Internet Plans");
                System.out.println("3. Get Cogeco Internet Plans");
                System.out.println("4. Get Shaw Internet Plans");
                System.out.println("5. Get Starlink Internet Plans");
                System.out.println("6. Search Internet Provider by Name");
                System.out.println("7. Use Frequency Count Feature");
                System.out.println("8. Use Search Frequency Feature");
                System.out.println("9. Get Best Plan");
                System.out.println("10.Use Page Ranking Feature");
                System.out.println("11.Find For a Word");
                System.out.println("12.Web Crawler");
                System.out.println("13.Exit");
                System.out.print("Enter your choice: ");

                // Handle input safely
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Process the user's choice
                switch (choice) {
                    case 1 -> scrapePlans("Bell", BellScraper.class);
                    case 2 -> scrapePlans("Rogers", RogersScraper.class);
                    case 3 -> scrapePlans("Cogeco", CogecoScraper.class);
                    case 4 -> scrapePlans("Shaw", MainShaw.class);
                    case 5 -> scrapePlans("Starlink", StarlinkScraper.class);
                    case 6 -> invokeFeature("Search Internet Provider by Name", SearchByName.class);
                    case 7 -> invokeFeature("Frequency Count", FrequencyCount.class);
                    case 8 -> invokeFeature("Search Frequency", SearchFrequency.class);
                    case 9 -> invokeFeature("Get Best Plan", PlanFinder.class);
                    case 10 -> invokeFeature("Page Ranking", FileSearchRanker.class);
                    case 11 -> invokeFeature("Find a Word", JsonSearch.class);
                    case 12 -> invokeFeature("Web Crawler", WebCrawler.class);
                    case 13 -> {
                        System.out.println("Exiting...");
                        sleep(2000);
                        clearConsole();
                        System.out.println("Thank you for using our services...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 12.");
                }

                // Ask user if they want to continue after each action
                System.out.println("Back to Menu? (Y/N)");
                String continueChoice = scanner.nextLine();
                if (!continueChoice.equalsIgnoreCase("Y") && !continueChoice.equalsIgnoreCase("Yes")) {
                    System.out.println("Exiting the program...");
                    sleep(2000);
                    clearConsole();
                    System.out.println("Thank you for using our services...");
                    break; // Exit the loop and the program
                } else {
                    clearConsole(); // Clear the console for a fresh menu
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // Log stack trace for debugging
            }
        }
    }

    // Generic method to scrape plans and handle errors
    private <T> void scrapePlans(String providerName, Class<T> scraperClass) {
        System.out.println("Scraping " + providerName + " plans...");
        try {
            scraperClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            System.out.println("Error while scraping " + providerName + " plans: " + e.getMessage());
        }
    }

    // Generic method to invoke a feature and handle errors
    private <T> void invokeFeature(String featureName, Class<T> featureClass) {
        System.out.println("Invoking " + featureName + "...");
        try {
            featureClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            System.out.println("Error while invoking " + featureName + ": " + e.getMessage());
        }
    }
}
