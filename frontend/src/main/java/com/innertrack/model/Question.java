package com.innertrack.model;


public class Question {
    private int idQuestion;
    private int idTest;
    private String contenu;

    // Constructeurs
    public Question() {}

    public Question(int idTest, String contenu) {
        this.idTest = idTest;
        this.contenu = contenu;
    }

    public Question(int idQuestion, int idTest, String contenu) {
        this.idQuestion = idQuestion;
        this.idTest = idTest;
        this.contenu = contenu;
    }

    // Getters et Setters
    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public String toString() {
        return "Question{" +
                "idQuestion=" + idQuestion +
                ", idTest=" + idTest +
                ", contenu='" + contenu + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;
        return idQuestion == question.idQuestion;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idQuestion);
    }
}
