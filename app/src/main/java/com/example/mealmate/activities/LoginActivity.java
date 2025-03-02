package com.example.mealmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;  // Import Log class
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealmate.dao.UserDao;
import com.example.mealmate.models.User;
import com.example.mealmate.R;
import com.example.mealmate.utils.NotificationHelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        NotificationHelper.createNotificationChannel(this);

        userDao = new UserDao(this);

        // Redirect to the Register Page
        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Login attempt with email: " + email);

                User user = userDao.getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    NotificationHelper.sendNotification(this, "Welcome Back!", "Let's make a meal together for your Favourite Person!!");

                    // Redirect to HomeActivity with user info
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("userId", user.getId());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("username", user.getUsername());
                    startActivity(intent);
                    finish();
                } else {
                    Log.w(TAG, "Login failed for email: " + email);  // Log failed login attempt
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        userDao.close();
        super.onDestroy();
    }
}
