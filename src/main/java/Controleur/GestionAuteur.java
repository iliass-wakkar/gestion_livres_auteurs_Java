package Controleur;

import Utiles.Connect;
import Utiles.PopUpMessage;

import java.sql.*;

public class GestionAuteur {
    static Connection connection = Connect.getConnection();
    static PopUpMessage popUpMessage ;

    // CREATE
    public static void createAuteur(int id, String nom, String nationalite) {
        try {
            String query = "INSERT INTO Auteur (id, nom, nationalite) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setString(2, nom);
                stmt.setString(3, nationalite);
                int rows = stmt.executeUpdate();
                popUpMessage = new PopUpMessage("success",rows + " auteur(s) créé(s)");
            }
        } catch (SQLException e) {
            popUpMessage = new PopUpMessage("error","Error creating author: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // READ ALL
    public static void readAllAuteurs() {
        try {
            String query = "SELECT * FROM Auteur";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("\nListe des auteurs:");
                System.out.println("-----------------------");
                while (rs.next()) {
                    System.out.printf("ID: %d, Nom: %s, Nationalité: %s%n",
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("nationalite"));
                }
                System.out.println("-----------------------");
            }
        } catch (SQLException e) {
            System.err.println("Error reading authors: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // READ SINGLE
    public static void readAuteur(int id) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "SELECT * FROM Auteur WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nAuteur trouvé:");
                        System.out.println("-----------------------");
                        System.out.printf("ID: %d, Nom: %s, Nationalité: %s%n",
                                rs.getInt("id"),
                                rs.getString("nom"),
                                rs.getString("nationalite"));
                        System.out.println("-----------------------");
                    } else {
                        System.out.println("Auteur non trouvé");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading author: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // UPDATE
    public static void updateAuteur(int id, String nom, String nationalite) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "UPDATE Auteur SET nom = ?, nationalite = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, nom);
                stmt.setString(2, nationalite);
                stmt.setInt(3, id);
                int rows = stmt.executeUpdate();
                System.out.println(rows + " auteur(s) mis à jour");
            }
        } catch (SQLException e) {
            System.err.println("Error updating author: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // DELETE
    public static void deleteAuteur(int id) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "DELETE FROM Auteur WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                System.out.println(rows + " auteur(s) supprimé(s)");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting author: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }
}