package com.innertrack.model;

import java.time.LocalDate;

public class EntreeJournal {
    private int idJournal;
    private int humeur;
    private String noteTextuelle;
    private LocalDate dateSaisie;
    private int idUser;

    public EntreeJournal() {
    }

    public EntreeJournal(int humeur, String noteTextuelle, LocalDate dateSaisie, int idUser) {
        this.humeur = humeur;
        this.noteTextuelle = noteTextuelle;
        this.dateSaisie = dateSaisie;
        this.idUser = idUser;
    }

    public EntreeJournal(int idJournal, int humeur,
            String noteTextuelle, LocalDate dateSaisie, int idUser) {
        this.idJournal = idJournal;
        this.humeur = humeur;
        this.noteTextuelle = noteTextuelle;
        this.dateSaisie = dateSaisie;
        this.idUser = idUser;
    }

    public EntreeJournal(int humeur, String emotionDominante,
            String noteTextuelle, LocalDate dateSaisie, int idUser) {
        this.humeur = humeur;
        this.noteTextuelle = noteTextuelle;
        this.dateSaisie = dateSaisie;
        this.idUser = idUser;
    }

    public int getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(int idJournal) {
        this.idJournal = idJournal;
    }

    public int getHumeur() {
        return humeur;
    }

    public void setHumeur(int humeur) {
        this.humeur = humeur;
    }

    public String getNoteTextuelle() {
        return noteTextuelle;
    }

    public void setNoteTextuelle(String noteTextuelle) {
        this.noteTextuelle = noteTextuelle;
    }

    public LocalDate getDateSaisie() {
        return dateSaisie;
    }

    public void setDateSaisie(LocalDate dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "EntreeJournal{"
                + "idJournal=" + idJournal
                + ", humeur=" + humeur
                + ", noteTextuelle=" + noteTextuelle
                + ", dateSaisie=" + dateSaisie + '}';
    }

}
