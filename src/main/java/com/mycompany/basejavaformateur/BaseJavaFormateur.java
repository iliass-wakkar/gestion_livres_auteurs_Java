/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.basejavaformateur;

 import java.sql.*;

public class BaseJavaFormateur {

    public static void main(String[] args) {
        try {
            // Chargement du pilote JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_livres_auteurs", "root", "2001");

            // Exécuter les opérations CRUD
            createFormateur(connection, 3, "Dupont", "123 Rue Exemple");
            afficherLesFormateurs(connection);
            updateFormateur(connection, 4, "Durand", "456 Rue Nouvelle");
            afficherLesFormateurs(connection);
            deleteFormateur(connection, 1);
            afficherLesFormateurs(connection);

            // Fermeture de la connexion
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour créer un formateur
    public static void createFormateur(Connection connection, int code, String nom, String adresse) throws SQLException {
        String query = "INSERT INTO formateur (CODE, NOM, ADRESSE) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, code);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, adresse);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Formateur créé avec succès.");
            }
        }
    }

    // Méthode pour lire et afficher tous les formateurs
    public static void afficherLesFormateurs(Connection connection) throws SQLException {
        String query = "SELECT * FROM formateur";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("Liste des formateurs:");
            while (resultSet.next()) {
                int code = resultSet.getInt("CODE");
                String nom = resultSet.getString("NOM");
                String adresse = resultSet.getString("ADRESSE");
                System.out.println("CODE : " + code + ", Nom : " + nom + ", ADRESSE : " + adresse);
            }
        }
    }

    // Méthode pour mettre à jour un formateur
    public static void updateFormateur(Connection connection, int code, String nom, String adresse) throws SQLException {
        String query = "UPDATE formateur SET NOM = ?, ADRESSE = ? WHERE CODE = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, adresse);
            preparedStatement.setInt(3, code);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Formateur mis à jour avec succès.");
            }
        }
    }

    // Méthode pour supprimer un formateur
    public static void deleteFormateur(Connection connection, int code) throws SQLException {
        String query = "DELETE FROM formateur WHERE CODE = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, code);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Formateur supprimé avec succès.");
            }
        }
    }
}
