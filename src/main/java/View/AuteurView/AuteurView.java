package View.AuteurView;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import com.formdev.flatlaf.FlatClientProperties;

public class AuteurView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public AuteurView() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0x2D2D2D));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x2D2D2D));

        JLabel titleLabel = new JLabel("Liste des Auteurs");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0xAEAEAE));

        JButton addButton = new JButton("Ajouter Auteur");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setBackground(new Color(0x0096C7));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        addButton.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        // Add spacing between header and table
        add(headerPanel, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER); // Add spacing

        // Table Setup
        String[] columns = {"ID", "Nom", "Nationalité", "Actions", "Détails"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(0x3A3A3A) : new Color(0x333333));
                }
                return c;
            }
        };

        // Custom renderers
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new DetailsRenderer());

        // Table styling
        table.setBackground(new Color(0x2D2D2D));
        table.setForeground(new Color(0xAEAEAE));
        table.setGridColor(new Color(0x4A4A4A)); // Set grid line color
        table.setShowGrid(true); // Show grid lines
        table.setIntercellSpacing(new Dimension(1, 1)); // Add spacing between cells
        table.setSelectionBackground(new Color(0x5A5A5A));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(40); // Slightly reduce row height
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.getTableHeader().setBackground(new Color(0x2A2A2A));
        table.getTableHeader().setForeground(new Color(0xAEAEAE));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add MouseListener to handle button clicks
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 3) { // Actions column
                    Rectangle cellRect = table.getCellRect(row, col, false);
                    if (cellRect.contains(e.getPoint())) {
                        // Determine which button was clicked
                        int buttonWidth = cellRect.width / 2;
                        int clickX = e.getX() - cellRect.x;

                        if (clickX < buttonWidth) {
                            // Éditer button clicked
                            JOptionPane.showMessageDialog(AuteurView.this,
                                    "Éditer l'auteur ID: " + tableModel.getValueAt(row, 0),
                                    "Éditer", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // Supprimer button clicked
                            int confirm = JOptionPane.showConfirmDialog(AuteurView.this,
                                    "Voulez-vous supprimer l'auteur ID: " + tableModel.getValueAt(row, 0) + "?",
                                    "Supprimer", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                tableModel.removeRow(row);
                            }
                        }
                    }
                } else if (row >= 0 && col == 4) { // Détails column
                    JOptionPane.showMessageDialog(AuteurView.this,
                            "Détails de l'auteur ID: " + tableModel.getValueAt(row, 0),
                            "Détails", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(0x2D2D2D));
        add(scrollPane, BorderLayout.CENTER);

        // Sample data (replace with real data)
        addSampleData();
    }

    private void addSampleData() {
        tableModel.addRow(new Object[]{1, "Victor Hugo", "Français", "", ""});
        tableModel.addRow(new Object[]{2, "Jane Austen", "Anglaise", "", ""});
        tableModel.addRow(new Object[]{3, "Gabriel García Márquez", "Colombien", "", ""});
    }

    // Custom header renderer
    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBackground(new Color(0x2A2A2A));
            setForeground(new Color(0xAEAEAE));
        }
    }

    // Action buttons renderer
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new GridLayout(1, 2, 5, 0)); // Two buttons side by side with spacing
            setBackground(new Color(0x2D2D2D));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll(); // Clear previous components

            // Edit Button
            JButton editButton = new JButton("Éditer");
            editButton.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Larger font
            editButton.setBackground(new Color(0x0096C7));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8)); // Adjust padding
            editButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6"); // Corner radius

            // Delete Button
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Larger font
            deleteButton.setBackground(new Color(0xFF5252)); // Red color
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8)); // Adjust padding
            deleteButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6"); // Corner radius

            add(editButton);
            add(deleteButton);
            return this;
        }
    }

    // Details column renderer
    private class DetailsRenderer extends JLabel implements TableCellRenderer {
        public DetailsRenderer() {
            setForeground(new Color(0x0096C7));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("<html><u>Voir détails</u></html>");
            setToolTipText("Afficher les détails de cet auteur");
            return this;
        }
    }
}