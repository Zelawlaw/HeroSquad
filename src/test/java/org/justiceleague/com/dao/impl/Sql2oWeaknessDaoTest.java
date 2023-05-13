package org.justiceleague.com.dao.impl;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.justiceleague.com.models.Weakness;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Sql2oWeaknessDaoTest {

    private Sql2oWeaknessDao weaknessDao;

    private Dotenv dotenv;
    private Connection conn;

    private static List<Weakness> weaknessesCreatedinTest = new ArrayList<>();


    @BeforeAll
    public void setUp() throws Exception {
        dotenv = Dotenv.load();
        String url = dotenv.get("dbUrl") != null ? dotenv.get("dbUrl") : System.getenv("dbUrl");
        String user = dotenv.get("dbUsername") != null ?dotenv.get("dbUsername"):System.getenv("dbUsername");
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
        weaknessDao = new Sql2oWeaknessDao(sql2o);
        conn = sql2o.open();
    }


    @Test
    void testConnection() {
        assertTrue(true);
    }

    @Test
    void testAddWeakness(){
      Weakness weakness = new Weakness("Kryptonite","Green Crystall that causes weakness");
     int id = weaknessDao.add(weakness);
      Weakness fetchedWeakness = weaknessDao.findById(id);
      weaknessesCreatedinTest.add(fetchedWeakness); // to be cleaned up later
      assertEquals("Kryptonite",fetchedWeakness.getName());
    }

    @Test
    void testFindWeakness(){
        Weakness weakness = new Weakness("Water weakness","unable to function in water");
        int Id =  weaknessDao.add(weakness);
        Weakness fetchedWeakness = weaknessDao.findById(Id);
        weaknessesCreatedinTest.add(fetchedWeakness);
        assertEquals(Id,fetchedWeakness.getId());

    }

    @Test
    void testUpdate(){
      Weakness weakness = new Weakness ("Magnets","cause damage to metal exoskeleton");
      int id = weaknessDao.add(weakness);
      weaknessDao.update(id,"Strong magnets",weakness.getDescription());
      weaknessesCreatedinTest.add(weaknessDao.findById(id));
      assertEquals("Strong magnets",weaknessDao.findById(id).getName());
    }

    @Test
    void testDeletebyId(){
        Weakness weakness = new Weakness("Sharp Sound","High pitched sound");
        weaknessDao.add(weakness);
        Weakness fetchedWeakness = weaknessDao.getAll().get(0);
        weaknessesCreatedinTest.add(fetchedWeakness); //to be cleaned up later
        weaknessDao.deleteById(fetchedWeakness.getId());
        assertEquals(0,weaknessDao.getAll().size());
    }

    @AfterEach
    void tearDown() {
       for(Weakness weakness: weaknessesCreatedinTest){
           weaknessDao.deleteById(weakness.getId());
       }
        conn.close();

    }


}