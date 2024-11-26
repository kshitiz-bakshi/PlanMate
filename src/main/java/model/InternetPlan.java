package model;

public class InternetPlan {
    private String planName;
    private String price;
    private String features;

    public InternetPlan(String planName, String price, String features) {
        this.planName = planName;
        this.price = price;
        this.features = features;
    }

    @Override
    public String toString() {
        return "Plan Name: " + planName + "\nPrice: " + price + "\nFeatures: " + features + "\n";
    }

    // Getters and Setters
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }
}
