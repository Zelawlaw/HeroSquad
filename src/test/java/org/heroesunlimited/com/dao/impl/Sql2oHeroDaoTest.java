package org.heroesunlimited.com.dao.impl;


import io.github.cdimascio.dotenv.Dotenv;
import org.heroesunlimited.com.models.Hero;
import org.heroesunlimited.com.models.Power;
import org.heroesunlimited.com.models.Squad;
import org.heroesunlimited.com.models.Weakness;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
 class Sql2oHeroDaoTest {

    private static final List<Integer> herosCreatedinTest = new ArrayList<>();
    private static final List<Integer> powersCreatedinTest = new ArrayList<>();
    private static final List<Integer> weaknessesCreatedinTest = new ArrayList<>();
    private static final List<Integer> squadsCreatedinTest = new ArrayList<>();
    private Sql2oHeroDao heroDao;
    private Sql2oPowerDao powerDao;
    private Sql2oWeaknessDao weaknessDao;
    private Sql2oSquadDao squadDao;
    private Dotenv dotenv;
    private Connection conn;

    int powerId1,powerId2,powerId3;
    int weaknessId1,weaknessId2,weaknessId3;
    int squadId1,squadId2,squadId3;

    @BeforeAll
    public void setUp() throws Exception {
        dotenv = Dotenv.load();
        String url = dotenv.get("dbUrl") != null ? dotenv.get("dbUrl") : System.getenv("dbUrl");
        String user = dotenv.get("dbUsername") != null ? dotenv.get("dbUsername") : System.getenv("dbUsername");
        String password = dotenv.get("dbPassword") != null ? dotenv.get("dbPassword") : System.getenv("dbPassword");
        Sql2o sql2o = new Sql2o(url, user, password);

        // Get the SQL script
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/createdb.sql");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String sqlScript = scanner.hasNext() ? scanner.next() : "";
        // Execute the SQL script
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sqlScript).executeUpdate();
        }
        heroDao = new Sql2oHeroDao(sql2o);
        powerDao = new Sql2oPowerDao(sql2o);
        weaknessDao = new Sql2oWeaknessDao(sql2o);
        squadDao = new Sql2oSquadDao(sql2o);
        conn = sql2o.open();

        //create powers,weaknesses and squads
        powerId1 = powerDao.add(new Power("Super strength","very strong"));
        weaknessId1 = weaknessDao.add(new Weakness("Kryptonite","green crystall makes Krytonians weak"));
        squadId1 = squadDao.add(new Squad("Justice Leage","Stop injustice"));

        powerId2 = powerDao.add(new Power("Super Speed","very faset"));
        weaknessId2 = weaknessDao.add(new Weakness("Heat","can overheat due to fast spead"));
        squadId2 = squadDao.add(new Squad("X Men","Track Metahumans"));

        powerId3 = powerDao.add(new Power("Intellect","very smart"));
        weaknessId3 = weaknessDao.add(new Weakness("Brittle Bones","very weak bones, can break easily"));
        squadId3 = squadDao.add(new Squad("Avengers","Avenging bad guys"));

        //add powers created
        powersCreatedinTest.add(powerId1);
        powersCreatedinTest.add(powerId2);
        powersCreatedinTest.add(powerId3);

        //add weaknesses created
        weaknessesCreatedinTest.add(weaknessId1);
        weaknessesCreatedinTest.add(weaknessId2);
        weaknessesCreatedinTest.add(weaknessId3);

        //add  squads created
        squadsCreatedinTest.add(squadId1);
        squadsCreatedinTest.add(squadId2);
        squadsCreatedinTest.add(squadId3);
    }


    @Test
    void testConnection() {
        assertTrue(true);
    }
    
    @Test
    void testAddHero(){

        Hero hero = new Hero("superman",30,powerId1,weaknessId1);
        int id = heroDao.add(hero);
        Hero fetchedHero = heroDao.getAll().get(0);
        herosCreatedinTest.add(id); // to be cleaned up later
        assertEquals("superman", fetchedHero.getName());
    }

    @Test
    void testFindHero() {
        Hero hero = new Hero("The Thing",78,powerId3,weaknessId3);
        hero.setSquadId(squadId3);
        int Id = heroDao.add(hero);
        Hero fetchedHero = heroDao.findById(Id);
        herosCreatedinTest.add(Id);
        assertEquals(Id, fetchedHero.getId());

    }

    @Test
    void testGetAllHeros(){
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        Hero hero2 = new Hero("batman",35,powerId2,weaknessId2);
        herosCreatedinTest.add(heroDao.add(hero1));
        herosCreatedinTest.add(heroDao.add(hero2));
        assertEquals(2,heroDao.getAll().size());
    }

    @Test
    void testGetAllHerosInSquad(){
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        hero1.setSquadId(squadId1);
        Hero hero2 = new Hero("batman",35,powerId2,weaknessId2);
        hero2.setSquadId(squadId2);
        Hero hero3 = new Hero("wonderwoman",25,powerId3,weaknessId3);
        hero3.setSquadId(squadId1);
        herosCreatedinTest.add(heroDao.add(hero1));
        herosCreatedinTest.add(heroDao.add(hero2));
        herosCreatedinTest.add(heroDao.add(hero3));
        assertEquals(2,heroDao.getAllInSquad(squadId1).size());
    }

    @Test
    void testGetAllHerosWithPower(){
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        hero1.setSquadId(squadId1);
        Hero hero2 = new Hero("batman",35,powerId1,weaknessId2);
        hero2.setSquadId(squadId2);
        Hero hero3 = new Hero("wonderwoman",25,powerId3,weaknessId3);
        hero3.setSquadId(squadId1);
        herosCreatedinTest.add(heroDao.add(hero1));
        herosCreatedinTest.add(heroDao.add(hero2));
        herosCreatedinTest.add(heroDao.add(hero3));
        assertEquals(2,heroDao.getAllWithPower(powerId1).size());
    }

    @Test
    void testGetAllHerosWithWeakness(){
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        hero1.setSquadId(squadId1);
        Hero hero2 = new Hero("batman",35,powerId1,weaknessId1);
        hero2.setSquadId(squadId2);
        Hero hero3 = new Hero("wonderwoman",25,powerId3,weaknessId3);
        hero3.setSquadId(squadId1);
        herosCreatedinTest.add(heroDao.add(hero1));
        herosCreatedinTest.add(heroDao.add(hero2));
        herosCreatedinTest.add(heroDao.add(hero3));
        assertEquals(1,heroDao.getAllWithWeakness(weaknessId3).size());
    }
    @Test
    void testGetAllHerosNotInSquad(){
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        Hero hero2 = new Hero("batman",35,powerId2,weaknessId2);
        hero2.setSquadId(squadId2);
        Hero hero3 = new Hero("wonderwoman",25,powerId3,weaknessId3);
        hero3.setSquadId(squadId1);

        herosCreatedinTest.add(heroDao.add(hero1));
        herosCreatedinTest.add(heroDao.add(hero2));
        herosCreatedinTest.add(heroDao.add(hero3));
        assertEquals(1,heroDao.getAllNotInSquad().size());
    }

    @Test
    void testUpdateHero(){
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        int Id = heroDao.add(hero1);
        Hero hero2 = new Hero("Flash",30,powerId1,weaknessId1);
        hero2.setSquadId(squadId3);
        int Id2 = heroDao.add(hero2);
        herosCreatedinTest.add(Id);
        herosCreatedinTest.add(Id2);
        heroDao.update(Id,squadId2,powerId3,weaknessId2);
        heroDao.update(Id2,0,powerId1,weaknessId1);
        Hero fetchedHero = heroDao.findById(Id);
        Hero fetchedHero2 = heroDao.findById(Id2);
        //hero 1 update
        assertEquals(squadId2,fetchedHero.getSquadId());
        assertEquals(powerId3,fetchedHero.getPowerId());
        assertEquals(weaknessId2,fetchedHero.getWeaknessId());
        //hero 2 update check if squad id is empty
        assertEquals(0,fetchedHero2.getSquadId());


     }

    @Test
    void testDeleteHero() {
        Hero hero = new Hero("WonderWoman",25,powerId3,weaknessId3);
        int id = heroDao.add(hero);
        herosCreatedinTest.add(id);
        heroDao.deleteById(id);
        assertEquals(0,heroDao.getAll().size());
    }

    @Test
    void testDeleteAllHeroes() {
        Hero hero1 = new Hero("superman",30,powerId1,weaknessId1);
        Hero hero2 = new Hero("batman",35,powerId2,weaknessId2);
        hero2.setSquadId(squadId2);
        Hero hero3 = new Hero("wonderwoman",25,powerId3,weaknessId3);
        hero3.setSquadId(squadId1);

        herosCreatedinTest.add(heroDao.add(hero1));
        herosCreatedinTest.add(heroDao.add(hero2));
        herosCreatedinTest.add(heroDao.add(hero3));

        heroDao.clearAllHeros();
        assertEquals(0,heroDao.getAll().size());

    }


    @AfterEach
    void tearDown() {
        for (Integer id : herosCreatedinTest) {
            heroDao.deleteById(id);
        }
  }

  @AfterAll
    void cleanup(){
     for(int i =0;i<3;i++){
         powerDao.deleteById(powersCreatedinTest.get(i));
         weaknessDao.deleteById(weaknessesCreatedinTest.get(i));
         squadDao.deleteById(squadsCreatedinTest.get(i));
     }


  }

}
