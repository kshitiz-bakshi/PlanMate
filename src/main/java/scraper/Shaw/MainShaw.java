package scraper.Shaw;

public class MainShaw {
    public static void main(String[] args) {
        try {
            System.out.println("Running TestSelenium...");
            TestSelenium.main(null);

//            System.out.println("Running TestSelenium2...");
//            TestSelenium2.main(null);

//            System.out.println("Running TestSelenium3...");
//            TestSelenium3.main(null);

//            System.out.println("Running TestSelenium4...");
//            TestSelenium4.main(null);

//            System.out.println("Running TestSelenium5...");
//            TestSelenium5.main(null);

//            System.out.println("All tests completed successfully!");
        } catch (Exception e) {
            System.err.println("An error occurred during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
