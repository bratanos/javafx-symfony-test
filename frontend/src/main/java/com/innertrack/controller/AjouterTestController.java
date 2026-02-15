package com.innertrack.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.innertrack.model.Question;
import com.innertrack.model.TestPsychologique;
import com.innertrack.service.QuestionCrudService;
import com.innertrack.service.TestPsychologiqueCrudService;
import com.innertrack.util.DBConnection;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Contrôleur pour l'interface d'ajout de tests psychologiques
 * Gère l'ajout du test et de ses questions
 */
public class AjouterTestController implements Initializable {

    // ============================================
    // FXML FIELDS - Liés au fichier FXML
    // ============================================

    @FXML private TextField titreField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private Spinner<Integer> nombreQuestionsSpinner;
    @FXML private Label questionsCountLabel;

    @FXML private Button ajouterQuestionBtn;
    @FXML private VBox ajoutQuestionBox;
    @FXML private TextArea questionContentArea;
    @FXML private VBox questionsListBox;
    @FXML private Label noQuestionsLabel;

    // ============================================
    // VARIABLES PRIVÉES
    // ============================================

    private TestPsychologiqueCrudService testService;
    private QuestionCrudService questionService;
    private List<String> questionsList; // Liste temporaire des questions
    private int editingQuestionIndex = -1; // Pour savoir si on édite une question
    private ListeTestsController listeTestsController; // Référence au contrôleur de la liste
    private Map<Integer, String> typesTestsMap; // id_type -> nom_type chargé depuis BD

    // ============================================
    // INITIALISATION
    // ============================================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser les services
        testService = new TestPsychologiqueCrudService();
        questionService = new QuestionCrudService();
        questionsList = new ArrayList<>();

        // Charger les types de tests depuis la BD
        chargerTypesTests();

        // Configurer les composants
        configurerTypeComboBox();
        configurerSpinner();

        // Mettre à jour le compteur de questions
        updateQuestionsCount();
    }

    /**
     * Charge les types de tests depuis la table type_test
     */
    private void chargerTypesTests() {
        typesTestsMap = new LinkedHashMap<>();

        try {
            Connection cnx = DBConnection.getInstance().getConnection();
            String sql = "SELECT id_type, nom_type FROM type_test ORDER BY id_type";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int idType = rs.getInt("id_type");
                String nomType = rs.getString("nom_type");
                typesTestsMap.put(idType, nomType);
            }

            System.out.println("✅ " + typesTestsMap.size() + " types de tests chargés depuis la BD");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du chargement des types de tests: " + e.getMessage());
            e.printStackTrace();

            // Fallback: types par défaut si erreur
            typesTestsMap.put(1, "Test de Personnalité");
            typesTestsMap.put(2, "Test Cognitif");
            typesTestsMap.put(3, "Test d'Anxiété");
        }
    }

    /**
     * Configure le ComboBox avec les types de tests chargés depuis la BD
     */
    private void configurerTypeComboBox() {
        // Vider d'abord le ComboBox
        typeComboBox.getItems().clear();

        // Remplir avec les types depuis la BD
        for (String nomType : typesTestsMap.values()) {
            typeComboBox.getItems().add(nomType);
        }

        // Sélectionner le premier élément par défaut
        if (!typeComboBox.getItems().isEmpty()) {
            typeComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Configure le Spinner pour le nombre de questions
     */
    private void configurerSpinner() {
        // Créer une SpinnerValueFactory avec min=1, max=100, valeur initiale=10
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10, 1);

        nombreQuestionsSpinner.setValueFactory(valueFactory);
        nombreQuestionsSpinner.setEditable(true);
    }

    // ============================================
    // GESTION DES QUESTIONS
    // ============================================

    /**
     * Affiche le formulaire d'ajout de question
     */
    @FXML
    private void ajouterQuestion() {
        ajoutQuestionBox.setVisible(true);
        ajoutQuestionBox.setManaged(true);
        questionContentArea.clear();
        questionContentArea.requestFocus();
        editingQuestionIndex = -1; // Mode ajout
    }

    /**
     * Valide et ajoute la question à la liste
     */
    @FXML
    private void validerQuestion() {
        String contenu = questionContentArea.getText().trim();

        // Validation
        if (contenu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Veuillez saisir le contenu de la question.");
            return;
        }

        // Ajouter ou modifier la question
        if (editingQuestionIndex >= 0) {
            // Mode édition
            questionsList.set(editingQuestionIndex, contenu);
            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Question modifiée avec succès!");
        } else {
            // Mode ajout
            questionsList.add(contenu);
            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Question ajoutée avec succès!");
        }

        // Réinitialiser le formulaire
        questionContentArea.clear();
        ajoutQuestionBox.setVisible(false);
        ajoutQuestionBox.setManaged(false);

        // Mettre à jour l'affichage
        afficherQuestions();
        updateQuestionsCount();
    }

    /**
     * Annule l'ajout/modification de question
     */
    @FXML
    private void annulerQuestion() {
        questionContentArea.clear();
        ajoutQuestionBox.setVisible(false);
        ajoutQuestionBox.setManaged(false);
        editingQuestionIndex = -1;
    }

    /**
     * Affiche la liste des questions ajoutées
     */
    private void afficherQuestions() {
        questionsListBox.getChildren().clear();

        if (questionsList.isEmpty()) {
            noQuestionsLabel.setVisible(true);
            noQuestionsLabel.setManaged(true);
            questionsListBox.getChildren().add(noQuestionsLabel);
        } else {
            noQuestionsLabel.setVisible(false);
            noQuestionsLabel.setManaged(false);

            for (int i = 0; i < questionsList.size(); i++) {
                VBox questionCard = createQuestionCard(i, questionsList.get(i));
                questionsListBox.getChildren().add(questionCard);
            }
        }
    }

    /**
     * Crée une carte visuelle pour afficher une question
     */
    private VBox createQuestionCard(int index, String contenu) {
        VBox card = new VBox(10);
        card.getStyleClass().add("question-card");
        card.setPadding(new Insets(15));

        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Numéro de la question
        Label numeroLabel = new Label(String.valueOf(index + 1));
        numeroLabel.getStyleClass().add("question-number");
        numeroLabel.setMinWidth(35);
        numeroLabel.setMinHeight(35);
        numeroLabel.setAlignment(Pos.CENTER);

        // Contenu de la question
        Label contenuLabel = new Label(contenu);
        contenuLabel.getStyleClass().add("question-text");
        contenuLabel.setWrapText(true);
        HBox.setHgrow(contenuLabel, Priority.ALWAYS);

        // Boutons d'action
        HBox actionsBox = new HBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);

        Button editBtn = new Button("✏️ Modifier");
        editBtn.getStyleClass().add("question-edit-btn");
        editBtn.setOnAction(e -> modifierQuestion(index));

        Button deleteBtn = new Button("🗑️ Supprimer");
        deleteBtn.getStyleClass().add("question-delete-btn");
        deleteBtn.setOnAction(e -> supprimerQuestion(index));

        actionsBox.getChildren().addAll(editBtn, deleteBtn);

        headerBox.getChildren().addAll(numeroLabel, contenuLabel, actionsBox);
        card.getChildren().add(headerBox);

        return card;
    }

    /**
     * Permet de modifier une question existante
     */
    private void modifierQuestion(int index) {
        editingQuestionIndex = index;
        questionContentArea.setText(questionsList.get(index));
        ajoutQuestionBox.setVisible(true);
        ajoutQuestionBox.setManaged(true);
        questionContentArea.requestFocus();
    }

    /**
     * Supprime une question de la liste
     */
    private void supprimerQuestion(int index) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la question");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette question ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                questionsList.remove(index);
                afficherQuestions();
                updateQuestionsCount();
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        "Question supprimée avec succès!");
            }
        });
    }

    /**
     * Met à jour le compteur de questions
     */
    private void updateQuestionsCount() {
        int count = questionsList.size();
        questionsCountLabel.setText(count + " question(s) ajoutée(s)");
    }

    // ============================================
    // GESTION DU TEST
    // ============================================

    /**
     * Enregistre le test et ses questions dans la base de données
     */
    @FXML
    private void enregistrerTest() {
        // Validation des champs
        if (!validerChamps()) {
            return;
        }

        // Créer l'objet TestPsychologique
        TestPsychologique test = new TestPsychologique();
        test.setTitre(titreField.getText().trim());

        // Trouver l'id_type correspondant au nom sélectionné
        String nomTypeSelectionne = typeComboBox.getValue();
        int idType = 1; // Par défaut
        for (Map.Entry<Integer, String> entry : typesTestsMap.entrySet()) {
            if (entry.getValue().equals(nomTypeSelectionne)) {
                idType = entry.getKey();
                break;
            }
        }
        test.setIdType(idType);

        test.setDescription(descriptionArea.getText().trim());
        test.setNombreQuestions(nombreQuestionsSpinner.getValue());

        // Ajouter le test à la BD
        testService.create(test);

        // Récupérer l'ID du test ajouté (dernier test ajouté)
        List<TestPsychologique> tests = testService.findAll();
        int idTestAjoute = tests.get(tests.size() - 1).getIdTest();

        // Ajouter toutes les questions
        for (String contenu : questionsList) {
            Question question = new Question();
            question.setIdTest(idTestAjoute);
            question.setContenu(contenu);
            questionService.create(question);
        }

        // Message de succès
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText("Test enregistré avec succès!");
        successAlert.setContentText(
                "Le test \"" + test.getTitre() + "\" a été enregistré avec "
                        + questionsList.size() + " question(s)."
        );
        successAlert.showAndWait();

        // Réinitialiser le formulaire
        reinitialiserFormulaire();

    }

    /**
     * Valide tous les champs du formulaire
     */
    private boolean validerChamps() {
        // Vérifier le titre
        if (titreField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Veuillez saisir le titre du test.");
            titreField.requestFocus();
            return false;
        }

        // Vérifier la description
        if (descriptionArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Veuillez saisir la description du test.");
            descriptionArea.requestFocus();
            return false;
        }

        // Vérifier qu'il y a au moins une question
        if (questionsList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Veuillez ajouter au moins une question au test.");
            return false;
        }

        // Vérifier que le nombre de questions correspond
        int nombreAttendu = nombreQuestionsSpinner.getValue();
        int nombreActuel = questionsList.size();

        if (nombreActuel != nombreAttendu) {
            showAlert(Alert.AlertType.WARNING, "Attention",
                    "Le nombre de questions ajoutées (" + nombreActuel
                            + ") ne correspond pas au nombre attendu (" + nombreAttendu + ").");
            return false;
        }

        return true;
    }

    /**
     * Annule l'ajout du test
     */
    @FXML
    private void annulerTest() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Annuler la création du test");
        confirmation.setContentText(
                "Êtes-vous sûr de vouloir annuler ? Toutes les données saisies seront perdues."
        );

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reinitialiserFormulaire();
            }
        });
    }

    /**
     * Réinitialise tous les champs du formulaire
     */
    private void reinitialiserFormulaire() {
        titreField.clear();
        typeComboBox.getSelectionModel().selectFirst();
        descriptionArea.clear();
        nombreQuestionsSpinner.getValueFactory().setValue(10);
        questionsList.clear();
        afficherQuestions();
        updateQuestionsCount();
        ajoutQuestionBox.setVisible(false);
        ajoutQuestionBox.setManaged(false);
    }

    // ============================================
    // UTILITAIRES
    // ============================================

    /**
     * Affiche une boîte de dialogue d'alerte
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Définit la référence au contrôleur de la liste des tests
     */
    public void setListeTestsController(ListeTestsController controller) {
        this.listeTestsController = controller;
    }

    /**
     * Retourne à la liste des tests
     */
    @FXML
    private void retournerListeTests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeTests.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1200, 800); // MÊME TAILLE QUE LA LISTE
            scene.getStylesheets().add(getClass().getResource("/styleListeTests.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Gestion des Tests Psychologiques");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) titreField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de retourner à la liste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}