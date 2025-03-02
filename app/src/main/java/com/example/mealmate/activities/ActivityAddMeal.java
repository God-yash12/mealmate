//package com.example.mealmate.activities;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.mealmate.R;
//import com.example.mealmate.dao.MealDAO;
//import com.example.mealmate.models.Meal;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//public class ActivityAddMeal extends AppCompatActivity {
//    private static final String TAG = "ActivityAddMeal";
//
//    private long userId;
//    private Button btnSubmitMeal, btnAddIngredient;
//    private EditText etMealDate, etMealName, etPrepTime, etCookTime;
//    private Spinner spinnerMealType;
//    private LinearLayout layoutIngredients;
//    private List<View> ingredientViewsList;
//    private String selectedMealType;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_meal);
//
//        // Retrieve user ID from Intent
//        userId = getIntent().getLongExtra("userId", -1);
//        if (userId == -1) {
//            showToast("User ID not found!");
//            finish();
//            return;
//        }
//        Log.d(TAG, "User ID received: " + userId);
//
//        initViews();
//        setupMealTypeSpinner();
//        setupListeners();
//    }
//
//    private void initViews() {
//        spinnerMealType = findViewById(R.id.spinnerMealType);
//        btnSubmitMeal = findViewById(R.id.btnSubmitMeal);
//        btnAddIngredient = findViewById(R.id.btnAddIngredient);
//        layoutIngredients = findViewById(R.id.layoutIngredients);
//        etMealDate = findViewById(R.id.etMealDate);
//        etMealName = findViewById(R.id.etAdditionalInfo);
//        etPrepTime = findViewById(R.id.etPrepTime);
//        etCookTime = findViewById(R.id.etCookTime);
//        ingredientViewsList = new ArrayList<>();
//    }
//
//    private void setupListeners() {
//        etMealDate.setOnClickListener(v -> showDatePickerDialog());
//        btnAddIngredient.setOnClickListener(v -> addIngredientField());
//        btnSubmitMeal.setOnClickListener(v -> submitMealData());
//    }
//
//    private void showDatePickerDialog() {
//        final Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                (view, year, month, day) -> {
//                    String selectedDate = day + "/" + (month + 1) + "/" + year;
//                    etMealDate.setText(selectedDate);
//                    Log.d(TAG, "Date selected: " + selectedDate);
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }
//
//    private void setupMealTypeSpinner() {
//        String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snack"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealTypes);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerMealType.setAdapter(adapter);
//
//        spinnerMealType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedMealType = mealTypes[position];
//                Log.d(TAG, "Meal type selected: " + selectedMealType);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                selectedMealType = null;
//                Log.d(TAG, "No meal type selected");
//            }
//        });
//    }
//
//    private void addIngredientField() {
//        LinearLayout ingredientLayout = new LinearLayout(this);
//        ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
//        ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//        ingredientLayout.setPadding(0, 10, 0, 10);
//
//        EditText newIngredientField = new EditText(this);
//        newIngredientField.setHint("Enter Ingredient");
//        newIngredientField.setLayoutParams(new LinearLayout.LayoutParams(
//                0,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1
//        ));
//
//        Button btnDelete = new Button(this);
//        btnDelete.setText("❌");
//        btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//        btnDelete.setOnClickListener(v -> {
//            layoutIngredients.removeView(ingredientLayout);
//            ingredientViewsList.remove(ingredientLayout);
//            Log.d(TAG, "Ingredient removed");
//        });
//
//        ingredientLayout.addView(newIngredientField);
//        ingredientLayout.addView(btnDelete);
//        layoutIngredients.addView(ingredientLayout);
//        ingredientViewsList.add(ingredientLayout);
//
//        Log.d(TAG, "New ingredient added");
//    }
//
//    private List<String> getIngredientList() {
//        List<String> ingredients = new ArrayList<>();
//        for (View view : ingredientViewsList) {
//            EditText etIngredient = (EditText) ((LinearLayout) view).getChildAt(0);
//            String ingredient = etIngredient.getText().toString().trim();
//            if (!ingredient.isEmpty()) {
//                ingredients.add(ingredient);
//            }
//        }
//        return ingredients;
//    }
//
//    private void submitMealData() {
//        // Validate input fields
//        String mealName = etMealName.getText().toString().trim();
//        if (mealName.isEmpty()) {
//            showToast("Please enter meal name/info");
//            return;
//        }
//
//        String mealDate = etMealDate.getText().toString().trim();
//        if (mealDate.isEmpty()) {
//            showToast("Please select a meal date");
//            return;
//        }
//
//        String prepTime = etPrepTime.getText().toString().trim();
//        String cookTime = etCookTime.getText().toString().trim();
//
//        List<String> ingredients = getIngredientList();
//        if (ingredients.isEmpty()) {
//            showToast("Please add at least one ingredient");
//            return;
//        }
//
//        // Convert ingredient list to a comma-separated string
//        String ingredientString = String.join(", ", ingredients);
//
//        Log.d(TAG, "Meal Data: " + mealName + ", " + mealDate + ", " + prepTime + ", " + cookTime + ", " + ingredientString + ", " + selectedMealType + ", User ID: " + userId);
//
//        // Save meal to database
//        Meal newMeal = new Meal(mealName, ingredientString, prepTime, cookTime, mealDate, selectedMealType, userId);
//        try {
//            MealDAO mealDAO = new MealDAO(this);
//            long resultId = mealDAO.insertMeal(newMeal);
//
//            if (resultId != -1) {
//                showToast("Meal added successfully!");
//                finish();
//            } else {
//                showToast("Failed to save meal");
//            }
//        } catch (Exception e) {
//            showToast("Error: " + e.getMessage());
//        }
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}
