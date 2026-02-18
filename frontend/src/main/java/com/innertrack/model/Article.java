package com.innertrack.model;

import java.time.LocalDate;

public class Article {
    private int id_Article;
    private String titre;
    private String contenu;
    private String auteur;
    private LocalDate datePublication;    private int id_categorie;

    private String categorieNom;

    // Constructor with all fields
    public Article(int id_Article, String titre, String contenu, String auteur, LocalDate datePublication, int id_categorie) {
        this.id_Article = id_Article;
        this.titre = titre;
        this.contenu = contenu;
        this.auteur = auteur;
        this.datePublication = datePublication;
        this.id_categorie = id_categorie;
    }

    // Constructor without id (for adding new articles)
    public Article(String titre, String contenu, String auteur, LocalDate datePublication, int id_categorie) {
        this.titre = titre;
        this.contenu = contenu;
        this.auteur = auteur;
        this.datePublication = datePublication;
        this.id_categorie = id_categorie;
    }

    // Empty constructor
    public Article() {
    }

    // Getters and Setters
    public int getId_Article() {
        return id_Article;
    }

    public void setId_Article(int id_Article) {
        this.id_Article = id_Article;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public int getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(int id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getCategorieNom() {
        return categorieNom;
    }

    public void setCategorieNom(String categorieNom) {
        this.categorieNom = categorieNom;
    }
    @Override
    public String toString() {
        return "Article{" +
                "id_Article=" + id_Article +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", auteur='" + auteur + '\'' +
                ", datePublication=" + datePublication +
                ", id_categorie=" + id_categorie +
                ", categorieNom='" + categorieNom + '\'' +
                '}';
    }
}
