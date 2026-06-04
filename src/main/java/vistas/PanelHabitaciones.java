package vistas;

import dao.HabitacionDAO;
import modelos.Habitacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelHabitaciones extends JPanel {

    private JTextField txtId, txtNumero, txtPrecio;
    private JComboBox<String> cbTipo, cbEstado;
    private JButton btnGuardar, btnLimpiar;
    private JTable tablaHabitaciones;
    private DefaultTableModel modeloTabla;
    private String rolUsuario; 

    private HabitacionDAO habitacionDAO = new HabitacionDAO();

    public PanelHabitaciones() {
        this("Administrador"); 
    }

    public PanelHabitaciones(String rol) {
        this.rolUsuario = rol;
        
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Control de Inventario de Habitaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 33, 33));
        add(lblTitulo, BorderLayout.NORTH);

        // Formulario Izquierdo (Sin la parte de la foto)
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 15));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(400, 600));

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 15));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("ID Habitación:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(240, 240, 240));
        panelCampos.add(txtId);

        panelCampos.add(new JLabel("Número de Habitación:"));
        txtNumero = new JTextField();
        panelCampos.add(txtNumero);

        panelCampos.add(new JLabel("Tipo de Habitación:"));
        cbTipo = new JComboBox<>(new String[]{"sencilla", "doble", "suite"});
        panelCampos.add(cbTipo);

        panelCampos.add(new JLabel("Precio por Noche ($):"));
        txtPrecio = new JTextField();
        panelCampos.add(txtPrecio);

        panelCampos.add(new JLabel("Estado Inicial:"));
        cbEstado = new JComboBox<>(new String[]{"libre", "ocupada", "reservada"});
        panelCampos.add(cbEstado);

        panelIzquierdo.add(panelCampos, BorderLayout.NORTH);

        // Botones de acción
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelAcciones.setBackground(Color.WHITE);
        btnLimpiar = new JButton("Limpiar");
        btnGuardar = new JButton("Guardar Habitación");
        btnGuardar.setBackground(new Color(0, 122, 255));
        btnGuardar.setForeground(Color.WHITE);
        panelAcciones.add(btnLimpiar);
        panelAcciones.add(btnGuardar);
        panelIzquierdo.add(panelAcciones, BorderLayout.SOUTH);

        // Tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder("Habitaciones Registradas"));

        String[] columnas = {"ID", "Número", "Tipo", "Precio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaHabitaciones = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaHabitaciones), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelTabla);
        splitPane.setDividerLocation(410);
        splitPane.setEnabled(false);
        add(splitPane, BorderLayout.CENTER);

        configurarEventos();
        cargarDatosTabla();

        if (rolUsuario != null && rolUsuario.equalsIgnoreCase("Auditor")) {
            btnGuardar.setEnabled(false);
            btnLimpiar.setEnabled(false);
        }
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(e -> {
            String numero = txtNumero.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (numero.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Enviamos null en el campo de la foto
            Habitacion nuevaHabitacion = new Habitacion(numero, cbTipo.getSelectedItem().toString(), precio, cbEstado.getSelectedItem().toString(), null);

            if (habitacionDAO.registrarHabitacion(nuevaHabitacion)) {
                JOptionPane.showMessageDialog(this, "¡Habitación registrada!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        List<Habitacion> lista = habitacionDAO.listarHabitaciones();
        for (Habitacion h : lista) {
            modeloTabla.addRow(new Object[]{h.getIdHabitacion(), h.getNumero(), h.getTipo(), "$" + h.getPrecio(), h.getEstado()});
        }
    }

    private void limpiarCampos() {
        txtId.setText(""); txtNumero.setText(""); txtPrecio.setText("");
        cbTipo.setSelectedIndex(0); cbEstado.setSelectedIndex(0);
        txtNumero.requestFocus();
    }
}