package com.example.mealmate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mealmate.R;
import com.example.mealmate.dao.UserDao;
import com.example.mealmate.models.User;

public class SettingsFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvPhone, tvAddress;
    private UserDao userDao;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize UserDao
        userDao = new UserDao(getContext());

        // Get userId from arguments
        if (getArguments() != null) {
            userId = getArguments().getLong("userId", -1);
        }

        // Initialize views
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvAddress = view.findViewById(R.id.tv_address);

        // Load user data
        if (userId != -1) {
            loadUserData(userId);
        }

        return view;
    }

    private void loadUserData(long userId) {
        User user = userDao.getUserById(userId);

        if (user != null) {
            // Display user data
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
            tvAddress.setText(user.getAddress());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close database connection
        if (userDao != null) {
            userDao.close();
        }
    }
}