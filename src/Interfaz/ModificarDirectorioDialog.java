/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Ignacio
 */
public class ModificarDirectorioDialog extends JDialog {
    private JTextField txtNombre;
    private boolean aceptado = false;
    private String nombreDirectorio;

    public ModificarDirectorioDialog(JFrame parent, String nombreActual) {
        super(parent, "Modificar Directorio", true);
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nuevo Nombre:"));
        txtNombre = new JTextField(nombreActual);
        add(txtNombre);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombreDirectorio = txtNombre.getText();

                if (nombreDirectorio.isEmpty()) {
                    JOptionPane.showMessageDialog(ModificarDirectorioDialog.this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
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

    public String getNombreDirectorio() {
        return nombreDirectorio;
    }

    public boolean isAceptado() {
        return aceptado;
    }
}
