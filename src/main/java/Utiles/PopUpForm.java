package Utiles;

import Modules.Livre;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.BiConsumer;

public class PopUpForm<T> extends JDialog {
    private JButton saveButton;
    private Function<Map<String, String>, T> creator; // Not final
    private BiConsumer<T, Map<String, String>> updater; // Not final
    private T existingObject;
    private boolean isEditMode;

    private Map<String, String> formData = new HashMap<>();
    private Map<String, JTextField> fields = new HashMap<>();

    // Constructor for inserting new objects
    public PopUpForm(String title, String[] fieldNames, Function<Map<String, String>, T> creator) {
        super((JFrame) null, title, true);
        this.creator = creator;
        this.isEditMode = false;
        this.existingObject = null;
        this.updater = (obj, data) -> {}; // Dummy no-op for insert mode
        setUpUI(fieldNames, null);
    }

    public void setFieldValue(String fieldName, String value) {
        if (fields.containsKey(fieldName)) {
            fields.get(fieldName).setText(value);
        }
    }
    // Constructor for editing existing objects
    public PopUpForm(String title, String[] fieldNames, T existingObject, BiConsumer<T, Map<String, String>> updater) {
        super((JFrame) null, title, true);
        this.creator = null;
        this.isEditMode = true;
        this.existingObject = existingObject;
        this.updater = updater;

        Object[] data = extractFieldValues(existingObject, fieldNames);
        setUpUI(fieldNames, data);
    }

    //function to extract fields
    private Object[] extractFieldValues(T object, String[] fieldNames) {
        if (object instanceof Livre) {
            Livre livre = (Livre) object;
            Object[] values = new Object[fieldNames.length];
            for (int i = 0; i < fieldNames.length; i++) {
                switch (fieldNames[i]) {
                    case "Titre":
                        values[i] = livre.getTitre();
                        break;
                    case "ID Auteur":
                        values[i] = livre.getId_auteur();
                        break;
                    default:
                        values[i] = "";
                }
            }
            return values;
        }
        throw new IllegalArgumentException("Unsupported object type");
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    // Get the created/updated object
    public T getObject() {
        collectFormData();
        if (isEditMode) {
            updater.accept(existingObject, formData); // Update existing object
            return existingObject;
        } else {
            return creator.apply(formData); // Create new object
        }
    }

    // Collect data from text fields into the formData map
    private void collectFormData() {
        formData.clear();
        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue().getText();
            formData.put(fieldName, fieldValue);
        }
    }

    // Common UI setup method
    private void setUpUI(String[] fieldNames, Object[] data) {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x2D2D2D));

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(fieldNames.length + 1, 2, 10, 10));
        formPanel.setBackground(new Color(0x2D2D2D));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add fields dynamically
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];

            // Label
            JLabel label = new JLabel(fieldName + ":");
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(new Color(0xAEAEAE));
            formPanel.add(label);

            // Text field
            JTextField textField = new JTextField();
            textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textField.setForeground(new Color(0xAEAEAE));
            textField.setBackground(new Color(0x3A3A3A));
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x4A4A4A), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            textField.setPreferredSize(new Dimension(200, 30));

            // Pre-fill data if in edit mode
            if (data != null && i < data.length && data[i] != null) {
                textField.setText(data[i].toString());
            }

            fields.put(fieldName, textField);
            formPanel.add(textField);
        }

        // Buttons
        saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(0x0096C7));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setPreferredSize(new Dimension(120, 30));
        saveButton.putClientProperty("JButton.buttonType", "none");

        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(new Color(0xFF5252));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(120, 30));

        cancelButton.addActionListener(e -> dispose());

        // Centered button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(0x2D2D2D));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Validates the form
    public boolean validateForm() {
        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
            String fieldValue = entry.getValue().getText().trim();
            if (fieldValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

//    // Example usage for Livre
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            // For inserting a new Livre
//            PopUpForm<Livre> addForm = new PopUpForm<>(
//                    "Ajouter Livre",
//                    new String[]{"Titre", "Auteur"},
//                    data -> new Livre(data.get("Titre"), data.get("Auteur"))
//            );
//            Livre newLivre = addForm.getObject();
//            System.out.println("Nouveau livre: " + newLivre);
//
//            // For editing an existing Livre
//            Livre existingLivre = new Livre("Les Misérables", 2);
//            PopUpForm<Livre> editForm = new PopUpForm<>(
//                    "Modifier Livre",
//                    new String[]{"Titre", "Auteur"},
//                    existingLivre,
//                    (livre, data) -> {
//                        livre.setTitre(data.get("Titre"));
//                        livre.setAuteur(data.get("Auteur"));
//                    }
//            );
//            Livre updatedLivre = editForm.getObject();
//            System.out.println("Livre modifié: " + updatedLivre);
//        });
//    }
}
