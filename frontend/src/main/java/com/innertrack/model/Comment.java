package com.innertrack.model;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int user;
    private String comment;
    private LocalDateTime creation;
    private boolean modified;
    private int likes;
    Comment parent;

    public int getId() { return id; }
    public int getUser() {
        return user;
    }
    public String getComment() {
        return comment;
    }

    public void setId(int id) { this.id = id; }
    public void setUser(int user) { this.user = user; }
    public void setComment(String comment) { this.comment = comment; }
    public void setCreation(LocalDateTime creation) { this.creation = creation; }
    public void setModified(boolean modified) { this.modified = modified; }
    public void setLikes(int likes) { this.likes = likes; }

    public Comment(int id, int user, String comment, LocalDateTime creation, boolean modified, int likes) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.creation = creation;
        this.modified = modified;
        this.likes = likes;
    }

    @Override
    public String toString() {
        return id + ", " + user + ", " + comment + ", " + creation + ", " + modified + ", " + likes;
    }


    public Comment(int user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    public Comment() {}
}
