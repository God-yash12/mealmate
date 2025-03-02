package com.example.mealmate.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.adapters.GroceryAdapter;
import com.example.mealmate.databases.DBHelper;
import com.example.mealmate.dao.GroceryDAO;
import com.example.mealmate.models.Grocery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GroceryListFragment extends Fragment implements AddGroceryFragment.OnGroceryAddedListener {

    private RecyclerView recyclerViewGrocery;
    private FloatingActionButton fabAddGrocery;
    private TextView tvNoGrocery;
    private GroceryDAO groceryDAO;
    private GroceryAdapter adapter;
    private long userId;  // The userId should be passed or retrieved from a session or shared preferences

    public GroceryListFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grocery_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerViewGrocery = view.findViewById(R.id.recyclerViewGrocery);
        fabAddGrocery = view.findViewById(R.id.fabAddGrocery);
        tvNoGrocery = view.findViewById(R.id.tvNoGrocery);

        // Initialize database helper and groceryDAO
        groceryDAO = new GroceryDAO(requireContext());

        // Set up RecyclerView
        recyclerViewGrocery.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Retrieve the userId from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("userId", -1); // Default value is -1 if not found
            if (userId != -1) {
                Log.d(TAG, "User ID in GroceryListFragment: " + userId);
                loadGroceryData();
            } else {
                Log.d(TAG, "User ID not found in GroceryListFragment");
                tvNoGrocery.setText("Error: User ID not found");
                tvNoGrocery.setVisibility(View.VISIBLE);
            }
        }


        // Load data
        loadGroceryData();

        // Floating button click: Navigate to AddGroceryFragment
        fabAddGrocery.setOnClickListener(v -> {
            AddGroceryFragment addGroceryFragment = AddGroceryFragment.newInstance(userId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addGroceryFragment) // Use the created instance
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onGroceryAdded() {
        // This will be called when a grocery is added in AddGroceryFragment
        loadGroceryData();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Reload data when returning to this fragment
        loadGroceryData();
    }

    private void loadGroceryData() {
        // Fetch all groceries from the database for the current user
        List<Grocery> groceryList = groceryDAO.getAllGroceriesForUser(userId);

        if (groceryList == null || groceryList.isEmpty()) {
            tvNoGrocery.setVisibility(View.VISIBLE);
            recyclerViewGrocery.setVisibility(View.GONE);
        } else {
            tvNoGrocery.setVisibility(View.GONE);
            recyclerViewGrocery.setVisibility(View.VISIBLE);

            // Initialize the adapter and set it to RecyclerView
            if (adapter == null) {
                adapter = new GroceryAdapter(groceryList, groceryDAO);
                recyclerViewGrocery.setAdapter(adapter);
            } else {
                // Update the adapter's data
                adapter.updateData(groceryList);
            }
        }
        // Call this method in onViewCreated after setting up the RecyclerView
        setupSwipeToDelete();
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Grocery groceryToDelete = adapter.getGroceryAt(position);

                // Show confirmation dialog
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Grocery Item")
                        .setMessage("Are you sure you want to delete this Grocery Item?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Delete from database
                            if (groceryDAO.deleteGrocery(groceryToDelete.getId())) {
                                adapter.removeItem(position);
                                Toast.makeText(requireContext(), "Grocery deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Failed to delete grocery", Toast.LENGTH_SHORT).show();
                                adapter.notifyItemChanged(position); // Refresh the item if delete failed
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            adapter.notifyItemChanged(position); // Refresh the item to undo the swipe
                        })
                        .setCancelable(false)
                        .show();
            }
        }).attachToRecyclerView(recyclerViewGrocery);
    }


}
