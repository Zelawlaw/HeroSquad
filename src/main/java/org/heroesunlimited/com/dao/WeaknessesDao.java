package org.heroesunlimited.com.dao;

import org.heroesunlimited.com.models.Weakness;

import java.util.List;

public interface WeaknessesDao {


    // LIST
    List<Weakness> getAll();

    // CREATE
    int add(Weakness weakness);

    // READ
    Weakness findById(int id);

    // UPDATE
    void update(int id, String name, String description);

    // DELETE
    void deleteById(int id);

    void clearAllWeaknesses();
}
