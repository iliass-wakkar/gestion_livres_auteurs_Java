package View;

import Utiles.Interfaces.IButtonEditorEventsHandler;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private int currentRow;
    private JTable table;
    private IButtonEditorEventsHandler eventsHandler;

    public ButtonEditor(JTable table, IButtonEditorEventsHandler eventsHandler) {
        this.table = table;
        this.eventsHandler = eventsHandler;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(new Color(0x3A3A3A));

        editButton = new JButton("Éditer");
        deleteButton = new JButton("Supprimer");

        styleButton(editButton, new Color(0x0096C7));
        styleButton(deleteButton, new Color(0xC70000));

        editButton.addActionListener(e -> {
            fireEditingStopped();
            if (eventsHandler != null && currentRow >= 0) {
                eventsHandler.onEdit(table.convertRowIndexToModel(currentRow));
            }
        });

        deleteButton.addActionListener(e -> {
            fireEditingStopped();
            if (eventsHandler != null && currentRow >= 0) {
                eventsHandler.onDelete(table.convertRowIndexToModel(currentRow));
            }
        });

        panel.add(editButton);
        panel.add(deleteButton);
    }

    public JButton getEditButton() {
        return editButton;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.currentRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}