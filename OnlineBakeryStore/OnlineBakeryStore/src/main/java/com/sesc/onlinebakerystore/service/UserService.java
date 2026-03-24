package com.sesc.onlinebakerystore.service;

import com.sesc.onlinebakerystore.model.Admin;
import com.sesc.onlinebakerystore.model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final String FILE_PATH = "src/main/resources/Profile_Details.txt";

    public void saveUser(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("Username: " + user.getUsername() + ", Email: " + user.getEmail() + ", Password: " + user.getPassword() +
                    ", Full Name: " + (user.getFullname() != null ? user.getFullname() : "") +
                    ", Telephone: " + (user.getTelephoneNo() != null ? user.getTelephoneNo() : "") +
                    ", IsAdmin: " + user.isAdmin());
            writer.newLine();
        }
    }

    public User authenticate(String username, String password) throws IOException {
        List<User> users = readUsers();
        System.out.println("Authenticating user: " + username);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User findByUsername(String username) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> readUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("Username: (.*?)(?:, Email: (.*?))?(?:, Password: (.*?))?(?:, Full Name: (.*?))?(?:, Telephone: (.*?))?(?:, IsAdmin: (.*?))?");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String username = matcher.group(1) != null ? matcher.group(1).trim() : "";
                    String email = matcher.group(2) != null ? matcher.group(2).trim() : "";
                    String password = matcher.group(3) != null ? matcher.group(3).trim() : "";
                    String fullname = matcher.group(4) != null ? matcher.group(4).trim() : "";
                    String telephoneNo = matcher.group(5) != null ? matcher.group(5).trim() : "";
                    boolean isAdmin = matcher.group(6) != null && Boolean.parseBoolean(matcher.group(6).trim());

                    User user = isAdmin ? new Admin(username, email, password, fullname, telephoneNo, new ArrayList<>())
                            : new User(username, email, password, fullname, telephoneNo, false);
                    users.add(user);
                }
            }
        }
        return users;
    }

    public void deleteUser(String username) throws IOException {
        List<User> users = readUsers();
        users.removeIf(user -> user.getUsername().equals(username));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write("Username: " + user.getUsername() + ", Email: " + user.getEmail() + ", Password: " + user.getPassword() +
                        ", Full Name: " + (user.getFullname() != null ? user.getFullname() : "") +
                        ", Telephone: " + (user.getTelephoneNo() != null ? user.getTelephoneNo() : "") +
                        ", IsAdmin: " + user.isAdmin());
                writer.newLine();
            }
        }
    }

    public void updateUser(String oldUsername, User updatedUser) throws IOException {
        List<User> users = readUsers();
        // Remove the old user
        users.removeIf(user -> user.getUsername().equals(oldUsername));
        // Add the updated user
        users.add(updatedUser);
        // Rewrite the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write("Username: " + user.getUsername() + ", Email: " + user.getEmail() + ", Password: " + user.getPassword() +
                        ", Full Name: " + (user.getFullname() != null ? user.getFullname() : "") +
                        ", Telephone: " + (user.getTelephoneNo() != null ? user.getTelephoneNo() : "") +
                        ", IsAdmin: " + user.isAdmin());
                writer.newLine();
            }
        }
    }

}