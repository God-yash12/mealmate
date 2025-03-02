package com.example.mealmate.models;

public class Meal {
    private long id;
    private String mealName;
    private String ingredients;
    private String prepTime;
    private String cookTime;
    private String mealDate;
    private String mealType;
    private long userId;

    // Constructor for creating a new meal (no ID yet)
    public Meal( String mealName, String ingredients, String prepTime, String cookTime, String mealDate, String mealType, long userId) {
        this.mealName = mealName;
        this.ingredients = ingredients;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.userId = userId;
    }

    // Constructor for retrieving an existing meal (with ID)
    public Meal(long id, String mealName, String ingredients, String prepTime, String cookTime, String mealDate, String mealType, long userId) {
        this.id = id;
        this.mealName = mealName;
        this.ingredients = ingredients;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getMealName() {
        return mealName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public String getMealDate() {
        return mealDate;
    }

    public String getMealType() {
        return mealType;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", mealName='" + mealName + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", prepTime='" + prepTime + '\'' +
                ", cookTime='" + cookTime + '\'' +
                ", mealDate='" + mealDate + '\'' +
                ", mealType='" + mealType + '\'' +
                ", userId=" + userId +
                '}';
    }
}