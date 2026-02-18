package com.innertrack.service;

import java.sql.SQLException;
import java.util.List;

public interface ICrudService<T> {
    boolean create(T entity) throws SQLException;

    T read(int id) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(int id) throws SQLException;

    List<T> findAll() throws SQLException;
}
