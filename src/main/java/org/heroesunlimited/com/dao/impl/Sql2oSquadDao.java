package org.heroesunlimited.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.heroesunlimited.com.dao.HerosDao;
import org.heroesunlimited.com.dao.SquadDao;
import org.heroesunlimited.com.models.Hero;
import org.heroesunlimited.com.models.Squad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Sql2oSquadDao implements SquadDao {

    private final Sql2o sql2o;
    private final Logger logger = LoggerFactory.getLogger("SquadDao");
    private HerosDao herosDao;

    @Override
    public List<Squad> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM squads") //raw sql
                    .executeAndFetch(Squad.class); //fetch a list
        }
    }

    @Override
    public int add(Squad squad) {

        String sql = "INSERT INTO squads (name,cause) VALUES (:name,:cause)"; //raw sql
        try (Connection con = sql2o.open()) { //try to open a connection
            return (int) con.createQuery(sql, true)
                    .bind(squad)
                    .executeUpdate()
                    .getKey();
        }
    }

    @Override
    public Squad findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM squads WHERE id=:id") //raw sql
                    .addParameter("id", id)
                    .executeAndFetchFirst(Squad.class); //fetch a list
        }
    }

    @Override
    public void update(int id, String name, String cause) {
        String sql = "UPDATE squads SET name = :name, cause = :cause WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("cause", cause)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        //first get all Heros in squad
        herosDao = new Sql2oHeroDao(sql2o);
        List<Hero> herosinsquad = new ArrayList<>();
        if (herosDao.getAllInSquad(id) != null) {
            herosinsquad = herosDao.getAllInSquad(id);
        }
        // reset heros squad 0 ( no squad)
        for (Hero hero : herosinsquad) {
            herosDao.update(hero.getId(), 0, hero.getPowerId(), hero.getWeaknessId());
        }
        //Then delete squad
        String sql = "DELETE FROM squads WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Squad findByname(String squadname) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM squads WHERE name=:name") //raw sql
                    .addParameter("name", squadname)
                    .executeAndFetchFirst(Squad.class); //fetch a list
        }
    }

    @Override
    public Squad findByName(String name) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM squads WHERE name=:name") //raw sql
                    .addParameter("name", name)
                    .executeAndFetchFirst(Squad.class); //fetch a list
        }
    }


}
