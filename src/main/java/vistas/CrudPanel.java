package vistas;

import dao.UsuarioDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import utilerias.ConexionBD;
import utilerias.Encriptador;
import utilerias.ReportePDF;
import utilerias.Roles;

public class CrudPanel extends JPanel {
    public static class Field {
        final String column;
        final String label;
        final String type;
        final boolean required;
        final String[] options;
        final boolean nullable;

        private Field(String column, String label, String type, boolean required, boolean nullable, String[] options) {
            this.column = column;
            this.label = label;
            this.type = type;
            this.required = required;
            this.nullable = nullable;
            this.options = options;
        }

        public static Field text(String column, String label, boolean required) {
            return new Field(column, label, "text", required, false, null);
        }

        public static Field nullableText(String column, String label) {
            return new Field(column, label, "text", false, true, null);
        }

        public static Field decimal(String column, String label, boolean required) {
            return new Field(column, label, "decimal", required, false, null);
        }

        public static Field integer(String column, String label, boolean required, boolean nullable) {
            return new Field(column, label, "int", required, nullable, null);
        }

        public static Field combo(String column, String label, String... options) {
            return new Field(column, label, "combo", true, false, options);
        }

        public static Field blob(String column, String label) {
            return new Field(column, label, "blob", false, true, null);
        }
    }

    private final String rolUsuario;
    private final String titulo;
    private final String tableName;
    private final String idColumn;
    private final String[] listColumns;
    private final String filterColumn;
    private final String dateColumn;
    private final String joinFrom;
    private final List<Field> fields;
    private final Map<String, Component> inputs = new LinkedHashMap<>();
    private final Map<String, byte[]> blobValues = new LinkedHashMap<>();
    private final Map<String, JLabel> blobLabels = new LinkedHashMap<>();
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable table = new JTable(model);
    private final JTextField txtId = new JTextField();
    private final JTextField txtBuscar = new JTextField();
    private final JTextField txtFechaInicio = new JTextField();
    private final JTextField txtFechaFin = new JTextField();
    private final JLabel lblTotal = new JLabel("Total: 0");
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;

    public CrudPanel(String rolUsuario, String titulo, String tableName, String idColumn, String[] listColumns,
            String filterColumn, String dateColumn, String joinFrom, List<Field> fields) {
        this.rolUsuario = rolUsuario;
        this.titulo = titulo;
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.listColumns = listColumns;
        this.filterColumn = filterColumn;
        this.dateColumn = dateColumn;
        this.joinFrom = joinFrom == null || joinFrom.isBlank() ? tableName : joinFrom;
        this.fields = fields;
        build();
        listar("");
        aplicarRol();
    }

    private void build() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBackground(new Color(245,248,252));

        JLabel title = new JLabel(titulo);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 51, 102));
        add(title, BorderLayout.NORTH);
        

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 51, 102)),    
                    "Formulario"
            )
        );
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtId.setEditable(false);
        txtId.setBackground(new Color(242, 242, 242));
        addRow(form, gbc, 0, "ID", txtId);

        int row = 1;
        for (Field f : fields) {
            if ("blob".equals(f.type)) {
                JPanel blobPanel = new JPanel(new BorderLayout(5, 5));
                blobPanel.setBackground(Color.WHITE);
                JLabel preview = new JLabel("Sin imagen", SwingConstants.CENTER);
                preview.setPreferredSize(new Dimension(150, 120));
                preview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                JButton select = new JButton("Seleccionar imagen");
                select.addActionListener(e -> seleccionarImagen(f.column));
                blobPanel.add(preview, BorderLayout.CENTER);
                blobPanel.add(select, BorderLayout.SOUTH);
                inputs.put(f.column, select);
                blobLabels.put(f.column, preview);
                addRow(form, gbc, row++, f.label, blobPanel);
            } else if ("combo".equals(f.type)) {
                JComboBox<String> combo = new JComboBox<>(f.options);
                inputs.put(f.column, combo);
                addRow(form, gbc, row++, f.label, combo);
            } else {
                JTextField text = new JTextField();
                inputs.put(f.column, text);
                addRow(form, gbc, row++, f.label, text);
            }
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        JButton btnNuevo = new JButton("Limpiar");
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);

        btnEditar.setBackground(new Color(255, 152, 0));
        btnEditar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(211, 47, 47));
        btnEliminar.setForeground(Color.WHITE);

        btnNuevo.setBackground(new Color(96, 125, 139));
        btnNuevo.setForeground(Color.WHITE);

        btnGuardar.setFocusPainted(false);
        btnEditar.setFocusPainted(false);
        btnEliminar.setFocusPainted(false);
        btnNuevo.setFocusPainted(false);
        
        actions.add(btnNuevo);
        actions.add(btnGuardar);
        actions.add(btnEditar);
        actions.add(btnEliminar);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        form.add(actions, gbc);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(Color.WHITE);
        right.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 51, 102)),
                "Consulta"
            )
        );
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.setBackground(Color.WHITE);
        txtBuscar.setPreferredSize(new Dimension(160, 28));
        txtFechaInicio.setPreferredSize(new Dimension(95, 28));
        txtFechaFin.setPreferredSize(new Dimension(95, 28));
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiarFiltro = new JButton("Limpiar filtros");
        JButton btnReporteGeneral = new JButton("PDF general");
        JButton btnReporteFecha = new JButton("PDF fechas");
        JButton btnReporteCriterio = new JButton("PDF criterio");
        JButton btnReporteResumen = new JButton("PDF resumen");
        JButton btnReporteDetalle = new JButton("PDF detalle");
        filters.add(new JLabel("Filtro:"));
        filters.add(txtBuscar);
        filters.add(new JLabel("Del:"));
        filters.add(txtFechaInicio);
        filters.add(new JLabel("Al:"));
        filters.add(txtFechaFin);
        filters.add(btnBuscar);
        filters.add(btnLimpiarFiltro);
        filters.add(btnReporteGeneral);
        filters.add(btnReporteFecha);
        filters.add(btnReporteCriterio);
        filters.add(btnReporteResumen);
        filters.add(btnReporteDetalle);
        filters.add(lblTotal);

        right.add(filters, BorderLayout.NORTH);
        right.add(new JScrollPane(table), BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, right);
        split.setDividerLocation(390);
        add(split, BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> listar(txtBuscar.getText().trim()));
        btnLimpiarFiltro.addActionListener(e -> {
            txtBuscar.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            listar("");
        });
        btnReporteGeneral.addActionListener(e -> reporteGeneral());
        btnReporteFecha.addActionListener(e -> reporteFechas());
        btnReporteCriterio.addActionListener(e -> reporteCriterio());
        btnReporteResumen.addActionListener(e -> reporteResumen());
        btnReporteDetalle.addActionListener(e -> reporteDetalle());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    cargarSeleccion();
                }
            }
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component component) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(component, gbc);
    }

    private void aplicarRol() {
        if (Roles.esConsultor(rolUsuario)) {
            btnGuardar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnGuardar.setToolTipText("El rol consultor es solo lectura.");
            btnEditar.setToolTipText("El rol consultor es solo lectura.");
            btnEliminar.setToolTipText("El rol consultor es solo lectura.");
        }
    }

    private void seleccionarImagen(String column) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imagenes JPG/PNG/GIF", "jpg", "jpeg", "png", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] bytes = fis.readAllBytes();
                blobValues.put(column, bytes);
                mostrarImagen(column, bytes);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo leer la imagen: " + ex.getMessage());
            }
        }
    }

    private void mostrarImagen(String column, byte[] bytes) {
        JLabel label = blobLabels.get(column);
        if (label == null) {
            return;
        }
        if (bytes == null || bytes.length == 0) {
            label.setIcon(null);
            label.setText("Sin imagen");
            return;
        }
        ImageIcon icon = new ImageIcon(bytes);
        Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
        label.setText("");
        label.setIcon(new ImageIcon(img));
    }

    private void listar(String filtro) {
        String sql = buildSelectSql(false);
        boolean hasFilter = filtro != null && !filtro.isBlank();
        if (hasFilter && filterColumn != null && !filterColumn.isBlank()) {
            sql += " WHERE " + filterColumn + " LIKE ?";
        }
        sql += " ORDER BY " + idColumn + " DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (hasFilter && filterColumn != null && !filterColumn.isBlank()) {
                ps.setString(1, "%" + filtro + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                llenarTabla(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al listar " + tableName + ": " + e.getMessage());
        }
    }

    private String buildSelectSql(boolean includeBlob) {
        List<String> cols = new ArrayList<>();
        for (String c : listColumns) {
            cols.add(c);
        }
        if (includeBlob) {
            for (Field f : fields) {
                if ("blob".equals(f.type) && !cols.contains(f.column)) {
                    cols.add(f.column);
                }
            }
        }
        return "SELECT " + String.join(", ", cols) + " FROM " + joinFrom;
    }

    private void llenarTabla(ResultSet rs) throws SQLException {
        model.setRowCount(0);
        model.setColumnCount(0);
        ResultSetMetaData md = rs.getMetaData();
        int count = md.getColumnCount();
        for (int i = 1; i <= count; i++) {
            model.addColumn(md.getColumnLabel(i));
        }
        int total = 0;
        while (rs.next()) {
            Object[] row = new Object[count];
            for (int i = 1; i <= count; i++) {
                Object value = rs.getObject(i);
                row[i - 1] = value instanceof byte[] ? "[BLOB]" : value;
            }
            model.addRow(row);
            total++;
        }
        lblTotal.setText("Total: " + total);
    }

    private void guardar() {
        try {
            validar();
            List<Field> insertFields = fields;
            StringBuilder cols = new StringBuilder();
            StringBuilder marks = new StringBuilder();
            for (Field f : insertFields) {
                if (!cols.isEmpty()) {
                    cols.append(", ");
                    marks.append(", ");
                }
                cols.append(f.column);
                marks.append("?");
            }
            String sql = "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + marks + ")";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                int index = 1;
                for (Field f : insertFields) {
                    setPreparedValue(ps, index++, f);
                }
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Registro guardado.");
            limpiarFormulario();
            listar(txtBuscar.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void editar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro con doble clic.");
            return;
        }
        try {
            validar();
            StringBuilder set = new StringBuilder();
            for (Field f : fields) {
                if (!set.isEmpty()) {
                    set.append(", ");
                }
                set.append(f.column).append("=?");
            }
            String sql = "UPDATE " + tableName + " SET " + set + " WHERE " + idColumn + "=?";
            try (Connection con = ConexionBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                int index = 1;
                for (Field f : fields) {
                    setPreparedValue(ps, index++, f);
                }
                ps.setInt(index, Integer.parseInt(txtId.getText()));
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Registro actualizado.");
            limpiarFormulario();
            listar(txtBuscar.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar: " + e.getMessage());
        }
    }

    private void eliminar() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro con doble clic.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Desea eliminar este registro?", "Confirmar baja", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + "=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro eliminado.");
            limpiarFormulario();
            listar(txtBuscar.getText().trim());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar. Revise registros relacionados. " + e.getMessage());
        }
    }

    private void validar() {
        for (Field f : fields) {
            if ("blob".equals(f.type)) {
                continue;
            }
            String value = getStringValue(f);
            if (f.required && value.isBlank()) {
                throw new IllegalArgumentException("El campo " + f.label + " es obligatorio.");
            }
        }
        if ("usuarios".equals(tableName)) {
            String password = getStringByColumn("password");
            if (!password.matches("^[a-fA-F0-9]{64}$") && !password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                throw new IllegalArgumentException("La contrasena debe tener minimo 8 caracteres, 1 mayuscula y 1 numero.");
            }
        }
    }

    private void setPreparedValue(PreparedStatement ps, int index, Field f) throws SQLException {
        if ("blob".equals(f.type)) {
            byte[] bytes = blobValues.get(f.column);
            if (bytes == null) {
                ps.setNull(index, Types.LONGVARBINARY);
            } else {
                ps.setBytes(index, bytes);
            }
            return;
        }
        String value = getStringValue(f);
        if (f.nullable && value.isBlank()) {
            ps.setNull(index, Types.NULL);
            return;
        }
        if ("int".equals(f.type)) {
            ps.setInt(index, Integer.parseInt(value));
        } else if ("decimal".equals(f.type)) {
            ps.setBigDecimal(index, new BigDecimal(value));
        } else if ("password".equals(f.column) && "usuarios".equals(tableName) && !value.matches("^[a-fA-F0-9]{64}$")) {
            ps.setString(index, Encriptador.encriptarSHA256(value));
        } else {
            ps.setString(index, value);
        }
    }

    private String getStringValue(Field f) {
        Component c = inputs.get(f.column);
        if (c instanceof JTextField text) {
            return text.getText().trim();
        }
        if (c instanceof JComboBox<?> combo) {
            Object selected = combo.getSelectedItem();
            return selected == null ? "" : selected.toString();
        }
        return "";
    }

    private String getStringByColumn(String column) {
        for (Field f : fields) {
            if (f.column.equals(column)) {
                return getStringValue(f);
            }
        }
        return "";
    }

    private void cargarSeleccion() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        Object id = model.getValueAt(row, findColumnIndex(idColumn));
        if (id == null) {
            return;
        }
        String sql = buildSelectSql(true) + " WHERE " + idColumn + "=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtId.setText(String.valueOf(rs.getObject(idColumn)));
                    for (Field f : fields) {
                        if ("blob".equals(f.type)) {
                            byte[] bytes = rs.getBytes(f.column);
                            blobValues.put(f.column, bytes);
                            mostrarImagen(f.column, bytes);
                        } else {
                            Component c = inputs.get(f.column);
                            String value = rs.getString(f.column);
                            if (c instanceof JTextField text) {
                                text.setText(value == null ? "" : value);
                            } else if (c instanceof JComboBox<?> combo) {
                                combo.setSelectedItem(value);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar registro: " + e.getMessage());
        }
    }

    private int findColumnIndex(String column) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equalsIgnoreCase(column)) {
                return i;
            }
        }
        return 0;
    }

    private void limpiarFormulario() {
        txtId.setText("");
        for (Field f : fields) {
            if ("blob".equals(f.type)) {
                blobValues.remove(f.column);
                mostrarImagen(f.column, null);
            } else {
                Component c = inputs.get(f.column);
                if (c instanceof JTextField text) {
                    text.setText("");
                } else if (c instanceof JComboBox<?> combo && combo.getItemCount() > 0) {
                    combo.setSelectedIndex(0);
                }
            }
        }
    }

    private void reporteGeneral() {
        generarReporte("Listado general - " + titulo, "Todos los registros", buildSelectSql(false), List.of());
    }

    private void reporteFechas() {
        if (dateColumn == null || dateColumn.isBlank()) {
            JOptionPane.showMessageDialog(this, "Esta tabla no tiene campo de fecha configurado.");
            return;
        }
        String inicio = txtFechaInicio.getText().trim();
        String fin = txtFechaFin.getText().trim();
        if (inicio.isBlank() || fin.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese rango de fechas en formato yyyy-MM-dd.");
            return;
        }
        LocalDate.parse(inicio);
        LocalDate.parse(fin);
        generarReporte("Reporte por rango de fechas - " + titulo, "Rango: " + inicio + " a " + fin,
                buildSelectSql(false) + " WHERE DATE(" + dateColumn + ") BETWEEN ? AND ?", List.of(inicio, fin));
    }

    private void reporteCriterio() {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese un criterio de busqueda.");
            return;
        }
        generarReporte("Reporte por criterio - " + titulo, "Criterio: " + criterio,
                buildSelectSql(false) + " WHERE " + filterColumn + " LIKE ?", List.of("%" + criterio + "%"));
    }

    private void reporteResumen() {
        String sql = "SELECT COUNT(*) AS total_registros FROM " + tableName;
        generarReporte("Reporte estadistico/resumen - " + titulo, "Conteo general de registros", sql, List.of());
    }

    private void reporteDetalle() {
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro con doble clic para generar detalle.");
            return;
        }
        generarReporte("Detalle individual - " + titulo, "ID: " + txtId.getText(),
                buildSelectSql(false) + " WHERE " + idColumn + "=?", List.of(txtId.getText()));
    }

    private void generarReporte(String tituloReporte, String descripcion, String sql, List<String> params) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(tituloReporte.replaceAll("[^a-zA-Z0-9]+", "_").toLowerCase() + ".pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                List<String> columnas = new ArrayList<>();
                List<List<String>> filas = new ArrayList<>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    columnas.add(md.getColumnLabel(i));
                }
                while (rs.next()) {
                    List<String> fila = new ArrayList<>();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        Object value = rs.getObject(i);
                        fila.add(value instanceof byte[] ? "[BLOB]" : String.valueOf(value == null ? "" : value));
                    }
                    filas.add(fila);
                }
                ReportePDF.generar(chooser.getSelectedFile().getAbsolutePath(), tituloReporte, descripcion, columnas, filas);
                JOptionPane.showMessageDialog(this, "PDF generado correctamente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
        }
    }
}
