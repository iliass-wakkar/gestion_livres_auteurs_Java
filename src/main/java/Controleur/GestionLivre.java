package Controleur;

import Utiles.Connect;
import Utiles.PopUpMessage;
import Modules.Livre;

import java.sql.*;
import java.util.ArrayList;

public class GestionLivre {

    // CREATE
    public static int createLivre(String titre, int idAuteur) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "INSERT INTO Livre (titre, id_auteur) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, titre);
                stmt.setInt(2, idAuteur);

                int rows = stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        new PopUpMessage("success", rows + " livre(s) créé(s) - ID généré: " + generatedId);
                        return generatedId; // Return the generated ID
                    } else {
                        new PopUpMessage("error", rows + " livre(s) créé(s) - ID non disponible");
                        return -1; // Indicate failure to generate ID
                    }
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur lors de la création du livre: " + e.getMessage());
            return -1; // Indicate failure
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
            String query = "SELECT l.id, l.titre, l.id_auteur, a.nom AS nom_auteur " +
                    "FROM Livre l JOIN Auteur a ON l.id_auteur = a.id";

            // Execute query
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Process each row in the result set
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titre = rs.getString("titre");
                    int idAuteur = rs.getInt("id_auteur");

                    // Create a Livre object and add it to the list
                    Livre livre = new Livre(id, titre, idAuteur);
                    livres.add(livre);
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur lors de la lecture des livres: " + e.getMessage());
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
                        String message = String.format(
                                "Livre trouvé:\n---------------------------------\nID: %d\nTitre: %s\nAuteur: %s\n---------------------------------",
                                rs.getInt("id"),
                                rs.getString("titre"),
                                rs.getString("auteur")
                        );
                        new PopUpMessage("info", message);
                    } else {
                        new PopUpMessage("warning", "Livre non trouvé");
                    }
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur lors de la lecture du livre: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // UPDATED VERSION USING LIVRE OBJECT
    public static void updateLivre(Livre livre) {
        if (livre == null) {
            new PopUpMessage("error", "Livre object cannot be null");
            return;
        }

        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "UPDATE Livre SET titre = ?, id_auteur = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, livre.getTitre());
                stmt.setInt(2, livre.getId_auteur());
                stmt.setInt(3, livre.getId());

                int rows = stmt.executeUpdate();
                new PopUpMessage(rows > 0 ? "success" : "warning",
                        rows > 0 ? "Livre mis à jour" : "Aucun livre modifié");
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur de mise à jour: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // Add delete method
    public static void deleteLivre(int id) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "DELETE FROM Livre WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                new PopUpMessage(rows > 0 ? "success" : "warning",
                        rows > 0 ? "Livre supprimé" : "Aucun livre supprimé");
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur de suppression: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

}