package org.justiceleague.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.justiceleague.com.dao.herosDao;
import org.justiceleague.com.models.Hero;
import org.justiceleague.com.models.Power;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

@RequiredArgsConstructor
public class Sql2oHeroDao  implements herosDao {

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
            return con.createQuery("SELECT * FROM heros WHERE squadId IS NULL") //raw sql
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
    public int add(Hero hero) {
        String sql = "INSERT INTO heros (name,age,squadid,powerid,weaknessid) VALUES (:name,:age,:squadId,:powerId" +
                ",:weaknessId)"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            return (int)  con.createQuery(sql, true)
                    .addParameter("name",hero.getName())
                    .addParameter("age",hero.getAge())
                    .addParameter("squadId",hero.getSquadId()==0?null:hero.getSquadId())
                    .addParameter("powerId",hero.getPowerId())
                    .addParameter("weaknessId",hero.getWeaknessId())
                    .executeUpdate()
                    .getKey();
        }
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

    }
}
