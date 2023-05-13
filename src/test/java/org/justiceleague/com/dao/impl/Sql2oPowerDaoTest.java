package org.justiceleague.com.dao.impl;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Sql2oPowerDaoTest {

    private Sql2oPowerDao powerDao;

    private Dotenv dotenv;
    private Connection conn;


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
    }

    @AfterEach
    void tearDown() {
        conn.close();

    }
}