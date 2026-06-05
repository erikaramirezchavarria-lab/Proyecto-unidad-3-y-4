package vistas;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import utilerias.Roles;
import utilerias.Sesion;
import javax.swing.ImageIcon;

public class MenuPrincipalForm extends JFrame {
    private JPanel panelContenido;
    private JButton btnUsuarios;
    private JButton btnClientes;
    private JButton btnHabitaciones;
    private JButton btnReservaciones;
    private JButton btnServicios;
    private JButton btnPagos;
    private JButton btnSalir;
    private final String rolUsuario;

    private final Color azul = new Color(0, 51, 102);
    private final Color azulSeleccion = new Color(0, 75, 150);
    private final Color dorado = new Color(255, 215, 0);
    private final Color texto = new Color(240, 240, 245);

    public MenuPrincipalForm() {
        this("admin");
    }

    public MenuPrincipalForm(String rol) {
        this.rolUsuario = rol == null ? "" : rol.toLowerCase();
        setTitle("KING HOTEL - " + this.rolUsuario);
        setSize(1280, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirMenu();
        construirInicio();
        configurarEventos();
        aplicarPermisosMenu();
    }

    private void construirMenu() {
        JPanel panelLateral = new JPanel();
        panelLateral.setBackground(azul);
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0, 35, 70)));
        panelLateral.setPreferredSize(new Dimension(250, 760));
        
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelLogo.setOpaque(false);

        ImageIcon iconoOriginal = new ImageIcon(
                getClass().getResource("/icons/King Hotel (2).png")
        );
        
        JLabel lblLogo = new JLabel(iconoOriginal);

        panelLogo.add(lblLogo);
        panelLateral.add(panelLogo);
        
        
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(215, 2));
        panelLateral.add(separador);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 18)));

        btnUsuarios = crearBotonMenu("Usuarios");
        btnClientes = crearBotonMenu("Huespedes");
        btnHabitaciones = crearBotonMenu("Habitaciones");
        btnReservaciones = crearBotonMenu("Reservas");
        btnServicios = crearBotonMenu("Servicios");
        btnPagos = crearBotonMenu("Pagos");
        btnSalir = crearBotonMenu("Cerrar sesion");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelLateral.add(btnUsuarios);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
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
        panelLateral.add(Box.createRigidArea(new Dimension(0, 22)));
        add(panelLateral, BorderLayout.WEST);
    }

    private void construirInicio() {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(250,248,240));

        String usuario = Sesion.usuarioActivo == null ? "sin sesion" : Sesion.usuarioActivo;
        String rol = Sesion.rolActivo == null ? rolUsuario : Sesion.rolActivo;

        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(255, 255, 230));
        barraSuperior.setPreferredSize(new Dimension(0, 55));
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 30));

        JLabel lblUsuario = new JLabel("Usuario: " + usuario + "   |   Rol: " + rolUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(new Color(0, 51, 102));
        lblUsuario.setHorizontalAlignment(SwingConstants.RIGHT);

        barraSuperior.add(lblUsuario, BorderLayout.EAST);
        
     JLabel bienvenida = new JLabel(
        "<html><center>"
        + "<h1 style='color:#003366;'>BIENVENIDO A KING HOTEL</h1>"
        + "<p style='color:#D4AF37;font-size:18px;'>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</p>"
        + "</center></html>",
        SwingConstants.CENTER
);
        
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));

        panelContenido.add(barraSuperior, BorderLayout.NORTH);
        panelContenido.add(bienvenida, BorderLayout.CENTER);

        add(panelContenido, BorderLayout.CENTER);
}
    
    private void seleccionarBoton(JButton botonSeleccionado) {
        JButton[] botones = {
            btnUsuarios, btnClientes, btnHabitaciones,
            btnReservaciones, btnServicios, btnPagos
        };

        for (JButton b : botones) {
            b.setContentAreaFilled(false);
            b.setBackground(azul);
            b.setForeground(texto);
        }

        botonSeleccionado.setContentAreaFilled(true);
        botonSeleccionado.setBackground(azulSeleccion);
        botonSeleccionado.setForeground(Color.WHITE);
    }

    private JButton crearBotonMenu(String textoBoton) {
        JButton boton = new JButton(textoBoton);
        boton.setMaximumSize(new Dimension(220, 44));
        boton.setPreferredSize(new Dimension(220, 44));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setForeground(texto);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(true);
                boton.setBackground(azulSeleccion);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setContentAreaFilled(false);
            }
        });
        return boton;
    }

    private void configurarEventos() {
        btnUsuarios.addActionListener(e -> {
            seleccionarBoton(btnUsuarios);
            cambiarPanel(new PanelUsuarios(rolUsuario));
        });

        btnClientes.addActionListener(e -> {
            seleccionarBoton(btnClientes);
            cambiarPanel(new PanelClientes(rolUsuario));
        });

        btnHabitaciones.addActionListener(e -> {
            seleccionarBoton(btnHabitaciones);
            cambiarPanel(new PanelHabitaciones(rolUsuario));
        });

        btnReservaciones.addActionListener(e -> {
            seleccionarBoton(btnReservaciones);
            cambiarPanel(new PanelReservaciones(rolUsuario));
        });

        btnServicios.addActionListener(e -> {
            seleccionarBoton(btnServicios);
            cambiarPanel(new PanelServicios(rolUsuario));
        });

        btnPagos.addActionListener(e -> {
            seleccionarBoton(btnPagos);
            cambiarPanel(new PanelPagos(rolUsuario));
        });

        btnSalir.addActionListener(e -> cerrarSesion());
    }


    private void aplicarPermisosMenu() {
        btnUsuarios.setVisible(Roles.esAdmin(rolUsuario));
    }

    private void cerrarSesion() {
        int r = JOptionPane.showConfirmDialog(this, "Seguro que desea cerrar sesion?", "Cerrar sesion",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            Sesion.usuarioActivo = null;
            Sesion.rolActivo = null;
            dispose();
            new LoginView().setVisible(true);
        }
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
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar FlatLaf: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(() -> new MenuPrincipalForm().setVisible(true));
    }
}
