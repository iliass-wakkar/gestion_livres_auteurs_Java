package Controleur;

import Utiles.Interfaces.IButtonEditorEventsHandler;
import Utiles.PopUpForm;
import Utiles.PopUpMessage;
import View.ButtonEditor;
import View.AuteurView.AuteurView;
import Modules.Auteur;
import Modules.Livre;
import View.LivresButtonEditor;
import View.AuteurView.AuteurLivresView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        // In your onEdit method:
        editForm = new PopUpForm<>(
                "Modifier un Auteur",
                new String[]{"Nom", "Nationalité"},
                selectedAuteur,
                (auteur, data) -> {
                    auteur.setNom(data.get("Nom"));
                    auteur.setNationalite(data.get("Nationalité"));
                },
                auteur -> {
                    Map<String, String> values = new HashMap<>();
                    values.put("Nom", auteur.getNom());
                    values.put("Nationalité", auteur.getNationalite());
                    return values;
                }
        );

        editForm.getSaveButton().addActionListener(e -> {
            try {
                Auteur updatedAuteur = editForm.getObject();
                if (updatedAuteur != null) {
                    // Call your update method
                    GestionAuteur.updateAuteur(updatedAuteur);
                    auteurView.populateTable(GestionAuteur.readAllAuteurs());

                    // This will now show proper author details
                    System.out.println("Auteur mis à jour: " + updatedAuteur);

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
                GestionAuteur.deleteAuteur(toDelete.getId());
                model.removeRow(row);

            }
        }
    }

    public void showAuthorBooks(int row) {
        Auteur auteur = auteurView.getAuteurAtRow(row);
        if (auteur != null) {
            ArrayList<Livre> livres = GestionAuteur.getBooksByAuthor(auteur.getId());

            StringBuilder message = new StringBuilder();
            message.append("Livres de ").append(auteur.getNom()).append(":\n\n");

            if (livres.isEmpty()) {
                message.append("Aucun livre trouvé");
            } else {
                for (Livre livre : livres) {
                    message.append("• ").append(livre.getTitre()).append("\n");
                }
            }

            JOptionPane.showMessageDialog(auteurView, message.toString(),
                    "Livres de " + auteur.getNom(), JOptionPane.INFORMATION_MESSAGE);
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

    public LivresButtonEditor getLivresButtonEditor() {
        return new LivresButtonEditor(auteurView.getTable(), row -> {
            Auteur auteur = auteurView.getAuteurAtRow(row);
            if (auteur != null) {
                ArrayList<Livre> livres = GestionAuteur.getBooksByAuthor(auteur.getId());

                JDialog livresDialog = new JDialog(
                        (JFrame)SwingUtilities.getWindowAncestor(auteurView),
                        "Livres de " + auteur.getNom(),
                        true
                );

                // Main content
                AuteurLivresView livresView = new AuteurLivresView(livres);

                // Header with author info
                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                headerPanel.setBackground(new Color(0x2D2D2D));

                JLabel authorLabel = new JLabel(auteur.getNom() + " (" + auteur.getNationalite() + ")");
                authorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                authorLabel.setForeground(new Color(0xAEAEAE));
                headerPanel.add(authorLabel, BorderLayout.WEST);

                JLabel countLabel = new JLabel(livres.size() + " livre(s)");
                countLabel.setForeground(new Color(0xAEAEAE));
                headerPanel.add(countLabel, BorderLayout.EAST);

                // Footer with close button
                JPanel footerPanel = new JPanel();
                footerPanel.setBackground(new Color(0x2D2D2D));
                JButton closeButton = new JButton("Fermer");
                closeButton.addActionListener(e -> livresDialog.dispose());
                footerPanel.add(closeButton);

                // Layout
                livresDialog.setLayout(new BorderLayout());
                livresDialog.add(headerPanel, BorderLayout.NORTH);
                livresDialog.add(livresView, BorderLayout.CENTER);
                livresDialog.add(footerPanel, BorderLayout.SOUTH);

                livresDialog.pack();
                livresDialog.setSize(600, 500);
                livresDialog.setLocationRelativeTo(auteurView);
                livresDialog.setVisible(true);
            }
        });
    }
}