package com.example.mealmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
//import com.example.mealmate.activities.ActivityAddMeal;
import com.example.mealmate.adapters.MealAdapter;
import com.example.mealmate.fragments.AddMealFragment;
import com.example.mealmate.dao.MealDAO;
import com.example.mealmate.models.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MealPlannerFragment extends Fragment {

    private static final String TAG = "MealPlannerFragment";
    private long userId;
    private RecyclerView recyclerViewMeals;
    private TextView tvNoMeals;
    private FloatingActionButton fabAddMeal;
    private List<Meal> mealList;
    private MealAdapter mealAdapter;
    private MealDAO mealDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meal_planner, container, false);

        Log.d(TAG, "onCreateView: Initializing MealPlannerFragment");

        // Initialize views
        recyclerViewMeals = rootView.findViewById(R.id.recyclerViewMeals);
        tvNoMeals = rootView.findViewById(R.id.tvNoMeals);
        fabAddMeal = rootView.findViewById(R.id.fabAddMeal);

        // Initialize database DAO
        mealDAO = new MealDAO(getContext());

        // Setup RecyclerView
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getContext()));
        mealList = new ArrayList<>();
        mealAdapter = new MealAdapter(getContext(), mealList);
        recyclerViewMeals.setAdapter(mealAdapter);

        // Retrieve the userId from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("userId", -1); // Default value is -1 if not found
            if (userId != -1) {
                Log.d(TAG, "User ID in MealPlannerFragment: " + userId);
                loadUserMeals();
            } else {
                Log.d(TAG, "User ID not found in MealPlannerFragment");
                tvNoMeals.setText("Error: User ID not found");
                tvNoMeals.setVisibility(View.VISIBLE);
            }
        }

//        // Open Add Meal Activity when FAB is clicked
//        fabAddMeal.setOnClickListener(v -> {
//            Log.d(TAG, "FAB clicked: Opening ActivityAddMeal with userId: " + userId);
//            Intent intent = new Intent(getActivity(), ActivityAddMeal.class);
//            intent.putExtra("userId", userId);
//            Log.d(TAG, "User ID in MealPlannerFragment go to add meal: " + userId);
//            startActivity(intent);
//        });

        fabAddMeal.setOnClickListener(v -> {
            Log.d(TAG, "FAB clicked: Opening AddMealFragment with userId: " + userId);

            AddMealFragment addMealFragment = new AddMealFragment();
//            Bundle bundle = new Bundle();
            assert bundle != null;
            bundle.putLong("userId", userId);
            addMealFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addMealFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload meals when returning to this fragment (e.g., after adding a new meal)
        if (userId != -1) {
            loadUserMeals();
        }
    }

    private void loadUserMeals() {
        // Get meals for the current user
        List<Meal> userMeals = mealDAO.getAllMealsByUserId(userId);

        if (userMeals.isEmpty()) {
            tvNoMeals.setVisibility(View.VISIBLE);
            recyclerViewMeals.setVisibility(View.GONE);
        } else {
            tvNoMeals.setVisibility(View.GONE);
            recyclerViewMeals.setVisibility(View.VISIBLE);
            mealAdapter.updateMealList(userMeals);
        }

        // Log meals for debugging
        Log.d(TAG, "Loaded " + userMeals.size() + " meals for user ID: " + userId);
        mealDAO.logAllMeals();
    }
}