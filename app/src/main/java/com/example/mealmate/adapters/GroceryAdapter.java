package com.example.mealmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.dao.GroceryDAO;
import com.example.mealmate.models.Grocery;

import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private List<Grocery> groceryList;
    private GroceryDAO groceryDAO;

    public GroceryAdapter(List<Grocery> groceryList, GroceryDAO groceryDAO) {
        this.groceryList = groceryList;
        this.groceryDAO = groceryDAO;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery_list, parent, false);
        return new GroceryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        Grocery currentGrocery = groceryList.get(position);

        holder.tvGroceryName.setText(currentGrocery.getName());
        holder.tvGroceryDetails.setText(String.format("%s %s",
                currentGrocery.getQuantity(), currentGrocery.getUnit()));
        holder.tvStore.setText(String.format("Store: %s", currentGrocery.getStore()));

        // Set initial state based on purchase status
        updateItemAppearance(holder, currentGrocery.isPurchased());

        // Set the purchased status
        holder.rbPurchased.setChecked(currentGrocery.isPurchased());

        // Handle purchased checkbox changes
        holder.rbPurchased.setOnClickListener(v -> {
            boolean isChecked = holder.rbPurchased.isChecked();

            // Update the model
            currentGrocery.setPurchased(isChecked);

            // Update in database
            boolean success = groceryDAO.updateGroceryPurchaseStatus(currentGrocery.getId(), isChecked);

            if (success) {
                // Update UI visibility and appearance
                updateItemAppearance(holder, isChecked);

                // Notify adapter of the change to ensure proper rendering
                notifyItemChanged(position);

                // Show success message (optional)
                if (isChecked) {
                    Toast.makeText(v.getContext(), "Item marked as purchased", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Revert the checkbox if update failed
                currentGrocery.setPurchased(!isChecked);
                holder.rbPurchased.setChecked(!isChecked);
                Toast.makeText(v.getContext(), "Failed to update purchase status", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup share button click listener
        setupShareButton(holder, position);
    }

    // Setup the share button functionality
    private void setupShareButton(GroceryViewHolder holder, int position) {
        holder.btnShareGrocery.setOnClickListener(v -> {
            // Get the grocery item
            Grocery grocery = groceryList.get(position);

            // Show phone number input dialog
            showPhoneNumberDialog(v.getContext(), grocery);
        });
    }

    // Show dialog to input recipient's phone number
    // Show dialog to input recipient's phone number
    private void showPhoneNumberDialog(Context context, Grocery grocery) {
        // Inflate custom dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sms_share, null);

        // Get reference to the EditText from the layout
        final EditText etPhoneNumber = dialogView.findViewById(R.id.etPhoneNumber);

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Share Grocery Item");
        builder.setView(dialogView);

        // Set up the buttons
        builder.setPositiveButton("Send", (dialog, which) -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNumber)) {
                sendSMS(context, phoneNumber, formatGroceryMessage(grocery));
            } else {
                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Format the grocery item details as a message
    private String formatGroceryMessage(Grocery grocery) {
        return "Grocery Item: " + grocery.getName() +
                "\nQuantity: " + grocery.getQuantity() + " " + grocery.getUnit() +
                "\nStore: " + grocery.getStore() +
                "\nCategory: " + grocery.getCategory();
    }

    // Send the SMS using Android's built-in SMS app
    private void sendSMS(Context context, String phoneNumber, String message) {
        try {
            Uri uri = Uri.parse("smsto:" + phoneNumber);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to update the appearance of the item based on purchase status
    private void updateItemAppearance(GroceryViewHolder holder, boolean isPurchased) {
        // Update visibility of radio button and purchased text
        if (isPurchased) {
            holder.rbPurchased.setVisibility(View.GONE);
            holder.tvPurchased.setVisibility(View.VISIBLE);
        } else {
            holder.rbPurchased.setVisibility(View.VISIBLE);
            holder.tvPurchased.setVisibility(View.GONE);
        }

        // Update strikethrough text effect
        if (isPurchased) {
            holder.tvGroceryName.setPaintFlags(holder.tvGroceryName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvGroceryDetails.setPaintFlags(holder.tvGroceryDetails.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvGroceryName.setPaintFlags(holder.tvGroceryName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvGroceryDetails.setPaintFlags(holder.tvGroceryDetails.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public Grocery getGroceryAt(int position) {
        return groceryList.get(position);
    }

    public void removeItem(int position) {
        groceryList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateData(List<Grocery> newGroceryList) {
        this.groceryList = newGroceryList;
        notifyDataSetChanged();
    }

    static class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroceryName;
        TextView tvGroceryDetails;
        TextView tvStore;
        RadioButton rbPurchased;
        ImageButton btnEditGrocery;
        ImageButton btnShareGrocery;
        TextView tvPurchased;

        GroceryViewHolder(View view) {
            super(view);
            tvGroceryName = view.findViewById(R.id.tvGroceryName);
            tvGroceryDetails = view.findViewById(R.id.tvGroceryDetails);
            tvStore = view.findViewById(R.id.tvStore);
            rbPurchased = view.findViewById(R.id.rbPurchased);
            btnEditGrocery = view.findViewById(R.id.btnEditGrocery);
            btnShareGrocery = view.findViewById(R.id.btnShareGrocery);
            tvPurchased = view.findViewById(R.id.tvPurchased);
        }
    }
}