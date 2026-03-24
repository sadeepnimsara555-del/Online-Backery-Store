package com.sesc.onlinebakerystore.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Model class representing an order in the online bakery store
public class Order {
    // Unique identifier for the order (typically a UUID)
    private String id;
    // Name of the product being ordered (e.g., "Chocolate Cake")
    private String productName;
    // Number of items ordered
    private int quantity;
    // Scheduled delivery or pickup time for the order
    private LocalDateTime scheduledTime;
    // Username of the user who placed the order
    private String username;

    // Constructor to initialize an Order object with all fields
    public Order(String id, String productName, int quantity, LocalDateTime scheduledTime, String username) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.scheduledTime = scheduledTime;
        this.username = username;
    }

    // Getters and Setters for accessing and modifying fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // Converts Order object to a pipe-separated string for file storage
    // Format: id|productName|quantity|scheduledTime|username
    public String toString() {
        // Define the date-time format for scheduledTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return String.format("%s|%s|%d|%s|%s", id, productName, quantity, scheduledTime.format(formatter), username);
    }

    // Static method to parse a pipe-separated string into an Order object
    // Used when reading orders from a file
    public static Order fromString(String line) {
        // Split the input string by the pipe delimiter
        String[] parts = line.split("\\|");
        // Validate that the string has exactly 5 parts
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid order format: " + line);
        }
        // Parse scheduledTime using the defined format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return new Order(
                parts[0], // id
                parts[1], // productName
                Integer.parseInt(parts[2]), // quantity
                LocalDateTime.parse(parts[3], formatter), // scheduledTime
                parts[4]); // username

    }
}