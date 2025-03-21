/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import proyecto2.so.TablaHash;

/**
 *
 * @author Ignacio
 */
public class ModificarArchivoDialog extends JDialog {
    private String nusuario;
    private JTextField Username;
    private JTextField txtNombre;
    private boolean aceptado = false;
    private String nombreArchivo;

    public ModificarArchivoDialog(JFrame parent, String nombreActual) {
        super(parent, "Modificar Archivo", true);
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));
        
        add(new JLabel("Nombre de Usuario:"));
        Username = new JTextField();
        add(Username);

        add(new JLabel("Nuevo Nombre:"));
        txtNombre = new JTextField(nombreActual);
        add(txtNombre);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombreArchivo = txtNombre.getText();
                setNusuario(Username.getText());

                if (nombreArchivo.isEmpty()) {
                    JOptionPane.showMessageDialog(ModificarArchivoDialog.this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
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

    public boolean isAceptado() {
        return aceptado;
    }

    public String getNusuario() {
        return nusuario;
    }

    public void setNusuario(String nusuario) {
        this.nusuario = nusuario;
    }
    
    
}
