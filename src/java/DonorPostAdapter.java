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

public class DonorPostAdapter extends FirestoreRecyclerAdapter<DonorPost, DonorPostAdapter.DonorPostHolder> {

    public DonorPostAdapter(@NonNull FirestoreRecyclerOptions<DonorPost> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DonorPostHolder holder, int position, @NonNull DonorPost model) {
        // Set name and blood group
        holder.textViewName.setText(model.getName() != null ? model.getName() : "Anonymous Donor");
        holder.textViewBloodGroup.setText(model.getBloodGroup() != null ? model.getBloodGroup() : "Blood Group Not Specified");

        // Set location with proper text
        holder.textViewLocation.setText(model.getLocation() != null ? model.getLocation() : "Location not specified");

        // Set phone with proper text
        holder.textViewPhone.setText(model.getPhone() != null ? model.getPhone() : "Phone not available");

        // Set health status
        String status = model.getHasDisease() ? "Not eligible to donate" : "Eligible to donate";
        holder.textViewStatus.setText(status);

        // Set timestamp if available
        if (model.getTimestamp() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            holder.textViewTimestamp.setText(dateFormat.format(model.getTimestamp()));
        } else {
            holder.textViewTimestamp.setText("Date not available");
        }

        holder.requestButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Request sent to " + model.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @NonNull
    @Override
    public DonorPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donor_post, parent, false);
        return new DonorPostHolder(view);
    }

    static class DonorPostHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewBloodGroup, textViewLocation,
                textViewPhone, textViewStatus, textViewTimestamp;
        Button requestButton;

        public DonorPostHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewBloodGroup = itemView.findViewById(R.id.textViewBloodGroup);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            requestButton = itemView.findViewById(R.id.requestButton);
        }
    }
}