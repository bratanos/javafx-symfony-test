package com.innertrack.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String email;
    private String password;
    private String status;
    private boolean isVerified;
    private List<String> roles;

    public User() {
        this.roles = new ArrayList<>();
        this.roles.add("ROLE_USER");
    }

    public User(int id, String email, String password, String status, boolean isVerified, List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.isVerified = isVerified;
        this.roles = roles;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", isVerified=" + isVerified +
                ", roles=" + roles +
                '}';
    }
}
