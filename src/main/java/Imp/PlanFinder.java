package Imp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class PlanFinder {

    public static void main(String[] args) {
        String filePath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/outputF.json";  // Adjust path as needed
        Scanner scanner = new Scanner(System.in);

        try {
            // Read JSON file and convert to JSON array
            JSONArray plansArray = loadPlansFromJSON(filePath);

            // Ask user for the type of plan they want
            System.out.println("Choose the type of plan you want to find:");
            System.out.println("1. Cheapest Plan");
            System.out.println("2. Highest Speed Plan");
            int choice = scanner.nextInt();

            // Switch case to choose between lowest-priced or highest-speed plan
            switch (choice) {
                case 1:
                    JSONObject cheapestPlan = findCheapestPlan(plansArray);
                    displayPlanDetails(cheapestPlan, "Cheapest");
                    break;
                case 2:
                    JSONObject highestSpeedPlan = findHighestSpeedPlan(plansArray);
                    displayPlanDetails(highestSpeedPlan, "Highest Speed");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

        } catch (IOException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Load plans from the JSON file and return as a JSON array.
     *
     * @param filePath The path to the JSON file.
     * @return JSONArray containing internet plans.
     * @throws IOException If there is an error reading the file.
     */
    public static JSONArray loadPlansFromJSON(String filePath) throws IOException {
        // Read the file content as a string
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Parse the JSON content into a JSON array
        return new JSONArray(jsonContent);
    }

    /**
     * Find the cheapest plan from the JSONArray of plans.
     *
     * @param plansArray JSONArray containing internet plans.
     * @return JSONObject representing the cheapest plan.
     */
    public static JSONObject findCheapestPlan(JSONArray plansArray) {
        JSONObject cheapestPlan = null;
        double lowestPrice = Double.MAX_VALUE;

        for (int i = 0; i < plansArray.length(); i++) {
            JSONObject plan = plansArray.getJSONObject(i);

            if (plan.has("Price")) {
                String priceStr = plan.getString("Price");
                double price = extractPrice(priceStr);

                if (price < lowestPrice) {
                    lowestPrice = price;
                    cheapestPlan = plan;
                }
            }
        }
        return cheapestPlan;
    }

    /**
     * Find the highest speed plan from the JSONArray of plans.
     *
     * @param plansArray JSONArray containing internet plans.
     * @return JSONObject representing the highest speed plan.
     */
    public static JSONObject findHighestSpeedPlan(JSONArray plansArray) {
        JSONObject highestSpeedPlan = null;
        double highestSpeed = 0;

        for (int i = 0; i < plansArray.length(); i++) {
            JSONObject plan = plansArray.getJSONObject(i);

            if (plan.has("Speed")) {
                String speedStr = plan.getString("Speed");
                double speed = extractSpeed(speedStr);

                if (speed > highestSpeed) {
                    highestSpeed = speed;
                    highestSpeedPlan = plan;
                }
            }
        }
        return highestSpeedPlan;
    }

    /**
     * Extract price value from the price string (removing "$" and parsing it to double).
     *
     * @param priceStr The price string with "$".
     * @return The price as a double.
     */
    public static double extractPrice(String priceStr) {
        priceStr = priceStr.replace("$", "").replace(",", "").trim();
        try {
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing price: " + priceStr);
            return Double.MAX_VALUE;
        }
    }

    /**
     * Extract speed value from the speed string (converting Gbps to Mbps and parsing to double).
     *
     * @param speedStr The speed string (e.g., "100 Mbps", "2 Gbps").
     * @return The speed as a double in Mbps.
     */
    public static double extractSpeed(String speedStr) {
        speedStr = speedStr.trim();

        // Check if the speed is in Gbps, if so convert it to Mbps
        if (speedStr.toLowerCase().contains("gbps")) {
            speedStr = speedStr.replaceAll("[^0-9.]", "");
            try {
                return Double.parseDouble(speedStr) * 1000; // Convert Gbps to Mbps
            } catch (NumberFormatException e) {
                System.out.println("Error parsing speed: " + speedStr);
                return 0;
            }
        }

        // Otherwise, assume the speed is in Mbps
        if (speedStr.toLowerCase().contains("mbps")) {
            speedStr = speedStr.replaceAll("[^0-9.]", "");
            try {
                return Double.parseDouble(speedStr);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing speed: " + speedStr);
                return 0;
            }
        }

        return 0; // Return 0 if the speed format is invalid
    }

    /**
     * Display the plan details.
     *
     * @param plan       The JSON object containing plan details.
     * @param planType   The type of plan (Cheapest or Highest Speed).
     */
    public static void displayPlanDetails(JSONObject plan, String planType) {
        if (plan != null) {
            System.out.println("\nThe " + planType + " Plan:");
            System.out.println("Provider: " + plan.getString("Service Provider"));
            System.out.println("Plan Name: " + plan.getString("Plan Name"));
            System.out.println("Price: $" + plan.getString("Price"));
            System.out.println("Speed: " + plan.getString("Speed"));
            System.out.println("Features: " + plan.getString("Features"));
            System.out.println("Plan Type: " + plan.getString("Plan type"));
        } else {
            System.out.println("No plans available matching your criteria.");
        }
    }
}
