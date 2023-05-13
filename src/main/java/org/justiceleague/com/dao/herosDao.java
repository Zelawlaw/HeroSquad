package org.justiceleague.com.dao;

import org.justiceleague.com.models.Hero;

import java.util.List;

public interface herosDao {

    // LIST
    List<Hero> getAll();

    // CREATE
    void add(Hero hero);

    // READ
    Hero findById(int id);

    // UPDATE
    void update(int id, int powerId, int weaknessId);

    // DELETE
    void deleteById(int id);

    void clearAllHeros();
}
