package Imp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class CsvToJsonConverter {

    public static void main(String[] args) {
        // Hardcoded CSV file path
        String csvFilePath = "/Users/kshitiz/Documents/ACC ProjectF/untitled/final.csv";  // Change this to the actual CSV file path
        String jsonFilePath = "outputF.json";  // Output JSON file path

        List<Map<String, String>> csvData = readCsv(csvFilePath);
        if (csvData != null && !csvData.isEmpty()) {
            // Convert CSV data to JSON and write to file
            JSONArray jsonArray = convertToJson(csvData);
            writeJsonToFile(jsonArray, jsonFilePath);
        } else {
            System.out.println("No valid data found in the CSV file.");
        }
    }

    // Method to read CSV file and store data in a List of Maps
    private static List<Map<String, String>> readCsv(String csvFilePath) {
        List<Map<String, String>> csvData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            String[] headers = null;

            // Read CSV file line by line
            while ((line = br.readLine()) != null) {
                // Clean up the line to remove unwanted characters (e.g., "*", extra spaces)
                line = line.trim().replaceAll("[*]", "");  // remove '*' characters and trim spaces

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;  // Skip the empty line and continue to the next iteration
                }

                // Assuming CSV is separated by commas (update this if using semicolons)
                String[] values = line.split(",");

                // Handle case where the first line is the header row
                if (headers == null) {
                    headers = values;  // Set the first row as headers
                    continue;  // Skip header row and move to next line (data rows)
                }

                // If the number of columns doesn't match the header, skip this row
                if (values.length != headers.length) {
//                    System.out.println("Skipping invalid row due to column mismatch: " + Arrays.toString(values));
                    continue;  // Skip to the next iteration if columns don't match
                }

                // Create a map for each row of data
                Map<String, String> rowData = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    // Clean up and trim spaces from header and values
                    rowData.put(headers[i].trim(), values[i].trim());
                }
                csvData.add(rowData);  // Add the row to the data list
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        return csvData;  // Return the collected data
    }

    // Method to convert List of Maps to JSON array
    private static JSONArray convertToJson(List<Map<String, String>> csvData) {
        JSONArray jsonArray = new JSONArray();

        for (Map<String, String> row : csvData) {
            JSONObject jsonObject = new JSONObject(row);
            jsonArray.put(jsonObject);  // Add each row to the JSON array
        }
        return jsonArray;
    }

    // Method to write JSON data to a file
    private static void writeJsonToFile(JSONArray jsonArray, String jsonFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFilePath))) {
            writer.write(jsonArray.toString(4));  // Pretty print with an indent of 4 spaces
            System.out.println("JSON data successfully written to " + jsonFilePath);
        } catch (IOException e) {
            System.out.println("Error writing JSON to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
