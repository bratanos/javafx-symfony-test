package com.innertrack.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.innertrack.model.Question;
import com.innertrack.model.TestPsychologique;
import com.innertrack.service.QuestionCrudService;
import com.innertrack.service.TestPsychologiqueCrudService;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contrôleur pour l'interface de liste et gestion des tests psychologiques
 * Permet de visualiser, modifier, supprimer les tests et leurs questions
 */
public class ListeTestsController implements Initializable {

    // ============================================
    // FXML FIELDS - Composants de l'interface
    // ============================================

    // En-tête et recherche
    @FXML private TextField searchField;
    @FXML private Label totalTestsLabel;
    @FXML private Label totalQuestionsLabel;

    // Conteneurs
    @FXML private VBox testsListContainer;
    @FXML private VBox questionsListContainer;

    // Section détails
    @FXML private VBox detailsSection;
    @FXML private VBox emptyStateSection;

    // Labels détails du test
    @FXML private Label testTitreLabel;
    @FXML private Label testTypeLabel;
    @FXML private Label testDescriptionLabel;
    @FXML private Label testNbQuestionsLabel;

    // Pied de page
    @FXML private Label statusLabel;

    // ============================================
    // VARIABLES PRIVÉES
    // ============================================

    private TestPsychologiqueCrudService testService;
    private QuestionCrudService questionService;
    private TestPsychologique testSelectionne;
    private List<TestPsychologique> tousLesTests;
    private List<Question> questionsActuelles;

    // Types de tests (correspondant à id_type dans la BD)
    private final String[] TYPES_TESTS = {
            "Test de Personnalité",
            "Test Cognitif",
            "Test d'Anxiété",
            "Test de Dépression",
            "Test d'Intelligence Émotionnelle",
            "Test de Mémoire",
            "Test d'Attention",
            "Évaluation Comportementale",
            "Test Projectif",
            "Autre"
    };

    // ============================================
    // INITIALISATION
    // ============================================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les services
        testService = new TestPsychologiqueCrudService();
        questionService = new QuestionCrudService();

        // Charger la liste des tests
        chargerTests();

        // Afficher l'état vide par défaut
        afficherEtatVide();

        // Mettre à jour les statistiques
        updateStatistiques();
    }

    // ============================================
    // CHARGEMENT DES DONNÉES
    // ============================================

    /**
     * Charge tous les tests depuis la base de données
     */
    private void chargerTests() {
        tousLesTests = testService.findAll();
        afficherTests(tousLesTests);
        updateStatistiques();
        updateStatus("Tests chargés avec succès");
    }

    /**
     * Affiche la liste des tests dans le panneau gauche
     */
    private void afficherTests(List<TestPsychologique> tests) {
        testsListContainer.getChildren().clear();

        if (tests.isEmpty()) {
            Label emptyLabel = new Label("Aucun test trouvé");
            emptyLabel.setStyle("-fx-text-fill: #a0aec0; -fx-font-style: italic;");
            testsListContainer.getChildren().add(emptyLabel);
            return;
        }

        for (TestPsychologique test : tests) {
            VBox testCard = creerCarteTest(test);
            testsListContainer.getChildren().add(testCard);
        }
    }

    /**
     * Crée une carte visuelle pour un test
     */
    private VBox creerCarteTest(TestPsychologique test) {
        VBox card = new VBox(8);
        card.getStyleClass().add("test-card");
        card.setPadding(new Insets(15));

        // Titre
        Label titre = new Label(test.getTitre());
        titre.getStyleClass().add("test-card-title");
        titre.setWrapText(true);

        // Type et nombre de questions
        HBox infoBox = new HBox(15);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel = new Label(getTypeNom(test.getIdType()));
        typeLabel.getStyleClass().add("test-card-badge");

        Label nbQuestions = new Label(test.getNombreQuestions() + " questions");
        nbQuestions.getStyleClass().add("test-card-info");

        infoBox.getChildren().addAll(typeLabel, nbQuestions);

        card.getChildren().addAll(titre, infoBox);

        // Action au clic
        card.setOnMouseClicked(event -> {
            selectionnerTest(test);
            // Retirer la sélection visuelle des autres cartes
            testsListContainer.getChildren().forEach(node -> {
                node.getStyleClass().remove("test-card-selected");
            });
            // Ajouter la sélection à cette carte
            card.getStyleClass().add("test-card-selected");
        });

        return card;
    }

    /**
     * Sélectionne un test et affiche ses détails
     */
    private void selectionnerTest(TestPsychologique test) {
        testSelectionne = test;

        // Afficher la section détails
        emptyStateSection.setVisible(false);
        emptyStateSection.setManaged(false);
        detailsSection.setVisible(true);
        detailsSection.setManaged(true);

        // Remplir les informations du test
        testTitreLabel.setText(test.getTitre());
        testTypeLabel.setText(getTypeNom(test.getIdType()));
        testDescriptionLabel.setText(test.getDescription());
        testNbQuestionsLabel.setText(String.valueOf(test.getNombreQuestions()));

        // Charger et afficher les questions
        chargerQuestionsDuTest(test.getIdTest());

        updateStatus("Test sélectionné: " + test.getTitre());
    }

    /**
     * Charge les questions d'un test spécifique
     */

    private void chargerQuestionsDuTest(int idTest) {
        try {
            questionsActuelles = questionService.recupererParTest(idTest);
            afficherQuestions(questionsActuelles);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des questions", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche les questions dans le panneau droit
     */
    private void afficherQuestions(List<Question> questions) {
        questionsListContainer.getChildren().clear();

        if (questions.isEmpty()) {
            Label emptyLabel = new Label("Aucune question pour ce test");
            emptyLabel.setStyle("-fx-text-fill: #a0aec0; -fx-font-style: italic; -fx-padding: 20;");
            questionsListContainer.getChildren().add(emptyLabel);
            return;
        }

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            VBox questionCard = creerCarteQuestion(question, i + 1);
            questionsListContainer.getChildren().add(questionCard);
        }
    }

    /**
     * Crée une carte visuelle pour une question avec boutons Oui/Non/Parfois
     */
    private VBox creerCarteQuestion(Question question, int numero) {
        VBox card = new VBox(12);
        card.getStyleClass().add("question-card");
        card.setPadding(new Insets(15));

        // En-tête avec numéro et texte
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.TOP_LEFT);

        // Numéro de la question
        Label numeroLabel = new Label(String.valueOf(numero));
        numeroLabel.getStyleClass().add("question-number");
        numeroLabel.setMinWidth(35);
        numeroLabel.setMinHeight(35);
        numeroLabel.setAlignment(Pos.CENTER);

        // Texte de la question
        Label contenuLabel = new Label(question.getContenu());
        contenuLabel.getStyleClass().add("question-text");
        contenuLabel.setWrapText(true);
        HBox.setHgrow(contenuLabel, Priority.ALWAYS);

        headerBox.getChildren().addAll(numeroLabel, contenuLabel);

        // Boutons de réponse (Oui, Non, Parfois)
        HBox reponsesBox = new HBox(10);
        reponsesBox.setAlignment(Pos.CENTER_LEFT);
        reponsesBox.setPadding(new Insets(10, 0, 0, 50)); // Indent pour aligner avec le texte

        Button btnOui = new Button("✓ Oui");
        btnOui.getStyleClass().addAll("reponse-btn", "reponse-btn-oui");

        Button btnNon = new Button("✗ Non");
        btnNon.getStyleClass().addAll("reponse-btn", "reponse-btn-non");

        Button btnParfois = new Button("~ Parfois");
        btnParfois.getStyleClass().addAll("reponse-btn", "reponse-btn-parfois");

        reponsesBox.getChildren().addAll(btnOui, btnNon, btnParfois);

        // Boutons d'action (Modifier, Supprimer)
        HBox actionsBox = new HBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        actionsBox.setPadding(new Insets(10, 0, 0, 0));

        Button editBtn = new Button("✏️ Modifier");
        editBtn.getStyleClass().add("question-edit-btn");
        editBtn.setOnAction(e -> modifierQuestion(question));

        Button deleteBtn = new Button("🗑️ Supprimer");
        deleteBtn.getStyleClass().add("question-delete-btn");
        deleteBtn.setOnAction(e -> supprimerQuestion(question));

        actionsBox.getChildren().addAll(editBtn, deleteBtn);

        card.getChildren().addAll(headerBox, reponsesBox, actionsBox);

        return card;
    }

    // ============================================
    // ACTIONS DE NAVIGATION
    // ============================================

    /**
     * Ouvre l'interface d'ajout de test
     */
    @FXML
    private void ouvrirAjoutTest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterTest.fxml"));
            Parent root = loader.load();

            // Passer une référence de ce contrôleur pour pouvoir rafraîchir la liste
            AjouterTestController controller = loader.getController();
            controller.setListeTestsController(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Test Psychologique");
            stage.setScene(scene);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) searchField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir l'interface d'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualise la liste des tests
     */
    @FXML
    private void actualiserListe() {
        chargerTests();
        afficherEtatVide();
        updateStatus("Liste actualisée");
    }

    // ============================================
    // ACTIONS CRUD - TEST
    // ============================================

    /**
     * Modifie le test sélectionné
     */
    @FXML
    private void modifierTest() {
        if (testSelectionne == null) {
            showWarning("Aucun test sélectionné", "Veuillez sélectionner un test à modifier.");
            return;
        }

        // Créer une boîte de dialogue pour modifier le test
        Dialog<TestPsychologique> dialog = new Dialog<>();
        dialog.setTitle("Modifier le Test");
        dialog.setHeaderText("Modification de: " + testSelectionne.getTitre());

        // Boutons
        ButtonType btnValider = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

        // Contenu
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        TextField titreField = new TextField(testSelectionne.getTitre());
        titreField.setPromptText("Titre du test");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(TYPES_TESTS);
        typeCombo.setValue(getTypeNom(testSelectionne.getIdType()));

        TextArea descArea = new TextArea(testSelectionne.getDescription());
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true);

        content.getChildren().addAll(
                new Label("Titre:"), titreField,
                new Label("Type:"), typeCombo,
                new Label("Description:"), descArea
        );

        dialog.getDialogPane().setContent(content);

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnValider) {
                testSelectionne.setTitre(titreField.getText());
                testSelectionne.setIdType(typeCombo.getSelectionModel().getSelectedIndex() + 1);
                testSelectionne.setDescription(descArea.getText());
                return testSelectionne;
            }
            return null;
        });

        Optional<TestPsychologique> result = dialog.showAndWait();
        result.ifPresent(test -> {
            testService.update(test);
            showInfo("Succès", "Test modifié avec succès!");
            chargerTests();
            selectionnerTest(test);
        });
    }

    /**
     * Supprime le test sélectionné
     */
    @FXML
    private void supprimerTest() {
        if (testSelectionne == null) {
            showWarning("Aucun test sélectionné", "Veuillez sélectionner un test à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le test: " + testSelectionne.getTitre());
        confirmation.setContentText(
                "Cette action supprimera le test et toutes ses questions ("
                        + testSelectionne.getNombreQuestions() + " questions).\n\n"
                        + "Êtes-vous sûr de vouloir continuer?"
        );

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            testService.delete(testSelectionne.getIdTest());
            showInfo("Succès", "Test supprimé avec succès!");
            testSelectionne = null;
            chargerTests();
            afficherEtatVide();
        }
    }

    // ============================================
    // ACTIONS CRUD - QUESTION
    // ============================================

    /**
     * Ajoute une nouvelle question au test sélectionné
     */
    @FXML
    private void ajouterNouvelleQuestion() {
        if (testSelectionne == null) {
            showWarning("Aucun test sélectionné", "Veuillez sélectionner un test.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouvelle Question");
        dialog.setHeaderText("Ajouter une question à: " + testSelectionne.getTitre());
        dialog.setContentText("Contenu de la question:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(contenu -> {
            if (!contenu.trim().isEmpty()) {
                Question nouvelleQuestion = new Question();
                nouvelleQuestion.setIdTest(testSelectionne.getIdTest());
                nouvelleQuestion.setContenu(contenu.trim());

                questionService.create(nouvelleQuestion);

                // Mettre à jour le nombre de questions
                testSelectionne.setNombreQuestions(testSelectionne.getNombreQuestions() + 1);
                testService.update(testSelectionne);

                showInfo("Succès", "Question ajoutée avec succès!");
                chargerQuestionsDuTest(testSelectionne.getIdTest());
                selectionnerTest(testSelectionne);
                chargerTests(); // Rafraîchir la liste pour mettre à jour le nombre

            }
        });
    }

    /**
     * Modifie une question existante
     */
    private void modifierQuestion(Question question) {
        TextInputDialog dialog = new TextInputDialog(question.getContenu());
        dialog.setTitle("Modifier la Question");
        dialog.setHeaderText("Modification de la question");
        dialog.setContentText("Nouveau contenu:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(contenu -> {
            if (!contenu.trim().isEmpty()) {
                question.setContenu(contenu.trim());
                questionService.update(question);
                showInfo("Succès", "Question modifiée avec succès!");
                chargerQuestionsDuTest(testSelectionne.getIdTest());
            }
        });
    }

    /**
     * Supprime une question
     */
    private void supprimerQuestion(Question question) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer cette question?");
        confirmation.setContentText(question.getContenu());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            questionService.delete(question.getIdQuestion());

            // Mettre à jour le nombre de questions
            testSelectionne.setNombreQuestions(testSelectionne.getNombreQuestions() - 1);
            testService.update(testSelectionne);

            showInfo("Succès", "Question supprimée avec succès!");
            chargerQuestionsDuTest(testSelectionne.getIdTest());
            selectionnerTest(testSelectionne);
            chargerTests();

        }
    }

    // ============================================
    // RECHERCHE
    // ============================================

    /**
     * Recherche un test par titre
     */
    @FXML
    private void rechercherTest() {
        String recherche = searchField.getText().toLowerCase().trim();

        if (recherche.isEmpty()) {
            afficherTests(tousLesTests);
            return;
        }

        List<TestPsychologique> testsFiltres = tousLesTests.stream()
                .filter(test -> test.getTitre().toLowerCase().contains(recherche))
                .toList();

        afficherTests(testsFiltres);
        updateStatus("Tests filtrés: " + testsFiltres.size() + " résultat(s)");
    }

    // ============================================
    // UTILITAIRES
    // ============================================

    /**
     * Affiche l'état vide (aucun test sélectionné)
     */
    private void afficherEtatVide() {
        detailsSection.setVisible(false);
        detailsSection.setManaged(false);
        emptyStateSection.setVisible(true);
        emptyStateSection.setManaged(true);
        testSelectionne = null;
    }

    /**
     * Met à jour les statistiques (nombre de tests et questions)
     */
    private void updateStatistiques() {
        if (tousLesTests != null) {
            totalTestsLabel.setText(String.valueOf(tousLesTests.size()));

            int totalQuestions = tousLesTests.stream()
                    .mapToInt(TestPsychologique::getNombreQuestions)
                    .sum();
            totalQuestionsLabel.setText(String.valueOf(totalQuestions));
        }
    }

    /**
     * Met à jour le label de statut
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Retourne le nom du type de test à partir de son ID
     */
    private String getTypeNom(int idType) {
        if (idType >= 1 && idType <= TYPES_TESTS.length) {
            return TYPES_TESTS[idType - 1];
        }
        return "Autre";
    }

    /**
     * Méthode publique pour rafraîchir la liste (appelée depuis AjouterTestController)
     */
    public void rafraichirListe() {
        chargerTests();
        updateStatus("Liste rafraîchie");
    }

    // ============================================
    // ALERTES ET DIALOGUES
    // ============================================

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
