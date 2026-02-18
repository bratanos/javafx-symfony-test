package com.innertrack.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.innertrack.model.Categorie;
import com.innertrack.service.CategorieService;
import com.innertrack.util.ValidationUtils;
import com.innertrack.model.Article;
import com.innertrack.service.ArticleService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class CategorieController implements Initializable {

    // =========================================================================
    // FXML FIELDS — these are automatically injected by JavaFX when the FXML
    // is loaded. The fx:id in the FXML must match the field name exactly.
    // =========================================================================

    @FXML private TextField  searchField;           // live search input
    @FXML private VBox       categoriesContainer;   // holds the category cards
    @FXML private VBox       emptyState;            // "select a category" placeholder
    @FXML private ScrollPane detailScrollPane;      // wraps the detail panel
    @FXML private VBox       detailView;            // detail panel content
    @FXML private Label      lblDetailTitle;        // category name in detail header
    @FXML private Label      lblDetailDescription;  // category description in detail header
    @FXML private Label      lblArticleCount;       // "X articles in this category"
    @FXML private TextField  nomField;              // editable name field
    @FXML private TextArea   descField;             // editable description field
    @FXML private Button     btnUpdate;             // save / create button
    @FXML private Button     btnDelete;             // delete button

    /**
     * This VBox lives inside the detail panel and is populated dynamically
     * by loadArticlesForCategory(). It shows clickable article rows.
     * Must have fx:id="articlesListBox" in CategorieView.fxml.
     */
    @FXML private VBox articlesListBox;

    // =========================================================================
    // SERVICES
    // CategorieService handles all DB operations for categories.
    // ArticleService is needed to fetch articles for the detail panel list.
    // =========================================================================

    private final CategorieService categorieService = new CategorieService();
    private final ArticleService   articleService   = new ArticleService();

    // =========================================================================
    // DATA & STATE
    // =========================================================================

    /**
     * ObservableList is a special JavaFX list that notifies listeners when
     * items are added/removed. We use it as the master data source.
     */
    private final ObservableList<Categorie> data = FXCollections.observableArrayList();

    /**
     * FilteredList wraps data and applies a predicate (filter condition).
     * When the predicate changes (user types in search), the list updates
     * automatically and refreshCards() redraws the card list.
     */
    private FilteredList<Categorie> filteredData;

    private Categorie selectedCategorie = null; // currently selected category object
    private VBox      selectedCard      = null; // currently highlighted card VBox

    // =========================================================================
    // NAVIGATION CALLBACK
    // =========================================================================

    /**
     * BiConsumer<Categorie, Article> is a functional interface that accepts
     * two arguments (a Categorie and an Article) and returns nothing.
     *
     * MainController injects this before loader.load() via setOnNavigateToArticles().
     * When the user clicks "Voir les articles" or an article row, we call:
     *   onNavigateToArticles.accept(categorie, article)
     * which tells MainController to open the Article window.
     *
     * Using a callback keeps CategorieController decoupled from MainController —
     * it doesn't need to import or reference it directly.
     */
    private BiConsumer<Categorie, Article> onNavigateToArticles;

    /**
     * If MainController sets this before load(), we auto-select this category
     * after initialize() runs (used when navigating back from Article window).
     */
    private Categorie pendingSelection = null;

    // =========================================================================
    // SETTERS — called by MainController BEFORE loader.load()
    // =========================================================================

    /**
     * Injects the navigation callback.
     * Must be called before loader.load() so it's available during initialize().
     */
    public void setOnNavigateToArticles(BiConsumer<Categorie, Article> callback) {
        this.onNavigateToArticles = callback;
    }

    /**
     * Requests that a specific category be auto-selected after the view loads.
     * Must be called before loader.load().
     */
    public void setPendingSelection(Categorie categorie) {
        this.pendingSelection = categorie;
    }

    // =========================================================================
    // INITIALIZE — called automatically by JavaFX after FXML injection
    // =========================================================================

    /**
     * This is the entry point for controller setup. JavaFX calls this after
     * all @FXML fields have been injected (so it's safe to use them here).
     *
     * Order matters:
     *   1. setupFilteredSearch() — must run before loadData() so the listener
     *      is in place when data arrives
     *   2. loadData()            — fills 'data' from the DB and draws cards
     *   3. pendingSelection      — auto-select only after cards are drawn
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFilteredSearch();
        loadData();

        // If we arrived here via back-navigation from the Article window,
        // auto-select the category the user was looking at.
        // Platform.runLater defers this until AFTER the JavaFX layout pass
        // completes, ensuring the card VBoxes exist in categoriesContainer.
        if (pendingSelection != null) {
            Platform.runLater(() ->
                    selectCategorieById(pendingSelection.getId_categorie()));
        }
    }

    // =========================================================================
    // SEARCH
    // =========================================================================

    /**
     * Wraps 'data' in a FilteredList and attaches a listener to searchField.
     * Every keystroke updates the predicate, which filters the list instantly.
     * refreshCards() redraws the visible cards to match the filtered list.
     */
    private void setupFilteredSearch() {
        // p -> true means "show everything" initially (no filter)
        filteredData = new FilteredList<>(data, p -> true);

        // addListener fires every time the text property changes
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(categorie -> {
                // Empty search = show all
                if (newVal == null || newVal.isBlank()) return true;
                String lower = newVal.toLowerCase();
                // Match on nom OR description (case-insensitive)
                return categorie.getNom().toLowerCase().contains(lower)
                        || (categorie.getDescription() != null
                        && categorie.getDescription().toLowerCase().contains(lower));
            });
            refreshCards(); // redraw cards to match filtered list
        });
    }

    // =========================================================================
    // DATA LOADING
    // =========================================================================

    /**
     * Fetches all categories from the DB and triggers a card redraw.
     * Called on startup and after any CRUD operation to keep the UI in sync.
     */
    private void loadData() {
        try {
            data.clear();                              // clear old data
            data.addAll(categorieService.findAll()); // fetch fresh from DB
            refreshCards();                            // redraw cards
        } catch (SQLException e) {
            ValidationUtils.showError("Impossible de charger les données: " + e.getMessage());
        }
    }

    /**
     * Clears and rebuilds the card list from filteredData.
     * Called after any change to data or the search predicate.
     */
    private void refreshCards() {
        categoriesContainer.getChildren().clear();
        for (Categorie cat : filteredData) {
            categoriesContainer.getChildren().add(createCategoryCard(cat));
        }
    }

    // =========================================================================
    // CARD CREATION
    // =========================================================================

    /**
     * Builds one VBox card for a category.
     *
     * Card layout:
     *   ┌────────────────────────────────────────┐
     *   │  Nom de la catégorie (bold)            │
     *   │  Description tronquée...               │
     *   │  [X articles]    [Voir les articles →] │
     *   └────────────────────────────────────────┘
     *
     * Clicking the card body opens the detail panel.
     * Clicking "Voir les articles →" opens the Article window.
     */
    private VBox createCategoryCard(Categorie categorie) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(16));
        card.setCursor(javafx.scene.Cursor.HAND);

        // ── Title ─────────────────────────────────────────────────────────────
        Label title = new Label(categorie.getNom());
        title.getStyleClass().add("card-title");

        // ── Description (truncated to 60 chars) ───────────────────────────────
        String desc = categorie.getDescription();
        if (desc != null && desc.length() > 60) {
            desc = desc.substring(0, 60) + "...";
        }
        Label description = new Label(desc != null ? desc : "Aucune description");
        description.getStyleClass().add("card-subtitle");
        description.setWrapText(true);

        // ── Footer: badge + "Voir les articles" button ────────────────────────
        try {
            int count = categorieService.getArticleCount(categorie.getId_categorie());

            // Badge: shows article count
            Label badge = new Label(count + " article" + (count != 1 ? "s" : ""));
            badge.getStyleClass().add("card-badge");

            // "Voir les articles →" button
            // When clicked, it opens the Article window filtered by this category.
            // e.consume() prevents the click from also triggering the card's
            // setOnMouseClicked handler below.
            Button btnViewArticles = new Button("Voir les articles →");
            btnViewArticles.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #0078d4;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-size: 11px;" +
                            "-fx-underline: true;" +
                            "-fx-padding: 0;"
            );
            btnViewArticles.setOnAction(e -> {
                e.consume(); // stop event from bubbling to card click handler
                if (onNavigateToArticles != null) {
                    // Open Article window: filter by this category, no article pre-selected
                    onNavigateToArticles.accept(categorie, null);
                }
            });

            // Spacer pushes the button to the far right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox footer = new HBox(8, badge, spacer, btnViewArticles);
            footer.setAlignment(Pos.CENTER_LEFT);

            card.getChildren().addAll(title, description, footer);

        } catch (Exception e) {
            // If article count query fails, show card without badge
            card.getChildren().addAll(title, description);
        }

        // ── Click handler: select this category and show detail panel ─────────
        card.setOnMouseClicked(event -> selectCategory(categorie, card));

        return card;
    }

    // =========================================================================
    // CATEGORY SELECTION
    // =========================================================================

    /**
     * Handles selecting a category — updates visual state and fills detail panel.
     *
     * Called when:
     *   - User clicks a card body
     *   - selectCategorieById() is called programmatically (back-navigation)
     *
     * @param categorie the Categorie object that was selected
     * @param card      the VBox card node that was clicked (for CSS highlighting)
     */
    private void selectCategory(Categorie categorie, VBox card) {
        // ── Visual: deselect previous card, highlight new one ─────────────────
        if (selectedCard != null) {
            selectedCard.getStyleClass().remove("card-selected");
        }
        card.getStyleClass().add("card-selected");
        selectedCard = card;
        selectedCategorie = categorie;

        // ── Show detail panel, hide empty state ───────────────────────────────
        emptyState.setVisible(false);
        emptyState.setManaged(false);   // setManaged(false) removes it from layout
        detailScrollPane.setVisible(true);
        detailScrollPane.setManaged(true);

        // ── Fill detail header ────────────────────────────────────────────────
        lblDetailTitle.setText(categorie.getNom());
        lblDetailDescription.setText(
                categorie.getDescription() != null
                        ? categorie.getDescription()
                        : "Aucune description"
        );

        // ── Fill edit form ────────────────────────────────────────────────────
        nomField.setText(categorie.getNom());
        descField.setText(categorie.getDescription());

        // ── Update article count stat ─────────────────────────────────────────
        try {
            int count = categorieService.getArticleCount(categorie.getId_categorie());
            lblArticleCount.setText(
                    count + " article" + (count > 1 ? "s" : "") + " dans cette catégorie"
            );
        } catch (Exception e) {
            lblArticleCount.setText("Statistiques non disponibles");
        }

        // ── Load article rows in the detail panel ─────────────────────────────
        // This populates articlesListBox with clickable article rows
        loadArticlesForCategory(categorie);

        // ── Reset update button to "save" mode ────────────────────────────────
        btnUpdate.setText("✓ Enregistrer");
        btnUpdate.setOnAction(e -> handleUpdate());
    }

    /**
     * Programmatically selects a category by its id_categorie.
     *
     * Used after back-navigation from the Article window to re-select the
     * category the user was looking at. We scan filteredData for the matching
     * category and simulate a card click.
     *
     * filteredData and categoriesContainer.getChildren() are in the same order,
     * so index i in filteredData = index i in the card list.
     */
    public void selectCategorieById(int id) {
        for (int i = 0; i < filteredData.size(); i++) {
            if (filteredData.get(i).getId_categorie() == id) {
                if (i < categoriesContainer.getChildren().size()) {
                    VBox card = (VBox) categoriesContainer.getChildren().get(i);
                    selectCategory(filteredData.get(i), card);
                }
                return;
            }
        }
    }

    // =========================================================================
    // ARTICLE LIST IN DETAIL PANEL
    // =========================================================================

    /**
     * Fetches articles for this category and populates articlesListBox
     * with one clickable row per article.
     *
     * We filter in Java (not SQL) because ArticleService.recuperer() returns all
     * articles. For large datasets you'd add a dedicated SQL method instead.
     */
    private void loadArticlesForCategory(Categorie categorie) {
        articlesListBox.getChildren().clear();

        try {
            List<Article> articles = articleService.findAll()
                    .stream()
                    .filter(a -> a.getId_categorie() == categorie.getId_categorie())
                    .toList();

            if (articles.isEmpty()) {
                // Friendly empty state inside the article list
                Label empty = new Label("Aucun article dans cette catégorie");
                empty.setStyle(
                        "-fx-text-fill: #8a8886;" +
                                "-fx-font-style: italic;" +
                                "-fx-font-size: 13px;"
                );
                articlesListBox.getChildren().add(empty);
                return;
            }

            // Build one clickable row per article
            for (Article article : articles) {
                articlesListBox.getChildren().add(buildArticleRow(article, categorie));
            }

        } catch (SQLException e) {
            Label err = new Label("Impossible de charger les articles");
            err.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 12px;");
            articlesListBox.getChildren().add(err);
        }
    }

    /**
     * Builds one clickable HBox row representing a single article.
     *
     * Layout:  📰  Titre de l'article          Auteur · Date  →
     *
     * Hover effect: background shifts from light grey to light blue.
     * Click: calls onNavigateToArticles with the article + its category,
     *        which tells MainController to open the Article window with
     *        that article pre-selected.
     */
    private HBox buildArticleRow(Article article, Categorie categorie) {
        // Icon
        Label icon = new Label("📰");
        icon.setStyle("-fx-font-size: 15px;");

        // Title — truncated with "…" if longer than the allowed width
        Label titre = new Label(article.getTitre());
        titre.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #323130;"
        );
        titre.setMaxWidth(180);
        // OverrunStyle.ELLIPSIS adds "…" at the end when text is clipped
        titre.setTextOverrun(OverrunStyle.ELLIPSIS);

        // Meta: author · publication date
        String dateStr = article.getDatePublication() != null
                ? article.getDatePublication().toString() : "—";
        Label meta = new Label(article.getAuteur() + " · " + dateStr);
        meta.setStyle("-fx-font-size: 11px; -fx-text-fill: #8a8886;");

        // Arrow indicator — hints the row is clickable
        Label arrow = new Label("→");
        arrow.setStyle("-fx-text-fill: #0078d4; -fx-font-weight: bold;");

        // Spacer pushes meta + arrow to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Assemble the row
        HBox row = new HBox(10, icon, titre, spacer, meta, arrow);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        applyRowStyle(row, false); // false = not hovered

        // Hover effect
        row.setOnMouseEntered(e -> applyRowStyle(row, true));
        row.setOnMouseExited(e  -> applyRowStyle(row, false));

        // Click: open Article window with this article pre-selected
        row.setOnMouseClicked(e -> {
            if (onNavigateToArticles != null) {
                // Pass both the category (for filter) and the article (for auto-selection)
                onNavigateToArticles.accept(categorie, article);
            }
        });

        return row;
    }

    /** Applies normal or hovered background style to an article row. */
    private void applyRowStyle(HBox row, boolean hovered) {
        String bg = hovered ? "#dce9f9" : "#f3f2f1";
        row.setStyle(
                "-fx-background-color: " + bg + ";" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;"
        );
    }

    // =========================================================================
    // FXML HANDLERS — Add / Update / Delete / Clear
    // =========================================================================

    /**
     * Called by the "+ Nouvelle Catégorie" button.
     * Puts the detail panel in "create mode": clears all fields and changes
     * the save button to call handleAdd() instead of handleUpdate().
     */
    @FXML
    private void showAddForm() {
        // Deselect any selected card
        if (selectedCard != null) {
            selectedCard.getStyleClass().remove("card-selected");
        }
        selectedCard = null;
        selectedCategorie = null;

        // Show detail panel in create mode
        emptyState.setVisible(false);
        emptyState.setManaged(false);
        detailScrollPane.setVisible(true);
        detailScrollPane.setManaged(true);

        lblDetailTitle.setText("Nouvelle Catégorie");
        lblDetailDescription.setText("Remplissez les champs ci-dessous");
        nomField.clear();
        descField.clear();
        lblArticleCount.setText("");

        // Clear article list (not relevant when creating)
        if (articlesListBox != null) {
            articlesListBox.getChildren().clear();
        }

        // Switch button to "create" mode
        btnUpdate.setText("+ Créer");
        btnUpdate.setOnAction(e -> handleAdd());
    }

    /**
     * Called when the user clicks "+ Créer" (after showAddForm()).
     * Validates input, checks for duplicates, then inserts into DB.
     */
    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        String nom         = nomField.getText().trim();
        String description = descField.getText().trim();

        try {
            // existsByNom() runs: SELECT COUNT(*) FROM categorie WHERE nom = ?
            if (categorieService.existsByNom(nom)) {
                ValidationUtils.showWarning("Une catégorie avec ce nom existe déjà !");
                return;
            }

            // Categorie(nom, description) uses the no-id constructor
            categorieService.create(new Categorie(nom, description));
            ValidationUtils.showInfo("Catégorie '" + nom + "' créée avec succès !");

            loadData();      // refresh the card list from DB
            clearSelection();// return to empty state

        } catch (SQLException ex) {
            ValidationUtils.showError("Erreur lors de la création : " + ex.getMessage());
        }
    }

    /**
     * Called when the user clicks "✓ Enregistrer" (in edit mode).
     * If no category is selected we fall back to handleAdd() —
     * this handles the edge case where the button was clicked without
     * a selection (shouldn't normally happen but is safe to handle).
     */
    @FXML
    private void handleUpdate() {
        if (selectedCategorie == null) {
            handleAdd();
            return;
        }
        if (!validateInput()) return;

        String nom         = nomField.getText().trim();
        String description = descField.getText().trim();

        try {
            // existsByNomExcludingId() checks for duplicates EXCLUDING this category's
            // own ID — prevents false "duplicate" warnings when saving without changing the name
            if (categorieService.existsByNomExcludingId(nom, selectedCategorie.getId_categorie())) {
                ValidationUtils.showWarning("Une autre catégorie avec ce nom existe déjà !");
                return;
            }

            selectedCategorie.setNom(nom);
            selectedCategorie.setDescription(description);
            categorieService.update(selectedCategorie); // UPDATE ... WHERE id_categorie = ?

            ValidationUtils.showInfo("Catégorie modifiée avec succès !");
            loadData(); // refresh cards with new data

        } catch (SQLException ex) {
            ValidationUtils.showError("Erreur lors de la modification : " + ex.getMessage());
        }
    }

    /**
     * Called when the user clicks "Supprimer".
     * Checks for linked articles first (DB constraint guard), then confirms
     * with the user before deleting.
     */
    @FXML
    private void handleDelete() {
        if (selectedCategorie == null) return;

        try {
            // hasArticles() runs: SELECT COUNT(*) FROM article WHERE id_categorie = ?
            // If > 0, we block deletion to preserve referential integrity
            if (categorieService.hasArticles(selectedCategorie.getId_categorie())) {
                ValidationUtils.showError(
                        "Impossible de supprimer cette catégorie !\n\n" +
                                "Elle contient des articles. Supprimez-les d'abord."
                );
                return;
            }

            // Ask the user to confirm before actually deleting
            if (!ValidationUtils.confirmDelete("la catégorie '" + selectedCategorie.getNom() + "'")) {
                return;
            }

            categorieService.delete(selectedCategorie.getId_categorie());
            ValidationUtils.showInfo("Catégorie supprimée avec succès !");

            loadData();
            clearSelection();

        } catch (SQLException ex) {
            ValidationUtils.showError("Erreur lors de la suppression : " + ex.getMessage());
        }
    }

    /**
     * Called by the "Annuler" button.
     * Deselects everything and returns to the empty state placeholder.
     */
    @FXML
    private void clearSelection() {
        if (selectedCard != null) {
            selectedCard.getStyleClass().remove("card-selected");
        }
        selectedCard = null;
        selectedCategorie = null;

        // Show empty state, hide detail panel
        emptyState.setVisible(true);
        emptyState.setManaged(true);
        detailScrollPane.setVisible(false);
        detailScrollPane.setManaged(false);

        // Reset button back to save mode (for next selection)
        btnUpdate.setText("✓ Enregistrer");
        btnUpdate.setOnAction(e -> handleUpdate());
    }

    // =========================================================================
    // VALIDATION
    // =========================================================================

    /**
     * Validates nomField and descField using ValidationUtils.
     * Returns false (and shows an error dialog) on the first failed rule.
     * Returns true only if all rules pass.
     */
    private boolean validateInput() {
        // nom is required
        if (!ValidationUtils.isNotEmpty(nomField,           "Le nom"))         return false;
        if (!ValidationUtils.hasMinLength(nomField,          "Le nom", 2))      return false;
        if (!ValidationUtils.hasMaxLength(nomField,          "Le nom", 100))    return false;
        if (!ValidationUtils.isValidText(nomField,           "Le nom"))         return false;
        if (!ValidationUtils.hasMeaningfulContent(nomField,  "Le nom"))         return false;
        if (!ValidationUtils.isSafeInput(nomField,           "Le nom"))         return false;

        // description is optional — only validate if the user typed something
        if (!descField.getText().trim().isEmpty()) {
            if (!ValidationUtils.hasMinLength(descField, "La description", 3))   return false;
            if (!ValidationUtils.hasMaxLength(descField, "La description", 500)) return false;
            if (!ValidationUtils.isValidText(descField,  "La description"))      return false;
            if (!ValidationUtils.isSafeInput(descField,  "La description"))      return false;
        }

        return true;
    }
}