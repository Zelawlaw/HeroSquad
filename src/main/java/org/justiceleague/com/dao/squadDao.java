package org.justiceleague.com.dao;

import org.justiceleague.com.models.Power;
import org.justiceleague.com.models.Squad;

import java.util.List;

public interface squadDao {


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
