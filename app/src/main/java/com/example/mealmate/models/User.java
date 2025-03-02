package com.example.mealmate.models;

public class User {
    private int id;
    private String username;
    private String phone;
    private String address;
    private String email;
    private String password;

    // Default constructor
    public User(int anInt, String string, String cursorString, String s, String string1, String cursorString1, String s1) {}

    // Constructor without id (for new users)
    public User(String username, String phone, String address, String email, String password) {
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    // Constructor with id (for existing users)
    public User(int id, String username, String phone, String address, String email, String password) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
