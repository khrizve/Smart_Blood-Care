package com.example.BloodCare; // IMPORTANT: Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * OpeningActivity handles the onboarding flow of the application.
 * It displays a series of introductory screens with illustrations, titles,
 * and descriptions, guiding the user through the app's features.
 * After completing the onboarding, it navigates to the LoginActivity.
 */
public class OpeningActivity extends AppCompatActivity {

    private ImageView imageViewIllustration;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private Button buttonAction;

    // --- Onboarding Content ---
    // Array of drawable resource IDs for onboarding illustrations
    private int[] illustrationResources = {
            R.drawable.blood_donation_3, // Step 1
            R.drawable.blood_donation_2, // Step 2 (Make sure you have this drawable!)
            R.drawable.blood_donation_1  // Step 3 (Make sure you have this drawable!)
    };

    // Array of string resource IDs for onboarding titles
    private int[] titleStrings = {
            R.string.onboarding_title_1, // Step 1 Title
            R.string.onboarding_title_2, // Step 2 Title
            R.string.onboarding_title_3  // Step 3 Title
    };

    // Array of string resource IDs for onboarding descriptions
    private int[] descriptionStrings = {
            R.string.onboarding_description_1, // Step 1 Description
            R.string.onboarding_description_2, // Step 2 Description
            R.string.onboarding_description_3  // Step 3 Description
    };
    // --------------------------

    private int currentStep = 0; // Tracks the current onboarding step

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the onboarding XML layout file
        setContentView(R.layout.activity_opening); // Make sure this matches your XML layout file name

        // Initialize UI elements by finding them by their IDs from the XML layout
        imageViewIllustration = findViewById(R.id.imageView_illustration);
        textViewTitle = findViewById(R.id.textView_title);
        textViewDescription = findViewById(R.id.textView_description);
        buttonAction = findViewById(R.id.button_action);

        // Set initial content for the first onboarding step
        updateOnboardingContent();

        // Set OnClickListener for the action button (Next/Get Started)
        buttonAction.setOnClickListener(v -> {
            currentStep++; // Move to the next step

            // Check if there are more onboarding steps
            if (currentStep < illustrationResources.length) {
                // If yes, update the UI with content for the next step
                updateOnboardingContent();
            } else {
                // If all onboarding steps are completed, navigate to the Login activity
                navigateToLoginActivity(); // Navigate to LoginActivity
            }
        });
    }

    /**
     * Updates the UI elements (image, title, description, and button text)
     * based on the currentStep. This method is called to refresh the onboarding
     * screen content.
     */
    private void updateOnboardingContent() {
        // Set the illustration image, title, and description based on the current step
        imageViewIllustration.setImageResource(illustrationResources[currentStep]);
        textViewTitle.setText(titleStrings[currentStep]);
        textViewDescription.setText(descriptionStrings[currentStep]);

        // Change button text on the last step to "Get Started"
        if (currentStep == illustrationResources.length - 1) {
            buttonAction.setText(R.string.button_get_started); // Text for the final step
        } else {
            buttonAction.setText(R.string.button_next); // Text for intermediate steps
        }
    }

    /**
     * Navigates to the LoginActivity of the application.
     * This method is called after the onboarding flow is complete.
     */
    private void navigateToLoginActivity() {
        // Create an Intent to start the LoginActivity
        Intent intent = new Intent(OpeningActivity.this, LoginActivity.class);
        startActivity(intent);
        // Finish OpeningActivity so the user cannot go back to it using the back button
        finish();
    }
}
