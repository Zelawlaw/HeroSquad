package org.justiceleague.com.dao.impl;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;
import org.justiceleague.com.models.Power;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Sql2oPowerDaoTest {

    private Sql2oPowerDao powerDao;

    private Dotenv dotenv;
    private Connection conn;

    private static List<Power> powersCreatedinTest = new ArrayList<>();


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
        powerDao = new Sql2oPowerDao(sql2o);
        conn = sql2o.open();
    }


    @Test
    void testConnection() {
        assertTrue(true);
    }

    @Test
    void testAddPower(){
      Power power = new Power("Ice Breath","freeze things with breath");
      powerDao.add(power);
      Power fetchedPower = powerDao.getAll().get(0);
      powersCreatedinTest.add(fetchedPower); // to be cleaned up later
      assertEquals("Ice Breath",fetchedPower.getName());
    }

    @Test
    void testDeletebyId(){
        Power power = new Power("Flight","Power to Fly");
        powerDao.add(power);
        Power fetchedPower = powerDao.getAll().get(0);
        powersCreatedinTest.add(fetchedPower); //to be cleaned up later
        powerDao.deleteById(fetchedPower.getId());
        assertEquals(0,powerDao.getAll().size());
    }

    @AfterEach
    void tearDown() {
       for(Power power: powersCreatedinTest){
           System.out.println("deleting :"+power.getName()+" id :"+power.getId());
           powerDao.deleteById(power.getId());
       }
        conn.close();

    }


}