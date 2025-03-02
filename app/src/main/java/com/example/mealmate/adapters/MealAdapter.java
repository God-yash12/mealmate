package com.example.mealmate.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.dao.MealDAO;
import com.example.mealmate.models.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealList;
    private Context context;
    private MealDAO mealDAO;

    public MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
        this.mealDAO = new MealDAO(context);
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);

        holder.tvMealName.setText(meal.getMealName());
        holder.tvMealType.setText(meal.getMealType());
        holder.tvMealDate.setText(meal.getMealDate());
        holder.tvPrepTime.setText("Prep: " + meal.getPrepTime());
        holder.tvCookTime.setText("Cook: " + meal.getCookTime());
        holder.tvIngredients.setText("Ingredients: " + meal.getIngredients());

        // Set up delete button click listener
        holder.btnDeleteMeal.setOnClickListener(v -> {
            showDeleteConfirmationDialog(meal, position);
        });

        // Set up edit button click listener (for future implementation)
        holder.btnEditMeal.setOnClickListener(v -> {
            Toast.makeText(context, "Edit functionality will be implemented later", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDeleteConfirmationDialog(Meal meal, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Meal");
        builder.setMessage("Are you sure you want to delete this meal?");

        // Add the buttons
        builder.setPositiveButton("Delete", (dialog, id) -> {
            deleteMeal(meal, position);
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteMeal(Meal meal, int position) {
        boolean deleted = mealDAO.deleteMeal(meal.getId());

        if (deleted) {
            // Remove the item from our list
            mealList.remove(position);
            // Notify adapter of the change
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mealList.size());
            Toast.makeText(context, "Meal deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete meal", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    // Method to update the adapter data
    public void updateMealList(List<Meal> newMealList) {
        this.mealList = newMealList;
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView tvMealName, tvMealType, tvMealDate, tvPrepTime, tvCookTime, tvIngredients;
        ImageButton btnEditMeal, btnDeleteMeal;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealType = itemView.findViewById(R.id.tvMealType);
            tvMealDate = itemView.findViewById(R.id.tvMealDate);
            tvPrepTime = itemView.findViewById(R.id.tvPrepTime);
            tvCookTime = itemView.findViewById(R.id.tvCookTime);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            btnEditMeal = itemView.findViewById(R.id.btnEditMeal);
            btnDeleteMeal = itemView.findViewById(R.id.btnDeleteMeal);
        }
    }
}