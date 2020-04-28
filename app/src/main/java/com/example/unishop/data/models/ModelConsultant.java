package com.example.unishop.data.models;

public class ModelConsultant {
    private String user_id;
    private String phone;
    private String email;
    private String firstname;
    private String lastname;
    private String date_created;
    private String created_by;

    public ModelConsultant() {
    }

    public ModelConsultant(String user_id, String phone, String email, String firstname, String lastname, String date_created, String created_by) {
        this.user_id = user_id;
        this.phone = phone;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date_created = date_created;
        this.created_by = created_by;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
