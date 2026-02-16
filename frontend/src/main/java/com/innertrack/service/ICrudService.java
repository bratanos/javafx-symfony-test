package com.innertrack.service;

import java.util.List;
import java.sql.SQLException;

public interface ICrudService<T> {
    boolean create(T entity) throws SQLException;

    T read(int id)  throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(int id) throws SQLException;

    List<T> findAll() throws SQLException;
}
