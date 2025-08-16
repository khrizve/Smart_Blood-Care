package com.example.BloodCare;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DonorActivity extends AppCompatActivity {
    private RecyclerView donorRecyclerView;
    private DonorPostAdapter donorAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        db = FirebaseFirestore.getInstance();
        donorRecyclerView = findViewById(R.id.donorRecyclerView);

        try {
            setupRecyclerView();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading donor posts", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        Query query = db.collection("donorPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<DonorPost> options = new FirestoreRecyclerOptions.Builder<DonorPost>()
                .setQuery(query, DonorPost.class)
                .build();

        donorAdapter = new DonorPostAdapter(options);
        donorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorRecyclerView.setAdapter(donorAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (donorAdapter != null) {
            donorAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (donorAdapter != null) {
            donorAdapter.stopListening();
        }
    }
}