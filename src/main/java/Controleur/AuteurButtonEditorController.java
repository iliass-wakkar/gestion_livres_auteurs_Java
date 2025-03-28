package Controleur;

import Utiles.Interfaces.IButtonEditorEventsHandler;
import Utiles.PopUpForm;
import Utiles.PopUpMessage;
import View.ButtonEditor;
import View.AuteurView.AuteurView;
import Modules.Auteur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AuteurButtonEditorController {
    private AuteurView auteurView;
    private PopUpForm<Auteur> editForm;

    public AuteurButtonEditorController(AuteurView auteurView) {
        this.auteurView = auteurView;
    }

    public void onEdit(int row) {
        Auteur selectedAuteur = auteurView.getAuteurAtRow(row);

        if (selectedAuteur == null) {
            new PopUpMessage("error", "Aucun auteur sélectionné");
            return;
        }

        editForm = new PopUpForm<>(
                "Modifier un Auteur",
                new String[]{"Nom", "Nationalité"},
                selectedAuteur,
                (auteur, data) -> {
                    auteur.setNom(data.get("Nom"));
                    auteur.setNationalite(data.get("Nationalité"));
                }
        );

        editForm.getSaveButton().addActionListener(e -> {
            try {
                Auteur updatedAuteur = editForm.getObject();
                if (updatedAuteur != null) {
                    // Call your update method
                    // GestionAuteur.updateAuteur(updatedAuteur);
                    // auteurView.populateTable(GestionAuteur.readAllAuteurs());
                    System.out.println("Updated auteur: " + updatedAuteur);
                    editForm.dispose();
                }
            } catch (Exception ex) {
                new PopUpMessage("error", "Erreur lors de la modification: " + ex.getMessage());
            }
        });

        editForm.setVisible(true);
    }

    public void onDelete(int row) {
        DefaultTableModel model = (DefaultTableModel) auteurView.getTable().getModel();
        if (row >= 0 && row < model.getRowCount()) {
            int confirm = JOptionPane.showConfirmDialog(
                    auteurView,
                    "Êtes-vous sûr de vouloir supprimer cet auteur?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Auteur toDelete = auteurView.getAuteurAtRow(row);
                // GestionAuteur.deleteAuteur(toDelete.getId());
                model.removeRow(row);
            }
        }
    }

    public ButtonEditor getButtonEditor() {
        return new ButtonEditor(auteurView.getTable(), new IButtonEditorEventsHandler() {
            @Override
            public void onEdit(int row) {
                AuteurButtonEditorController.this.onEdit(row);
            }

            @Override
            public void onDelete(int row) {
                AuteurButtonEditorController.this.onDelete(row);
            }
        });
    }
}