package org.heroesunlimited.com.dao;

import org.heroesunlimited.com.models.Squad;

import java.util.List;

public interface SquadDao {


    // LIST
    List<Squad> getAll();

    // CREATE
    int add(Squad squad);

    // READ
    Squad findById(int id);

    // UPDATE
    void update(int id, String name, String cause);

    // DELETE
    void deleteById(int id);

    void clearAllSquads();
}
