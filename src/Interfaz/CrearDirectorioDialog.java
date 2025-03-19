/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Ignacio
 */
public class CrearDirectorioDialog extends JDialog {
    private JTextField txtNombre;
    private JComboBox<String> comboDirectorios;
    private boolean aceptado = false;
    private String nombreDirectorio;
    private String directorioPadre;

    public CrearDirectorioDialog(JFrame parent, String[] directoriosDisponibles) {
        super(parent, "Crear Directorio", true);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nombre del Directorio:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Ubicación:"));
        comboDirectorios = new JComboBox<>(directoriosDisponibles);
        add(comboDirectorios);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            nombreDirectorio = txtNombre.getText();
            directorioPadre = (String) comboDirectorios.getSelectedItem();

            if (nombreDirectorio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                aceptado = true;
                setVisible(false);
            }
        });

        btnCancelar.addActionListener(e -> {
            aceptado = false;
            setVisible(false);
        });

        add(btnAceptar);
        add(btnCancelar);

        setLocationRelativeTo(parent);
    }

    public String getNombreDirectorio() {
        return nombreDirectorio;
    }

    public String getDirectorioPadre() {
        return directorioPadre;
    }

    public boolean isAceptado() {
        return aceptado;
    }
}