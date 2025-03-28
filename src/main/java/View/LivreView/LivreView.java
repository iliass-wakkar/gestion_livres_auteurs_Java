package View.LivreView;

import Controleur.ButtonEditorController;
import View.ButtonEditor;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import Modules.Livre;
import View.ButtonRenderer;
import com.formdev.flatlaf.FlatClientProperties;

public class LivreView extends JPanel {
    private JButton addButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private ButtonEditorController buttonEditorController;

    public LivreView(ArrayList<Livre> livreList) {
        buttonEditorController = new ButtonEditorController(this);
        initializeUI(livreList);
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JTable getTable() {
        return table;
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
        addButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER);

        // Create table model with columns
        String[] columns = {"ID", "Titre", "ID Auteur", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only Actions column is editable
            }
        };

        // Create table with model
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

        // Configure Actions column
        TableColumn actionsColumn = table.getColumnModel().getColumn(3);
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(buttonEditorController.getButtonEditor());

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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(0x2D2D2D));
        add(scrollPane, BorderLayout.CENTER);

        populateTable(livreList);
    }

    public void populateTable(ArrayList<Livre> livreList) {
        tableModel.setRowCount(0);
        for (Livre livre : livreList) {
            Object[] rowData = {
                    livre.getId(),
                    livre.getTitre(),
                    livre.getId_auteur(),
                    "" // Empty string for Actions column
            };
            tableModel.addRow(rowData);
        }
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBackground(new Color(0x2A2A2A));
            setForeground(new Color(0xAEAEAE));
        }
    }

    public Livre getLivreAtRow(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return null;
        }
        return new Livre(
                (int) tableModel.getValueAt(row, 0),
                (String) tableModel.getValueAt(row, 1),
                (int) tableModel.getValueAt(row, 2)
        );
    }
}