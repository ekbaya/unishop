package com.example.unishop.models;

public class User {
    private String email, enrolledBy, firstname, id_number, lastname, phone, role, uid;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnrolledBy() {
        return enrolledBy;
    }

    public void setEnrolledBy(String enrolledBy) {
        this.enrolledBy = enrolledBy;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", enrolledBy='" + enrolledBy + '\'' +
                ", firstname='" + firstname + '\'' +
                ", id_number='" + id_number + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
