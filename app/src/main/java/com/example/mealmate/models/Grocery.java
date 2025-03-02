package com.example.mealmate.models;

public class Grocery {
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String store;
    private String category;
    private long userId;
    private  boolean isPurchased;

    // Default constructor
    public Grocery() {
    }

    // Constructor with parameters
    public Grocery(String name, double quantity, String unit, String store, String category, long userId, boolean isPurchased) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.store = store;
        this.category = category;
        this.userId = userId;
        this.isPurchased = false;
    }

    public Grocery(long id, String name, double quantity, String unit, String store, String category, long userId, boolean isPurchased) {
        this.id = (int) id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.store = store;
        this.category = category;
        this.userId = userId;
        this.isPurchased = isPurchased;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    // Corrected toString method
    @Override
    public String toString() {
        return "Grocery{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", store='" + store + '\'' +
                ", category='" + category + '\'' +
                ", userId=" + userId +
                ", isPurchased=" + isPurchased +
                '}';
    }
}
