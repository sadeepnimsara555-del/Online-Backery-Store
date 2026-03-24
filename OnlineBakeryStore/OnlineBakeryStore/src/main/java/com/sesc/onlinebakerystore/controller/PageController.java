package com.sesc.onlinebakerystore.controller;

import com.sesc.onlinebakerystore.model.Order;
import com.sesc.onlinebakerystore.model.User;
import com.sesc.onlinebakerystore.service.OrderService;
import com.sesc.onlinebakerystore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        model.addAttribute("isLoggedIn", isLoggedIn != null ? isLoggedIn : false);
        model.addAttribute("isAdmin", isAdmin != null ? isAdmin : false);
        return "dashboard";
    }

    @GetMapping("/service")
    public String service(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        boolean isLoggedIn = username != null;
        boolean isAdmin = false;
        if (isLoggedIn) {
            try {
                User user = userService.findByUsername(username);
                if (user == null) {
                    session.invalidate();
                    return "redirect:/login?error=User not found";
                }
                isAdmin = user.isAdmin();
            } catch (IOException e) {
                isAdmin = false;
            }
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isAdmin", isAdmin);
        return "service";
    }

    @GetMapping("/aboutUs")
    public String aboutUs(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        boolean isLoggedIn = username != null;
        boolean isAdmin = false;
        if (isLoggedIn) {
            try {
                User user = userService.findByUsername(username);
                if (user == null) {
                    session.invalidate();
                    return "redirect:/login?error=User not found";
                }
                isAdmin = user.isAdmin();
            } catch (IOException e) {
                isAdmin = false;
            }
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isAdmin", isAdmin);
        return "aboutUs";
    }

    @GetMapping("/contactUs")
    public String contactUs(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        boolean isLoggedIn = username != null;
        boolean isAdmin = false;
        if (isLoggedIn) {
            try {
                User user = userService.findByUsername(username);
                if (user == null) {
                    session.invalidate();
                    return "redirect:/login?error=User not found";
                }
                isAdmin = user.isAdmin();
            } catch (IOException e) {
                isAdmin = false;
            }
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isAdmin", isAdmin);
        return "contactUs";
    }

    @GetMapping("/order")
    public String showOrderPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                session.invalidate();
                return "redirect:/login?error=User not found";
            }
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("isAdmin", user.isAdmin());
            return "order";
        } catch (IOException e) {
            session.invalidate();
            return "redirect:/login?error=Unable to fetch user data";
        }
    }

    @GetMapping("/chart")
    public String showChart(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                session.invalidate();
                return "redirect:/login?error=User not found";
            }
            List<Order> orders = orderService.getUserOrders(username);
            model.addAttribute("orders", orders);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("isAdmin", user.isAdmin());
            if (orders.isEmpty()) {
                model.addAttribute("error", "No orders found.");
            }
            return "chart";
        } catch (IOException e) {
            model.addAttribute("error", "Error retrieving orders: " + e.getMessage());
            model.addAttribute("orders", List.of());
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("isAdmin", false);
            return "chart";
        }
    }

    @GetMapping("/editchart")
    public String showEditOrderForm(@RequestParam String orderId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return "redirect:/login";
        }
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                session.invalidate();
                return "redirect:/login?error=User not found";
            }
            Order order = orderService.findById(orderId);
            if (order == null || !order.getUsername().equals(username)) {
                model.addAttribute("error", "Order not found or access denied");
                return "redirect:/chart";
            }
            model.addAttribute("order", order);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("isAdmin", user.isAdmin());
            return "edit-order";
        } catch (IOException e) {
            model.addAttribute("error", "Error retrieving order: " + e.getMessage());
            return "redirect:/chart";
        }
    }
}