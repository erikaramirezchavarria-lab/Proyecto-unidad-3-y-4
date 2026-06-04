package vistas;

import dao.ServicioDAO;
import modelos.Servicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelServicios extends JPanel {

    private JTextField txtIdReserva, txtDescripcion, txtPrecio;
    private JComboBox<String> cbTipoServicio;
    private JButton btnGuardar, btnLimpiar;
    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;
    private String rolUsuario;
    
    // Instancia del DAO
    private ServicioDAO servicioDAO = new ServicioDAO();

    public PanelServicios() {
        this("Administrador");
    }

    public PanelServicios(String rol) {
        this.rolUsuario = rol;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Cargar Servicio a Catálogo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 15));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(380, 600));

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 20));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("ID de Reservación:"));
        txtIdReserva = new JTextField();
        panelCampos.add(txtIdReserva);

        panelCampos.add(new JLabel("Categoría:"));
        String[] servicios = {"Restaurante", "Spa", "Lavandería", "Minibar", "Tours"};
        cbTipoServicio = new JComboBox<>(servicios);
        panelCampos.add(cbTipoServicio);

        panelCampos.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panelCampos.add(txtDescripcion);

        panelCampos.add(new JLabel("Costo ($):"));
        txtPrecio = new JTextField();
        panelCampos.add(txtPrecio);

        panelIzquierdo.add(panelCampos, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelAcciones.setBackground(Color.WHITE);
        btnLimpiar = new JButton("Limpiar");
        btnGuardar = new JButton("Guardar Servicio");
        btnGuardar.setBackground(new Color(0, 122, 255));
        btnGuardar.setForeground(Color.WHITE);
        panelAcciones.add(btnLimpiar);
        panelAcciones.add(btnGuardar);
        panelIzquierdo.add(panelAcciones, BorderLayout.CENTER);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Servicios Registrados"));
        modeloTabla = new DefaultTableModel(new String[]{"ID Reserva", "Categoría", "Descripción", "Costo"}, 0);
        tablaServicios = new JTable(modeloTabla);
        panelTabla.add(new JScrollPane(tablaServicios), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelTabla);
        splitPane.setDividerLocation(390);
        add(splitPane, BorderLayout.CENTER);

        // EVENTO GUARDAR CORREGIDO
        btnGuardar.addActionListener(e -> {
            try {
                int reserva = Integer.parseInt(txtIdReserva.getText().trim());
                String categoria = cbTipoServicio.getSelectedItem().toString();
                String desc = txtDescripcion.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());

                // Crear el objeto modelo
                Servicio nuevoServicio = new Servicio();
                nuevoServicio.setNombre(categoria);
                nuevoServicio.setDescripcion(desc);
                nuevoServicio.setPrecio(precio);
                
                // Llamar al DAO usando el método correcto 'guardarServicio'
                if (servicioDAO.guardarServicio(nuevoServicio)) {
                    modeloTabla.addRow(new Object[]{reserva, categoria, desc, precio});
                    JOptionPane.showMessageDialog(this, "¡Servicio guardado exitosamente!");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar en la Base de Datos.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID y el Precio deben ser números válidos.");
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void limpiarCampos() {
        txtIdReserva.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtIdReserva.requestFocus();
    }
}