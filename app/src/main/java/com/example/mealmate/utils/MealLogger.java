package com.example.mealmate.utils;

import android.util.Log;

public class MealLogger {
    private static final String TAG = "MealDebug";

    public static void logMealData(String mealName, String mealDate, String mealType) {
        Log.d(TAG, "Meal Name: " + mealName);
        Log.d(TAG, "Meal Date: " + mealDate);
        Log.d(TAG, "Meal Type: " + mealType);
    }

    public static void logMealStatus(boolean isAdded) {
        if (isAdded) {
            Log.d(TAG, "Meal successfully added to the database.");
        } else {
            Log.e(TAG, "Failed to add meal to the database.");
        }
    }
}
