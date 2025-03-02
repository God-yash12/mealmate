package com.example.mealmate.databases;

import android.provider.BaseColumns;

public final class MealContract {
    private MealContract() {}

    public static class MealEntry implements BaseColumns {
        public static final String TABLE_NAME = "meals";
        public static final String COLUMN_MEAL_NAME = "meal_name";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_PREP_TIME = "prep_time";
        public static final String COLUMN_COOK_TIME = "cook_time";
        public static final String COLUMN_MEAL_DATE = "meal_date";
        public static final String COLUMN_MEAL_TYPE = "meal_type";
        public static final String COLUMN_USER_ID = "user_id";

    }
}
