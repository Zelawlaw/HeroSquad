package org.heroesunlimited.com.dao;

import org.heroesunlimited.com.models.Power;

import java.util.List;

public interface PowersDao {

    // LIST
    List<Power> getAll();

    // CREATE
    int add(Power power);

    // READ
    Power findById(int id);

    // UPDATE
    void update(int id, String name, String description);

    // DELETE
    void deleteById(int id);


}
