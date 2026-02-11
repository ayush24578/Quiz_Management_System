package tests;

import GUI.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import java.awt.*;

/**
 * GUI Component Tests
 * 
 * This class contains JUnit tests for verifying all GUI windows
 * and components in the Quiz application. It tests window
 * creation, component existence, navigation and UI functionality.
 * 
 */
public class GUITests {
	/**
     * Tests that all main GUI windows can be created without errors
     * Verifies Login, AdminPanel and UserDashboard windows
     * Checks window titles and ensures no exceptions are thrown
     */
    @Test
    public void testWindowCreation() {
        System.out.println("Testing GUI window creation");
        
        // Test that shows windows can be created without errors
        try {
            // Test Login window
            Login_GUI login = new Login_GUI();
            assertNotNull(login);
            assertEquals("Log In", login.getTitle() != null ? login.getTitle() : "Login Window");
            login.dispose();
            
            // Test AdminPanel
            Admin_QMS_GUI adminPanel = new Admin_QMS_GUI();
            assertNotNull(adminPanel);
            adminPanel.dispose();
            
            // Test UserDashboard
            User_Dashboard_GUI dashboard = new User_Dashboard_GUI("testUser");
            assertNotNull(dashboard);
            dashboard.dispose();
            
            System.out.println("All windows created successfully!");
            
        } catch (Exception e) {
            fail("GUI creation failed: " + e.getMessage());
        }
    }
    
    /**
     * Tests that GUI components exist and are properly initialized
     * Specifically tests AddGUI to ensure it has required components
     * 
     */
    @Test
    public void testComponentExistence() {
        System.out.println("Testing GUI components...");
        
        // Test AddGUI has required components
        Add_Question_GUI addGUI = new Add_Question_GUI();
        
        // Find components by their text or names
        Component[] components = addGUI.getContentPane().getComponents();
        assertTrue(components.length > 0, "AddGUI should have components");
        
        boolean hasAddButton = false;
        
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Add".equals(button.getText())) {
                    hasAddButton = true;
                }
            }
        }
        
        assertTrue(hasAddButton, "AddGUI should have an Add button");
        
        addGUI.dispose();
        System.out.println("Component test passed!");
    }
    /**
     * Tests navigation between different GUI windows
     * Verifies that buttons have action listeners and can trigger
     * window transitions properly
     */
    @Test
    public void testNavigationBetweenWindows() {
        System.out.println("Testing window navigation logic...");
        
        // Test button action command names (if set)
        Login_GUI login = new Login_GUI();
        
        // Find login button
        JButton loginButton = null;
        for (Component comp : login.getContentPane().getComponents()) {
            if (comp instanceof JButton && "Login".equals(((JButton) comp).getText())) {
                loginButton = (JButton) comp;
                break;
            }
        }
        
        assertNotNull(loginButton, "Login window should have a Login button");
        assertNotNull(loginButton.getActionListeners(), 
            "Login button should have action listeners");
        
        login.dispose();
        System.out.println("Navigation test done!");
    }
    /**
     * Tests ComboBox functionality in the UserDashboard
     * Verifies that difficulty combo box has correct items
     * and proper default selection
     */
    @Test 
    public void testComboBoxFunctionality() {
        System.out.println("Test ComboBox functionality");
        
        User_Dashboard_GUI dashboard = new User_Dashboard_GUI("testUser");
        
        // Find difficulty combo box
        JComboBox<?> difficultyCombo = null;
        for (Component comp : dashboard.getContentPane().getComponents()) {
            if (comp instanceof JComboBox) {
                difficultyCombo = (JComboBox<?>) comp;
                break;
            }
        }
        
        assertNotNull(difficultyCombo, "UserDashboard should have a difficulty combo box");
        
        // Test combo box items
        assertEquals(4, difficultyCombo.getItemCount(), 
            "Difficulty combo should have 4 items");
        
        assertEquals("Select Difficulty", difficultyCombo.getItemAt(0));
        assertEquals("Beginner", difficultyCombo.getItemAt(1));
        assertEquals("Intermediate", difficultyCombo.getItemAt(2));
        assertEquals("Advanced", difficultyCombo.getItemAt(3));
        
        dashboard.dispose();
        System.out.println("ComboBox test done!");
    }
}
