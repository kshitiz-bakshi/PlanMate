package Imp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class CheapestPlanFinder {

    public static void main(String[] args) {
        String filePath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/output.json";

        try {
            // Read JSON file content as a String
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse JSON file to extract plan details
            JSONArray plansArray = new JSONArray(jsonContent);

            // Find the cheapest plan
            JSONObject cheapestPlan = findCheapestPlan(plansArray);

            // Print the cheapest plan
            if (cheapestPlan != null) {
                System.out.println("\nThe Best Plan:");
                System.out.println("Provider: " + cheapestPlan.getString("Service Provider"));
                System.out.println("Plan Name: " + cheapestPlan.getString("Plan Name"));
                System.out.println("Price: $" + cheapestPlan.getString("Price"));
                System.out.println("Features: " + cheapestPlan.getString("Features"));
                System.out.println("Plan Type: " + cheapestPlan.getString("Plan type"));
            } else {
                System.out.println("No plans available in the JSON file.");
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Print all the plans from the JSON array.
     *
     * @param plansArray JSONArray containing internet plans.
     */


    /**
     * Find the cheapest plan from a JSONArray of internet plans.
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
     * Extract price value from the price string (removing "$" and parsing it to double).
     *
     * @param priceStr The price string with "$".
     * @return The price as a double.
     */
    public static double extractPrice(String priceStr) {
        // Remove "$" symbol, spaces, and any commas
        priceStr = priceStr.replace("$", "").replace(",", "").trim();

        try {
            return Double.parseDouble(priceStr); // Convert to double
        } catch (NumberFormatException e) {
            System.out.println("Error parsing price: " + priceStr);
            return Double.MAX_VALUE; // Return a high value if parsing fails
        }
    }
}
