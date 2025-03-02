package com.example.mealmate.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mealmate.databases.MealContract;
import com.example.mealmate.models.Meal;
import com.example.mealmate.databases.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class MealDAO {
    private static final String TAG = "MealDAO";
    private SQLiteDatabase db;
    private final DBHelper dbHelper;

    public MealDAO(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert a new meal into the database
    public long insertMeal(Meal meal) {
        ContentValues values = new ContentValues();
        values.put(MealContract.MealEntry.COLUMN_MEAL_NAME, meal.getMealName());
        values.put(MealContract.MealEntry.COLUMN_INGREDIENTS, meal.getIngredients());
        values.put(MealContract.MealEntry.COLUMN_PREP_TIME, meal.getPrepTime());
        values.put(MealContract.MealEntry.COLUMN_COOK_TIME, meal.getCookTime());
        values.put(MealContract.MealEntry.COLUMN_MEAL_DATE, meal.getMealDate());
        values.put(MealContract.MealEntry.COLUMN_MEAL_TYPE, meal.getMealType());
        values.put(MealContract.MealEntry.COLUMN_USER_ID, meal.getUserId());

        return db.insert(MealContract.MealEntry.TABLE_NAME, null, values);
    }

//    public Cursor getAllMeals() {
//        String[] projection = {
//                MealContract.MealEntry._ID,
//                MealContract.MealEntry.COLUMN_MEAL_NAME,
//                MealContract.MealEntry.COLUMN_INGREDIENTS,
//                MealContract.MealEntry.COLUMN_PREP_TIME,
//                MealContract.MealEntry.COLUMN_COOK_TIME,
//                MealContract.MealEntry.COLUMN_MEAL_DATE,
//                MealContract.MealEntry.COLUMN_MEAL_TYPE,
//                MealContract.MealEntry.COLUMN_USER_ID
//        };
//        return db.query(
//                MealContract.MealEntry.TABLE_NAME,
//                projection,
//                null, null, null, null, null
//        );
//    }

    // Add this method to your MealDAO class
    public Cursor getMealsByUserId(long userId) {
        String selection = MealContract.MealEntry.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        return db.query(
                MealContract.MealEntry.TABLE_NAME,
                null,  // Get all columns
                selection,
                selectionArgs,
                null,
                null,
                MealContract.MealEntry.COLUMN_MEAL_DATE + " DESC"  // Sort by date, newest first
        );
    }


    public boolean deleteMeal(long mealId) {
        String selection = MealContract.MealEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(mealId) };

        int rowsDeleted = db.delete(
                MealContract.MealEntry.TABLE_NAME,
                selection,
                selectionArgs
        );

        return rowsDeleted > 0;
    }

    // Update cursorToMealList to include the meal ID
    @SuppressLint("Range")
    public List<Meal> cursorToMealList(Cursor cursor) {
        List<Meal> mealList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MealContract.MealEntry._ID));
                String mealName = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_INGREDIENTS));
                String prepTime = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_PREP_TIME));
                String cookTime = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_COOK_TIME));
                String mealDate = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_DATE));
                String mealType = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_TYPE));
                long mealUserId = cursor.getLong(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_USER_ID));

                Meal meal = new Meal(id, mealName, ingredients, prepTime, cookTime, mealDate, mealType, mealUserId);
                mealList.add(meal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return mealList;
    }

    // Method to retrieve all meals for a user as a List
    public List<Meal> getAllMealsByUserId(long userId) {
        Cursor cursor = getMealsByUserId(userId);
        return cursorToMealList(cursor);
    }

    public void logAllMeals() {
        Cursor cursor = db.query(
                MealContract.MealEntry.TABLE_NAME,
                null, null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MealContract.MealEntry._ID));
                @SuppressLint("Range") String mealName = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_NAME));
                @SuppressLint("Range") String ingredients = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_INGREDIENTS));
                @SuppressLint("Range") String mealDate = cursor.getString(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_DATE));
                @SuppressLint("Range") long userId = cursor.getLong(cursor.getColumnIndex(MealContract.MealEntry.COLUMN_USER_ID));

                Log.d("MealDAO", "Meal ID: " + id +
                        ", Meal Name: " + mealName +
                        ", Ingredients: " + ingredients +
                        ", Meal Date: " + mealDate +
                        ", User ID: " + userId);
            } while (cursor.moveToNext());
        } else {
            Log.d("MealDAO", "No meals found in database.");
        }

        cursor.close();
    }
}
