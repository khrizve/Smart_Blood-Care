package com.example.BloodCare;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecipientActivity extends AppCompatActivity {
    private RecyclerView recipientRecyclerView;
    private RecipientPostAdapter recipientAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list);

        db = FirebaseFirestore.getInstance();
        recipientRecyclerView = findViewById(R.id.recipientRecyclerView);

        try {
            setupRecyclerView();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading recipient posts", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        Query query = db.collection("receiverPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ReceiverPost> options = new FirestoreRecyclerOptions.Builder<ReceiverPost>()
                .setQuery(query, ReceiverPost.class)
                .build();

        recipientAdapter = new RecipientPostAdapter(options);
        recipientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipientRecyclerView.setAdapter(recipientAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (recipientAdapter != null) {
            recipientAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recipientAdapter != null) {
            recipientAdapter.stopListening();
        }
    }
}