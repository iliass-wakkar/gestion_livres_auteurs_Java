package Controleur;

import Modules.Auteur;
import Utiles.PopUpForm;
import Utiles.PopUpMessage;
import View.AuteurView.AuteurView;

import javax.swing.*;
import java.util.ArrayList;

public class AuteurViewController {
    private String[] addFields = {"Nom", "Nationalité"};
    private AuteurView auteurView;
    private ArrayList<Auteur> auteurList;
    private GestionAuteur gestionAuteur = new GestionAuteur();
    private PopUpForm<Auteur> addForm;

    public AuteurViewController(AuteurView auteurView) {
        this.auteurList = getAuteurList();
        this.auteurView = auteurView;
        initFunctions();
    }

    private void initFunctions() {
        addButton();
    }

    private ArrayList<Auteur> getAuteurList() {
        return gestionAuteur.readAllAuteurs();
    }

    private void addButton() {
        auteurView.getAddButton().addActionListener(e -> {
            addForm = new PopUpForm<>(
                    "Ajouter un Auteur",
                    addFields,
                    data -> {
                        try {
                            String nom = data.get("Nom");
                            String nationalite = data.get("Nationalité");

                            // Validate name
                            if (nom == null || nom.trim().isEmpty()) {
                                throw new IllegalArgumentException("Le nom ne peut pas être vide !");
                            }

                            // Validate nationality
                            if (nationalite == null || nationalite.trim().isEmpty()) {
                                throw new IllegalArgumentException("La nationalité ne peut pas être vide !");
                            }

                            return new Auteur(0, nom, nationalite);

                        } catch (IllegalArgumentException ex) {
                            new PopUpMessage("error", ex.getMessage());
                            return null; // Prevent form submission
                        }
                    }
            );

            addForm.getSaveButton().addActionListener(ev -> {
                Auteur newAuteur = addForm.getObject();
                if (newAuteur != null) {
                    try {
                        int newId = gestionAuteur.createAuteur(
                                newAuteur.getNom(),
                                newAuteur.getNationalite()
                        );

                        if (newId != -1) {
                            // Refresh table and close form
                            auteurView.populateTable(gestionAuteur.readAllAuteurs());
                            addForm.dispose();
                        }
                    } catch (Exception ex) {
                        new PopUpMessage("error", "Erreur de base de données: " + ex.getMessage());
                    }
                }
            });

            addForm.setVisible(true);
        });
    }

    private Auteur getAuteurById(int id) {
        for (Auteur auteur : auteurList) {
            if (auteur.getId() == id) {
                return auteur;
            }
        }
        return null;
    }
}