package Imp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class WebCrawler {

    private static final String SAVE_DIRECTORY = "saved_pages";
    private static final int MAX_PAGES = 40; // Limit to avoid overloading the server
    private Set<String> visitedPages = new HashSet<>();
    private Queue<String> pagesToVisit = new LinkedList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for a valid URL and validate it
        String startUrl;
        while (true) {
            System.out.print("Enter the website URL to start crawling (e.g., www.xyz.com): ");
            String inputUrl = scanner.nextLine();

            // Automatically append "https://" if not provided
            startUrl = ensureHttps(inputUrl);

            // Validate the URL format
            if (isValidUrl(startUrl)) {
                break;  // Exit the loop if URL is valid
            } else {
                System.out.println("Entered invalid URL. Please enter a valid website address.");
            }
        }

        WebCrawler crawler = new WebCrawler();
        crawler.crawl(startUrl);

    }

    private static String ensureHttps(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url.trim();
        }
        return url.trim();
    }

    private static boolean isValidUrl(String url) {
        try {
            // Check if the URL can be parsed and has a host
            java.net.URL parsedUrl = new java.net.URL(url);
            return parsedUrl.getHost() != null && !parsedUrl.getHost().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void crawl(String startUrl) {
        pagesToVisit.add(startUrl);

        while (!pagesToVisit.isEmpty() && visitedPages.size() < MAX_PAGES) {
            String url = pagesToVisit.poll();
            if (url != null && !visitedPages.contains(url)) {
                visitPage(url);
            }
        }

        System.out.println("Crawling completed. Total pages visited: " + visitedPages.size());
    }

    private void visitPage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("Visiting: " + url);
            visitedPages.add(url);

            // Save the HTML content of the page to a file
            saveHtmlToFile(doc, url);

            // Extract and add links to other pages on the site
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String linkHref = link.absUrl("href");
                if (linkHref.startsWith(url) && !visitedPages.contains(linkHref)) {
                    pagesToVisit.add(linkHref);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid URL encountered: " + url + " - " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error accessing: " + url + " - " + e.getMessage());
        }
    }

    private void saveHtmlToFile(Document doc, String url) {
        try {
            // Create the directory if it doesn't exist
            File directory = new File(SAVE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Generate a safe filename using the URL
            String safeFileName = url.replace("https://", "").replace("http://", "")
                    .replaceAll("[^a-zA-Z0-9.-]", "_");
            File file = new File(SAVE_DIRECTORY + File.separator + safeFileName + ".html");

            // Save HTML content to a file
            FileWriter writer = new FileWriter(file);
            writer.write(doc.html());
            writer.close();

            System.out.println("Saved page to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving file for URL: " + url + " - " + e.getMessage());
        }
    }
}
