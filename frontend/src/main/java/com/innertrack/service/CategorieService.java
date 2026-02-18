package com.innertrack.service;

import com.innertrack.model.Categorie;
import com.innertrack.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CategorieService — handles all database operations for Categories
 * ─────────────────────────────────────────────────────────────────────────────
 * Implements ICrudService<Categorie> for the five core CRUD methods, plus
 * additional helper methods for duplicate checking, article counting, and search.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class CategorieService implements ICrudService<Categorie> {

    private Connection connection;

    public CategorieService() {
        connection = DBConnection.getInstance().getConnection();
    }

    // =========================================================================
    // CRUD METHODS — required by ICrudService<Categorie>
    // =========================================================================

    /**
     * create() — inserts a new Categorie into the database.
     *
     * Validation: the nom field cannot be null or empty.
     *
     * @param categorie the Categorie object to insert (id_categorie will be auto-generated)
     * @return true if the insert succeeded
     * @throws SQLException if nom is empty OR if a database error occurs
     */
    @Override
    public boolean create(Categorie categorie) throws SQLException {
        // Validation: nom is required
        if (categorie.getNom() == null || categorie.getNom().trim().isEmpty()) {
            throw new SQLException("Le nom de la catégorie ne peut pas être vide");
        }

        String sql = "INSERT INTO categorie (nom, description) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categorie.getNom().trim());
            // If description is null, store an empty string instead
            ps.setString(2, categorie.getDescription() != null
                    ? categorie.getDescription().trim() : "");
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * read() — retrieves a single Categorie by its primary key.
     *
     * @param id the id_categorie value to search for
     * @return the Categorie object
     * @throws SQLException if not found OR if a database error occurs
     */
    @Override
    public Categorie read(int id) throws SQLException {
        String sql = "SELECT * FROM categorie WHERE id_categorie = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categorie c = new Categorie();
                    c.setId_categorie(rs.getInt("id_categorie"));
                    c.setNom(rs.getString("nom"));
                    c.setDescription(rs.getString("description"));
                    return c;
                } else {
                    throw new SQLException("Catégorie avec ID " + id + " n'existe pas");
                }
            }
        }
    }

    /**
     * update() — modifies an existing Categorie in the database.
     *
     * @param categorie the Categorie object with updated values
     * @return true if the update succeeded
     * @throws SQLException if nom is empty, if categorie not found, or if a database error occurs
     */
    @Override
    public boolean update(Categorie categorie) throws SQLException {
        if (categorie.getNom() == null || categorie.getNom().trim().isEmpty()) {
            throw new SQLException("Le nom de la catégorie ne peut pas être vide");
        }

        String sql = "UPDATE categorie SET nom = ?, description = ? WHERE id_categorie = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categorie.getNom().trim());
            ps.setString(2, categorie.getDescription() != null
                    ? categorie.getDescription().trim() : "");
            ps.setInt(3, categorie.getId_categorie());

            int rows = ps.executeUpdate();
            // If 0 rows were affected, the category ID didn't exist
            if (rows == 0) throw new SQLException("Catégorie introuvable");
            return true;
        }
    }

    /**
     * delete() — removes a Categorie from the database by its primary key.
     *
     * IMPORTANT: This method first checks if the category has linked articles.
     * If it does, deletion is blocked to preserve referential integrity.
     *
     * @param id the id_categorie value
     * @return true if the delete succeeded
     * @throws SQLException if the category has articles, if not found, or if a database error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        // Guard: prevent deletion if this category still has articles
        if (hasArticles(id)) {
            throw new SQLException(
                    "Impossible de supprimer cette catégorie car elle contient des articles"
            );
        }

        String sql = "DELETE FROM categorie WHERE id_categorie = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("Catégorie introuvable");
            return true;
        }
    }

    /**
     * findAll() — retrieves all Categories from the database.
     *
     * @return a List of all Categorie objects (empty list if table is empty)
     * @throws SQLException if a database error occurs
     */
    @Override
    public List<Categorie> findAll() throws SQLException {
        String sql = "SELECT * FROM categorie";
        List<Categorie> categories = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Categorie c = new Categorie();
                c.setId_categorie(rs.getInt("id_categorie"));
                c.setNom(rs.getString("nom"));
                c.setDescription(rs.getString("description"));
                categories.add(c);
            }
        }
        return categories;
    }

    // =========================================================================
    // ADDITIONAL HELPER METHODS
    // =========================================================================

    /**
     * Check if a category has any articles linked to it.
     *
     * Used by delete() to prevent deletion of categories with articles,
     * and by controllers to decide whether to show a "delete" warning.
     *
     * @param categorieId the id_categorie value
     * @return true if at least one article references this category, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean hasArticles(int categorieId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM article WHERE id_categorie = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categorieId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // COUNT(*) > 0 means articles exist
            }
        }
        return false;
    }

    /**
     * Search for categories by name using a LIKE query.
     *
     * @param nom the search term (will be wrapped with % wildcards)
     * @return a List of matching Categorie objects (empty if none match)
     * @throws SQLException if a database error occurs
     */
    public List<Categorie> rechercherParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM categorie WHERE nom LIKE ?";
        List<Categorie> categories = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%"); // % wildcards for partial matching
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categorie c = new Categorie();
                    c.setId_categorie(rs.getInt("id_categorie"));
                    c.setNom(rs.getString("nom"));
                    c.setDescription(rs.getString("description"));
                    categories.add(c);
                }
            }
        }
        return categories;
    }

    /**
     * Check if a category with this exact name already exists.
     * Used to prevent duplicate names before calling create().
     *
     * @param nom the name to check
     * @return true if a category with this name exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean existsByNom(String nom) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categorie WHERE nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nom.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Check if a name exists EXCLUDING a specific category's ID.
     *
     * This is the "update-safe" duplicate check. When editing a category,
     * we check if the new name conflicts with ANOTHER category — not the
     * one being edited itself.
     *
     * @param nom the name to check
     * @param excludeId the id_categorie to exclude from the search
     * @return true if another category has this name, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean existsByNomExcludingId(String nom, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categorie WHERE nom = ? AND id_categorie != ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nom.trim());
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Get the count of articles in a specific category.
     *
     * Used to display stats in the UI: "X articles in this category".
     *
     * @param categorieId the id_categorie value
     * @return the number of articles (0 if none)
     * @throws SQLException if a database error occurs
     */
    public int getArticleCount(int categorieId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM article WHERE id_categorie = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categorieId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}