package com.example.BloodCare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 100;

    private ImageView profileImage;
    private EditText nameEditText, mobileEditText, bloodGroupEditText, ageEditText;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private Uri selectedImageUri;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private boolean permissionChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        bloodGroupEditText = findViewById(R.id.bloodGroupEditText);
        ageEditText = findViewById(R.id.ageEditText);
        saveButton = findViewById(R.id.saveButton);

        // Initialize the ActivityResultLauncher for the gallery intent
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        profileImage.setImageURI(selectedImageUri);
                    }
                });

        // Set click listener for profile image to change it
        profileImage.setOnClickListener(v -> checkPermissionAndOpenGallery());

        // Set click listener for save button
        saveButton.setOnClickListener(v -> saveProfileChanges());

        // Load current user data
        loadUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-check permission status when the activity resumes
        // This is crucial for when the user returns from the app settings page
        if (permissionChecked) {
            String permission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission = Manifest.permission.READ_MEDIA_IMAGES;
            } else {
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            }

            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                permissionChecked = false;
                openGallery();
            }
        }
    }

    /**
     * Checks for the correct storage permission based on Android version and opens gallery.
     * If permission is denied permanently, it opens app settings.
     */
    private void checkPermissionAndOpenGallery() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Permission denied, but not permanently. Request again.
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        STORAGE_PERMISSION_CODE);
            } else {
                // Permission denied permanently ("Don't ask again"). Open app settings.
                Toast.makeText(this, "Permission is required. Please enable it in settings.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                permissionChecked = true;
            }
        } else {
            // Permission already granted. Open gallery.
            openGallery();
        }
    }

    /**
     * Opens gallery to select an image
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to access gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Loads current user data from Firestore
     */
    private void loadUserData() {
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        nameEditText.setText(user.getName());
                        mobileEditText.setText(user.getMobile());
                        bloodGroupEditText.setText(user.getBloodGroup());
                        ageEditText.setText(String.valueOf(user.getAge()));
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * Saves profile changes to Firestore
     */
    private void saveProfileChanges() {
        if (currentUser == null) return;

        String name = nameEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();
        String bloodGroup = bloodGroupEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();

        if (name.isEmpty() || mobile.isEmpty() || bloodGroup.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user data in Firestore
        User updatedUser = new User(name, mobile, bloodGroup, age);
        db.collection("users").document(currentUser.getUid())
                .set(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}