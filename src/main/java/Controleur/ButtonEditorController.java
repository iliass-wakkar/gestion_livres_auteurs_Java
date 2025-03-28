package Controleur;

import Utiles.Interfaces.IButtonEditorEventsHandler;
import Utiles.PopUpForm;
import Utiles.PopUpMessage;
import View.ButtonEditor;
import View.LivreView.LivreView;
import Modules.Livre;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ButtonEditorController {
    private LivreView livreView;
    private PopUpForm<Livre> editForm;

    public ButtonEditorController(LivreView livreView) {
        this.livreView = livreView;
    }

    public void onEdit(int row) {
        try {
            Livre selectedLivre = livreView.getLivreAtRow(row);
            if (selectedLivre == null) {
                new PopUpMessage("error", "Aucun livre sélectionné ou table non initialisée");
                return;
            }

            // Create and show edit form immediately
            editForm = new PopUpForm<>(
                    "Modifier un Livre",
                    new String[]{"Titre", "ID Auteur"},
                    selectedLivre,
                    (livre, data) -> {
                        livre.setTitre(data.get("Titre"));
                        livre.setId_auteur(Integer.parseInt(data.get("ID Auteur")));
                    }
            );

            // Handle save action
            editForm.getSaveButton().addActionListener(e -> {
                try {
                    Livre updatedLivre = editForm.getObject();
                    if (updatedLivre != null) {
                        GestionLivre.updateLivre(updatedLivre);
                        editForm.dispose();
                        // Refresh table if needed
                        livreView.populateTable(GestionLivre.readAllLivres());
                    }
                } catch (Exception ex) {
                    new PopUpMessage("error", "Erreur lors de la modification: " + ex.getMessage());
                }
            });

            editForm.setVisible(true); // Show the form

        } catch (Exception ex) {
            new PopUpMessage("error", "Erreur lors de la modification: " + ex.getMessage());
        }
    }

    public void onDelete(int row) {
        DefaultTableModel model = (DefaultTableModel) livreView.getTable().getModel();
        if (row >= 0 && row < model.getRowCount()) {
            int confirm = JOptionPane.showConfirmDialog(
                    livreView,
                    "Êtes-vous sûr de vouloir supprimer ce livre?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Livre toDelete = livreView.getLivreAtRow(row);
                GestionLivre.deleteLivre(toDelete.getId());
                model.removeRow(row);
            }
        }
    }

    public ButtonEditor getButtonEditor() {
        return new ButtonEditor(livreView.getTable(), new IButtonEditorEventsHandler() {
            @Override
            public void onEdit(int row) {
                ButtonEditorController.this.onEdit(row);
            }

            @Override
            public void onDelete(int row) {
                ButtonEditorController.this.onDelete(row);
            }
        });
    }
}