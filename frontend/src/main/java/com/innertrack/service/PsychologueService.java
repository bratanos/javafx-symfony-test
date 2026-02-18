package com.innertrack.service;

import com.innertrack.dao.UserDao;
import com.innertrack.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PsychologueService {
    private final UserDao userDao = new UserDao();

    public List<User> getAllPsychologues() throws SQLException {
        return userDao.findAll().stream()
                .filter(u -> u.getRoles().contains("ROLE_PSYCHOLOGUE"))
                .collect(Collectors.toList());
    }

    // Add more psychologue specialized logic here
}
