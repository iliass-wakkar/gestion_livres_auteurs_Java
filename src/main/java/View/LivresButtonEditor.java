package View;

import Utiles.Interfaces.LivresButtonHandler;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LivresButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton button;
    private int currentRow;
    private JTable table;
    private LivresButtonHandler handler;

    public LivresButtonEditor(JTable table, LivresButtonHandler handler) {
        this.table = table;
        this.handler = handler;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(new Color(0x3A3A3A));

        button = new JButton("Livres");
        styleButton(button);

        button.addActionListener(this::handleButtonClick);

        panel.add(button);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0x4CAF50)); // Green color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
    }

    private void handleButtonClick(ActionEvent e) {
        fireEditingStopped();
        if (handler != null && currentRow >= 0) {
            handler.onShowLivres(table.convertRowIndexToModel(currentRow));
        }
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