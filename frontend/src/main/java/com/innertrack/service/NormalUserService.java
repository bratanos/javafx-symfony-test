package com.innertrack.service;

import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class NormalUserService {
    private final UserDao userDao = new UserDao();

    public List<User> getAllNormalUsers() throws SQLException {
        return userDao.findAll().stream()
                .filter(u -> u.getRoles().contains("ROLE_USER"))
                .collect(Collectors.toList());
    }

    // Add more user specialized logic here
}
