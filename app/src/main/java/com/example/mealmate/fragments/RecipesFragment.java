package com.example.mealmate.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mealmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipesFragment extends Fragment {

    private RecyclerView recyclerViewRecipe;
    private FloatingActionButton fabAddRecipe;

    public RecipesFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewRecipe = view.findViewById(R.id.recyclerViewRecipe); // Corrected ID
        fabAddRecipe = view.findViewById(R.id.fabAddRecipe);

        recyclerViewRecipe.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Button click -> Open AddRecipeFragment
        fabAddRecipe.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddRecipeFragment()) // Corrected to AddRecipeFragment
                    .addToBackStack(null) // Allow back navigation
                    .commit();
        });
    }
}