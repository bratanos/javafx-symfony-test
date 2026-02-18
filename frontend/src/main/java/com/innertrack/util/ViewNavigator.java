package com.innertrack.util;

import com.innertrack.model.User;
import com.innertrack.model.Admin;
import com.innertrack.model.Psychologue;
import com.innertrack.session.SessionManager;

public class ViewNavigator {
    public static void navigateToDashboard() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user instanceof Admin) {
            ViewManager.loadView("admin/dashboard");
        } else if (user instanceof Psychologue) {
            ViewManager.loadView("psychologue/dashboard");
        } else {
            ViewManager.loadView("user/dashboard");
        }
    }
}
