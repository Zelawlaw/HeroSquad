package org.heroesunlimited.com.dao.impl;

import io.github.cdimascio.dotenv.Dotenv;
import org.heroesunlimited.com.models.Hero;
import org.heroesunlimited.com.models.Power;
import org.heroesunlimited.com.models.Squad;
import org.heroesunlimited.com.models.Weakness;
import org.junit.jupiter.api.*;
import org.postgresql.util.PSQLException;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Sql2oPowerDaoTest {

    private static final List<Power> powersCreatedinTest = new ArrayList<>();
    private static final List<Hero> herosCreatedinTest = new ArrayList<>();
    private static final List<Weakness> weaknessCreatedinTest = new ArrayList<>();
    private static final List<Squad> squadsCreatedinTest = new ArrayList<>();
    private Sql2oHeroDao heroDao;
    private Sql2oPowerDao powerDao;
    private Sql2oWeaknessDao weaknessDao;
    private Sql2oSquadDao squadDao;
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
        powerDao = new Sql2oPowerDao(sql2o);
        heroDao = new Sql2oHeroDao(sql2o);
        weaknessDao = new Sql2oWeaknessDao(sql2o);
        squadDao = new Sql2oSquadDao(sql2o);
        
        conn = sql2o.open();
    }


    @Test
    void testConnection() {
        assertTrue(true);
    }

    @Test
    void testAddPower() {
        Power power = new Power("Ice Breath", "freeze things with breath");
        powerDao.add(power);
        Power fetchedPower = powerDao.getAll().get(0);
        powersCreatedinTest.add(fetchedPower); // to be cleaned up later
        assertEquals("Ice Breath", fetchedPower.getName());
    }

    @Test
    void testFindPower() {
        Power power = new Power("Laser vision", "shoot lasers from eyes");
        int Id = powerDao.add(power);
        Power fetchedPower = powerDao.findById(Id);
        Power fectedPower2 = powerDao.findByName(power.getName());
        powersCreatedinTest.add(fetchedPower);
        assertEquals(Id, fetchedPower.getId());
        assertEquals(power.getName(),fectedPower2.getName());
    }

    @Test
    void testUpdate() {
        Power power = new Power("Claws", "Titanium retractible claws");
        int id = powerDao.add(power);
        powerDao.update(id, "Metal Claws", power.getDescription());
        powersCreatedinTest.add(powerDao.findById(id));
        Assertions.assertEquals("Metal Claws", powerDao.findById(id).getName());
    }

    @Test
    void testDeletebyId() {
        Power power = new Power("Flight", "Power to Fly");
        powerDao.add(power);
        Power fetchedPower = powerDao.getAll().get(0);
        powersCreatedinTest.add(fetchedPower); //to be cleaned up later
        powerDao.deleteById(fetchedPower.getId());
        assertEquals(0, powerDao.getAll().size());
    }
    
    @Test
    void testDeletePowerWithHeroThrowsException(){
        Power power = new Power("Flight", "Power to Fly");
        int powerId = powerDao.add(power);
        powersCreatedinTest.add(powerDao.findById(powerId));//to be cleaned up later
        Weakness weakness = new Weakness("Bones","Brittle Bones");
        int weaknessId = weaknessDao.add(weakness);
        weaknessCreatedinTest.add(weaknessDao.findById(weaknessId));//to be cleaned up later
        Hero hero = new Hero("Mr.Glass",60,powerId,weaknessId);
        int heroId = heroDao.add(hero);
        herosCreatedinTest.add(heroDao.findById(heroId));//to be cleaned up later
        Power fetchedPower = powerDao.findById(powerId);
        powersCreatedinTest.add(powerDao.findById(powerId)); //to be cleaned up later

      Throwable exception =   assertThrows(Exception.class, () -> {
            powerDao.deleteById(fetchedPower.getId());
        });

      assertTrue(exception.getMessage().contains("violates foreign key constraint"));
    }

    @AfterEach
    void tearDown() {
        for(Hero hero: herosCreatedinTest){
            heroDao.deleteById(hero.getId());
        }
        for (Power power : powersCreatedinTest) {
            powerDao.deleteById(power.getId());
        }
        
        for(Weakness weakness: weaknessCreatedinTest){
            weaknessDao.deleteById(weakness.getId());
        }
        for(Squad squad: squadsCreatedinTest){
            squadDao.deleteById(squad.getId());
        }

        
        conn.close();

    }

    

}