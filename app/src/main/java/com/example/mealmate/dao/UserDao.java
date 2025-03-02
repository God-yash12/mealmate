package com.example.mealmate.dao;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mealmate.databases.DBContract;
import com.example.mealmate.databases.DBHelper;
import com.example.mealmate.models.User;

public class UserDao {
    private final DBHelper dbHelper;
    private SQLiteDatabase database;

    public UserDao(Context context) {
        dbHelper = new DBHelper(context);
        open();
    }

    public void open() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        dbHelper.close();
    }

    public long registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // ✅ Corrected

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("phone", user.getPhone());
        values.put("address", user.getAddress());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());

        long userId = db.insert("users", null, values);
        db.close();

        if (userId != -1) {
            user.setId((int) userId);
        }

        return userId;
    }


    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        SQLiteDatabase db = database;
        Cursor cursor = db.query(
                DBContract.UserEntry.TABLE_NAME,
                null,
                DBContract.UserEntry.COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null
        );
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PASSWORD))
            );
            cursor.close();
        }
        return user;
    }

    // Add this method to your UserDao class
    public int updateUserProfileImage(User user) {
        ContentValues values = new ContentValues();

        return database.update(
                DBContract.UserEntry.TABLE_NAME,
                values,
                DBContract.UserEntry._ID + " = ?",
                new String[]{String.valueOf(user.getId())}
        );
    }
    @SuppressLint("Range")
    public User getUserById(long id) {
        SQLiteDatabase db = database;
        Cursor cursor = db.query(
                DBContract.UserEntry.TABLE_NAME,
                null,
                DBContract.UserEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PASSWORD))
            );
        }
        cursor.close();
        return user;
    }

}
