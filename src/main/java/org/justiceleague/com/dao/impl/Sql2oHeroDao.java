package org.justiceleague.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.justiceleague.com.dao.herosDao;
import org.justiceleague.com.models.Hero;
import org.sql2o.Sql2o;

import java.util.List;

@RequiredArgsConstructor
public class Sql2oHeroDao  implements herosDao {

    private final Sql2o sql2o;

    @Override
    public List<Hero> getAll() {
        return null;
    }

    @Override
    public List<Hero> getAllNotInSquad() {
        return null;
    }

    @Override
    public List<Hero> getAllInSquad(int squadId) {
        return null;
    }

    @Override
    public int add(Hero hero) {
        return 0;
    }

    @Override
    public Hero findById(int id) {
        return null;
    }

    @Override
    public void update(int id, int squadId, int powerId, int weaknessId) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void clearAllHeros() {

    }
}
