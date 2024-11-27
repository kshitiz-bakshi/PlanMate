package scraper.Shaw;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVWriter;

public class TestSelenium {
    public static void main(String[] args) throws IOException {
//
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run browser in headless mode
        options.addArguments("--disable-gpu"); // Applicable for Windows, avoids GPU issues
        options.addArguments("--no-sandbox"); // Improves stability in headless mode
        options.addArguments("--disable-dev-shm-usage"); // Reduces memory issues in Dock
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.shaw.ca/");
//            System.out.println("Website opened");

            // Wait for the View Plans button to be present
            WebElement viewPlansButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='/internet/plans#plansOverview' and contains(@class, 'c-call-to-action__primary-button')]")
            ));

            // Wait until the button is clickable
            wait.until(ExpectedConditions.elementToBeClickable(viewPlansButton));

            // Scroll to the View Plans button
            Actions actions = new Actions(driver);
            actions.moveToElement(viewPlansButton).perform();
            Thread.sleep(500); // Pause for a moment to ensure the scroll completes

            // Click the button
            try {
                viewPlansButton.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPlansButton);
            }

            System.out.println("Element clicked");

            // Call the method to extract plan details
            List<PlanDetails> planDetailsList = extractPlanDetails(driver);

            // Write plan details to shaw_plans.csv without headers
            writeToCSV(planDetailsList, "shaw_plans.csv");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static List<PlanDetails> extractPlanDetails(WebDriver driver) {
        List<PlanDetails> planDetailsList = new ArrayList<>();
        HashSet<String> uniquePlanNames = new HashSet<>(); // Use a Set to track unique plan names

        boolean hasMoreElements = true;

        // Get the initial scroll height
        long lastScrollHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight;");

        while (hasMoreElements) {
            try {
                // Get all card elements
                List<WebElement> cardElements = driver.findElements(By.className("c-card__card-face-inner"));

                // Check if any card elements were found
                if (cardElements.isEmpty()) {
                    break; // Exit loop if no more elements are found
                }

                // Iterate over card elements only once
                for (WebElement card : cardElements) {
                    // Extract plan name
                    String planName = card.findElement(By.className("c-card__title")).getText().trim();

                    // Check for uniqueness
                    if (!uniquePlanNames.contains(planName)) {
                        System.out.println("Found Plan Name: " + planName);

                        // Extract additional details
                        String planPrice = card.findElement(By.className("c-price")).getText().trim();
                        String planTerm = card.findElement(By.className("c-price__term")).getText().trim();
                        String planDetails = card.findElement(By.className("c-price__details")).getText().trim();

                        // Check for the plan offer only if it exists
                        String planOffer = extractOptionalElementText(card, By.className("c-badge__copy"));

                        // Extract features
                        List<String> planFeatures = new ArrayList<>();
                        List<WebElement> featuresElements = card.findElements(By.className("c-features-list__list-item"));
                        for (WebElement feature : featuresElements) {
                            planFeatures.add(feature.getText().trim());
                        }

                        // Create a PlanDetails object and add it to the list
                        PlanDetails planDetailsObject = new PlanDetails(planName, planPrice, planTerm, planDetails, planOffer, planFeatures);
                        planDetailsList.add(planDetailsObject);
                        uniquePlanNames.add(planName); // Mark this plan as seen
                    }
                }

                // Scroll down to load more elements
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);"); // Scroll down 1000 pixels
                Thread.sleep(1000); // Pause to allow more elements to load

                // Check if the scroll height has changed
                long newScrollHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight;");
                if (newScrollHeight == lastScrollHeight) {
//                    System.out.println("Reached the bottom of the page.");
                    break; // Exit loop if at the bottom
                }
                lastScrollHeight = newScrollHeight; // Update the last scroll height

            } catch (Exception e) {
                System.out.println("Error while fetching elements or scrolling: " + e.getMessage());
                hasMoreElements = false; // Exit loop on error
            }
        }
        return planDetailsList;
    }

    private static void writeToCSV(List<PlanDetails> planDetailsList, String csvFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            // Write each plan's details without headers
            for (PlanDetails plan : planDetailsList) {
                String[] data = {
                        plan.name,
                        plan.price,
                        plan.term,
                        plan.details,
                        plan.offer,
                        String.join(";", plan.features), // Join features into a single string
                        "internet plan", // Plan type
                        "current rogers and fido customers" // Plan sub-type
                };

                // Print the details to the console
                System.out.println("Plan Details:");
                System.out.println("Name: " + plan.name);
                System.out.println("Price: " + plan.price);
                System.out.println("Term: " + plan.term);
                System.out.println("Details: " + plan.details);
                System.out.println("Offer: " + plan.offer);
                System.out.println("Features: " + String.join(", ", plan.features));
                System.out.println("Plan Type: internet plan");
                System.out.println("Plan Sub-Type: current rogers and fido customers");
                System.out.println("--------------------------------------------");

                // Write the details to the CSV
                writer.writeNext(data);
            }
            System.out.println("Data written to CSV file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Utility function to extract text from an element or return null if the element doesn't exist
    private static String extractOptionalElementText(WebElement parentElement, By by) {
        try {
            WebElement element = parentElement.findElement(by);
            return element != null ? element.getText().trim() : null;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return null; // Element not found, return null
        }
    }

    static class PlanDetails {
        String name;
        String price;
        String term;
        String details;
        String offer;
        List<String> features;

        PlanDetails(String name, String price, String term, String details, String offer, List<String> features) {
            this.name = name;
            this.price = price;
            this.term = term;
            this.details = details;
            this.offer = offer;
            this.features = features;
        }

        @Override
        public String toString() {
            return "Plan Name: " + name +
                    ", Price: " + price +
                    ", Term: " + term +
                    ", Details: " + details +
                    ", Offer: " + offer +
                    ", Features: " + features;
        }
    }
}
