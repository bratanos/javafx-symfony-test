package com.innertrack.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {

    boolean create(T t);

    Optional<T> findById(int id);

    List<T> findAll();

    boolean update(T t);

    boolean delete(int id);
}
