package com.example.mealmate.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mealmate.R;
import com.example.mealmate.dao.MealDAO;
import com.example.mealmate.models.Meal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMealFragment extends Fragment {
    private static final String TAG = "AddMealFragment";
    private static final String ARG_USER_ID = "userId";

    private long userId;
    private Button btnSubmitMeal, btnAddIngredient;
    private EditText etMealDate, etMealName, etPrepTime, etCookTime;
    private Spinner spinnerMealType;
    private LinearLayout layoutIngredients;
    private List<View> ingredientViewsList;
    private String selectedMealType;

    public static AddMealFragment newInstance(long userId) {
        AddMealFragment fragment = new AddMealFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_meal, container, false);

        if (getArguments() != null) {
            userId = getArguments().getLong(ARG_USER_ID, -1);
        }
        if (userId == -1) {
            showToast("User ID not found!");
            return view;
        }
        Log.d(TAG, "User ID received: " + userId);

        initViews(view);
        setupMealTypeSpinner();
        setupListeners();
        return view;
    }

    private void initViews(View view) {
        spinnerMealType = view.findViewById(R.id.spinnerMealType);
        btnSubmitMeal = view.findViewById(R.id.btnSubmitMeal);
        btnAddIngredient = view.findViewById(R.id.btnAddIngredient);
        layoutIngredients = view.findViewById(R.id.layoutIngredients);
        etMealDate = view.findViewById(R.id.etMealDate);
        etMealName = view.findViewById(R.id.etAdditionalInfo);
        etPrepTime = view.findViewById(R.id.etPrepTime);
        etCookTime = view.findViewById(R.id.etCookTime);
        ingredientViewsList = new ArrayList<>();
    }

    private void setupListeners() {
        etMealDate.setOnClickListener(v -> showDatePickerDialog());
        btnAddIngredient.setOnClickListener(v -> addIngredientField());
        btnSubmitMeal.setOnClickListener(v -> submitMealData());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, day) -> {
                    String selectedDate = day + "/" + (month + 1) + "/" + year;
                    etMealDate.setText(selectedDate);
                    Log.d(TAG, "Date selected: " + selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupMealTypeSpinner() {
        String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snack"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mealTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);

        spinnerMealType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMealType = mealTypes[position];
                Log.d(TAG, "Meal type selected: " + selectedMealType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMealType = null;
                Log.d(TAG, "No meal type selected");
            }
        });
    }

    private void addIngredientField() {
        LinearLayout ingredientLayout = new LinearLayout(getContext());
        ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
        ingredientLayout.setPadding(0, 10, 0, 10);

        EditText newIngredientField = new EditText(getContext());
        newIngredientField.setHint("Enter Ingredient");
        newIngredientField.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        Button btnDelete = new Button(getContext());
        btnDelete.setText("❌");
        btnDelete.setOnClickListener(v -> {
            layoutIngredients.removeView(ingredientLayout);
            ingredientViewsList.remove(ingredientLayout);
            Log.d(TAG, "Ingredient removed");
        });

        ingredientLayout.addView(newIngredientField);
        ingredientLayout.addView(btnDelete);
        layoutIngredients.addView(ingredientLayout);
        ingredientViewsList.add(ingredientLayout);
    }

    private List<String> getIngredientList() {
        List<String> ingredients = new ArrayList<>();
        for (View view : ingredientViewsList) {
            EditText etIngredient = (EditText) ((LinearLayout) view).getChildAt(0);
            String ingredient = etIngredient.getText().toString().trim();
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    private void submitMealData() {
        String mealName = etMealName.getText().toString().trim();
        if (mealName.isEmpty()) {
            showToast("Please enter meal name/info");
            return;
        }
        String mealDate = etMealDate.getText().toString().trim();
        if (mealDate.isEmpty()) {
            showToast("Please select a meal date");
            return;
        }
        String prepTime = etPrepTime.getText().toString().trim();
        String cookTime = etCookTime.getText().toString().trim();
        List<String> ingredients = getIngredientList();
        if (ingredients.isEmpty()) {
            showToast("Please add at least one ingredient");
            return;
        }
        String ingredientString = String.join(", ", ingredients);

        Meal newMeal = new Meal(mealName, ingredientString, prepTime, cookTime, mealDate, selectedMealType, userId);
        try {
            MealDAO mealDAO = new MealDAO(getContext());
            long resultId = mealDAO.insertMeal(newMeal);
            if (resultId != -1) {
                showToast("Meal added successfully!");
                getActivity().getSupportFragmentManager().popBackStack();
            } else {
                showToast("Failed to save meal");
            }
        } catch (Exception e) {
            showToast("Error: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
