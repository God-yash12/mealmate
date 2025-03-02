package com.example.mealmate.databases;


import android.provider.BaseColumns;

public class GroceryContract {

    private GroceryContract() {}

    public static class GroceryEntry implements BaseColumns {
        public static final String TABLE_NAME = "grocery";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_STORE = "store";
        public static final String COLUMN_CATEGORY = "category";

        public static final String COLUMN_IS_PURCHASED = "is_purchased";

        public static final String COLUMN_USER_ID = "user_id";
    }
}
