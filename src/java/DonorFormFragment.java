package com.example.BloodCare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * DonorFormFragment represents the UI and logic for the Blood Donor form.
 * It extends Fragment, encapsulating its own layout and behavior,
 * demonstrating modularity and adherence to OOP principles.
 * This updated version includes logic to save the form data to Firestore.
 */
public class DonorFormFragment extends Fragment {

    // UI elements for the donor form
    private EditText etDonorName;
    private EditText etDonorBloodGroup;
    private EditText etDonorLocation;
    private EditText etDonorPhone;
    private RadioGroup rgDonorDisease;
    private RadioButton rbDonorDiseaseYes;
    private RadioButton rbDonorDiseaseNo;
    private Button btnDonorSubmit;

    // Firestore instance
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment from fragment_donor_form.xml
        View view = inflater.inflate(R.layout.fragment_donor_form, container, false);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize UI components from the inflated view
        etDonorName = view.findViewById(R.id.et_donor_name);
        etDonorBloodGroup = view.findViewById(R.id.et_donor_blood_group);
        etDonorLocation = view.findViewById(R.id.et_donor_location);
        etDonorPhone = view.findViewById(R.id.et_donor_phone);
        rgDonorDisease = view.findViewById(R.id.rg_donor_disease);
        rbDonorDiseaseYes = view.findViewById(R.id.rb_donor_disease_yes);
        rbDonorDiseaseNo = view.findViewById(R.id.rb_donor_disease_no);
        btnDonorSubmit = view.findViewById(R.id.btn_donor_submit);

        // Set an OnClickListener for the submit button
        btnDonorSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle form submission
                submitDonorForm();
            }
        });

        return view;
    }

    /**
     * Retrieves data from the form fields, performs validation, and saves the data
     * to a Firestore database.
     */
    private void submitDonorForm() {
        // Retrieve data from EditText fields
        String name = etDonorName.getText().toString().trim();
        String bloodGroup = etDonorBloodGroup.getText().toString().trim();
        String location = etDonorLocation.getText().toString().trim();
        String phone = etDonorPhone.getText().toString().trim();

        // Determine if the donor has any disease based on RadioButton selection
        boolean hasDisease = rbDonorDiseaseYes.isChecked();

        // Basic validation: check if required fields are not empty
        if (name.isEmpty() || bloodGroup.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if validation fails
        }

        // Create a new DonorPost object
        DonorPost newPost = new DonorPost(name, bloodGroup, location, phone, hasDisease);

        // Add the new post to the "donorPosts" collection in Firestore
        db.collection("donorPosts")
                .add(newPost)
                .addOnSuccessListener(documentReference -> {
                    // This block is executed if the data is saved successfully
                    Toast.makeText(getContext(), "Donor request submitted successfully!", Toast.LENGTH_LONG).show();
                    // Optionally, clear the form fields
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    // This block is executed if there is an error
                    Toast.makeText(getContext(), "Error submitting donor request.", Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Clears all the input fields in the form.
     */
    private void clearForm() {
        etDonorName.setText("");
        etDonorBloodGroup.setText("");
        etDonorLocation.setText("");
        etDonorPhone.setText("");
        rgDonorDisease.check(R.id.rb_donor_disease_no); // Reset to 'No'
    }
}
