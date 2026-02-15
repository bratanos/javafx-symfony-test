package com.innertrack.model;
import java.time.LocalDate;


public class Inscription {

    private int idInscription;
        private int idEvent;
        private String nomParticipant;
        private String emailParticipant;
        private LocalDate dateInscription;
        private String titreEvent;




        public Inscription(int idInscription, int idEvenement, LocalDate dateInscription, String statut, String nomParticipant) {
        }


    public Inscription(int idInscription, int idEvent, String nomParticipant, String emailParticipant, LocalDate dateInscription, String titreEvent) {
            this.idInscription = idInscription;
            this.idEvent = idEvent;
            this.nomParticipant = nomParticipant;
            this.emailParticipant = emailParticipant;
            this.dateInscription = dateInscription;
            this.titreEvent = titreEvent;
        }

        public int getIdInscription() {
            return idInscription;
        }

        public void setIdInscription(int idInscription) {
            this.idInscription = idInscription;
        }

        public int getIdEvent() {
            return idEvent;
        }

        public void setIdEvent(int idEvent) {
            this.idEvent = idEvent;
        }

    public String getTitreEvent() {
        return titreEvent;
    }

    public String getNomParticipant() {
            return nomParticipant;
        }

        public void setNomParticipant(String nomParticipant) {
            this.nomParticipant = nomParticipant;
        }

        public String getEmailParticipant() {
            return emailParticipant;
        }

        public void setEmailParticipant(String emailParticipant) {
            this.emailParticipant = emailParticipant;
        }

        public LocalDate getDateInscription() {
            return dateInscription;
        }

        public void setDateInscription(LocalDate dateInscription) {
            this.dateInscription = dateInscription;
        }

    public void setTitreEvent(String titreEvent) {
        this.titreEvent = titreEvent;
    }
}


