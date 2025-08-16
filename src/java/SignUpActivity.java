package com.example.BloodCare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

// Firebase Imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

/**
 * SignupActivity handles user registration by storing details in Firebase
 * Authentication and Firestore. It now provides fields for name, mobile number,
 * blood group, age and password, along with options for "Facebook Signup," "Google Signup,"
 * and "Login." Upon successful signup, it navigates to the MainActivity.
 */
public class SignUpActivity extends AppCompatActivity {

    // Declare UI elements
    private ImageView backArrow;
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText bloodGroupEditText; // New EditText for blood group
    private EditText ageEditText;        // New EditText for age
    private EditText passwordEditText;
    private MaterialButton signupButton;
    private TextView loginLink;

    // Firebase objects
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements by finding them by their IDs from the XML layout
        backArrow = findViewById(R.id.backArrow);
        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        bloodGroupEditText = findViewById(R.id.bloodGroupEditText); // Find new UI element
        ageEditText = findViewById(R.id.ageEditText);                // Find new UI element
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);

        // Set OnClickListener for the back arrow
        backArrow.setOnClickListener(v -> finish());

        // Set OnClickListener for the Signup button
        signupButton.setOnClickListener(v -> {
            // Get the text from the input fields
            String name = nameEditText.getText().toString().trim();
            String mobile = mobileEditText.getText().toString().trim();
            String bloodGroup = bloodGroupEditText.getText().toString().trim(); // Get blood group
            String ageStr = ageEditText.getText().toString().trim();           // Get age as a string
            String password = passwordEditText.getText().toString().trim();

            // Perform validation on user input
            if (name.isEmpty() || mobile.isEmpty() || bloodGroup.isEmpty() || ageStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr); // Convert age string to integer

            // Call the signup method
            signupUser(name, mobile, bloodGroup, age, password);
        });

        // Set OnClickListener for the Login link
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish SignUpActivity
        });
    }

    /**
     * Attempts to create a new user with the provided mobile number and password
     * using Firebase Authentication.
     * @param name The name of the user.
     * @param mobile The mobile number of the user.
     * @param bloodGroup The blood group of the user.
     * @param age The age of the user.
     * @param password The password for the new user.
     */
    private void signupUser(String name, String mobile, String bloodGroup, int age, String password) {
        // Use mobile number and password for Firebase Auth
        mAuth.createUserWithEmailAndPassword(mobile + "@bloodcare.com", password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Signup success, now save user data to Firestore
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserDataToFirestore(user.getUid(), name, mobile, bloodGroup, age);
                        }
                        Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finish SignUpActivity
                    } else {
                        // If signup fails, display a message to the user.
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(SignUpActivity.this, "The password is too weak.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(SignUpActivity.this, "This mobile number is already registered.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignUpActivity.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Saves user details (name, mobile, blood group, and age) to a "users" collection in Firestore.
     * @param userId The unique ID of the user from Firebase Authentication.
     * @param name The name of the user.
     * @param mobile The mobile number of the user.
     * @param bloodGroup The blood group of the user.
     * @param age The age of the user.
     */
    private void saveUserDataToFirestore(String userId, String name, String mobile, String bloodGroup, int age) {
        // Create a new User object with the new fields
        User user = new User(name, mobile, bloodGroup, age);

        // Add a new document with a generated ID
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Log the success or show a toast
                    Toast.makeText(SignUpActivity.this, "User data saved to Firestore.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Log the failure or show a toast
                    Toast.makeText(SignUpActivity.this, "Error saving user data.", Toast.LENGTH_SHORT).show();
                });
    }
}
