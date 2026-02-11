package GUI;

import java.awt.Color;
import java.awt.Font;

/**
 * Centeralized style configuration for the Quiz Management System.
 * Defines standard fonts and colors for a consistent, modern UI.
 */
public class StyleConfig {
    
    // Fonts
    public static final String PRIMARY_FONT_NAME = "Segoe UI";
    public static final Font TITLE_FONT = new Font(PRIMARY_FONT_NAME, Font.BOLD, 22);
    public static final Font HEADER_FONT = new Font(PRIMARY_FONT_NAME, Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font(PRIMARY_FONT_NAME, Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font(PRIMARY_FONT_NAME, Font.BOLD, 14);
    
    // Colors
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219);    // Soft Blue
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);    // Emerald Green
    public static final Color DANGER_COLOR = new Color(231, 76, 60);     // Alizarin Red
    public static final Color BG_COLOR = new Color(245, 246, 250);         // Light Gray/White
    public static final Color TEXT_COLOR = new Color(47, 54, 64);          // Dark Gray
    public static final Color WHITE = Color.WHITE;
    
    // UI Helpers
    public static void applyPanelStyle(javax.swing.JPanel panel) {
        panel.setBackground(BG_COLOR);
    }
}
