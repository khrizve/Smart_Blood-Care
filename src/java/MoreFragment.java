package com.example.BloodCare;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class MoreFragment extends Fragment {

    // UI components for the options
    private LinearLayout settingsOption;
    private LinearLayout aboutOption;
    private LinearLayout logoutOption;

    // Firebase authentication instance
    private FirebaseAuth mAuth;

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is where the layout is inflated and views are initialized.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        // Initialize Firebase and UI components
        initializeFirebase();
        initializeViews(view);

        // Set up click listeners for the options
        setupClickListeners();

        return view;
    }

    /**
     * Initializes the Firebase Authentication instance.
     * A try-catch block is used to handle potential exceptions during initialization,
     * although it's unlikely with Firebase.
     */
    private void initializeFirebase() {
        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            // Log or show a toast message if Firebase fails to initialize
            Toast.makeText(getContext(), "Error initializing Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Initializes the UI components by finding them in the inflated layout.
     * @param view The root view of the fragment's layout.
     */
    private void initializeViews(View view) {
        settingsOption = view.findViewById(R.id.settings_option);
        aboutOption = view.findViewById(R.id.about_option);
        logoutOption = view.findViewById(R.id.logout_option);
    }

    /**
     * Sets up click listeners for the interactive UI options.
     * Each option (settings, about, logout) is assigned a specific action.
     */
    private void setupClickListeners() {
        settingsOption.setOnClickListener(v -> openSettingsActivity());
        aboutOption.setOnClickListener(v -> openAboutActivity());
        logoutOption.setOnClickListener(v -> handleLogout());
    }

    /**
     * Starts the SettingsActivity when the settings option is clicked.
     */
    private void openSettingsActivity() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the AboutActivity when the about option is clicked.
     */
    private void openAboutActivity() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the user logout process.
     * It checks if a user is currently logged in before attempting to sign out.
     * A try-catch block is used to handle potential exceptions during the sign-out process.
     */
    private void handleLogout() {
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            try {
                mAuth.signOut();
                navigateToLogin();
                Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Handle potential errors during sign out
                Toast.makeText(getContext(), "Logout failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            // Inform the user if no one is logged in
            Toast.makeText(getContext(), "No user is currently logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigates the user to the LoginActivity and clears the activity back stack.
     * This prevents the user from returning to the previous logged-in screens using the back button.
     */
    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // Flags to clear the activity stack and start a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // Finish the current activity to remove it from the stack
        requireActivity().finish();
    }
}