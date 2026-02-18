package com.innertrack.service;

import java.sql.SQLException;
import java.util.List;

public interface ICrudService<T> {

    void create(T t) throws SQLException;

    T read(int id) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    List<T> findAll() throws SQLException;
}