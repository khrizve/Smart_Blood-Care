package com.example.BloodCare;

/**
 * User class represents a user object with name, mobile number, blood group, and age.
 * This is a simple Java Object (POJO) used for storing user data in Firestore.
 */
public class User {
    private String name;
    private String mobile;
    private String bloodGroup;
    private int age;
    private String profileImageUrl; // Not used for local storage but kept for future use

    // Required empty public constructor for Firebase
    public User() {
    }

    public User(String name, String mobile, String bloodGroup, int age) {
        this.name = name;
        this.mobile = mobile;
        this.bloodGroup = bloodGroup;
        this.age = age;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
