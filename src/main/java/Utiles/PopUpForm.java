package Utiles;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.BiConsumer;

public class PopUpForm<T> extends JDialog {
    private JButton saveButton;
    private Function<Map<String, String>, T> creator;
    private BiConsumer<T, Map<String, String>> updater;
    private T existingObject;
    private boolean isEditMode;
    private Function<T, Map<String, String>> fieldExtractor;

    private Map<String, String> formData = new HashMap<>();
    private Map<String, JTextField> fields = new HashMap<>();

    // Constructor for inserting new objects
    public PopUpForm(String title, String[] fieldNames,
                     Function<Map<String, String>, T> creator) {
        this(title, fieldNames, null, creator, null, null);
    }

    // Constructor for editing existing objects
    public PopUpForm(String title, String[] fieldNames,
                     T existingObject,
                     BiConsumer<T, Map<String, String>> updater,
                     Function<T, Map<String, String>> fieldExtractor) {
        this(title, fieldNames, existingObject, null, updater, fieldExtractor);
    }

    // Main private constructor
    private PopUpForm(String title, String[] fieldNames,
                      T existingObject,
                      Function<Map<String, String>, T> creator,
                      BiConsumer<T, Map<String, String>> updater,
                      Function<T, Map<String, String>> fieldExtractor) {
        super((JFrame) null, title, true);
        this.creator = creator;
        this.updater = updater;
        this.existingObject = existingObject;
        this.isEditMode = existingObject != null;
        this.fieldExtractor = fieldExtractor;

        Map<String, String> initialValues = isEditMode ?
                fieldExtractor.apply(existingObject) : null;
        setUpUI(fieldNames, initialValues);
    }

    public void setFieldValue(String fieldName, String value) {
        if (fields.containsKey(fieldName)) {
            fields.get(fieldName).setText(value);
        }
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public T getObject() {
        collectFormData();
        if (isEditMode) {
            updater.accept(existingObject, formData);
            return existingObject;
        } else {
            return creator.apply(formData);
        }
    }

    private void collectFormData() {
        formData.clear();
        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
            formData.put(entry.getKey(), entry.getValue().getText());
        }
    }

    private void setUpUI(String[] fieldNames, Map<String, String> initialValues) {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x2D2D2D));

        JPanel formPanel = new JPanel(new GridLayout(fieldNames.length + 1, 2, 10, 10));
        formPanel.setBackground(new Color(0x2D2D2D));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (String fieldName : fieldNames) {
            JLabel label = new JLabel(fieldName + ":");
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(new Color(0xAEAEAE));
            formPanel.add(label);

            JTextField textField = new JTextField();
            textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textField.setForeground(new Color(0xAEAEAE));
            textField.setBackground(new Color(0x3A3A3A));
            textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x4A4A4A), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            if (initialValues != null && initialValues.containsKey(fieldName)) {
                textField.setText(initialValues.get(fieldName));
            }

            fields.put(fieldName, textField);
            formPanel.add(textField);
        }

        saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");

        // ... (keep your existing button styling code)

        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(0x2D2D2D));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean validateForm() {
        for (JTextField field : fields.values()) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}