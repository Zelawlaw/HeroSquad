package org.justiceleague.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.justiceleague.com.dao.powersDao;
import org.justiceleague.com.models.Power;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

@RequiredArgsConstructor
public class Sql2oPowerDao implements powersDao {

    private final Sql2o sql2o;
    @Override
    public List<Power> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM powers") //raw sql
                    .executeAndFetch(Power.class); //fetch a list
        }
    }

    @Override
    public void add(Power power) {
        String sql = "INSERT INTO powers (name,description) VALUES (:name,:description)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
           con.createQuery(sql, true)
                    .bind(power)
                    .executeUpdate()
                    .getKey();
        } catch (Sql2oException ex) {
            System.out.println(ex); //oops we have an error!
        }
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

        String sql = "DELETE FROM powers WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllPowers() {

    }
}
