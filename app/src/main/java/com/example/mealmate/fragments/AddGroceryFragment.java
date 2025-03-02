package com.example.mealmate.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mealmate.R;
import com.example.mealmate.dao.GroceryDAO;
import com.example.mealmate.models.Grocery;

public class AddGroceryFragment extends Fragment {

    public interface OnGroceryAddedListener {
        void onGroceryAdded();
    }

    private EditText etGroceryName;
    private EditText etGroceryQuantity;
    private Spinner spinnerUnit;
    private EditText etStoreName;
    private Spinner spinnerCategory;
    private Button btnSaveGrocery;
    private long userId;

    private OnGroceryAddedListener callback;
    private boolean isPurchased;

    public static AddGroceryFragment newInstance(long userId) {
        AddGroceryFragment fragment = new AddGroceryFragment();
        Bundle args = new Bundle();
        args.putLong("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Try to get the parent fragment as our callback
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnGroceryAddedListener) {
            callback = (OnGroceryAddedListener) parentFragment;
        } else {
            // Try to get the activity as our callback
            try {
                callback = (OnGroceryAddedListener) context;
            } catch (ClassCastException e) {
                Log.e("AddGroceryFragment", "Parent activity/fragment must implement OnGroceryAddedListener", e);
            }
        }
    }

    public AddGroceryFragment() {
        this.isPurchased = false; // Default to false
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_grocery_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        etGroceryName = view.findViewById(R.id.etGroceryName);
        etGroceryQuantity = view.findViewById(R.id.etGroceryQuantity);
        spinnerUnit = view.findViewById(R.id.spinnerUnit);
        etStoreName = view.findViewById(R.id.etStoreName);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        btnSaveGrocery = view.findViewById(R.id.btnSaveGrocery);

        // Retrieve userId from arguments
        if (getArguments() != null) {
            userId = getArguments().getLong("userId", -1);
            Log.d("AddGroceryFragment", "userId: " + userId);
        }

        // Set up save button click listener
        btnSaveGrocery.setOnClickListener(v -> saveGrocery());
    }

    private void saveGrocery() {
        // Get values from form
        String name = etGroceryName.getText().toString().trim();
        String quantityStr = etGroceryQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String store = etStoreName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            etGroceryName.setError("Please enter grocery name");
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            etGroceryQuantity.setError("Please enter quantity");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            etGroceryQuantity.setError("Please enter a valid number");
            return;
        }

        // Create grocery object with userId
        Grocery grocery = new Grocery(name, quantity, unit, store, category, userId, false);

        // Save to database
        GroceryDAO groceryDAO = new GroceryDAO(requireContext());
        long id = groceryDAO.insertGrocery(grocery);

        if (id > 0) {
            Toast.makeText(requireContext(), "Grocery added successfully", Toast.LENGTH_SHORT).show();
            clearForm();

            // Notify callback before navigating back
            if (callback != null) {
                callback.onGroceryAdded();
            }

            requireActivity().getSupportFragmentManager().popBackStack(); // Go back to previous fragment
        } else {
            Toast.makeText(requireContext(), "Failed to add grocery", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etGroceryName.setText("");
        etGroceryQuantity.setText("");
        etStoreName.setText("");
        spinnerUnit.setSelection(0);
        spinnerCategory.setSelection(0);
    }
}