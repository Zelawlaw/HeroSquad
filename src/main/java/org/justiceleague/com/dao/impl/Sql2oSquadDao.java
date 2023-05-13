package org.justiceleague.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.justiceleague.com.dao.squadDao;
import org.justiceleague.com.models.Squad;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

@RequiredArgsConstructor
public class Sql2oSquadDao implements squadDao {

    private final Sql2o sql2o;
    @Override
    public List<Squad> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM squads") //raw sql
                    .executeAndFetch(Squad.class); //fetch a list
        }
    }

    @Override
    public int add(Squad squad) {

        String sql = "INSERT INTO squads (name,description) VALUES (:name,:description)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
       return (int)  con.createQuery(sql, true)
                    .bind(squad)
                    .executeUpdate()
                    .getKey();
        }
    }

    @Override
    public Squad findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM squads WHERE id=:id") //raw sql
                    .addParameter("id",id)
                    .executeAndFetchFirst(Squad.class); //fetch a list
        }
    }

    @Override
    public void update(int id, String name, String cause) {
        String sql = "UPDATE squads SET name = :name, cause = :cause WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name",name)
                    .addParameter("cause",cause)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {

        String sql = "DELETE FROM squads WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllSquads() {

    }
}
