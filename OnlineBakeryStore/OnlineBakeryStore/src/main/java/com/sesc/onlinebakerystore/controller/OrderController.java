package com.sesc.onlinebakerystore.controller;

import com.sesc.onlinebakerystore.model.Order;
import com.sesc.onlinebakerystore.model.User;
import com.sesc.onlinebakerystore.service.OrderService;
import com.sesc.onlinebakerystore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Controller class to handle HTTP requests for order-related operations
@Controller
public class OrderController {

    // Inject OrderService for order operations
    @Autowired
    private OrderService orderService;

    // Inject UserService to verify user details
    @Autowired
    private UserService userService;

    // Handles POST request to place a new order
    @PostMapping("/order/place")
    public String placeOrder(
            @RequestParam String productName, // Product name from form
            @RequestParam int quantity, // Quantity from form
            @RequestParam String scheduledTime, // Scheduled time from form
            HttpSession session, // Session to check logged-in user
            Model model // Model to pass data to the view
    ) {
        // Get logged-in username from session
        String username = (String) session.getAttribute("loggedInUser");
        // Redirect to login if user is not logged in
        if (username == null) {
            return "redirect:/login";
        }

        try {
            // Verify user exists
            User user = userService.findByUsername(username);
            if (user == null) {
                session.invalidate(); // Invalidate session if user not found
                return "redirect:/login?error=User not found";
            }

            // Parse scheduledTime string into LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime scheduledDateTime = LocalDateTime.parse(scheduledTime, formatter);

            // Create new Order object with a random UUID
            Order order = new Order(
                    UUID.randomUUID().toString(),
                    productName,
                    quantity,
                    scheduledDateTime,
                    username
            );

            // Get all existing orders, add new order, sort, and save
            List<Order> existingOrders = orderService.getAllOrders();
            existingOrders.add(order);
            bubbleSortByScheduledTime(existingOrders); // Sort by scheduled time
            orderService.saveAllOrders(existingOrders);

            // Redirect to chart page on success
            return "redirect:/chart";
        } catch (IOException e) {
            // Handle file I/O errors
            model.addAttribute("error", "Error placing order: " + e.getMessage());
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("isAdmin", false);
            return "order"; // Return order view with error
        }
    }

    // Handles POST request to update an existing order
    @PostMapping("/order/update")
    public String updateOrder(
            @RequestParam String orderId, // ID of order to update
            @RequestParam String productName, // Updated product name
            @RequestParam int quantity, // Updated quantity
            @RequestParam String scheduledTime, // Updated scheduled time
            HttpSession session, // Session to check logged-in user
            Model model // Model to pass data to the view
    ) {
        // Get logged-in username from session
        String username = (String) session.getAttribute("loggedInUser");
        // Redirect to login if user is not logged in
        if (username == null) {
            return "redirect:/login";
        }

        try {
            // Verify user exists
            User user = userService.findByUsername(username);
            if (user == null) {
                session.invalidate(); // Invalidate session if user not found
                return "redirect:/login?error=User not found";
            }

            // Find existing order by ID
            Order existingOrder = orderService.findById(orderId);
            // Check if order exists and belongs to the user
            if (existingOrder == null || !existingOrder.getUsername().equals(username)) {
                model.addAttribute("error", "Order not found or access denied");
                return "redirect:/chart";
            }

            // Parse scheduledTime string into LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime scheduledDateTime = LocalDateTime.parse(scheduledTime, formatter);

            // Create updated Order object
            Order updatedOrder = new Order(
                    orderId,
                    productName,
                    quantity,
                    scheduledDateTime,
                    username
            );

            // Update order in service
            orderService.updateOrder(orderId, updatedOrder);
            // Redirect to chart page on success
            return "redirect:/chart";
        } catch (IOException e) {
            // Handle file I/O errors
            model.addAttribute("error", "Error updating order: " + e.getMessage());
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("isAdmin", false);
            return "edit-order"; // Return edit-order view with error
        }
    }

    // Handles POST request to delete an order
    @PostMapping("/order/delete")
    public String deleteOrder(
            @RequestParam String orderId, // ID of order to delete
            HttpSession session, // Session to check logged-in user
            Model model // Model to pass data to the view
    ) {
        // Get logged-in username from session
        String username = (String) session.getAttribute("loggedInUser");
        // Redirect to login if user is not logged in
        if (username == null) {
            return "redirect:/login";
        }

        try {
            // Verify user exists
            User user = userService.findByUsername(username);
            if (user == null) {
                session.invalidate(); // Invalidate session if user not found
                return "redirect:/login?error=User not found";
            }

            // Find order by ID
            Order order = orderService.findById(orderId);
            // Check if order exists and belongs to the user
            if (order == null || !order.getUsername().equals(username)) {
                model.addAttribute("error", "Order not found or access denied");
                return "redirect:/chart";
            }

            // Delete order
            orderService.deleteOrder(orderId);
            // Redirect to chart page on success
            return "redirect:/chart";
        } catch (IOException e) {
            // Handle file I/O errors
            model.addAttribute("error", "Error deleting order: " + e.getMessage());
            return "redirect:/chart";
        }
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
}