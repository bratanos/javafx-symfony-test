package com.innertrack.service;

import java.util.List;

public interface ICrudService<T> {
    boolean create(T entity);

    T read(int id);

    boolean update(T entity);

    boolean delete(int id);

    List<T> findAll();
}
