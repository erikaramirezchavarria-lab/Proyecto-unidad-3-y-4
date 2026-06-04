package vistas;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipalForm extends JFrame {

    private JPanel panelLateral, panelContenido;
    private JButton btnClientes, btnHabitaciones, btnReservaciones, btnServicios, btnPagos, btnSalir;
    private String rolUsuario; 

    // Paleta de colores Premium de "King Hotel" basada en tu Login
    private final Color AZUL_MAJESTIC = new Color(0, 51, 102);  // El azul oscuro de tus botones
    private final Color AZUL_SELECCION = new Color(0, 75, 150); // Variante para el efecto hover
    private final Color DORADO_CORONA = new Color(255, 215, 0);   // El tono del detalle superior
    private final Color BLANCO_PURO = new Color(255, 255, 255);
    private final Color TEXTO_HUESO = new Color(240, 240, 245);

    public MenuPrincipalForm() {
        this("Administrador"); 
    }

    public MenuPrincipalForm(String rol) {
        this.rolUsuario = rol;

        // Título actualizado con la identidad de tu marca
        setTitle("KING HOTEL - Sistema de Gestión de Alta Hostelería (" + rolUsuario.toUpperCase() + ")");
        setSize(1150, 720); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. SIDEBAR LATERAL (Rediseñado con el Azul Majestic del Login)
        panelLateral = new JPanel();
        panelLateral.setBackground(AZUL_MAJESTIC); 
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0, 35, 70)));
        
        panelLateral.setPreferredSize(new Dimension(270, 720)); // Un pelín más ancho para iconos estilizados
        panelLateral.setMinimumSize(new Dimension(270, 720));

        // CABECERA DEL LOGOTIPO EN EL SIDEBAR
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        panelLogo.setOpaque(false);
        
        // El logo con corona dorada y texto blanco que combina con tu cabecera amarilla/dorada
        JLabel lblLogo = new JLabel("👑   KING HOTEL");
        lblLogo.setFont(new Font("Roboto", Font.BOLD, 20));
        lblLogo.setForeground(DORADO_CORONA); 
        panelLogo.add(lblLogo);
        panelLateral.add(panelLogo);

        // Separador elegante debajo del logo corporativo
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(230, 2));
        separador.setForeground(new Color(0, 70, 140));
        separador.setBackground(new Color(0, 35, 70));
        panelLateral.add(separador);

        panelLateral.add(Box.createRigidArea(new Dimension(0, 25)));

        // Creación de botones estilizados con textos elegantes en blanco
        btnClientes = crearBotonMenu("👥   Gestión de Clientes");
        btnHabitaciones = crearBotonMenu("🛏️   Control de Habitaciones");
        btnReservaciones = crearBotonMenu("📅   Reservaciones de Hotel");
        btnServicios = crearBotonMenu("🛎️   Servicios Adicionales");
        btnPagos = crearBotonMenu("💳   Facturación y Pagos");
        
        // Botón salir destacado sutilmente
        btnSalir = crearBotonMenu("🚪   Cerrar Sesión");
        btnSalir.setFont(new Font("Roboto", Font.BOLD, 13));

        // Agregando componentes al Sidebar
        panelLateral.add(btnClientes);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        panelLateral.add(btnHabitaciones);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        panelLateral.add(btnReservaciones);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        panelLateral.add(btnServicios);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        panelLateral.add(btnPagos);
        
        panelLateral.add(Box.createVerticalGlue()); 
        panelLateral.add(btnSalir);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 25)));

        // 2. PANEL CENTRAL DE CONTENIDO
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(BLANCO_PURO);
        
        // Mensaje de bienvenida adaptado al nombre e identidad visual de la marca
        JLabel lblBienvenida = new JLabel("<html><center><h1 style='color:#003366; font-family:Roboto;'>¡Bienvenido a King Hotel System!</h1>"
                + "<p style='font-size:12px; color:#555555;'>Usted ha ingresado al ecosistema administrativo con el rol de: <b style='color:#003366;'>" + rolUsuario + "</b></p>"
                + "<br><br><p style='color:#888888; font-size:11px;'>Seleccione un módulo del panel ejecutivo izquierdo para iniciar sus operaciones.</p></center></html>", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelContenido.add(lblBienvenida, BorderLayout.CENTER);

        add(panelLateral, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        configurarEventos();

        panelLateral.revalidate();
        panelLateral.repaint();
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setMaximumSize(new Dimension(240, 45)); // Un poquito más altos para mejor área de clic
        boton.setPreferredSize(new Dimension(240, 45));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setFont(new Font("Roboto", Font.PLAIN, 14)); 
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Configuración de colores iniciales (texto claro sobre fondo oscuro)
        boton.setForeground(TEXTO_HUESO);

        // Efectos dinámicos "Hover" elegantes al pasar el mouse por encima
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(true);
                boton.setBackground(AZUL_SELECCION);
                boton.setForeground(BLANCO_PURO);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(false);
                boton.setForeground(TEXTO_HUESO);
            }
        });

        return boton;
    }

    private void configurarEventos() {
        btnClientes.addActionListener(e -> cambiarPanel(new PanelClientes(rolUsuario)));
        btnHabitaciones.addActionListener(e -> cambiarPanel(new PanelHabitaciones(rolUsuario)));
        btnReservaciones.addActionListener(e -> cambiarPanel(new PanelReservaciones(rolUsuario)));
        btnServicios.addActionListener(e -> cambiarPanel(new PanelServicios(rolUsuario)));
        btnPagos.addActionListener(e -> cambiarPanel(new PanelPagos(rolUsuario)));
        
        btnSalir.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cerrar sesión en King Hotel?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r == JOptionPane.YES_OPTION) {
                this.dispose();
                // 🔥 CORRECCIÓN: Quitamos el paso de parámetros para cumplir con las especificaciones de tu constructor vacío
                new controladores.LoginController(); 
            }
        });
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        panelContenido.removeAll();
        panelContenido.add(nuevoPanel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new MenuPrincipalForm().setVisible(true));
    }
}