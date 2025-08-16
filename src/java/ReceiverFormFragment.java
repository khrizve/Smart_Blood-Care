package com.example.BloodCare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * ReceiverFormFragment represents the UI and logic for the Blood Receiver form.
 * It extends Fragment, encapsulating its own layout and behavior,
 * demonstrating modularity and adherence to OOP principles.
 * This updated version includes logic to save the form data to Firestore.
 */
public class ReceiverFormFragment extends Fragment {

    // UI elements for the receiver form
    private EditText etReceiverName;
    private EditText etReceiverLocation;
    private EditText etReceiverPhone;
    private EditText etReceiverBloodGroup;
    private EditText etReceiverPurpose;
    private Button btnReceiverSubmit;

    // Firestore instance
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment from fragment_receiver_form.xml
        View view = inflater.inflate(R.layout.fragment_receiver_form, container, false);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize UI components from the inflated view
        etReceiverName = view.findViewById(R.id.et_receiver_name);
        etReceiverLocation = view.findViewById(R.id.et_receiver_location);
        etReceiverPhone = view.findViewById(R.id.et_receiver_phone);
        etReceiverBloodGroup = view.findViewById(R.id.et_receiver_blood_group);
        etReceiverPurpose = view.findViewById(R.id.et_receiver_purpose);
        btnReceiverSubmit = view.findViewById(R.id.btn_receiver_submit);

        // Set an OnClickListener for the submit button
        btnReceiverSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle form submission
                submitReceiverForm();
            }
        });

        return view;
    }

    /**
     * Retrieves data from the form fields, performs a basic validation, and saves the data
     * to a Firestore database.
     */
    private void submitReceiverForm() {
        // Retrieve data from EditText fields
        String name = etReceiverName.getText().toString().trim();
        String location = etReceiverLocation.getText().toString().trim();
        String phone = etReceiverPhone.getText().toString().trim();
        String bloodGroup = etReceiverBloodGroup.getText().toString().trim();
        String purpose = etReceiverPurpose.getText().toString().trim();

        // Basic validation: check if required fields are not empty
        if (name.isEmpty() || location.isEmpty() || phone.isEmpty() || bloodGroup.isEmpty() || purpose.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if validation fails
        }

        // Create a new ReceiverPost object
        ReceiverPost newPost = new ReceiverPost(name, location, phone, purpose, bloodGroup);

        // Add the new post to the "receiverPosts" collection in Firestore
        db.collection("receiverPosts")
                .add(newPost)
                .addOnSuccessListener(documentReference -> {
                    // This block is executed if the data is saved successfully
                    Toast.makeText(getContext(), "Receiver request submitted successfully!", Toast.LENGTH_LONG).show();
                    // Optionally, clear the form fields
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    // This block is executed if there is an error
                    Toast.makeText(getContext(), "Error submitting receiver request.", Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Clears all the input fields in the form.
     */
    private void clearForm() {
        etReceiverName.setText("");
        etReceiverLocation.setText("");
        etReceiverPhone.setText("");
        etReceiverBloodGroup.setText("");
        etReceiverPurpose.setText("");
    }
}