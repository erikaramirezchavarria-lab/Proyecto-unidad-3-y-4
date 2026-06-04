/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package controladores;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vistas.LoginView;

/**
 *
 * @author Gilberto
 */
public class LoginController {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());  // Tema oscuro (recomendado)
            UIManager.put("Button.arc", 12);              // botones más redondeados
            UIManager.put("Component.arc", 10);           // componentes en general
            UIManager.put("TextComponent.arc", 10);       // campos de texto redondeados

            // Color de acento más bonito (azul brillante moderno)
            UIManager.put("Component.accentColor", new java.awt.Color(59, 130, 246)); // azul #3b82f6

            // Otras opciones:
            // UIManager.setLookAndFeel(new FlatLightLaf());
            // UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception ex) {
            System.err.println("Error al aplicar FlatLaf: " + ex.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
        
    }
}