package org.justiceleague.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.justiceleague.com.dao.powersDao;
import org.justiceleague.com.models.Power;
import org.sql2o.Sql2o;

import java.util.List;

@RequiredArgsConstructor
public class Sql2oPowerDao implements powersDao {

    private final Sql2o sql2o;
    @Override
    public List<Power> getAll() {
        return null;
    }

    @Override
    public void add(Power power) {

    }

    @Override
    public Power findById(int id) {
        return null;
    }

    @Override
    public void update(int id, String name, String description) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void clearAllPowers() {

    }
}
