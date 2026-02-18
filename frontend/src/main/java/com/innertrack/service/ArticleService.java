package com.innertrack.service;

import com.innertrack.model.Article;
import com.innertrack.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * ArticleService — handles all database operations for Articles
 * ─────────────────────────────────────────────────────────────────────────────
 * Implements ICrudService<Article>, which enforces the five core CRUD methods:
 *   - create()  — INSERT
 *   - read()    — SELECT WHERE id = ?
 *   - update()  — UPDATE
 *   - delete()  — DELETE
 *   - findAll() — SELECT all rows
 *
 * Also includes additional helper methods for duplicate checking.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class ArticleService implements ICrudService<Article> {

    private Connection connection;

    /**
     * Constructor — gets the singleton DB connection.
     * MyDatabase.getInstance().getConnection() returns the same Connection
     * object every time (singleton pattern), so all services share one connection.
     */
    public ArticleService() {
        connection = DBConnection.getInstance().getConnection();
    }

    // =========================================================================
    // CRUD METHODS — required by ICrudService<Article>
    // =========================================================================

    /**
     * create() — inserts a new Article into the database.
     *
     * @param article the Article object to insert (id_Article will be auto-generated)
     * @return true if the insert succeeded (1 row affected), false otherwise
     * @throws SQLException if a database error occurs
     */
    @Override
    public boolean create(Article article) throws SQLException {
        String sql = "INSERT INTO article (titre, contenu, auteur, datePublication, id_categorie) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, article.getTitre());
            ps.setString(2, article.getContenu());
            ps.setString(3, article.getAuteur());
            // LocalDate → java.sql.Date conversion for JDBC
            ps.setDate(4, Date.valueOf(article.getDatePublication()));
            ps.setInt(5, article.getId_categorie());

            // executeUpdate() returns the number of rows affected
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * read() — retrieves a single Article by its primary key.
     *
     * @param id the id_Article value to search for
     * @return the Article object with all fields populated
     * @throws SQLException if the article is not found OR if a database error occurs
     */
    @Override
    public Article read(int id) throws SQLException {
        String sql = "SELECT * FROM article WHERE id_Article = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Article a = new Article();
                    a.setId_Article(rs.getInt("id_Article"));
                    a.setTitre(rs.getString("titre"));
                    a.setContenu(rs.getString("contenu"));
                    a.setAuteur(rs.getString("auteur"));
                    // java.sql.Date → LocalDate conversion
                    a.setDatePublication(rs.getDate("datePublication").toLocalDate());
                    a.setId_categorie(rs.getInt("id_categorie"));
                    return a;
                } else {
                    throw new SQLException("Article with ID " + id + " not found");
                }
            }
        }
    }

    /**
     * update() — modifies an existing Article in the database.
     *
     * @param article the Article object with updated values (must have a valid id_Article)
     * @return true if the update succeeded, false if no rows were affected
     * @throws SQLException if a database error occurs
     */
    @Override
    public boolean update(Article article) throws SQLException {
        String sql = "UPDATE article SET titre=?, contenu=?, auteur=?, datePublication=?, id_categorie=? " +
                "WHERE id_Article=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, article.getTitre());
            ps.setString(2, article.getContenu());
            ps.setString(3, article.getAuteur());
            ps.setDate(4, Date.valueOf(article.getDatePublication()));
            ps.setInt(5, article.getId_categorie());
            ps.setInt(6, article.getId_Article()); // WHERE clause — identifies which row to update

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * delete() — removes an Article from the database by its primary key.
     *
     * @param id the id_Article value
     * @return true if the delete succeeded, false if no rows were affected
     * @throws SQLException if a database error occurs
     */
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM article WHERE id_Article = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * findAll() — retrieves all Articles from the database.
     *
     * @return a List of all Article objects (empty list if table is empty)
     * @throws SQLException if a database error occurs
     */
    @Override
    public List<Article> findAll() throws SQLException {
        String sql = "SELECT * FROM article";
        List<Article> articles = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Article a = new Article();
                a.setId_Article(rs.getInt("id_Article"));
                a.setTitre(rs.getString("titre"));
                a.setContenu(rs.getString("contenu"));
                a.setAuteur(rs.getString("auteur"));
                a.setDatePublication(rs.getDate("datePublication").toLocalDate());
                a.setId_categorie(rs.getInt("id_categorie"));
                articles.add(a);
            }
        }
        return articles;
    }

    // =========================================================================
    // ADDITIONAL HELPER METHODS — not part of the interface
    // =========================================================================

    /**
     * Check if an article with this exact title already exists.
     * Used to prevent duplicate titles before calling create().
     *
     * @param titre the title to check
     * @return true if an article with this title exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean existsByTitre(String titre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM article WHERE titre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titre.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // COUNT(*) > 0 means at least one exists
            }
        }
        return false;
    }

    /**
     * Check if a title exists EXCLUDING a specific article's ID.
     *
     * This is the "update-safe" duplicate check. When a user edits an article,
     * we want to check if the new title conflicts with ANOTHER article — but
     * not with the article being edited itself.
     *
     * Example: editing article ID 5 from "Old Title" to "New Title"
     *   → existsByTitreExcludingId("New Title", 5) checks:
     *     "does any article OTHER than #5 have the title 'New Title'?"
     *
     * @param titre the title to check
     * @param excludeId the id_Article to exclude from the search
     * @return true if another article has this title, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean existsByTitreExcludingId(String titre, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM article WHERE titre = ? AND id_Article != ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titre.trim());
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}