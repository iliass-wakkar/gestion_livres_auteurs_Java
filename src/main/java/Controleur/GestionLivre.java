package Controleur;

import Utiles.Connect;
import java.sql.*;
import java.util.ArrayList;

import Modules.Livre;
public class GestionLivre {

    // CREATE
    public static void createLivre(String titre, int idAuteur) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();

            // Adjusted query to exclude the `id` column since it's auto-incremented
            String query = "INSERT INTO Livre (titre, id_auteur) VALUES (?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                // Set parameters for the query
                stmt.setString(1, titre);
                stmt.setInt(2, idAuteur);

                // Execute the query
                int rows = stmt.executeUpdate();

                // Retrieve the auto-generated ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // Get the auto-generated ID
                        System.out.println(rows + " livre(s) créé(s) - ID généré: " + generatedId);
                    } else {
                        System.out.println(rows + " livre(s) créé(s) - ID non disponible");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du livre: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // READ ALL (with author name)
    public static ArrayList<Livre> readAllLivres() {
        Connection connection = null;
        ArrayList<Livre> livres = new ArrayList<>();

        try {
            // Get database connection
            connection = Connect.getConnection();
            String query = "SELECT l.id, l.titre, a.nom AS auteur FROM Livre l JOIN Auteur a ON l.id_auteur = a.id";

            // Execute query
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Process each row in the result set
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titre = rs.getString("titre");
                    int auteur = rs.getInt("auteur");

                    // Create a Livre object and add it to the list
                    Livre livre = new Livre(id, titre, auteur);
                    livres.add(livre);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading books: " + e.getMessage());
        } finally {
            // Close the connection
            Connect.closeConnection(connection);
        }

        // Return the list of Livre objects
        return livres;
    }

    // READ SINGLE
    public static void readLivre(int id) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "SELECT l.id, l.titre, a.nom as auteur FROM Livre l JOIN Auteur a ON l.id_auteur = a.id WHERE l.id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\nLivre trouvé:");
                        System.out.println("---------------------------------");
                        System.out.printf("ID: %d, Titre: %s, Auteur: %s%n",
                                rs.getInt("id"),
                                rs.getString("titre"),
                                rs.getString("auteur"));
                        System.out.println("---------------------------------");
                    } else {
                        System.out.println("Livre non trouvé");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading book: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // UPDATE
    public static void updateLivre(int id, String titre, int idAuteur) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "UPDATE Livre SET titre = ?, id_auteur = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, titre);
                stmt.setInt(2, idAuteur);
                stmt.setInt(3, id);
                int rows = stmt.executeUpdate();
                System.out.println(rows + " livre(s) mis à jour - ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // DELETE
    public static void deleteLivre(int id) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "DELETE FROM Livre WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                System.out.println(rows + " livre(s) supprimé(s) - ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }
}