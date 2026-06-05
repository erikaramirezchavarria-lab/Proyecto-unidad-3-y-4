package controladores;

import dao.HuespedDAO;
import modelos.Huesped;
import vistas.PanelClientes;
import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class HuespedController {
    
    private PanelClientes vista;
    private HuespedDAO huespedDAO;

    public HuespedController(PanelClientes vista) {
        this.vista = vista;
        this.huespedDAO = new HuespedDAO();
        
        // Aquí se enlazarían los eventos si decides sacar la lógica del panel
    }
    
    // Aquí se mudarían los métodos de registrar y listar...
}
