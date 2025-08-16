// ContactAdminActivity.java
// This Java class handles the logic for the contact form screen.
// It manages user input, simulates a request submission, and provides feedback.

package com.example.BloodCare;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent; // Added for launching the activity

import androidx.appcompat.app.AppCompatActivity;

public class ContactAdminActivity extends AppCompatActivity {

    // UI elements
    private EditText etEmail;
    private EditText etMessage;
    private Button btnSubmitRequest;
    private ProgressBar progressBar;
    private TextView tvStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the XML layout defined for this activity
        setContentView(R.layout.activity_contact);

        // Initialize UI elements by finding them by their IDs in the layout
        etEmail = findViewById(R.id.et_email);
        etMessage = findViewById(R.id.et_message);
        btnSubmitRequest = findViewById(R.id.btn_submit_request);
        progressBar = findViewById(R.id.progress_bar);
        tvStatusMessage = findViewById(R.id.tv_status_message);

        // Set an OnClickListener for the submit button
        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequest(); // Call the method to handle request submission
            }
        });
    }

    /**
     * Handles the submission of the password reset request.
     * It performs basic validation, shows a loading state,
     * simulates an asynchronous operation, and provides feedback to the user.
     */
    private void submitRequest() {
        String email = etEmail.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        // Basic input validation
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (message.isEmpty()) {
            etMessage.setError("Message is required");
            etMessage.requestFocus();
            return;
        }

        // Show loading indicator and disable the button
        progressBar.setVisibility(View.VISIBLE);
        btnSubmitRequest.setEnabled(false);
        tvStatusMessage.setText("Sending request...");
        tvStatusMessage.setTextColor(getResources().getColor(android.R.color.darker_gray));

        // Simulate a network request or background task
        // In a real application, you would send this data to your backend server
        // (e.g., via an API call using Retrofit, Volley, or HttpURLConnection).
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide loading indicator and re-enable the button
                progressBar.setVisibility(View.GONE);
                btnSubmitRequest.setEnabled(true);

                // Simulate success or failure
                boolean requestSuccessful = true; // In a real app, this would depend on API response

                if (requestSuccessful) {
                    tvStatusMessage.setText("Request sent successfully! An admin will contact you shortly.");
                    tvStatusMessage.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    Toast.makeText(ContactAdminActivity.this, "Request submitted!", Toast.LENGTH_SHORT).show();
                    // Optionally clear fields after successful submission
                    etEmail.setText("");
                    etMessage.setText("");
                } else {
                    tvStatusMessage.setText("Failed to send request. Please try again later.");
                    tvStatusMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    Toast.makeText(ContactAdminActivity.this, "Submission failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000); // Simulate a 3-second delay for the "network request"
    }
}