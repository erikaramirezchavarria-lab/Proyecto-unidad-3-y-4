package vistas;

import dao.PagoDAO;
import modelos.Pago;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPagos extends JPanel {

    private JTextField txtIdReserva, txtIdHuesped, txtMontoHabitacion, txtMontoServicios, txtTotal;
    private JComboBox<String> cbMetodoPago;
    private JButton btnProcesarPago;
    private PagoDAO pagoDAO = new PagoDAO();
    private String rolUsuario; // Variable para el rol

    // Constructor vacío
    public PanelPagos() {
        this("Administrador");
    }

    // Constructor corregido que recibe el rol (esto soluciona el error de compilación)
    public PanelPagos(String rol) {
        this.rolUsuario = rol;
        inicializarComponentes();
        
        // Aplicar restricciones de rol si es necesario
        if (rol != null && rol.equalsIgnoreCase("Auditor")) {
            btnProcesarPago.setEnabled(false);
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Caja: Registro y Facturación de Pagos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelIzquierdo = new JPanel(new GridLayout(6, 2, 10, 10));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(380, 600));

        panelIzquierdo.add(new JLabel("ID de Reservación:"));
        txtIdReserva = new JTextField();
        panelIzquierdo.add(txtIdReserva);

        panelIzquierdo.add(new JLabel("ID de Huésped (Obligatorio):"));
        txtIdHuesped = new JTextField();
        panelIzquierdo.add(txtIdHuesped);

        panelIzquierdo.add(new JLabel("Subtotal Hospedaje ($):"));
        txtMontoHabitacion = new JTextField("240.00");
        panelIzquierdo.add(txtMontoHabitacion);

        panelIzquierdo.add(new JLabel("Subtotal Servicios ($):"));
        txtMontoServicios = new JTextField("35.00");
        panelIzquierdo.add(txtMontoServicios);

        panelIzquierdo.add(new JLabel("Total Neto a Pagar ($):"));
        txtTotal = new JTextField("275.00");
        panelIzquierdo.add(txtTotal);

        panelIzquierdo.add(new JLabel("Método de Pago:"));
        String[] metodos = {"Efectivo", "Tarjeta", "Transferencia"};
        cbMetodoPago = new JComboBox<>(metodos);
        panelIzquierdo.add(cbMetodoPago);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnProcesarPago = new JButton("Procesar Pago");
        btnProcesarPago.setBackground(new Color(46, 139, 87));
        btnProcesarPago.setForeground(Color.WHITE);
        panelAcciones.add(btnProcesarPago);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelAcciones, BorderLayout.SOUTH);

        // Lógica de Registro Real
        btnProcesarPago.addActionListener(e -> {
            try {
                int idReserva = Integer.parseInt(txtIdReserva.getText().trim());
                int idHuesped = Integer.parseInt(txtIdHuesped.getText().trim());
                double total = Double.parseDouble(txtTotal.getText().trim());
                String metodo = cbMetodoPago.getSelectedItem().toString().toLowerCase();

                Pago nuevoPago = new Pago(idReserva, idHuesped, total, metodo);

                if (pagoDAO.registrarPago(nuevoPago)) {
                    JOptionPane.showMessageDialog(this, "¡Pago registrado en la base de datos!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar en la BD. Verifique que el Huesped exista.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Ingrese valores numéricos válidos.");
            }
        });
    }
}