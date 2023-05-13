package org.justiceleague.com.dao;

import org.justiceleague.com.models.Hero;
import org.justiceleague.com.models.Power;

import java.util.List;

public interface powersDao {

    // LIST
    List<Power> getAll();

    // CREATE
    void add(Power power);

    // READ
    Power findById(int id);

    // UPDATE
    void update(int id, String name,String description);

    // DELETE
    void deleteById(int id);

    void clearAllPowers();
}
