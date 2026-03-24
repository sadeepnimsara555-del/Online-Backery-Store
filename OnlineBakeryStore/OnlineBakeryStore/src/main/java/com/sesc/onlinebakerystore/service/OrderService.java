package com.sesc.onlinebakerystore.service;

import com.sesc.onlinebakerystore.model.Order;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

// Service class for managing order-related business logic and persistence
@Service
public class OrderService {
    // File path for storing orders
    private static final String OUTPUT_FILE = "src/main/resources/order.txt";
    // In-memory queue to store orders, synchronized with the file
    private Queue<Order> orderQueue = new LinkedBlockingQueue<>();

    // Constructor: Initializes the service by loading orders from file into the queue
    public OrderService() {
        try {
            loadOrdersIntoQueue();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize OrderService: " + e.getMessage(), e);
        }
    }

    // Loads orders from file into the in-memory queue
    private void loadOrdersIntoQueue() throws IOException {
        // Clear the existing queue
        orderQueue.clear();
        // Read orders from file and add to queue
        List<Order> orders = readOrders();
        orderQueue.addAll(orders);
    }

    // Reads all orders from order.txt, parsing each line into an Order object
    private List<Order> readOrders() throws IOException {
        List<Order> orders = new ArrayList<>();
        File file = new File(OUTPUT_FILE);
        // Return empty list if file doesn't exist
        if (!file.exists()) return orders;

        // Read file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;
                // Parse line into Order object
                orders.add(Order.fromString(line));
            }
        }
        return orders;
    }

    // Creates the output file (and its parent directories) if it doesn't exist
    private void createOutputFile() throws IOException {
        File file = new File(OUTPUT_FILE);
        file.getParentFile().mkdirs(); // Create parent directories if needed
        if (!file.exists()) {
            file.createNewFile(); // Create the file
        }
    }

    // Writes a list of orders to order.txt
    private void writeOrders(List<Order> orders) throws IOException {
        createOutputFile(); // Ensure file exists
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            // Write each order as a pipe-separated string
            for (Order order : orders) {
                writer.write(order.toString());
                writer.newLine();
            }
        }
    }

    // Retrieves all orders for a specific user, sorted by scheduled time
    public List<Order> getUserOrders(String username) throws IOException {
        List<Order> allOrders = readOrders();
        List<Order> userOrders = new ArrayList<>();
        // Filter orders by username
        for (Order order : allOrders) {
            if (order.getUsername().equals(username)) {
                userOrders.add(order);
            }
        }
        // Sort orders by scheduled time
        bubbleSortByScheduledTime(userOrders);
        return userOrders;
    }

    // Sorts orders by scheduled time using bubble sort
    private void bubbleSortByScheduledTime(List<Order> orders) {
        int n = orders.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Swap orders if the current order's time is after the next order's time
                if (orders.get(j).getScheduledTime().isAfter(orders.get(j + 1).getScheduledTime())) {
                    Order temp = orders.get(j);
                    orders.set(j, orders.get(j + 1));
                    orders.set(j + 1, temp);
                }
            }
        }
    }

    // Adds a new order to the file and queue
    public void addOrder(Order order) throws IOException {
        List<Order> orders = readOrders();
        orders.add(order); // Add new order
        bubbleSortByScheduledTime(orders); // Sort by scheduled time
        orderQueue.offer(order); // Add to in-memory queue
        writeOrders(orders); // Write to file
    }

    // Updates an existing order by ID
    public void updateOrder(String orderId, Order updatedOrder) throws IOException {
        List<Order> orders = readOrders();
        boolean found = false;
        // Find and replace the order with the given ID
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(orderId)) {
                orders.set(i, updatedOrder);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        bubbleSortByScheduledTime(orders); // Sort updated list
        writeOrders(orders); // Write to file
        loadOrdersIntoQueue(); // Reload queue
    }

    // Deletes an order by ID
    public void deleteOrder(String orderId) throws IOException {
        List<Order> orders = readOrders();
        // Remove order with matching ID
        orders.removeIf(order -> order.getId().equals(orderId));
        bubbleSortByScheduledTime(orders); // Sort remaining orders
        writeOrders(orders); // Write to file
        loadOrdersIntoQueue(); // Reload queue
    }

    // Finds an order by ID
    public Order findById(String orderId) throws IOException {
        List<Order> orders = readOrders();
        // Return first order matching the ID, or null if not found
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    // Retrieves all orders from the file
    public List<Order> getAllOrders() throws IOException {
        return readOrders();
    }

    // Saves a list of orders to the file and updates the queue
    public void saveAllOrders(List<Order> orders) throws IOException {
        bubbleSortByScheduledTime(orders); // Sort orders
        writeOrders(orders); // Write to file
        loadOrdersIntoQueue(); // Reload queue
    }
}