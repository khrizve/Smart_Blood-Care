package com.example.BloodCare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RecipientPostAdapter extends FirestoreRecyclerAdapter<ReceiverPost, RecipientPostAdapter.RecipientPostHolder> {

    public RecipientPostAdapter(@NonNull FirestoreRecyclerOptions<ReceiverPost> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipientPostHolder holder, int position, @NonNull ReceiverPost model) {
        // Set name
        holder.textViewName.setText(model.getName() != null ? model.getName() : "Anonymous Recipient");

        // Set location with proper text
        holder.textViewLocation.setText(model.getLocation() != null ? model.getLocation() : "Location not specified");

        // Set phone with proper text
        holder.textViewPhone.setText(model.getPhone() != null ? model.getPhone() : "Phone not available");

        // Set blood group with proper text
        String bloodGroupText = model.getBloodGroup() != null ?
                "Needed Blood Group: " + model.getBloodGroup() : "Blood Group not specified";
        holder.textViewBlood.setText(bloodGroupText);

        // Set purpose with proper text
        holder.textViewPurpose.setText(model.getPurpose() != null ? model.getPurpose() : "Purpose not specified");

        // Set timestamp if available
        if (model.getTimestamp() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            holder.textViewTimestamp.setText(dateFormat.format(model.getTimestamp()));
        } else {
            holder.textViewTimestamp.setText("Date not available");
        }

        holder.donateButton.setOnClickListener(v -> {
            String message = "Donation request sent to " + model.getName();
            if (model.getBloodGroup() != null) {
                message += " (Needed Blood Group: " + model.getBloodGroup() + ")";
            }
            Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    @NonNull
    @Override
    public RecipientPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipient_post, parent, false);
        return new RecipientPostHolder(view);
    }

    static class RecipientPostHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation, textViewPhone, textViewBlood,
                textViewPurpose, textViewTimestamp;
        Button donateButton;

        public RecipientPostHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewBlood = itemView.findViewById(R.id.textViewBlood);
            textViewPurpose = itemView.findViewById(R.id.textViewPurpose);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            donateButton = itemView.findViewById(R.id.donateButton);
        }
    }
}