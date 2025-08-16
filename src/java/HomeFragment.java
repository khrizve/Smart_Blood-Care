package com.example.BloodCare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

//added by digonto
import androidx.core.content.ContextCompat;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ImageView profileImage;
    private TextView userNameLabel;

    private CardView bloodDonorCard;
    private CardView bloodRecipientCard;
    private CardView createPostCard;
    private CardView bloodGivenCard;

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;


    private CardView cardAPos, cardANeg, cardBPos, cardBNeg, cardOPos, cardONeg, cardABPos, cardABNeg;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        profileImage = view.findViewById(R.id.profile_image);
        userNameLabel = view.findViewById(R.id.user_name_label);

        bloodDonorCard = view.findViewById(R.id.card_blood_donor);
        bloodRecipientCard = view.findViewById(R.id.card_blood_recipient);
        createPostCard = view.findViewById(R.id.card_create_post);
        bloodGivenCard = view.findViewById(R.id.card_blood_given);


        cardAPos = view.findViewById(R.id.cardAPos);
        cardANeg = view.findViewById(R.id.cardANeg);
        cardBPos = view.findViewById(R.id.cardBPos);
        cardBNeg = view.findViewById(R.id.cardBNeg);
        cardOPos = view.findViewById(R.id.cardOPos);
        cardONeg = view.findViewById(R.id.cardONeg);
        cardABPos = view.findViewById(R.id.cardABPos);
        cardABNeg = view.findViewById(R.id.cardABNeg);

        // Set default user name (will be updated if user is logged in)
        userNameLabel.setText("Guest");

        // Load user data if logged in
        if (currentUser != null) {
            loadUserData();
        }

        profileImage.setOnClickListener(v -> {
            if (currentUser != null) {
                // Open edit profile for logged in users
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            } else {
                // Show toast for guest users
                Toast.makeText(getContext(), "Please login to edit profile", Toast.LENGTH_SHORT).show();
            }
        });

        // Rest of your card click listeners remain the same
        bloodDonorCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DonorActivity.class);
            startActivity(intent);
        });

        bloodRecipientCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.BloodCare.RecipientActivity.class);
            startActivity(intent);
        });

        createPostCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.BloodCare.CreatePostActivity.class);
            startActivity(intent);
        });

        bloodGivenCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.BloodCare.BloodGivenActivity.class);
            startActivity(intent);
        });

        //added by digonto
        setupBloodTypeCards();

        return view;
    }

    /**
     * Loads user data from Firestore and updates the UI
     */
    private void loadUserData() {
        if (currentUser == null) return;

        DocumentReference userRef = db.collection("users").document(currentUser.getUid());
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null && userNameLabel != null) {
                    userNameLabel.setText(user.getName());
                }
            }
        }).addOnFailureListener(e -> {
            // Keep default name if loading fails
        });
    }


    private void setupBloodTypeCards() {
        // Pair cards with blood types
        Map<CardView, String> bloodCards = new HashMap<>();
        bloodCards.put(cardAPos, "A+");
        bloodCards.put(cardANeg, "A-");
        bloodCards.put(cardBPos, "B+");
        bloodCards.put(cardBNeg, "B-");
        bloodCards.put(cardOPos, "O+");
        bloodCards.put(cardONeg, "O-");
        bloodCards.put(cardABPos, "AB+");
        bloodCards.put(cardABNeg, "AB-");

        // Track last selected card
        final CardView[] lastSelected = {null};

        // Set listeners
        for (Map.Entry<CardView, String> entry : bloodCards.entrySet()) {
            CardView card = entry.getKey();
            String type = entry.getValue();

            if (card == null) continue; // Skip if card not found

            card.setOnClickListener(v -> {
                // Reset previous selection
                if (lastSelected[0] != null) {
                    lastSelected[0].setCardBackgroundColor(
                            ContextCompat.getColor(requireContext(), R.color.card_default_color));
                }

                // Highlight new selection
                card.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.card_selected_color));
                lastSelected[0] = card;

                // Launch activity
                startActivity(new Intent(getActivity(), BloodTypeInfoActivity.class)
                        .putExtra("BLOOD_TYPE", type));
            });
        }
    }
}