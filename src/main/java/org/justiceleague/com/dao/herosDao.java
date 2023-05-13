package org.justiceleague.com.dao;

import org.justiceleague.com.models.Hero;

import java.util.List;

public interface herosDao {

    // LIST ALL
    List<Hero> getAll();

    //LIST ALL NOT IN A SQUAD
    List<Hero> getAllNotInSquad();

    //LIST ALL IN squad
    List<Hero> getAllInSquad(int squadId);

    // CREATE
    int add(Hero hero);

    // READ
    Hero findById(int id);

    // UPDATE
    void update(int id,int squadId, int powerId, int weaknessId);

    // DELETE
    void deleteById(int id);

    void clearAllHeros();
}
