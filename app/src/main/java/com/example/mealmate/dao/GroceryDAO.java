package com.example.mealmate.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mealmate.databases.GroceryContract;
import com.example.mealmate.models.Grocery;
import com.example.mealmate.databases.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class GroceryDAO {
    private static final String TABLE_GROCERY = GroceryContract.GroceryEntry.TABLE_NAME;
    private static final String COLUMN_ID = GroceryContract.GroceryEntry._ID;
    private static final String COLUMN_NAME = GroceryContract.GroceryEntry.COLUMN_NAME;
    private static final String COLUMN_QUANTITY = GroceryContract.GroceryEntry.COLUMN_QUANTITY;
    private static final String COLUMN_UNIT = GroceryContract.GroceryEntry.COLUMN_UNIT;
    private static final String COLUMN_STORE = GroceryContract.GroceryEntry.COLUMN_STORE;
    private static final String COLUMN_CATEGORY = GroceryContract.GroceryEntry.COLUMN_CATEGORY;

    private static final String COLUMN_USER_ID = GroceryContract.GroceryEntry.COLUMN_USER_ID;

    private final DBHelper dbHelper;

    public GroceryDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Insert a new grocery item
    public long insertGrocery(Grocery grocery) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, grocery.getName());
        values.put(COLUMN_QUANTITY, grocery.getQuantity());
        values.put(COLUMN_UNIT, grocery.getUnit());
        values.put(COLUMN_STORE, grocery.getStore());
        values.put(COLUMN_CATEGORY, grocery.getCategory());
        values.put(COLUMN_USER_ID, grocery.getUserId());
        values.put(GroceryContract.GroceryEntry.COLUMN_IS_PURCHASED, grocery.isPurchased() ? 1 : 0);

        long id = db.insert(TABLE_GROCERY, null, values);
        db.close();
        return id;
    }

    // Get all grocery items for a specific user
    public List<Grocery> getAllGroceriesForUser(long userId) {
        List<Grocery> groceryList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GROCERY + " WHERE " + COLUMN_USER_ID + " = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Grocery grocery = new Grocery();
                grocery.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                grocery.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                grocery.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                grocery.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT)));
                grocery.setStore(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE)));
                grocery.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                grocery.setPurchased(cursor.getInt(cursor.getColumnIndexOrThrow(
                        GroceryContract.GroceryEntry.COLUMN_IS_PURCHASED)) == 1);

                groceryList.add(grocery);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return groceryList;
    }

    // Add to your GroceryDAO class
    public boolean deleteGrocery(long groceryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(TABLE_GROCERY, COLUMN_ID + " = ?", new String[]{String.valueOf(groceryId)});
        db.close();
        return result > 0;
    }

    // Add to your GroceryDAO class
    public boolean updateGroceryPurchaseStatus(long groceryId, boolean isPurchased) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("is_purchased", isPurchased ? 1 : 0); // Assuming you'll add this column to your table

        int result = db.update(TABLE_GROCERY, values, COLUMN_ID + " = ?", new String[]{String.valueOf(groceryId)});
        db.close();
        return isPurchased;
    }
}
