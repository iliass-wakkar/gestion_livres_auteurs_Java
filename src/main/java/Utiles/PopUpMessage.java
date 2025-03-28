package Utiles;

import javax.swing.*;
import java.awt.*;

public class PopUpMessage {
    // Constructor
    public PopUpMessage(String type, String message) {
        // Normalize the type to lowercase for consistency
        type = type.toLowerCase();

        // Create the dialog
        JDialog dialog = new JDialog((JFrame) null, getTitle(type), true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null); // Center the dialog

        // Panel for the content
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(getBackgroundColor(type));

        // Icon label
        JLabel iconLabel = new JLabel(getIcon(type));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(iconLabel, BorderLayout.WEST);

        // Message label
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(getForegroundColor(type));
        panel.add(messageLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(getButtonColor(type));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(okButton);

        // Add components to the dialog
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show the dialog
        dialog.setVisible(true);
    }

    // Helper method to get the dialog title based on the message type
    private String getTitle(String type) {
        return switch (type.toLowerCase()) {
            case "success" -> "Success";
            case "info" -> "Information";
            case "warning" -> "Warning";
            case "error" -> "Error";
            default -> "Message";
        };
    }

    // Helper method to get the background color based on the message type
    private Color getBackgroundColor(String type) {
        return switch (type.toLowerCase()) {
            case "success" -> new Color(225, 255, 225); // Light green
            case "info" -> new Color(225, 243, 255); // Light blue
            case "warning" -> new Color(255, 243, 225); // Light yellow
            case "error" -> new Color(255, 225, 225); // Light red
            default -> Color.WHITE;
        };
    }

    // Helper method to get the foreground (text) color based on the message type
    private Color getForegroundColor(String type) {
        return switch (type.toLowerCase()) {
            case "success" -> new Color(0, 128, 0); // Green
            case "info" -> new Color(0, 120, 212); // Blue
            case "warning" -> new Color(212, 140, 0); // Orange
            case "error" -> new Color(212, 0, 0); // Red
            default -> Color.BLACK;
        };
    }

    // Helper method to get the button background color based on the message type
    private Color getButtonColor(String type) {
        return switch (type.toLowerCase()) {
            case "success" -> new Color(0, 128, 0); // Green
            case "info" -> new Color(0, 120, 212); // Blue
            case "warning" -> new Color(212, 140, 0); // Orange
            case "error" -> new Color(212, 0, 0); // Red
            default -> Color.GRAY;
        };
    }

    // Helper method to get the icon based on the message type
    private Icon getIcon(String type) {
        return switch (type.toLowerCase()) {
            case "success" -> UIManager.getIcon("OptionPane.informationIcon"); // Success icon
            case "info" -> UIManager.getIcon("OptionPane.informationIcon"); // Info icon
            case "warning" -> UIManager.getIcon("OptionPane.warningIcon"); // Warning icon
            case "error" -> UIManager.getIcon("OptionPane.errorIcon"); // Error icon
            default -> null;
        };
    }
    public static void main(String[] args) {
        // Success dialog
        new PopUpMessage("success", "Operation completed successfully!");

        // Error dialog
        new PopUpMessage("error", "An unexpected error occurred.");

        // Warning dialog
        new PopUpMessage("warning", "This action cannot be undone.");

        // Info dialog
        new PopUpMessage("info", "This is an informational message.");
    }
}