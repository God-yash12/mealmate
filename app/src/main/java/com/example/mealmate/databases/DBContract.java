package com.example.mealmate.databases;

import android.provider.BaseColumns;

public final class DBContract {
    // Private constructor to prevent instantiation
    private DBContract() {}

    /* Inner class that defines the user table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }
}
