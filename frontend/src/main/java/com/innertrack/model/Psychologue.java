package com.innertrack.model;

public class Psychologue extends User {
    public Psychologue() {
        super();
        this.getRoles().add("ROLE_PSYCHOLOGUE");
    }

    public Psychologue(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setRoles(user.getRoles());
        this.setVerified(user.isVerified());
        this.setStatus(user.getStatus());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setProfilePicture(user.getProfilePicture());
    }
}
