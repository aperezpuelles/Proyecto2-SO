/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import proyecto2.so.TablaHash;

/**
 *
 * @author Ignacio
 */
public class FrameAccesoDenegado extends JDialog{
    private JTextField txtNombre;
    private JTextField txtTamano;
    private JComboBox<String> comboColor;
    private boolean aceptado = false;
    
    private static final TablaHash<String, Color> tablaColores = new TablaHash<>();
    static {
        tablaColores.put("Rojo", Color.RED);
        tablaColores.put("Verde", Color.GREEN);
        tablaColores.put("Azul", Color.BLUE);
        tablaColores.put("Amarillo", Color.YELLOW);
        tablaColores.put("Naranja", new Color(255, 165, 0)); // Naranja
        tablaColores.put("Morado", new Color(128, 0, 128)); // Morado
    }

    public FrameAccesoDenegado(JFrame parent) {
        super(parent, "Acceso Denegado", true);
        setSize(350, 100);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("El modo usuario no tiene autorización para crear archivos"));

        setLocationRelativeTo(parent);
    }

    public boolean isAceptado() {
        return aceptado;
    }
    
}
