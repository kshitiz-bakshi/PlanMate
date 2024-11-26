package scraper.Shaw;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVWriter;

public class TestSelenium2 {
    public static void main(String[] args) throws IOException {
//        System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\\\sachi\\\\Downloads\\\\chromedriver-win64 (1)\\\\chromedriver-win64\\\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.shaw.ca/");
            System.out.println("Website opened");

            // Wait for the View Plans button to be present
            WebElement viewPlansButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/internet/plans#plansOverview' and contains(@class, 'c-call-to-action__primary-button')]")));

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

            // Click on the "New Rogers Customers" tab
            clickOnNewRogersCustomersTab(driver, wait);

            // Call the method to extract plan details
            List<PlanDetails> planDetailsList = extractPlanDetails(driver);

            // Write plan details to shaw_plans.csv
            writeToCSV(planDetailsList, "shaw_plans.csv");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void clickOnNewRogersCustomersTab(WebDriver driver, WebDriverWait wait) {
        try {
            // Wait for the New Rogers Customers tab to be clickable
            WebElement newCustomersTab = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-rogers-customers")));
            newCustomersTab.click();
            System.out.println("Clicked on New Rogers Customers tab");
            // Wait for some time to ensure the page loads completely
            Thread.sleep(2000); // Adjust the duration as necessary
        } catch (Exception e) {
            System.out.println("Error clicking New Rogers Customers tab: " + e.getMessage());
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

                // Iterate over card elements
                for (WebElement card : cardElements) {
                    try {
                        // Extract plan name
                        String planName = extractElementText(card, By.className("c-card__title"));

                        // Check for uniqueness
                        if (planName != null && !uniquePlanNames.contains(planName)) {
                            System.out.println("Found Plan Name: " + planName);

                            // Extract additional details
                            String planPrice = extractElementText(card, By.className("c-price"));
                            String planTerm = extractElementText(card, By.className("c-price__term"));
                            String planDetails = extractElementText(card, By.className("c-price__details"));
                            String planOffer = extractElementText(card, By.className("c-badge__copy"));

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
                    } catch (StaleElementReferenceException e) {
                        System.out.println("StaleElementReferenceException encountered, re-fetching elements.");
                        // Re-fetch the card elements and continue
                        cardElements = driver.findElements(By.className("c-card__card-face-inner"));
                        break; // Restart the loop to reprocess elements
                    }
                }

                // Scroll down to load more elements
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);"); // Scroll down 1000 pixels
                Thread.sleep(1000); // Pause to allow more elements to load

                // Check if the scroll height has changed
                long newScrollHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight;");
                if (newScrollHeight == lastScrollHeight) {
                    System.out.println("Reached the bottom of the page.");
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

    // Utility function to extract text from an element or return null if the element doesn't exist
    private static String extractElementText(WebElement parentElement, By by) {
        try {
            return parentElement.findElement(by).getText().trim();
        } catch (Exception e) {
            return null;
        }
    }

    private static void writeToCSV(List<PlanDetails> planDetailsList, String csvFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile, true))) { // Open in append mode
            // Write each plan's details
            for (PlanDetails plan : planDetailsList) {
                String[] data = {
                    plan.name,
                    plan.price,
                    plan.term,
                    plan.details,
                    plan.offer,
                    String.join(";", plan.features), // Join features into a single string
                    "internet plan", // Plan type
                    "new customers" // Plan sub-type
                };
                writer.writeNext(data);
            }
            System.out.println("Data appended to CSV file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
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
