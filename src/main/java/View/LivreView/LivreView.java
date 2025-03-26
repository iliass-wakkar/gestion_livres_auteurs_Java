package View.LivreView;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import Controleur.GestionLivreView;
import Modules.Livre;
import com.formdev.flatlaf.FlatClientProperties;

public class LivreView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private GestionLivreView gestionLivreView;
    private JButton addButton;

    public LivreView(ArrayList<Livre> livreList) {
        initializeUI(livreList);
    }

    public JButton getAddButton() {
        return addButton;
    }

    private void initializeUI(ArrayList<Livre> livreList) {
        setLayout(new BorderLayout());
        setBackground(new Color(0x2D2D2D));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x2D2D2D));

        JLabel titleLabel = new JLabel("Liste des Livres");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0xAEAEAE));

        addButton = new JButton("Ajouter Livre");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setBackground(new Color(0x0096C7));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        addButton.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER); // Add spacing

        // Table Setup
        String[] columns = {"ID", "Titre", "ID Auteur", "Actions"};
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

        // Custom renderer for Actions column
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());

        // Table styling
        table.setBackground(new Color(0x2D2D2D));
        table.setForeground(new Color(0xAEAEAE));
        table.setGridColor(new Color(0x4A4A4A));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(new Color(0x5A5A5A));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(40);
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.getTableHeader().setBackground(new Color(0x2A2A2A));
        table.getTableHeader().setForeground(new Color(0xAEAEAE));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add MouseListener for button clicks
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 3) { // Actions column
                    Rectangle cellRect = table.getCellRect(row, col, false);
                    int buttonWidth = cellRect.width / 2;
                    int clickX = e.getX() - cellRect.x;

                    if (clickX < buttonWidth) {
                        // Éditer clicked
                        JOptionPane.showMessageDialog(LivreView.this,
                                "Éditer le livre ID: " + tableModel.getValueAt(row, 0),
                                "Éditer", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Supprimer clicked
                        int confirm = JOptionPane.showConfirmDialog(LivreView.this,
                                "Supprimer le livre ID: " + tableModel.getValueAt(row, 0) + "?",
                                "Supprimer", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            tableModel.removeRow(row);
                        }
                    }
                }
            }
        });

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(0x2D2D2D));
        add(scrollPane, BorderLayout.CENTER);

        // Populate table with data from livreList
        populateTable(livreList);
    }

    public void populateTable(ArrayList<Livre> livreList) {
        if(livreList == null) {
            tableModel.addRow(new Object[]{0, "no content", 0, ""});

        }else {
            // Clear existing data
            tableModel.setRowCount(0);

            // Add data from the list
            for (Livre livre : livreList) {
                Object[] rowData = {
                        livre.getId(),
                        livre.getTitre(),
                        livre.getId_auteur(),
                        "" // Empty placeholder for buttons
                };
                tableModel.addRow(rowData);
            }
        }
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
            setLayout(new GridLayout(1, 2, 5, 0));
            setBackground(new Color(0x2D2D2D));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();

            // Éditer Button
            JButton editButton = new JButton("Éditer");
            editButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            editButton.setBackground(new Color(0x0096C7));
            editButton.setForeground(Color.WHITE);
            editButton.setFocusPainted(false);
            editButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            editButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6");

            // Supprimer Button
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            deleteButton.setBackground(new Color(0xFF5252));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            deleteButton.putClientProperty(FlatClientProperties.STYLE, "arc: 6");

            add(editButton);
            add(deleteButton);
            return this;
        }
    }
}