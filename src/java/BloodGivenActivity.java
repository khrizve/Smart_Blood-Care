package com.example.BloodCare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BloodGivenActivity extends AppCompatActivity {

    private static final String TAG = "BloodGivenActivity";
    private Button btnSelectDate, btnConfirm, btnViewRecords;
    private TextView tvSelectedDate;
    private Calendar selectedDate = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_given);

        // Initialize Firebase Authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // User is not logged in, handle this case (e.g., redirect to login)
            Toast.makeText(this, "Please log in to record a donation.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnViewRecords = findViewById(R.id.btnViewRecords);
        btnConfirm.setEnabled(false); // Disable confirm button initially
    }

    private void setupClickListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnConfirm.setOnClickListener(v -> confirmDonation());
        btnViewRecords.setOnClickListener(v -> {
            // Start the activity to view donation records
            Intent intent = new Intent(BloodGivenActivity.this, DonationRecordsActivity.class);
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    updateUIAfterDateSelection();
                    showNextDonationDate();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        // Set maximum date to today
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateUIAfterDateSelection() {
        tvSelectedDate.setText(dateFormat.format(selectedDate.getTime()));
        btnConfirm.setEnabled(true);
    }

    private void showNextDonationDate() {
        Calendar nextDonation = (Calendar) selectedDate.clone();
        nextDonation.add(Calendar.WEEK_OF_YEAR, 8);
        String nextDate = dateFormat.format(nextDonation.getTime());
        Toast.makeText(this,
                "You can donate again after " + nextDate,
                Toast.LENGTH_LONG).show();
    }

    private void confirmDonation() {
        // First, get the user's data from Firestore
        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, get the user's name and phone
                        String userName = documentSnapshot.getString("name");
                        String userPhone = documentSnapshot.getString("mobile");
                        String donationDate = dateFormat.format(selectedDate.getTime());

                        // Now save the donation record with the user's details
                        saveDonationRecord(userName, userPhone, donationDate);
                    } else {
                        // Document does not exist
                        Toast.makeText(BloodGivenActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data: ", e);
                    Toast.makeText(BloodGivenActivity.this, "Failed to get user data.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDonationRecord(String userName, String userPhone, String donationDate) {
        // Create a new donation record map
        Map<String, Object> donation = new HashMap<>();
        donation.put("userId", currentUser.getUid());
        donation.put("userName", userName);
        donation.put("userPhone", userPhone);
        donation.put("donationDate", donationDate);
        donation.put("timestamp", new java.util.Date());

        // Add a new document with a generated ID to the "donations" collection
        db.collection("donations")
                .add(donation)
                .addOnSuccessListener(documentReference -> {
                    String confirmationMessage = "Blood donation recorded for " + donationDate;
                    Toast.makeText(BloodGivenActivity.this, confirmationMessage, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BloodGivenActivity.this, "Error saving donation record.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error adding document", e);
                });
    }
}
