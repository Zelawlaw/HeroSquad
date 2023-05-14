package org.heroesunlimited.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.heroesunlimited.com.dao.HerosDao;
import org.heroesunlimited.com.models.Hero;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Sql2oHeroDao  implements HerosDao {

    private final Sql2o sql2o;

    @Override
    public List<Hero> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM heros") //raw sql
                    .executeAndFetch(Hero.class); //fetch a list
        }
    }

    @Override
    public List<Hero> getAllNotInSquad() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM heros WHERE squadId = 0") //raw sql
                    .executeAndFetch(Hero.class); //fetch a list
        }
    }

    @Override
    public List<Hero> getAllInSquad(int squadId) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM heros WHERE squadId = :squadId") //raw sql
                    .addParameter("squadId",squadId)
                    .executeAndFetch(Hero.class); //fetch a list
        }
    }

    @Override
    public List<Hero> getAllWithPower(int powerId) {
        Connection con = null;
        List<Hero> heros = new ArrayList<>();
        try{
            con = sql2o.open();
            heros= con.createQuery("SELECT * FROM heros WHERE powerId = :powerId") //raw sql
                    .addParameter("powerId",powerId)
                    .executeAndFetch(Hero.class); //fetch a list
        }
        catch(Exception Ex){
            System.out.println("");
        }
        finally{
           con.close();
        }
     return heros;
    }

    @Override
    public List<Hero> getAllWithWeakness(int weaknessId) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM heros WHERE weaknessId = :weaknessId") //raw sql
                    .addParameter("weaknessId",weaknessId)
                    .executeAndFetch(Hero.class); //fetch a list
        }
    }

    @Override
    public int add(Hero hero) {
        String sql = "INSERT INTO heros (name,age,squadid,powerid,weaknessid) VALUES (:name,:age,:squadId,:powerId" +
                ",:weaknessId)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            return (int)  con.createQuery(sql, true)
                    .addParameter("name",hero.getName())
                    .addParameter("age",hero.getAge())
                    .addParameter("squadId",hero.getSquadId())
                    .addParameter("powerId",hero.getPowerId())
                    .addParameter("weaknessId",hero.getWeaknessId())
                    .executeUpdate()
                    .getKey();
        }
    }

    @Override
    public Hero findById(int id) {
        String sql = "SELECT * FROM heros WHERE id=:id";
        try(Connection con = sql2o.open()){
         return  con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Hero.class);
        }
    }

    @Override
    public void update(int id, int squadId, int powerId, int weaknessId) {
        String sql = "UPDATE  heros SET squadid = :squadId,powerid = :powerId,weaknessid =:weaknessId " +
                "WHERE id = :id"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
           con.createQuery(sql)
                    .addParameter("id",id)
                    .addParameter("squadId",squadId)
                    .addParameter("powerId",powerId)
                    .addParameter("weaknessId",weaknessId)
                    .executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM heros WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllHeros() {
        String sql = "DELETE FROM heros";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
