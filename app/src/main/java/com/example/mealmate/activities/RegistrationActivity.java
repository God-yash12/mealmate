package com.example.mealmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealmate.R;
import com.example.mealmate.dao.UserDao;
import com.example.mealmate.models.User;
import com.example.mealmate.utils.NotificationHelper;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";  // Define a tag for logging

    private EditText etUsername, etPhone, etAddress, etEmail, etPassword;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UserDao
        userDao = new UserDao(this);
        userDao.open();

        Log.d(TAG, "UserDao initialized and database opened.");

        // Initialize views
        initializeViews();

        TextView loginTextView = findViewById(R.id.loginTextView);
        NotificationHelper.createNotificationChannel(this);

        // Redirect to login page when clicking on "Already have an account? Log in"
        loginTextView.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to LoginActivity");
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked.");
            registerUser();
        });
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    private void registerUser() {
        // Get values
        String username = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "User Input - Username: " + username + ", Phone: " + phone + ", Email: " + email);

        // Validate input
        if (!validateInput(username, phone, address, email, password)) {
            Log.w(TAG, "User input validation failed.");
            return;
        }

        // Create User object
        User newUser = new User(username, phone, address, email, password);
        Log.d(TAG, "Created User object: " + newUser);

        try {
            // Attempt to register user
            long userId = userDao.registerUser(newUser);

            if (userId != -1) {
                Log.i(TAG, "User added to database successfully with ID: " + userId);
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                NotificationHelper.sendNotification(this, "You have Login successfully", "Let's Login and make plan Together");
                finish();
            } else {
                Log.e(TAG, "Registration failed! Email might already exist in the database.");
                Toast.makeText(this, "Registration failed! Email already exists.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while inserting user: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String username, String phone, String address, String email, String password) {
        if (username.isEmpty() || phone.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Log.w(TAG, "Validation failed: Some fields are empty.");
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.w(TAG, "Validation failed: Invalid email format.");
            Toast.makeText(this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Log.w(TAG, "Validation failed: Password too short.");
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDao != null) {
            Log.d(TAG, "Closing UserDao connection.");
            userDao.close();
        }
    }
}
