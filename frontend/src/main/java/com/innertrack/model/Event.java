package com.innertrack.model;

import java.time.LocalDate;

public class Event {

    private int idEvent;
    private String titre;
    private String description;
    private LocalDate dateEvent;
    private TypeEvent typeEvent;
    private LocalDate dateCreation;
    private int capacite;
    private boolean status;

    public Event() {
    }

    public Event(int idEvent, String titre, String description, LocalDate dateEvent, TypeEvent typeEvent, LocalDate dateCreation, int capacite, boolean status) {
        this.idEvent = idEvent;
        this.titre = titre;
        this.description = description;
        this.dateEvent = dateEvent;
        this.typeEvent = typeEvent;
        this.dateCreation = dateCreation;
        this.capacite = capacite;
        this.status = status;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDate dateEvent) {
        this.dateEvent = dateEvent;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStatut(boolean statut) {
        this.status = statut;
    }

    public boolean isStatut() {
        return status;
    }
}
