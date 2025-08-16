package com.example.BloodCare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonationRecordsActivity extends AppCompatActivity {

    private static final String TAG = "DonationRecordsActivity";
    private RecyclerView recyclerView;
    private DonationRecordsAdapter adapter;
    private List<DonationRecord> donationRecordsList = new ArrayList<>();

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_records);

        // Initialize Firebase Authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please log in to view records.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewDonationRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DonationRecordsAdapter(donationRecordsList);
        recyclerView.setAdapter(adapter);

        fetchDonationRecords();
    }

    private void fetchDonationRecords() {
        db.collection("donations")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donationRecordsList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            DonationRecord record = document.toObject(DonationRecord.class);
                            donationRecordsList.add(record);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "No donation records found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching records: ", e);
                    Toast.makeText(this, "Failed to fetch donation records.", Toast.LENGTH_SHORT).show();
                });
    }

    // A simple POJO to represent a donation record
    public static class DonationRecord {
        private String userId;
        private String userName;
        private String userPhone;
        private String donationDate;

        public DonationRecord() {}

        // Getters
        public String getUserId() { return userId; }
        public String getUserName() { return userName; }
        public String getUserPhone() { return userPhone; }
        public String getDonationDate() { return donationDate; }

        // Setters (if needed for Firestore)
        public void setUserId(String userId) { this.userId = userId; }
        public void setUserName(String userName) { this.userName = userName; }
        public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
        public void setDonationDate(String donationDate) { this.donationDate = donationDate; }
    }

    // RecyclerView Adapter
    private static class DonationRecordsAdapter extends RecyclerView.Adapter<DonationRecordsAdapter.RecordViewHolder> {

        private final List<DonationRecord> records;

        public DonationRecordsAdapter(List<DonationRecord> records) {
            this.records = records;
        }

        @NonNull
        @Override
        public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RecordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
            DonationRecord record = records.get(position);
            holder.textView.setText("Donated on: " + record.getDonationDate());
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        static class RecordViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public RecordViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
