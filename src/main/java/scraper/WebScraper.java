package scraper;

import model.InternetPlan;
import java.util.List;

public interface WebScraper {
    List<InternetPlan> scrape();
}
