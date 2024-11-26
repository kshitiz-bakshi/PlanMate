package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StarlinkScraper {

    public static void main(String[] args) {
        // Set up ChromeDriver
//        System.setProperty("webdriver.chrome.driver", "path_to_your_chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the URL
            String url = "https://www.starlink.com/ca/service-plans";
            driver.get(url);

            // Wait for the page to load (increase if necessary)
            Thread.sleep(5000);

            // Extract the data from the page
            List<WebElement> planTitles = driver.findElements(By.xpath("//div[contains(@class, 'service-plan-card-title')]"));
            List<WebElement> planDescriptions = driver.findElements(By.xpath("//div[contains(@class, 'service-plan-card-subtitle')]"));
            List<WebElement> keyFeatures = driver.findElements(By.xpath("//div[contains(@class, 'key-feature')]"));
            List<WebElement> planPrices = driver.findElements(By.xpath("//div[contains(@class, 'price-list-item-price')]"));

            // Prepare lists for storing data
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Plan Name", "Description", "Key Features", "Price"}); // CSV header

            // Extract data and format properly for CSV
            for (int i = 0; i < planTitles.size(); i++) {
                String planName = planTitles.get(i).getText().trim();
                String description = i < planDescriptions.size() ? planDescriptions.get(i).getText().trim() : "N/A";

                StringBuilder keyFeaturesText = new StringBuilder();
                // Collect key features for each plan
                if (i < keyFeatures.size()) {
                    List<WebElement> features = keyFeatures.get(i).findElements(By.xpath(".//div[contains(@class, 'key-feature')]"));
                    for (WebElement feature : features) {
                        keyFeaturesText.append(feature.getText().trim()).append("; ");
                    }
                }

                String price = i < planPrices.size() ? planPrices.get(i).getText().trim() : "N/A";

                // Print data to console
                System.out.println("Plan Name: " + planName);
                System.out.println("Description: " + description);
                System.out.println("Key Features: " + keyFeaturesText.toString().trim());
                System.out.println("Price: " + price);
                System.out.println("-----x------x------x-------x-----");

                // Add data to CSV list
                data.add(new String[]{planName, description, keyFeaturesText.toString().trim(), price});
            }

            // Save data to CSV
            saveToCSV(data, "starlink_service_plans.csv");
//            System.out.println("Data saved to starlink_service_plans.csv");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    // Method to save data to a CSV file
    private static void saveToCSV(List<String[]> data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                // Ensure each value is properly quoted and separated by commas
                writer.write("\"" + String.join("\",\"", row) + "\"");
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }
}
