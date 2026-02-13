# javafx-symfony-test

---

# CSS Styling & Collaboration Guide

This guide explains our project's styling architecture and how the team can contribute while keeping the UI consistent and bug-free.

## 🏗️ CSS Architecture
Our styling is organized into four main layers in `frontend/src/main/resources/css`:

1. **`variables.css` (The Global Tokens)**
   - Contains colors, spacing, and border rules.
   - **Usage**: Always use these variables (e.g., `-fx-text-fill: -color-primary;`) instead of hardcoding hex codes.
   - **Rule**: Avoid changing existing values unless you want to update the entire application's theme.

2. **`base.css` (Global Resets)**
   - Applies default looks to standard JavaFX controls (`button`, `text-field`, etc.).
   - **Rule**: Only edit this if you want to change how *every* button in the app looks.

3. **`utility.css` (Reusable Helpers)**
   - Short classes for margins (`.mt-1`), padding (`.p-2`), or text alignment.
   - **Usage**: Add these classes to elements in Scene Builder or FXML for quick layout adjustments.

4. **`components/` (Page-Specific Styles)**
   - Where 90% of your work should happen.
   - Example: `login.css`, `dashboard.css`.

---

## 🛡️ How to NOT "Break" the App
To prevent your changes from affecting other people's pages, follow these rules:

### 1. Scope your styles
In your FXML, give your root container a unique class (e.g., `.dashboard-container`). In your CSS file, nest all your styles under that class.

**Bad (Global)**:
```css
.button { -fx-background-color: red; } /* Breaks EVERY button in the app */
```

**Good (Scoped)**:
```css
.dashboard-container .button { -fx-background-color: red; } /* Only affects buttons inside the dashboard */
```

### 2. Don't touch `base.css` unless necessary
If you need a specific button to look different, give it a custom class (e.g., `.btn-delete`) instead of editing the generic `.button` class in `base.css`.

### 3. Use Variables
Instead of: `color: #ff0000;`
Use: `-fx-text-fill: -color-danger;`
This ensures that if we update our theme later, your page updates automatically.

---

## 🛠️ Typical Workflow
1. Create a new CSS file for your page in `resources/css/components/yourscreen.css`.
2. Add `@import "../variables.css";` at the top.
3. Add a unique class to your FXML root element.
4. Write your styles inside that unique class scope.
5. In your Java controller or FXML, load the stylesheet.

---

## ❓ Questions?
If you're unsure if a change is "global" or "local", check `base.css`. If it's in there, it's global!
