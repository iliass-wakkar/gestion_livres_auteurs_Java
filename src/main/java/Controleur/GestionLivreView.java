package Controleur;

import Modules.Livre;
import Utiles.PopUpForm;
import Utiles.PopUpMessage;
import View.LivreView.LivreView;

import javax.swing.*;
import java.util.ArrayList;

public class GestionLivreView {
    private String[] addFields = {"Titre", "ID Auteur"};
    private LivreView livreView;
    private ArrayList<Livre> livreList;
    private GestionLivre gestionLivre = new GestionLivre();
    private PopUpForm<Livre> addForm;

    public GestionLivreView(LivreView livreView) {
        this.livreList = getLivreList();
        this.livreView = livreView;
        initfunctions();
    }

    public LivreView getLivreView() {
        return livreView;
    }

    private void initfunctions() {
        addButton();
    }

    private ArrayList<Livre> getLivreList() {
        return gestionLivre.readAllLivres();
    }

    private void addButton() {
        livreView.getAddButton().addActionListener(e -> {
            addForm = new PopUpForm<>(
                    "Ajouter un Livre",
                    addFields,
                    data -> {
                        try {
                            String titre = data.get("Titre");
                            String idAuteurStr = data.get("ID Auteur");

                            // Validate title
                            if (titre == null || titre.trim().isEmpty()) {
                                throw new IllegalArgumentException("Le titre ne peut pas être vide !");
                            }

                            // Validate author ID
                            int idAuteur;
                            try {
                                idAuteur = Integer.parseInt(idAuteurStr);
                            } catch (NumberFormatException ex) {
                                throw new IllegalArgumentException("L'ID de l'auteur doit être un nombre valide !");
                            }

                            return new Livre(0, titre, idAuteur);

                        } catch (IllegalArgumentException ex) {
                            new PopUpMessage("error", ex.getMessage());
                            return null; // Prevent form submission
                        }
                    }
            );

            addForm.getSaveButton().addActionListener(ev -> {
                Livre newLivre = addForm.getObject();
                if (newLivre != null) {
                    try {
                        int newId = gestionLivre.createLivre(
                                newLivre.getTitre(),
                                newLivre.getId_auteur()
                        );

                        if (newId != -1) {
                            // Refresh table and close form
                            livreView.populateTable(gestionLivre.readAllLivres());
                            addForm.dispose();  // <-- THIS CLOSES THE POPUP
                        }
                    } catch (Exception ex) {
                        new PopUpMessage("error", "Erreur de base de données: " + ex.getMessage());
                    }
                }
            });

            addForm.setVisible(true);
        });
    }


    private Livre getLivreById(int id) {
        for (Livre livre : livreList) {
            if (livre.getId() == id) {
                return livre;
            }
        }
        return null;
    }
}