package Utiles;

import View.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Lancher {
    public static void main(String[] args) {
        // Initialize FlatLaf Dark theme
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
