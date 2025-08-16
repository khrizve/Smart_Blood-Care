package com.example.BloodCare;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

/**
 * A data model class representing a Blood Receiver post.
 * This class follows OOP principles by encapsulating the data related to a receiver post.
 * It's used to easily convert the form data into a Firestore-compatible object.
 *
 * It uses the @ServerTimestamp annotation to automatically add a timestamp from the server,
 * which is more reliable than a local device timestamp.
 */
public class ReceiverPost {
    private String name;
    private String location;
    private String phone;
    private String purpose;
    private String bloodGroup;
    private @ServerTimestamp Date timestamp;

    public ReceiverPost() {
        // Required empty constructor for Firestore
    }

    public ReceiverPost(String name, String location, String phone, String purpose, String bloodGroup) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.purpose = purpose;
        this.bloodGroup = bloodGroup;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}