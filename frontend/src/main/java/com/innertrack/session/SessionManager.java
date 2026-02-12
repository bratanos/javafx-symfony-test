package com.innertrack.session;

import com.innertrack.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private String jwtToken;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void cleanSession() {
        currentUser = null;
        jwtToken = null;
    }
}
