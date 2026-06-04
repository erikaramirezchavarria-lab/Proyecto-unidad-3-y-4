package vistas;

import dao.HabitacionDAO;
import dao.ReservacionDAO;
import modelos.Reservacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelReservaciones extends JPanel {

    private JTextField txtIdCliente, txtNumHabitacion, txtFechaEntrada, txtFechaSalida;
    private JButton btnGuardar, btnLimpiar;
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private String rolUsuario;

    private ReservacionDAO resDAO = new ReservacionDAO();
    private HabitacionDAO habitacionDAO = new HabitacionDAO();

    public PanelReservaciones(String rol) {
        this.rolUsuario = rol;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Alta de Reservación Nueva");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 15));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(380, 600));

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 20));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("ID del Huesped:"));
        txtIdCliente = new JTextField();
        panelCampos.add(txtIdCliente);
        panelCampos.add(new JLabel("ID de Habitación:")); // Cambiado para mayor claridad
        txtNumHabitacion = new JTextField();
        panelCampos.add(txtNumHabitacion);
        panelCampos.add(new JLabel("Fecha Inicio (AAAA-MM-DD):"));
        txtFechaEntrada = new JTextField();
        panelCampos.add(txtFechaEntrada);
        panelCampos.add(new JLabel("Fecha Fin (AAAA-MM-DD):"));
        txtFechaSalida = new JTextField();
        panelCampos.add(txtFechaSalida);

        panelIzquierdo.add(panelCampos, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelAcciones.setBackground(Color.WHITE);
        btnLimpiar = new JButton("Limpiar");
        btnGuardar = new JButton("Confirmar Reserva");
        btnGuardar.setBackground(new Color(255, 140, 0));
        btnGuardar.setForeground(Color.WHITE);
        panelAcciones.add(btnLimpiar);
        panelAcciones.add(btnGuardar);
        panelIzquierdo.add(panelAcciones, BorderLayout.CENTER);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Reservaciones Activas"));
        modeloTabla = new DefaultTableModel(new String[]{"ID Huesped", "ID Habitación", "F. Inicio", "F. Fin"}, 0);
        tablaReservas = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelTabla);
        splitPane.setDividerLocation(390);
        add(splitPane, BorderLayout.CENTER);

        if (rolUsuario != null && rolUsuario.equalsIgnoreCase("Auditor")) {
            btnGuardar.setEnabled(false);
            btnLimpiar.setEnabled(false);
        }

        configurarEventos();
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> {
            try {
                // Aquí realizamos las conversiones necesarias a int
                int idHuesped = Integer.parseInt(txtIdCliente.getText().trim());
                int idHabitacion = Integer.parseInt(txtNumHabitacion.getText().trim());
                String fechaInicio = txtFechaEntrada.getText().trim();
                String fechaFin = txtFechaSalida.getText().trim();

                if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Llene todos los campos de fecha.");
                    return;
                }

                // CREAMOS EL OBJETO MODELO con los datos convertidos
                Reservacion nuevaReserva = new Reservacion(idHuesped, idHabitacion, fechaInicio, fechaFin);

                // ENVIAMOS EL OBJETO AL DAO
                if (resDAO.guardarReservacion(nuevaReserva)) {
                    // Actualizamos estado enviando el ID convertido a String
                    habitacionDAO.actualizarEstadoHabitacion(String.valueOf(idHabitacion), "reservada");
                    
                    modeloTabla.addRow(new Object[]{idHuesped, idHabitacion, fechaInicio, fechaFin});
                    JOptionPane.showMessageDialog(this, "¡Reserva confirmada!");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar en la BD.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID de Huesped y de Habitación deben ser números.");
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void limpiarCampos() {
        txtIdCliente.setText(""); txtNumHabitacion.setText("");
        txtFechaEntrada.setText(""); txtFechaSalida.setText("");
        txtIdCliente.requestFocus();
    }
}