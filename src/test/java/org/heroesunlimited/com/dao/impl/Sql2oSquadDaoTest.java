package org.heroesunlimited.com.dao.impl;

import io.github.cdimascio.dotenv.Dotenv;
import org.heroesunlimited.com.models.Hero;
import org.heroesunlimited.com.models.Power;
import org.heroesunlimited.com.models.Squad;
import org.heroesunlimited.com.models.Weakness;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Sql2oSquadDaoTest {

    private static final List<Squad> squadsCreatedinTest = new ArrayList<>();
    private static final List<Integer> herosCreatedinTest = new ArrayList<>();
    private static final List<Integer> powerssCreatedinTest = new ArrayList<>();
    private static final List<Integer> weaknessCreatedinTest = new ArrayList<>();
    private Sql2oSquadDao squadDao;
    private Sql2oHeroDao heroDao;
    private Sql2oPowerDao powerDao;
    private Sql2oWeaknessDao weaknessDao;
    private Dotenv dotenv;
    private Connection conn;

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
        squadDao = new Sql2oSquadDao(sql2o);
        heroDao = new Sql2oHeroDao(sql2o);
        powerDao = new Sql2oPowerDao(sql2o);
        weaknessDao = new Sql2oWeaknessDao(sql2o);
        conn = sql2o.open();
    }


    @Test
    void testConnection() {
        assertTrue(true);
    }

    @Test
    void testAddSquad() {
        Squad squad = new Squad("Justice League", "Justice for All");
        squadDao.add(squad);
        Squad fetchedSquad = squadDao.getAll().get(0);
        squadsCreatedinTest.add(fetchedSquad); // to be cleaned up later
        assertEquals("Justice League", fetchedSquad.getName());
    }

    @Test
    void testFindSquad() {
        Squad squad = new Squad("Guardians", "Universal peace");
        int Id = squadDao.add(squad);
        Squad fetchedSquad = squadDao.findById(Id);
        squadsCreatedinTest.add(fetchedSquad);
        assertEquals(Id, fetchedSquad.getId());

    }

    @Test
    void testUpdate() {
        Squad squad = new Squad("Avengers", "Word Peace");
        int id = squadDao.add(squad);
        squadDao.update(id, "X-Men", "Track MetaHumans");
        squadsCreatedinTest.add(squadDao.findById(id));
        assertEquals("X-Men", squadDao.findById(id).getName());
    }

    @Test
    void testDeletebyId() {
        Power power = new Power("Intellect", "Very smart");
        int powerId = powerDao.add(power);
        Weakness weakness = new Weakness("Trusting", "trusts too much");
        int weaknessId = weaknessDao.add(weakness);
        Squad squad = new Squad("BatMen", "Follow Batman's code");
        int squadId = squadDao.add(squad);
        Hero hero = new Hero("Batman", 45, powerId, weaknessId);
        hero.setSquadId(squadId);
        int heroId = heroDao.add(hero);
        weaknessCreatedinTest.add(weaknessId);
        powerssCreatedinTest.add(powerId);
        herosCreatedinTest.add(heroId);
        Squad fetchedSquad = squadDao.getAll().get(0);
        squadsCreatedinTest.add(fetchedSquad); //to be cleaned up later
        squadDao.deleteById(fetchedSquad.getId());
        assertEquals(0, squadDao.getAll().size());
    }

    @AfterEach
    void tearDown() {
        for (int id : herosCreatedinTest) {
            heroDao.deleteById(id);
        }
        for (Squad squad : squadsCreatedinTest) {
            squadDao.deleteById(squad.getId());
        }

        for (int id : powerssCreatedinTest) {
            powerDao.deleteById(id);
        }
        for (int id : weaknessCreatedinTest) {
            weaknessDao.deleteById(id);
        }

        conn.close();
    }


}