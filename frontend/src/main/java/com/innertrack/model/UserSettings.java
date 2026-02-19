package com.innertrack.model;

public class UserSettings {
    private int userId;
    private String theme; // LIGHT, DARK
    private String fontSize; // SMALL, NORMAL, LARGE
    private String language; // EN, FR

    public UserSettings() {
    }

    public UserSettings(int userId, String theme, String fontSize, String language) {
        this.userId = userId;
        this.theme = theme;
        this.fontSize = fontSize;
        this.language = language;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
