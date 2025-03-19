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
public class CrearArchivoDialog extends JDialog{
    private JTextField txtNombre;
    private JTextField txtTamano;
    private JComboBox<String> comboDirectorios;
    private JComboBox<String> comboColor;
    private boolean aceptado = false;
    private String nombreArchivo;
    private double tamanoArchivo;
    private Color colorArchivo;
    private static final TablaHash<String, Color> tablaColores = new TablaHash<>();
    static {
        tablaColores.put("Rojo", Color.RED);
        tablaColores.put("Verde", Color.GREEN);
        tablaColores.put("Azul", Color.BLUE);
        tablaColores.put("Amarillo", Color.YELLOW);
        tablaColores.put("Naranja", new Color(255, 165, 0));
        tablaColores.put("Morado", new Color(128, 0, 128)); 
    }

    public CrearArchivoDialog(JFrame parent, String[] directoriosDisponibles) {
        super(parent, "Crear Archivo", true);
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Tamaño (bloques):"));
        txtTamano = new JTextField();
        add(txtTamano);

        add(new JLabel("Color:"));
        String[] colores = {"Rojo", "Verde", "Azul", "Amarillo", "Naranja", "Morado"};
        comboColor = new JComboBox<>(colores);
        add(comboColor);
        
        add(new JLabel("Ubicación:"));
        comboDirectorios = new JComboBox<>(directoriosDisponibles);
        add(comboDirectorios);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombreArchivo = txtNombre.getText();
                try {
                    tamanoArchivo = Double.parseDouble(txtTamano.getText());
                } catch (NumberFormatException ex) {
                    tamanoArchivo = -1; 
                }
                String colorSeleccionado = (String) comboColor.getSelectedItem();
                colorArchivo = tablaColores.get(colorSeleccionado);

                if (nombreArchivo.isEmpty() || tamanoArchivo <= 0) {
                    JOptionPane.showMessageDialog(CrearArchivoDialog.this, "Datos inválidos. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    aceptado = true;
                    setVisible(false);
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptado = false;
                setVisible(false);
            }
        });

        add(btnAceptar);
        add(btnCancelar);

        setLocationRelativeTo(parent);
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public double getTamanoArchivo() {
        return tamanoArchivo;
    }

    public Color getColorArchivo() {
        return colorArchivo;
    }

    public boolean isAceptado() {
        return aceptado;
    }
    
    public String getDirectorioSeleccionado() {
        return (String) comboDirectorios.getSelectedItem();
    }    
}
