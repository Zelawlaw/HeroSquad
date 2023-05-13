package org.heroesunlimited.com.dao;

import org.heroesunlimited.com.models.Hero;

import java.util.List;

public interface herosDao {

    // LIST ALL
    List<Hero> getAll();

    //LIST ALL NOT IN A SQUAD
    List<Hero> getAllNotInSquad();

    //LIST ALL IN squad
    List<Hero> getAllInSquad(int squadId);

    //LIST ALL with power
    List<Hero> getAllWithPower(int powerId);

    //LIST ALL with weakness
    List<Hero> getAllWithWeakness(int weaknessId);

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
