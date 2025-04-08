package View;

import Utiles.Interfaces.LivresButtonHandler;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LivresButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton button;
    private int currentRow;
    private JTable table;
    private LivresButtonHandler handler;

    public LivresButtonEditor(JTable table, LivresButtonHandler handler) {
        this.table = table;
        this.handler = handler;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setBackground(new Color(0x3A3A3A));

        button = new JButton("Livres");
        styleButton(button);

        button.addActionListener(e -> {
            fireEditingStopped();
            if (handler != null && currentRow >= 0) {
                handler.onShowLivres(table.convertRowIndexToModel(currentRow));
            }
        });

        panel.add(button);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(new Color(0x4CAF50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x388E3C), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_ROUND_RECT);

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0x43A047));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0x4CAF50));
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.currentRow = row;
        if (isSelected) {
            button.setBackground(new Color(0x388E3C));
        } else {
            button.setBackground(new Color(0x4CAF50));
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}