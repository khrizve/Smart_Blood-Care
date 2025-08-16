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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import java.util.Objects;

/**
 * LoginActivity handles user authentication. It provides fields for mobile number
 * and password, with options for "Forgot Password," "Facebook Login,"
 * "Google Login," and "Signup." It now uses Firebase Authentication to verify
 * user credentials.
 */
public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private ImageView backArrow;
    private EditText mobileEditText;
    private EditText passwordEditText;
    private MaterialButton loginButton;
    private TextView signupLink;
    private TextView guestViewText;

    // Firebase object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements by finding them by their IDs from the XML layout
        backArrow = findViewById(R.id.backArrow);
        mobileEditText = findViewById(R.id.mobileEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);
        guestViewText = findViewById(R.id.guestViewText);

        // Set OnClickListener for the back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and go back to the previous screen
                finish();
            }
        });

        // Set OnClickListener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the mobile and password input fields
                String mobile = mobileEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform basic validation
                if (mobile.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // For this implementation, we use the mobile number as the email for Firebase Auth.
                String email = mobile + "@bloodcare.com";

                // Call the login method
                loginUser(email, password);
            }
        });

        // Set OnClickListener for Signup link
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Signup clicked!", Toast.LENGTH_SHORT).show();
                // Navigate to the SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for Guest View text (New Addition)
        guestViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Continuing as Guest!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish LoginActivity so the user cannot go back to it
            }
        });
    }

    /**
     * Handles the user login process using Firebase Authentication.
     * @param email The user's email (constructed from mobile number for this example).
     * @param password The user's password.
     */
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finish LoginActivity
                    } else {
                        // If sign in fails, display a message to the user.
                        // This is where proper exception handling comes in handy.
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(LoginActivity.this, "No user found with this mobile number.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(LoginActivity.this, "Wrong password.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
    }
}
