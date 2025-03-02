package com.example.mealmate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mealmate.R;
import com.example.mealmate.dao.UserDao;
import com.example.mealmate.fragments.MealPlannerFragment;
import com.example.mealmate.fragments.GroceryListFragment;
import com.example.mealmate.fragments.RecipesFragment;
import com.example.mealmate.fragments.SettingsFragment;
import com.example.mealmate.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Button btnLogout;
    private TextView tvWelcome;
    private ImageView ivUserProfile;
    private UserDao userDao;
    private BottomNavigationView bottomNavigationView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);
        ivUserProfile = findViewById(R.id.ivUserProfile);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize UserDao
        userDao = new UserDao(this);
        userDao.open();

        String email = getIntent().getStringExtra("email");
        User user = userDao.getUserByEmail(email);

        if (email != null && !email.isEmpty()) {

            if (user != null) {
                tvWelcome.setText("Welcome, " + user.getUsername() + "  " + user.getId());

                // Pass userId to fragments
                if (savedInstanceState == null) {
                    MealPlannerFragment mealPlannerFragment = new MealPlannerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", user.getId());
                    mealPlannerFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mealPlannerFragment)
                            .commit();
                }
                // Optional: Save to SharedPreferences for easy access elsewhere
                saveUserToPrefs(user.getId(), user.getUsername());
            }
        }

        // Handle Bottom Navigation Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (user == null) return false; // Safety check
            long userId = user.getId();
            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            bundle.putLong("userId", userId);
            // Now you can use userId in HomeActivity
            Log.d(TAG, "Received userId: " + userId);

            if (item.getItemId() == R.id.nav_meal_planner) {
                selectedFragment = new MealPlannerFragment();
            } else if (item.getItemId() == R.id.nav_grocery_list) {
                selectedFragment = new GroceryListFragment();
            } else if (item.getItemId() == R.id.nav_recipes) {
                selectedFragment = new RecipesFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });


        // Logout Button Handling
        ivUserProfile.setOnClickListener(v -> {
            if (btnLogout.getVisibility() == View.GONE) {
                btnLogout.setVisibility(View.VISIBLE);
            } else {
                btnLogout.setVisibility(View.GONE);
            }
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // Optional: Save user info to SharedPreferences for app-wide access
    private void saveUserToPrefs(long userId, String username) {
        getSharedPreferences("MealMate", MODE_PRIVATE)
                .edit()
                .putLong("userId", userId)
                .putString("username", username)
                .apply();
    }

}
