package com.innertrack.service;

import java.sql.SQLException;
import java.util.List;

public interface ICrudService<T> {

    void ajouter(T t) throws SQLException;

    void modifier(T t) throws SQLException;

    void supprimer(T t) throws SQLException;

    List<T> recuperer() throws SQLException;
}