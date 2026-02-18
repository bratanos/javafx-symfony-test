package com.innertrack.service;

import com.innertrack.dao.UserDao;
import com.innertrack.model.Admin;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminService {
    private final UserDao userDao = new UserDao();

    public List<Admin> getAllAdmins() throws SQLException {
        return userDao.findAll().stream()
                .filter(u -> u.getRoles().contains("ROLE_ADMIN"))
                .map(Admin::new)
                .collect(Collectors.toList());
    }

    public boolean deleteUser(int userId) throws SQLException {
        return userDao.delete(userId);
    }

    // Add more administrative specialized logic here
}
