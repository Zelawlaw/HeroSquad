package org.heroesunlimited.com.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStream;
import java.util.Scanner;

public class DbConfig {
    //connect to psql datasource
    public static Sql2o datasource() {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("dbUrl") != null ? dotenv.get("dbUrl") : System.getenv("dbUrl");
        String user = dotenv.get("dbUsername") != null ? dotenv.get("dbUsername") : System.getenv("dbUsername");
        String password = dotenv.get("dbPassword") != null ? dotenv.get("dbPassword") : System.getenv("dbPassword");
        Sql2o sql2o = new Sql2o(url, user, password);

        // Get the SQL script
        InputStream inputStream = DbConfig.class.getClassLoader().getResourceAsStream("db/createdb.sql");
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String sqlScript = scanner.hasNext() ? scanner.next() : "";
        // Execute the SQL script to set up database
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sqlScript).executeUpdate();
        }

        return sql2o;
    }
}
