package Controleur;

import Modules.Livre;
import Utiles.Connect;
import Utiles.PopUpMessage;
import Modules.Auteur;
import java.sql.*;
import java.util.ArrayList;

public class GestionAuteur {
    // CREATE - Returns generated ID
    public static int createAuteur(String nom, String nationalite) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "INSERT INTO Auteur (nom, nationalite) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nom);
                stmt.setString(2, nationalite);

                int rows = stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        new PopUpMessage("success", rows + " auteur(s) créé(s) - ID généré: " + generatedId);
                        return generatedId;
                    } else {
                        new PopUpMessage("error", rows + " auteur(s) créé(s) - ID non disponible");
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur création auteur: " + e.getMessage());
            return -1;
        } finally {
            Connect.closeConnection(connection);
        }
    }

    // READ ALL - Returns ArrayList<Auteur>
    public static ArrayList<Auteur> readAllAuteurs() {
        Connection connection = null;
        ArrayList<Auteur> auteurs = new ArrayList<>();

        try {
            connection = Connect.getConnection();
            String query = "SELECT * FROM Auteur";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    auteurs.add(new Auteur(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("nationalite")
                    ));
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur lecture auteurs: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
        return auteurs;
    }

    // READ SINGLE - Returns Auteur object
    public static Auteur readAuteur(int id) {
        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "SELECT * FROM Auteur WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Auteur(
                                rs.getInt("id"),
                                rs.getString("nom"),
                                rs.getString("nationalite")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur lecture auteur: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
        return null;
    }

    // UPDATE - Using Auteur object
    public static void updateAuteur(Auteur auteur) {
        if (auteur == null) {
            new PopUpMessage("error", "Auteur object cannot be null");
            return;
        }

        Connection connection = null;
        try {
            connection = Connect.getConnection();
            String query = "UPDATE Auteur SET nom = ?, nationalite = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, auteur.getNom());
                stmt.setString(2, auteur.getNationalite());
                stmt.setInt(3, auteur.getId());

                int rows = stmt.executeUpdate();
                new PopUpMessage(rows > 0 ? "success" : "warning",
                        rows > 0 ? "Auteur mis à jour" : "Aucun auteur modifié");
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur mise à jour: " + e.getMessage());
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
                new PopUpMessage(rows > 0 ? "success" : "warning",
                        rows > 0 ? "Auteur supprimé" : "Aucun auteur supprimé");
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur suppression: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
    }

    public static ArrayList<Livre> getBooksByAuthor(int authorId) {
        Connection connection = null;
        ArrayList<Livre> livres = new ArrayList<>();

        try {
            connection = Connect.getConnection();
            String query = "SELECT * FROM Livre WHERE id_auteur = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, authorId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        livres.add(new Livre(
                                rs.getInt("id"),
                                rs.getString("titre"),
                                rs.getInt("id_auteur")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            new PopUpMessage("error", "Erreur lors de la récupération des livres: " + e.getMessage());
        } finally {
            Connect.closeConnection(connection);
        }
        return livres;
    }
}