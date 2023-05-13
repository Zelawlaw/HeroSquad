package org.justiceleague.com.dao;

import org.justiceleague.com.models.Power;
import org.justiceleague.com.models.Weakness;

import java.util.List;

public interface weaknessesDao {


    // LIST
    List<Weakness> getAll();

    // CREATE
    void add(Weakness weakness);

    // READ
    Weakness findById(int id);

    // UPDATE
    void update(int id, String name,String description);

    // DELETE
    void deleteById(int id);

    void clearAllWeaknesses();
}
