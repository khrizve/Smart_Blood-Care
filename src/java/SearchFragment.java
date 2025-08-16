package com.example.BloodCare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private RadioGroup postTypeRadioGroup;
    private Spinner bloodGroupSpinner;
    private EditText locationEditText;
    private Button searchButton;
    private RecyclerView postsRecyclerView;
    private MapView mapView;

    private FirebaseFirestore db;
    private PostsAdapter postsAdapter;
    private List<Object> postsList;
    private boolean isDonorSearch = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        db = FirebaseFirestore.getInstance();

        postTypeRadioGroup = view.findViewById(R.id.post_type_radio_group);
        bloodGroupSpinner = view.findViewById(R.id.blood_group_spinner);
        locationEditText = view.findViewById(R.id.location_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        mapView = view.findViewById(R.id.map_view);

        setupMap();
        setupBloodGroupSpinner();
        checkLocationPermission();

        postTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            isDonorSearch = checkedId == R.id.donor_radio_button;
        });

        searchButton.setOnClickListener(v -> performSearch());

        postsList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postsList, isDonorSearch);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsRecyclerView.setAdapter(postsAdapter);

        return view;
    }

    /**
     * Checks for location permissions and requests them if not granted.
     * This is a prerequisite for using the map and geocoding.
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            setupMap();
        }
    }

    /**
     * Configures the MapView, including setting the user agent for tile downloads.
     */
    private void setupMap() {
        // Set a user agent to ensure map tiles can be downloaded
        Configuration.getInstance().setUserAgentValue(requireActivity().getPackageName());

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);
        GeoPoint startPoint = new GeoPoint(23.8103, 90.4125); // Default to Dhaka coordinates
        mapController.setCenter(startPoint);
    }

    /**
     * Sets up the blood group selection spinner.
     */
    private void setupBloodGroupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.blood_groups_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapter);
    }

    /**
     * Performs the search operation based on the user's input.
     */
    private void performSearch() {
        String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();
        String locationQuery = locationEditText.getText().toString().trim().toLowerCase();

        if (bloodGroup.equals("Select Blood Group") && locationQuery.isEmpty()) {
            Toast.makeText(getContext(), "Please select blood group or enter location", Toast.LENGTH_SHORT).show();
            return;
        }

        String collectionName = isDonorSearch ? "donorPosts" : "receiverPosts";

        db.collection(collectionName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postsList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            boolean matchesBloodGroup = bloodGroup.equals("Select Blood Group") ||
                                    document.getString("bloodGroup").equals(bloodGroup);

                            boolean matchesLocation = locationQuery.isEmpty() ||
                                    document.getString("location").toLowerCase().contains(locationQuery);

                            if (matchesBloodGroup && matchesLocation) {
                                if (isDonorSearch) {
                                    DonorPost post = document.toObject(DonorPost.class);
                                    postsList.add(post);
                                } else {
                                    ReceiverPost post = document.toObject(ReceiverPost.class);
                                    postsList.add(post);
                                }
                            }
                        }

                        if (postsList.isEmpty()) {
                            Toast.makeText(getContext(), "No matching posts found", Toast.LENGTH_SHORT).show();
                        } else {
                            // Geocode the location and update the map
                            if (!locationQuery.isEmpty()) {
                                geocodeAndShowLocationOnMap(locationQuery);
                            }
                        }

                        postsAdapter.setDonorSearch(isDonorSearch);
                        postsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error getting posts: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Uses Geocoder to find the latitude and longitude of the location string
     * and updates the map to show a marker at that position.
     * @param locationQuery The location string entered by the user.
     */
    private void geocodeAndShowLocationOnMap(String locationQuery) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationQuery, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                GeoPoint point = new GeoPoint(address.getLatitude(), address.getLongitude());

                IMapController mapController = mapView.getController();
                mapView.getOverlays().clear();

                Marker marker = new Marker(mapView);
                marker.setPosition(point);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle("Search results for: " + locationQuery);
                mapView.getOverlays().add(marker);

                mapController.setCenter(point);
                mapController.setZoom(15.0); // Zoom in a bit for a better view
                mapView.invalidate();
            } else {
                Toast.makeText(getContext(), "Location not found for: " + locationQuery, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Geocoding service not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
