package com.innertrack.model;

public class TestPsychologique {

    private int idTest;
    private String titre;
    private int idType;
    private String description;
    private int nombreQuestions;

    public TestPsychologique() {}

    public TestPsychologique(String titre, int idType, String description, int nombreQuestions) {
        this.titre = titre;
        this.idType = idType;
        this.description = description;
        this.nombreQuestions = nombreQuestions;
    }

    public TestPsychologique(int idTest, String titre, int idType, String description, int nombreQuestions) {
        this.idTest = idTest;
        this.titre = titre;
        this.idType = idType;
        this.description = description;
        this.nombreQuestions = nombreQuestions;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNombreQuestions() {
        return nombreQuestions;
    }

    public void setNombreQuestions(int nombreQuestions) {
        this.nombreQuestions = nombreQuestions;
    }

    @Override
    public String toString() {
        return "TestPsychologique{" +
                "idTest=" + idTest +
                ", titre='" + titre + '\'' +
                ", idType=" + idType +
                ", description='" + description + '\'' +
                ", nombreQuestions=" + nombreQuestions +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestPsychologique that = (TestPsychologique) o;
        return idTest == that.idTest;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(idTest);
    }


}
