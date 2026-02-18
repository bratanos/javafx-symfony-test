package com.innertrack.model;

import java.time.LocalDate;

public class Habitude {

    private int idHabit;
    private String nomHabitude;
    private String emotionDominantes;
    private String noteTextuelle;
    private int niveauEnergie;
    private int niveauStress;
    private int qualiteSommeil;
    private LocalDate dateCreation;
    private int idUser;
    private Integer idJournal;

    public Habitude() {
    }

    /*
     * public Habitude(int idHabit, String nomHabitude, String emotionDominantes,
     * String noteTextuelle, int niveauEnergie, int niveauStress,
     * int qualiteSommeil, LocalDate dateCreation, int idUser, Integer idJournal) {
     * this.idHabit = idHabit;
     * this.nomHabitude = nomHabitude;
     * this.emotionDominantes = emotionDominantes;
     * this.noteTextuelle = noteTextuelle;
     * this.niveauEnergie = niveauEnergie;
     * this.niveauStress = niveauStress;
     * this.qualiteSommeil = qualiteSommeil;
     * this.dateCreation = dateCreation;
     * this.idUser = idUser;
     * this.idJournal = idJournal;
     * }
     */

    public Habitude(String nomHabitude, String emotionDominantes, String noteTextuelle,
            int niveauEnergie, int niveauStress, int qualiteSommeil,
            LocalDate dateCreation, int idUser) {
        this.nomHabitude = nomHabitude;
        this.emotionDominantes = emotionDominantes;
        this.noteTextuelle = noteTextuelle;
        this.niveauEnergie = niveauEnergie;
        this.niveauStress = niveauStress;
        this.qualiteSommeil = qualiteSommeil;
        this.dateCreation = dateCreation;
        this.idUser = idUser;
    }

    public int getIdHabit() {
        return idHabit;
    }

    public void setIdHabit(int idHabit) {
        this.idHabit = idHabit;
    }

    public String getNomHabitude() {
        return nomHabitude;
    }

    public void setNomHabitude(String nomHabitude) {
        this.nomHabitude = nomHabitude;
    }

    public String getEmotionDominantes() {
        return emotionDominantes;
    }

    public void setEmotionDominantes(String emotionDominantes) {
        this.emotionDominantes = emotionDominantes;
    }

    public String getNoteTextuelle() {
        return noteTextuelle;
    }

    public void setNoteTextuelle(String noteTextuelle) {
        this.noteTextuelle = noteTextuelle;
    }

    public int getNiveauEnergie() {
        return niveauEnergie;
    }

    public void setNiveauEnergie(int niveauEnergie) {
        this.niveauEnergie = niveauEnergie;
    }

    public int getNiveauStress() {
        return niveauStress;
    }

    public void setNiveauStress(int niveauStress) {
        this.niveauStress = niveauStress;
    }

    public int getQualiteSommeil() {
        return qualiteSommeil;
    }

    public void setQualiteSommeil(int qualiteSommeil) {
        this.qualiteSommeil = qualiteSommeil;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Integer getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(Integer idJournal) {
        this.idJournal = idJournal;
    }

    @Override
    public String toString() {
        return "Habitude{" +
                "id=" + idHabit +
                ", nom='" + nomHabitude + '\'' +
                ", emotion='" + emotionDominantes + '\'' +
                ", energie=" + niveauEnergie +
                ", stress=" + niveauStress +
                ", sommeil=" + qualiteSommeil +
                ", date=" + dateCreation +
                '}';
    }
}