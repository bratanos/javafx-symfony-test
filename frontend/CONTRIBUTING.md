# InnerTrack Development Guide

Welcome to the modernized InnerTrack codebase! This document explains the current project structure and how to add new features or styles.

## 🏗️ Project Structure

The project follows a modular structure for clarity and scalability.

- **Controllers (`com.innertrack.controller`)**
  - Managed by `ViewManager`.
  - **Auth**: Logic for user onboarding.
  - **Dashboards**: Separate packages for `admin`, `user`, and `psychologue`.
  - **Profile**: Shared logic for user settings and preferences.

- **Services (`com.innertrack.service`)**
  - Contains business logic and orchestrates DAOs. 
  - Unique services for `AdminService`, `PsychologueService`, and `UserService` allow for specialized role-based logic.

- **Data Access (`com.innertrack.dao`)**
  - Direct database interaction using JDBC. Use `UserDao` for core user operations.

- **Resources (`src/main/resources`)**
  - **`fxml/`**: Hierarchical storage for UI views. 
    - Keep role-specific views in their own folders (e.g., `fxml/admin/`).
  - **`style/`**:
    - `global.css`: Base variables and shared utility classes.
    - `dashboards.css`: Modern layouts, sidebar designs, and premium card styles.
    - `style.css`: Legacy authentication and general utility styles.

---

## 🎨 How to Add Styles

1. **Variables First**: Use the HSL variables in `global.css` for consistent colors.
2. **Component Specific**: If a style only applies to dashboards, add it to `dashboards.css`.
3. **Avoid Inline**: Use `styleClass` in FXML instead of `-fx-style` attributes.

---

## 📄 How to Add a New Page

1. **Create FXML**: Place your file in a logical folder under `src/main/resources/fxml/`.
2. **Create Controller**: Create a matching class in `com.innertrack.controller`.
3. **Register in `module-info.java`**: Ensure the controller package is `opened` to `javafx.fxml`.
4. **Load the View**: Use `ViewManager.loadView("path/to/filename")` (without `.fxml` extension).

---

## 🤝 Integration Walkthrough for Teammates

If you are merging work from an older version of the project:

1. **Hierarchy Matters**: FXMLs are now organized into subfolders. Update your `ViewManager.loadView` calls to include folder paths (e.g., `"auth/login"` instead of just `"login"`).
2. **CSS Overhaul**: We are moving away from scattered `.css` files. Merge your custom styles into `dashboards.css` or `global.css` wherever possible.
3. **Session Consistency**: The dashboard now uses `SessionManager` to display user names and profile pictures. Use `SessionManager.getInstance().getCurrentUser()` in your controllers instead of passing user objects around.
4. **Absolute Paths**: For profile pictures or local uploads, use absolute paths (`Path.toAbsolutePath()`) to ensure persistence across different local environments.

---

## 🚀 Recent Changes Summary (Commit Reference)

**Commit Message Recommendation:**
`refactor: modernize dashboard UI, implement absolute file persistence, and cleanup unused legacy code`
