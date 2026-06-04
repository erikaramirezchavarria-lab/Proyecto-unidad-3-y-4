package vistas;

import dao.HuespedDAO;
import modelos.Huesped;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class PanelClientes extends JPanel {

    private JTextField txtId, txtNombre, txtTelefono, txtCorreo;
    private JButton btnGuardar, btnLimpiar, btnCargarFoto;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JLabel lblVistaPreviaFoto;
    private String rolUsuario; 

    // Variables utilitarias para el manejo del archivo BLOB
    private File archivoImagenSeleccionado = null;
    private HuespedDAO huespedDAO = new HuespedDAO();

    // Constructor por defecto
    public PanelClientes() {
        this("Administrador"); 
    }

    // Constructor oficial que recibe el rol desde el MenuPrincipalForm
    public PanelClientes(String rol) {
        this.rolUsuario = rol;
        
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Título de la Sección
        JLabel lblTitulo = new JLabel("Alta de Cliente Nuevo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 33, 33));
        add(lblTitulo, BorderLayout.NORTH);

        // Contenedor del Formulario Izquierdo
        JPanel panelIzquierdo = new JPanel(new BorderLayout(10, 15));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(400, 600));

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 15));
        panelCampos.setBackground(Color.WHITE);

        panelCampos.add(new JLabel("Identificación (ID Auto):"));
        txtId = new JTextField();
        txtId.setEditable(false); // 🔥 Autoincremental en BD, no se edita
        txtId.setBackground(new Color(240, 240, 240));
        panelCampos.add(txtId);

        panelCampos.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelCampos.add(txtTelefono);

        panelCampos.add(new JLabel("Correo Electrónico:"));
        txtCorreo = new JTextField();
        panelCampos.add(txtCorreo);

        panelIzquierdo.add(panelCampos, BorderLayout.NORTH);

        // COMPONENTE FOTO BLOB
        JPanel panelBlob = new JPanel(new BorderLayout(5, 5));
        panelBlob.setBackground(Color.WHITE);
        panelBlob.setBorder(BorderFactory.createTitledBorder("Fotografía del Cliente (BLOB)"));

        lblVistaPreviaFoto = new JLabel("[ Sin imagen seleccionada ]", SwingConstants.CENTER);
        lblVistaPreviaFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        lblVistaPreviaFoto.setPreferredSize(new Dimension(120, 120));

        btnCargarFoto = new JButton("Seleccionar Imagen (JPG/PNG)");
        
        panelBlob.add(lblVistaPreviaFoto, BorderLayout.CENTER);
        panelBlob.add(btnCargarFoto, BorderLayout.SOUTH);

        panelIzquierdo.add(panelBlob, BorderLayout.CENTER);

        // Botones de Acción Inline
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelAcciones.setBackground(Color.WHITE);
        btnLimpiar = new JButton("Limpiar");
        btnGuardar = new JButton("Guardar Cliente");
        btnGuardar.setBackground(new Color(0, 122, 255));
        btnGuardar.setForeground(Color.WHITE);
        panelAcciones.add(btnLimpiar);
        panelAcciones.add(btnGuardar);

        panelIzquierdo.add(panelAcciones, BorderLayout.SOUTH);

        // TABLA DE REGISTROS INLINE (Lado Derecho)
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createTitledBorder("Clientes Registrados"));

        String[] columnas = {"ID", "Nombre", "Teléfono", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que editen las celdas directamente en la tabla
            }
        };
        tablaClientes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // JSplitPane para división exacta
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelTabla);
        splitPane.setDividerLocation(410);
        splitPane.setEnabled(false);

        add(splitPane, BorderLayout.CENTER);

        configurarEventos();
        cargarDatosTabla(); // 🔥 Carga los huéspedes de la base de datos al iniciar

        // VALIDACIÓN DE ROL: Bloqueo de acciones para el Auditor (Solo Lectura)
        if (rolUsuario != null && rolUsuario.equalsIgnoreCase("Auditor")) {
            btnGuardar.setEnabled(false);
            btnLimpiar.setEnabled(false);
            btnCargarFoto.setEnabled(false);
            
            btnGuardar.setToolTipText("Los usuarios con rol Auditor solo tienen permisos de lectura.");
            btnCargarFoto.setToolTipText("Los usuarios con rol Auditor solo tienen permisos de lectura.");
        }
    }

    private void configurarEventos() {
        // Evento para cargar fotografía desde la PC
        btnCargarFoto.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            // Filtro para aceptar solo imágenes
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes JPG & PNG", "jpg", "jpeg", "png"));
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                archivoImagenSeleccionado = chooser.getSelectedFile();
                lblVistaPreviaFoto.setText("📷 " + archivoImagenSeleccionado.getName());
            }
        });

        // Evento del botón Guardar (Conexión corregida)
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String correo = txtCorreo.getText().trim();

            // 🔥 CORRECCIÓN: Quitamos la validación obligatoria del ID en código, ya que es auto-incremental en MySQL.
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre completo es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Procesamiento de la imagen BLOB
            byte[] bytesFoto = null;
            if (archivoImagenSeleccionado != null) {
                try (FileInputStream fis = new FileInputStream(archivoImagenSeleccionado)) {
                    bytesFoto = fis.readAllBytes();
                } catch (IOException ex) {
                    System.err.println("Error al procesar la imagen: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error al procesar el archivo de imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Crear el objeto modelo con los datos recolectados
            Huesped nuevoHuesped = new Huesped(nombre, telefono, correo, bytesFoto);

            // Intentar persistir en la base de datos mediante el DAO
            boolean exito = huespedDAO.registrarHuesped(nuevoHuesped);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Huésped registrado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDatosTabla(); // Refrescar la tabla con el nuevo registro y su ID real
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al registrar en la Base de Datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    // Método para consultar el DAO y pintar la tabla en tiempo real
    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0); // Borrar registros viejos en la vista
        List<Huesped> lista = huespedDAO.listarHuespedes();
        
        for (Huesped h : lista) {
            modeloTabla.addRow(new Object[]{
                h.getIdHuesped(),
                h.getNombre(),
                h.getTelefono(),
                h.getEmail()
            });
        }
    }

    private void limpiarCampos() {
        txtId.setText(""); 
        txtNombre.setText(""); 
        txtTelefono.setText(""); 
        txtCorreo.setText("");
        lblVistaPreviaFoto.setText("[ Sin imagen seleccionada ]");
        archivoImagenSeleccionado = null; // Resetear el archivo seleccionado
        txtNombre.requestFocus();
    }
}