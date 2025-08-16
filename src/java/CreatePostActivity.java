package com.example.BloodCare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * CreatePostActivity is responsible for allowing users to create blood donation
 * or blood reception posts. It provides options to select between being a
 * 'Blood Donor' or 'Blood Receiver' and dynamically loads the respective forms.
 *
 * This activity demonstrates the use of Fragments to manage different UI sections
 * and adheres to OOP principles by encapsulating form logic within separate Fragment classes.
 */
public class CreatePostActivity extends AppCompatActivity {

    // UI elements
    private Button btnBloodDonor;
    private Button btnBloodReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_create_post.xml layout
        setContentView(R.layout.activity_create_post);

        // Initialize UI components by finding their IDs from the layout
        btnBloodDonor = findViewById(R.id.btn_blood_donor);
        btnBloodReceiver = findViewById(R.id.btn_blood_receiver);

        // Set click listener for the Blood Donor button
        btnBloodDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When clicked, load the DonorFormFragment into the fragment_container
                loadFragment(new DonorFormFragment());
            }
        });

        // Set click listener for the Blood Receiver button
        btnBloodReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When clicked, load the ReceiverFormFragment into the fragment_container
                loadFragment(new ReceiverFormFragment());
            }
        });

        // Optionally, load a default fragment when the activity starts (e.g., Donor form)
        // if (savedInstanceState == null) {
        //     loadFragment(new DonorFormFragment());
        // }
    }

    /**
     * Helper method to load a specified Fragment into the fragment_container.
     * This method encapsulates the logic for Fragment transactions, promoting reusability.
     *
     * @param fragment The Fragment to be loaded.
     */
    private void loadFragment(Fragment fragment) {
        // Get the FragmentManager to manage fragments within this activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Begin a new FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the existing fragment in the 'fragment_container' with the new fragment
        // The second argument (null) means no tag is assigned to the fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        // Add the transaction to the back stack, allowing the user to navigate back
        // to the previous fragment (if any) using the back button
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction to apply the changes
        fragmentTransaction.commit();
    }
}
