package com.example.BloodCare;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

/**
 * A data model class representing a Blood Donor post.
 * This class follows OOP principles by encapsulating the data related to a donor post.
 * It's used to easily convert the form data into a Firestore-compatible object.
 *
 * It uses the @ServerTimestamp annotation to automatically add a timestamp from the server,
 * which is more reliable than a local device timestamp.
 */
public class DonorPost {
    private String name;
    private String bloodGroup;
    private String location;
    private String phone;
    private boolean hasDisease;
    private @ServerTimestamp Date timestamp;

    public DonorPost() {
        // Required empty constructor for Firestore
    }

    public DonorPost(String name, String bloodGroup, String location, String phone, boolean hasDisease) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.phone = phone;
        this.hasDisease = hasDisease;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getHasDisease() {
        return hasDisease;
    }

    public void setHasDisease(boolean hasDisease) {
        this.hasDisease = hasDisease;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
