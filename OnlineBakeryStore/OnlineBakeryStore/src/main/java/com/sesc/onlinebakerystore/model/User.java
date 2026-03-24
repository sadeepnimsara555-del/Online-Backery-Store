package com.sesc.onlinebakerystore.model;

// Class representing a generic User entity (base class)
public class User {
    protected String username;
    protected String email;
    protected String password;
    protected String fullname;
    protected String telephoneNo;
    protected boolean isAdmin; // Flag to indicate if the user is an admin

    // Default constructor
    public User() {}

    // Constructor with core fields (for backward compatibility)
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = false; // Default to non-admin
    }

    // Constructor with all fields
    public User(String username, String email, String password, String fullname, String telephoneNo, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.telephoneNo = telephoneNo;
        this.isAdmin = isAdmin;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}