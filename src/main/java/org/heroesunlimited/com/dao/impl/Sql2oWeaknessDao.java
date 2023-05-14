package org.heroesunlimited.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.heroesunlimited.com.dao.WeaknessesDao;
import org.heroesunlimited.com.models.Weakness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

@RequiredArgsConstructor
public class Sql2oWeaknessDao implements WeaknessesDao {

    private final Sql2o sql2o;
    private final Logger logger = LoggerFactory.getLogger("WeaknessDao");

    @Override
    public List<Weakness> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM weaknesses") //raw sql
                    .executeAndFetch(Weakness.class); //fetch a list
        }
    }

    @Override
    public int add(Weakness weakness) {

        String sql = "INSERT INTO weaknesses (name,description) VALUES (:name,:description)"; //raw sql
        try (Connection con = sql2o.open()) { //try to open a connection
            return (int) con.createQuery(sql, true)
                    .bind(weakness)
                    .executeUpdate()
                    .getKey();
        }
    }

    @Override
    public Weakness findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM weaknesses WHERE id=:id") //raw sql
                    .addParameter("id", id)
                    .executeAndFetchFirst(Weakness.class); //fetch a list
        }
    }

    @Override
    public void update(int id, String name, String description) {
        String sql = "UPDATE weaknesses SET name = :name, description = :description WHERE id=:id";
        try (Connection con = sql2o.open().createQuery(sql)
                .addParameter("id", id)
                .addParameter("name", name)
                .addParameter("description", description)
                .executeUpdate()) {

        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) throws Sql2oException {

        String sql = "DELETE FROM weaknesses WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }


}
