package com.innertrack.model;


public class Categorie
{
    private int id_categorie;
    private String nom;
    private String description;

    // Constructor with all fields
    public Categorie(int id_categorie, String nom, String description) {
        this.id_categorie = id_categorie;
        this.nom = nom;
        this.description = description;
    }

    // Constructor without id (for adding new categories)
    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Empty constructor
    public Categorie() {
    }

    // Getters and Setters
    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id_categorie=" + id_categorie +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
