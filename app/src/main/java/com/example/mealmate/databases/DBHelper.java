package com.example.mealmate.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MealMate.db";
    private static final int DATABASE_VERSION = 6;

    // Table Creation Queries
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBContract.UserEntry.COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    DBContract.UserEntry.COLUMN_PHONE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    DBContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL)";


    private static final String CREATE_MEAL_TABLE =
            "CREATE TABLE " + MealContract.MealEntry.TABLE_NAME + " (" +
                    MealContract.MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MealContract.MealEntry.COLUMN_MEAL_NAME + " TEXT, " +
                    MealContract.MealEntry.COLUMN_INGREDIENTS + " TEXT, " +
                    MealContract.MealEntry.COLUMN_PREP_TIME + " TEXT, " +
                    MealContract.MealEntry.COLUMN_COOK_TIME + " TEXT, " +
                    MealContract.MealEntry.COLUMN_MEAL_DATE + " TEXT, " +
                    MealContract.MealEntry.COLUMN_MEAL_TYPE + " TEXT," +
                    MealContract.MealEntry.COLUMN_USER_ID + " INTEGER, " +
                    "FOREIGN KEY(" + MealContract.MealEntry.COLUMN_USER_ID + ") REFERENCES " + DBContract.UserEntry.TABLE_NAME + "(" + DBContract.UserEntry._ID + "))";


//    grocery table
private static final String CREATE_GROCERY_TABLE =
        "CREATE TABLE " + GroceryContract.GroceryEntry.TABLE_NAME + " (" +
                GroceryContract.GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryContract.GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GroceryContract.GroceryEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                GroceryContract.GroceryEntry.COLUMN_UNIT + " TEXT NOT NULL, " +
                GroceryContract.GroceryEntry.COLUMN_STORE + " TEXT, " +
                GroceryContract.GroceryEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                GroceryContract.GroceryEntry.COLUMN_IS_PURCHASED + " BOOLEAN, " +
                GroceryContract.GroceryEntry.COLUMN_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + GroceryContract.GroceryEntry.COLUMN_USER_ID + ") REFERENCES " +
                DBContract.UserEntry.TABLE_NAME + "(" + DBContract.UserEntry._ID + "));";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create both User and Meal tables
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MEAL_TABLE);
        db.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables and create them again on version upgrade
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MealContract.MealEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GroceryContract.GroceryEntry.TABLE_NAME);
        onCreate(db);
    }
}
