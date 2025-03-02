package com.example.mealmate.databases;

public class UserTable {
    public static final String TABLE_NAME = "users";

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "phone TEXT NOT NULL, " +
                    "address TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL)";
}
