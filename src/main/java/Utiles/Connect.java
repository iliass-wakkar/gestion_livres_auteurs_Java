package Utiles;

import java.sql.*;

public class Connect {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_livres_auteurs";
    private static final String USER = "root";
    private static final String PASSWORD = "2001";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}