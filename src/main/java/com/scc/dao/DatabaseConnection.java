package com.scc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:waterRefilling.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            System.out.println("[✗] JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[✗] DB Connection Error: " + e.getMessage());
        }
        return conn;
    }
}