package controladores;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vistas.LoginView;
import com.formdev.flatlaf.FlatLightLaf; 

public class LoginController {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Component.accentColor", new java.awt.Color(59, 130, 246));
        } catch (Exception ex) {
            System.err.println("Error al aplicar FlatLaf: " + ex.getMessage());
        }
        
        try {
            // Inicializar FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Fallo al inicializar FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}
