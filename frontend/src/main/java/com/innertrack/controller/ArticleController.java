package com.innertrack.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import com.innertrack.model.Article;
import com.innertrack.model.Categorie;
import com.innertrack.service.ArticleService;
import com.innertrack.service.CategorieService;
import com.innertrack.util.ValidationUtils;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {

    // ── Form fields ───────────────────────────────────────────────────────────
    @FXML private TextField           tfTitre;
    @FXML private TextField           tfAuteur;
    @FXML private DatePicker          dpDatePublication;
    @FXML private ComboBox<Categorie> cbCategorie;
    @FXML private TextArea            taContenu;
    @FXML private TextArea            taPreview;

    // ── Left sidebar ──────────────────────────────────────────────────────────
    @FXML private TextField                   tfSearch;
    @FXML private TableView<Article>          tableArticles;
    @FXML private TableColumn<Article,String> colTitre;
    @FXML private TableColumn<Article,String> colAuteur;
    @FXML private TableColumn<Article,String> colCategorie;
    @FXML private TableColumn<Article,LocalDate> colDate;

    // ── Right panel ───────────────────────────────────────────────────────────
    @FXML private VBox       emptyState;
    @FXML private ScrollPane detailScrollPane;
    @FXML private VBox       detailView;
    @FXML private Label      lblDetailTitle;
    @FXML private Label      lblDetailMeta;

    // ── Buttons ───────────────────────────────────────────────────────────────
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    /**
     * The "← Retour aux catégories" button.
     * Must have fx:id="btnBack" in ArticleView.fxml.
     * Visible always — it opens the Category window.
     */
    @FXML private Button btnBack;

    // =========================================================================
    // SERVICES & DATA
    // =========================================================================

    private final ArticleService    articleService;
    private final CategorieService  categorieService;

    /**
     * Master list of all articles loaded from DB.
     * ObservableList notifies the TableView automatically when items change.
     */
    private final ObservableList<Article>   articleData  = FXCollections.observableArrayList();

    /**
     * Master list of categories for the ComboBox.
     */
    private final ObservableList<Categorie> categoryData = FXCollections.observableArrayList();

    /**
     * FilteredList wraps articleData and applies a search predicate.
     * The TableView is bound to this, not directly to articleData.
     */
    private FilteredList<Article> filteredData;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =========================================================================
    // NAVIGATION STATE — set by MainController before loader.load()
    // =========================================================================

    /**
     * Runnable injected by MainController.
     * Calling run() opens the Category window (navigating "back").
     * A Runnable is a functional interface with a single no-arg method: run().
     */
    private Runnable onNavigateToCategories;

    /**
     * If set before initialize(), the search field is pre-filled with this
     * category's name to filter the table to only its articles.
     */
    private Categorie filterCategorie = null;

    /**
     * If set before initialize(), this article is auto-selected in the table
     * after the data loads.
     */
    private Article initialSelection = null;

    // =========================================================================
    // CONSTRUCTOR — called by MainController manually (not by FXML)
    // =========================================================================

    /**
     * ArticleController uses constructor injection because it needs services.
     * MainController creates it with: new ArticleController(articleService, categorieService)
     * then calls loader.setController(controller) before loader.load().
     */
    public ArticleController(ArticleService articleService, CategorieService categorieService) {
        this.articleService   = articleService;
        this.categorieService = categorieService;
    }

    // =========================================================================
    // SETTERS — must be called BEFORE loader.load()
    // =========================================================================

    /** Injects the "open category window" callback. */
    public void setOnNavigateToCategories(Runnable callback) {
        this.onNavigateToCategories = callback;
    }

    /** Pre-sets a category filter applied during initialize(). */
    public void setFilterCategorie(Categorie categorie) {
        this.filterCategorie = categorie;
    }

    /** Pre-sets an article to auto-select after data loads. */
    public void setInitialSelection(Article article) {
        this.initialSelection = article;
    }

    // =========================================================================
    // INITIALIZE
    // =========================================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadCategories();
        setupFilteredSearch();
        loadData();           // starts a background thread to load articles
        showEmptyState();     // start with placeholder visible

        // When a table row is selected → fill the form and show detail panel
        tableArticles.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        fillForm(selected);
                        showDetailPanel(selected);
                    }
                });

        // ── Apply category filter if we arrived from the Category window ──────
        if (filterCategorie != null) {
            // Pre-fill search field — this triggers setupFilteredSearch()'s listener
            // which applies the predicate automatically
            tfSearch.setText(filterCategorie.getNom());
            // Show a dismissible banner so the user knows a filter is active
            showFilterBanner(filterCategorie);
        }

        // ── Auto-select a specific article if one was passed in ───────────────
        if (initialSelection != null) {
            // Platform.runLater schedules this AFTER the current frame.
            // loadData() runs on a background thread — the table may still be
            // empty right now. We wait until the JavaFX thread is idle before
            // trying to find and select the article in the table.
            Platform.runLater(() -> selectArticleById(initialSelection.getId_Article()));
        }
    }

    // =========================================================================
    // TABLE SETUP
    // =========================================================================

    /**
     * Wires each TableColumn to the corresponding Article property.
     * PropertyValueFactory("titre") calls getTitre() on each Article object.
     */
    private void setupTable() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorieNom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        tableArticles.setItems(filteredData); // will be set properly in setupFilteredSearch
    }

    // =========================================================================
    // SEARCH
    // =========================================================================

    private void setupFilteredSearch() {
        filteredData = new FilteredList<>(articleData, p -> true);

        // Bind the table to the filtered list (not the raw articleData)
        tableArticles.setItems(filteredData);

        tfSearch.textProperty().addListener((obs, old, newVal) ->
                filteredData.setPredicate(article -> {
                    if (newVal == null || newVal.isBlank()) return true;
                    String lower = newVal.toLowerCase();
                    // Match titre, auteur, or category name (case-insensitive)
                    return article.getTitre().toLowerCase().contains(lower)
                            || article.getAuteur().toLowerCase().contains(lower)
                            || (article.getCategorieNom() != null
                            && article.getCategorieNom().toLowerCase().contains(lower));
                }));
    }

    // =========================================================================
    // DATA LOADING
    // =========================================================================

    /**
     * Loads categories into the ComboBox.
     * Also sets up custom cell rendering so the ComboBox shows nom (not toString()).
     */
    private void loadCategories() {
        try {
            categoryData.setAll(categorieService.findAll());
            cbCategorie.setItems(categoryData);

            // Custom cell factory: tells JavaFX how to render each Categorie in the dropdown
            cbCategorie.setCellFactory(param -> new ListCell<>() {
                @Override protected void updateItem(Categorie item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNom());
                }
            });

            // Button cell: the selected value shown when dropdown is closed
            cbCategorie.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(Categorie item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNom());
                }
            });
        } catch (SQLException e) {
            ValidationUtils.showError("Impossible de charger les catégories : " + e.getMessage());
        }
    }

    /**
     * Loads all articles from DB on a background thread.
     *
     * WHY a background thread?
     * DB queries can be slow. Running them on the JavaFX Application Thread
     * would freeze the UI. Task<Void> runs the work off-thread and uses
     * Platform.runLater() to update the UI safely back on the main thread.
     *
     * After loading, we also resolve each article's category name by calling
     * categorieService.recupererParId() so the "Catégorie" column has text.
     */
    private void loadData() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                var articles = articleService.findAll();

                // Enrich each article with its category name (for the table column)
                for (Article a : articles) {
                    try {
                        Categorie cat = categorieService.read(a.getId_categorie());
                        a.setCategorieNom(cat.getNom());
                    } catch (SQLException e) {
                        a.setCategorieNom("N/A");
                    }
                }

                // Platform.runLater: must update JavaFX UI components on the FX thread
                Platform.runLater(() -> {
                    articleData.setAll(articles);
                    tableArticles.getSelectionModel().clearSelection();

                    // If we have a pending auto-selection, apply it now that data is loaded
                    if (initialSelection != null) {
                        selectArticleById(initialSelection.getId_Article());
                    }
                });
                return null;
            }
        };

        task.setOnFailed(e ->
                ValidationUtils.showError("Erreur chargement : " + task.getException().getMessage()));

        new Thread(task).start(); // start background thread
    }

    /**
     * Finds an article in the table by ID and selects it.
     * The selection listener then fills the form and shows the detail panel.
     */
    private void selectArticleById(int id) {
        for (Article a : articleData) {
            if (a.getId_Article() == id) {
                tableArticles.getSelectionModel().select(a);
                tableArticles.scrollTo(a); // scroll the table so the row is visible
                return;
            }
        }
    }

    // =========================================================================
    // PANEL VISIBILITY
    // =========================================================================

    /**
     * Shows the "nothing selected" placeholder and hides the detail panel.
     * setManaged(false) removes the node from layout calculations entirely
     * (unlike setVisible(false) which just makes it transparent but still takes space).
     */
    private void showEmptyState() {
        emptyState.setVisible(true);
        emptyState.setManaged(true);
        detailScrollPane.setVisible(false);
        detailScrollPane.setManaged(false);
    }

    /**
     * Shows the detail/edit panel and updates the header labels.
     * Called whenever a table row is selected.
     */
    private void showDetailPanel(Article article) {
        lblDetailTitle.setText(article.getTitre());

        String catName = article.getCategorieNom() != null ? article.getCategorieNom() : "—";
        String date    = article.getDatePublication() != null
                ? article.getDatePublication().format(DATE_FMT) : "—";
        lblDetailMeta.setText(article.getAuteur() + "  ·  " + catName + "  ·  " + date);

        taPreview.setText(article.getContenu());

        emptyState.setVisible(false);
        emptyState.setManaged(false);
        detailScrollPane.setVisible(true);
        detailScrollPane.setManaged(true);
    }

    /** Shows the panel in "new article" mode with empty fields. */
    private void showNewArticlePanel() {
        lblDetailTitle.setText("Nouvel article");
        lblDetailMeta.setText("Remplissez le formulaire ci-dessous");
        taPreview.clear();
        emptyState.setVisible(false);
        emptyState.setManaged(false);
        detailScrollPane.setVisible(true);
        detailScrollPane.setManaged(true);
    }

    // =========================================================================
    // FILTER BANNER
    // =========================================================================

    /**
     * Shows a dismissible blue banner at the top of the sidebar.
     * Tells the user "you are viewing articles filtered by [Category]"
     * and offers an "× Effacer" button to clear the filter.
     *
     * We insert the banner at index 0 of the sidebar VBox (above everything else).
     * We find the sidebar VBox by calling tfSearch.getParent() —
     * tfSearch is a direct child of the sidebar VBox.
     */
    private void showFilterBanner(Categorie categorie) {
        // Left label
        Label info = new Label("🔍 Filtré par : " + categorie.getNom());
        info.setStyle("-fx-font-size: 12px; -fx-text-fill: #0078d4; -fx-font-weight: bold;");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Clear button
        Button clearBtn = new Button("× Effacer le filtre");
        clearBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #0078d4;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 11px;" +
                        "-fx-underline: true;"
        );

        // Banner HBox
        HBox banner = new HBox(8, info, spacer, clearBtn);
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setPadding(new Insets(6, 12, 6, 12));
        banner.setStyle("-fx-background-color: #dce9f9; -fx-background-radius: 6px;");

        // Clear action: reset search and remove banner from layout
        clearBtn.setOnAction(e -> {
            tfSearch.clear();
            filterCategorie = null;
            if (banner.getParent() instanceof VBox sidebar) {
                sidebar.getChildren().remove(banner);
            }
        });

        // Insert at top of sidebar (above search field)
        // "instanceof VBox sidebar" is Java 16+ pattern matching — it checks the
        // type AND casts in one line, binding the result to the variable 'sidebar'
        if (tfSearch.getParent() instanceof VBox sidebar) {
            sidebar.getChildren().add(0, banner);
        }
    }

    // =========================================================================
    // FXML HANDLERS
    // =========================================================================

    /**
     * "← Retour aux catégories" button.
     * Calls the Runnable injected by MainController, which opens the Category window.
     */
    @FXML
    private void goBackToCategories() {
        if (onNavigateToCategories != null) {
            onNavigateToCategories.run();
        }
    }

    /** "➕ Nouvel Article" sidebar button — clears form, opens blank detail panel. */
    @FXML
    private void handleNewArticle() {
        tableArticles.getSelectionModel().clearSelection();
        clearFormFields();
        showNewArticlePanel();
    }

    /** Validates then inserts a new article into the DB. */
    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        String titre = tfTitre.getText().trim();
        setFormDisabled(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // existsByTitre: SELECT COUNT(*) WHERE titre = ?
                if (articleService.existsByTitre(titre)) {
                    Platform.runLater(() ->
                            ValidationUtils.showWarning("Un article avec ce titre existe déjà !"));
                    return null;
                }

                Article article = new Article(
                        titre,
                        taContenu.getText().trim(),
                        tfAuteur.getText().trim(),
                        dpDatePublication.getValue(),
                        cbCategorie.getValue().getId_categorie()
                );
                articleService.create(article);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            setFormDisabled(false);
            ValidationUtils.showInfo("Article ajouté avec succès !");
            loadData();
            clearForm();
        });
        task.setOnFailed(e -> {
            setFormDisabled(false);
            ValidationUtils.showError("Erreur ajout : " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    /** Validates then updates the selected article in the DB. */
    @FXML
    private void handleUpdate() {
        Article selected = tableArticles.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ValidationUtils.showWarning("Veuillez sélectionner un article à modifier.");
            return;
        }
        if (!validateInput()) return;

        String titre = tfTitre.getText().trim();
        setFormDisabled(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // existsByTitreExcludingId: checks duplicate excluding this article's own ID
                if (articleService.existsByTitreExcludingId(titre, selected.getId_Article())) {
                    Platform.runLater(() ->
                            ValidationUtils.showWarning("Un autre article avec ce titre existe déjà !"));
                    return null;
                }
                selected.setTitre(titre);
                selected.setContenu(taContenu.getText().trim());
                selected.setAuteur(tfAuteur.getText().trim());
                selected.setDatePublication(dpDatePublication.getValue());
                selected.setId_categorie(cbCategorie.getValue().getId_categorie());
                articleService.update(selected);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            setFormDisabled(false);
            ValidationUtils.showInfo("Article modifié !");
            loadData();
            clearForm();
        });
        task.setOnFailed(e -> {
            setFormDisabled(false);
            ValidationUtils.showError("Erreur modification : " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    /** Confirms then deletes the selected article from the DB. */
    @FXML
    private void handleDelete() {
        Article selected = tableArticles.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ValidationUtils.showWarning("Veuillez sélectionner un article à supprimer.");
            return;
        }
        if (!ValidationUtils.confirmDelete("l'article \"" + selected.getTitre() + "\"")) return;

        setFormDisabled(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                articleService.delete(selected.getId_Article());
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            setFormDisabled(false);
            ValidationUtils.showInfo("Article supprimé !");
            loadData();
            clearForm();
        });
        task.setOnFailed(e -> {
            setFormDisabled(false);
            ValidationUtils.showError("Erreur suppression : " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    /** Clears the form and returns to empty state. */
    @FXML
    private void clearForm() {
        clearFormFields();
        tableArticles.getSelectionModel().clearSelection();
        showEmptyState();
    }

    /** Reloads data and clears everything. */
    @FXML
    private void handleRefresh() {
        tfSearch.clear();
        loadData();
        clearForm();
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    /** Clears only the input fields (does not touch panel visibility). */
    private void clearFormFields() {
        tfTitre.clear();
        tfAuteur.clear();
        taContenu.clear();
        dpDatePublication.setValue(null);
        cbCategorie.setValue(null);
        taPreview.clear();
    }

    /** Fills form fields from the given Article object. */
    private void fillForm(Article a) {
        tfTitre.setText(a.getTitre());
        tfAuteur.setText(a.getAuteur());
        taContenu.setText(a.getContenu());
        dpDatePublication.setValue(a.getDatePublication());
        taPreview.setText(a.getContenu());

        // Match the article's id_categorie to a Categorie in the ComboBox list
        for (Categorie cat : categoryData) {
            if (cat.getId_categorie() == a.getId_categorie()) {
                cbCategorie.setValue(cat);
                break;
            }
        }
    }

    /**
     * Runs all validation rules in order.
     * Each ValidationUtils method shows its own error dialog and returns false on failure.
     * We return false on the FIRST failure (short-circuit) so only one error shows at a time.
     */
    private boolean validateInput() {
        if (!ValidationUtils.isNotEmpty(tfTitre,  "Le titre"))                         return false;
        if (!ValidationUtils.hasLengthBetween(tfTitre, "Le titre", 3, 200))            return false;
        if (!ValidationUtils.isValidText(tfTitre,  "Le titre"))                         return false;
        if (!ValidationUtils.hasMeaningfulContent(tfTitre, "Le titre"))                 return false;
        if (!ValidationUtils.isSafeInput(tfTitre,  "Le titre"))                         return false;

        if (!ValidationUtils.isNotEmpty(tfAuteur, "L'auteur"))                          return false;
        if (!ValidationUtils.hasLengthBetween(tfAuteur, "L'auteur", 2, 100))           return false;
        if (!ValidationUtils.isValidName(tfAuteur, "L'auteur"))                         return false;
        if (!ValidationUtils.isSafeInput(tfAuteur, "L'auteur"))                         return false;

        if (!ValidationUtils.isNotEmpty(taContenu, "Le contenu"))                       return false;
        if (!ValidationUtils.hasMinLength(taContenu, "Le contenu", 10))                 return false;
        if (!ValidationUtils.hasMaxLength(taContenu, "Le contenu", 10000))              return false;
        if (!ValidationUtils.hasMeaningfulContent(taContenu, "Le contenu"))             return false;
        if (!ValidationUtils.isSafeInput(taContenu, "Le contenu"))                      return false;

        if (!ValidationUtils.isDateSelected(dpDatePublication.getValue(), "La date"))   return false;
        if (!ValidationUtils.isNotFutureDate(dpDatePublication.getValue(),
                "La date de publication"))                                               return false;
        if (!ValidationUtils.isDateInRange(dpDatePublication.getValue(),
                "La date de publication",
                LocalDate.of(1900, 1, 1), LocalDate.now()))                            return false;

        if (!ValidationUtils.isNotNull(cbCategorie.getValue(), "La catégorie"))         return false;

        return true;
    }

    /** Enables/disables all form controls during async DB operations. */
    private void setFormDisabled(boolean disabled) {
        tfTitre.setDisable(disabled);
        tfAuteur.setDisable(disabled);
        taContenu.setDisable(disabled);
        dpDatePublication.setDisable(disabled);
        cbCategorie.setDisable(disabled);
        btnAdd.setDisable(disabled);
        btnUpdate.setDisable(disabled);
        btnDelete.setDisable(disabled);
        btnClear.setDisable(disabled);
        tfSearch.setDisable(disabled);
    }
}