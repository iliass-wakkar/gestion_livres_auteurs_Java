package View;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton editButton;
    private JButton deleteButton;

    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        setOpaque(true);

        editButton = new JButton("Éditer");
        deleteButton = new JButton("Supprimer");

        styleButton(editButton, new Color(0x0096C7));
        styleButton(deleteButton, new Color(0xC70000));

        add(editButton);
        add(deleteButton);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (isSelected) {
            setBackground(new Color(0x5A5A5A));
        } else {
            setBackground(row % 2 == 0 ? new Color(0x3A3A3A) : new Color(0x333333));
        }
        return this;
    }
}