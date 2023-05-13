package org.justiceleague.com.dao.impl;

import lombok.RequiredArgsConstructor;
import org.justiceleague.com.dao.herosDao;
import org.justiceleague.com.models.Hero;
import org.justiceleague.com.models.Power;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

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
        return null;
    }

    @Override
    public List<Hero> getAllInSquad(int squadId) {
        return null;
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
