package scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RogersScraper {

    public static void main(String[] args) throws InterruptedException, IOException {

        // Create ChromeOptions and enable headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run browser in headless mode
        options.addArguments("--disable-gpu"); // Applicable for Windows, avoids GPU issues
        options.addArguments("--no-sandbox"); // Improves stability in headless mode
        options.addArguments("--disable-dev-shm-usage"); // Reduces memory issues in Docker or constrained environments
//        WebDriver driver = new ChromeDriver();
        // Initialize WebDriver with headless ChromeOptions
        WebDriver driver = new ChromeDriver(options);

        // Takes you to the Rogers homepage
        driver.get("https://www.rogers.com/");
        driver.manage().window().maximize(); // For better experience browser window is maximized

        // Locate Internet menu option then selects it by clicking that option
        WebElement Internet = driver.findElement(By.xpath("//*[@id=\"geMainMenuDropdown_1\"]/div"));
        Internet.click();

        // Locate and click on the "Shop All Internet" option
        WebElement ShopAllInternet = driver.findElement(By.xpath("//*[@id=\"geMainMenuDropdown_1\"]/div/div/div/ul/li[1]/ge-link/a"));
        ShopAllInternet.click();

        // Wait for 3 seconds to ensure the page is fully loaded
        Thread.sleep(3000);

        // Create a new CSV file to store the extracted data
        FileWriter csvWriter = new FileWriter("internet_plans.csv");

        // Write the header row in the CSV file
        csvWriter.append("Plan Name,Price,Frequency,Features\n");

        // Identify and capture all the plan elements on the page
        List<WebElement> planElements = driver.findElements(By.cssSelector("dsa-tile-plan"));

        // Fetches the plan related details by iterating through each plan
        for (WebElement plan : planElements) {

            // Extract the plan name
            String planName = plan.findElement(By.cssSelector(".dsa-tile-plan__heading")).getText();

            // Fetches the price related details (dollars and cents)
            String planPrice = plan.findElement(By.cssSelector(".ds-price__amountDollars")).getText();
            String planCents = plan.findElement(By.cssSelector(".ds-price__amountCents")).getText();
            String planFrequency = plan.findElement(By.cssSelector(".ds-price__amountFrequency")).getText(); // Extract the frequency of the price (e.g., per month)

            // Extract the features (e.g., speed, device support) listed for the plan
            List<WebElement> features = plan.findElements(By.cssSelector("ul li span"));
            StringBuilder featureList = new StringBuilder(); // To store the concatenated list of features
            for (WebElement feature : features) {
                featureList.append(feature.getText()).append("; "); // Append each feature to the list
            }

            // Print the extracted information to the console (optional for debugging purposes)
            System.out.println("Plan: " + planName);
            System.out.println("Price: $" + planPrice + planCents + " " + planFrequency);
            System.out.println("Features: " + featureList.toString());
            System.out.println("------x------x------x------x------");

            // Finally the extracted information is written to CSV file
            csvWriter.append(planName)
                    .append(",")
                    .append("$").append(planPrice).append(planCents)
                    .append(",")
                    .append(planFrequency)
                    .append(",")
                    .append(featureList.toString())
                    .append("\n");
        }

        // Close the CSV writer to save the file
        csvWriter.flush();
        csvWriter.close();

        // Close the browser after the process is completed
        driver.quit();
    }
}