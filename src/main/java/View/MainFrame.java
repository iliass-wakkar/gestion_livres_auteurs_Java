package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

import Controleur.GestionLivre;
import Controleur.GestionLivreView;
import Modules.Livre;
import View.AuteurView.AuteurView;
import View.LivreView.LivreView;
import com.formdev.flatlaf.FlatDarkLaf;

public class MainFrame extends JFrame {
    private AuteurView auteurView = new AuteurView();
    private ArrayList<Livre> livreList = GestionLivre.readAllLivres();
    private LivreView livreView = new LivreView(livreList);
    private GestionLivreView gestionLivreView = new GestionLivreView(livreView);

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        // Set dark theme
        FlatDarkLaf.setup();
        UIManager.put("TabbedPane.selectedBackground", new Color(0x2A2A2A));
        UIManager.put("TabbedPane.hoverColor", new Color(0x4A4A4A));
        UIManager.put("Button.foreground", Color.WHITE);

        setTitle("Gestion Bibliothèque");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0x1E1E1E));

        // Create tabbed pane with modern styling
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabbedPane.setBackground(new Color(0x2D2D2D));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add tabs with icons
        tabbedPane.addTab("Auteurs", createTabIcon("👤"), auteurView);
        tabbedPane.addTab("Livres", createTabIcon("📚"), livreView);

        // Add tabbed pane to frame
        add(tabbedPane, BorderLayout.CENTER);

        // Add some padding
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0x2D2D2D));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0xAEAEAE));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0x5A5A5A)));

        panel.add(titleLabel, BorderLayout.NORTH);

        // Add temporary content
        JLabel tempContent = new JLabel("Contenu à ajouter...");
        tempContent.setForeground(new Color(0x8E8E8E));
        tempContent.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        tempContent.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(tempContent, BorderLayout.CENTER);

        return panel;
    }

    private Icon createTabIcon(String emoji) {
        // Use a JLabel to render the emoji
        JLabel label = new JLabel(emoji);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24)); // Ensure emoji font is used
        label.setForeground(new Color(0x0096C7)); // Cyan accent

        // Return a custom Icon implementation
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                // Paint the label at the specified position
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.translate(x, y);
                label.setBounds(0, 0, getIconWidth(), getIconHeight());
                label.paint(g2d);
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 32; // Fixed width for the icon
            }

            @Override
            public int getIconHeight() {
                return 32; // Fixed height for the icon
            }
        };
    }


}