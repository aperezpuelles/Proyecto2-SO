/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

/**
 *
 * @author Ignacio
 */
import javax.swing.*;
import java.awt.*;
import proyecto2.so.Bloque;
import proyecto2.so.Nodo;
import proyecto2.so.SD;

public class PanelDisco extends JPanel {
    private SD sd;
    private JPanel[] panelesBloques; 

    public PanelDisco(SD sd) {
        this.sd = sd;
        setLayout(new GridLayout(5, 8, 5, 5)); 
        panelesBloques = new JPanel[sd.getBloquesmax()]; 
        cargarBloques(); 
    }

    private void cargarBloques() {
        Nodo<Bloque> actual = sd.getBloques().getHead();
        int index = 0;

        while (actual != null && index < panelesBloques.length) {
            Bloque bloque = actual.getData();

            JPanel panelBloque = new JPanel(new BorderLayout());
            panelBloque.setPreferredSize(new Dimension(50, 50));
            panelBloque.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel label = new JLabel(String.valueOf(bloque.getNumero()), SwingConstants.CENTER);
            panelBloque.add(label, BorderLayout.SOUTH);

            panelesBloques[index] = panelBloque; 
            panelesBloques[index].setBackground(Color.WHITE);
            add(panelBloque);

            actual = actual.getNext();
            index++;
        }
    }

    public void actualizarVista() {
        Nodo<Bloque> actual = sd.getBloques().getHead();
        int index = 0;

        while (actual != null && index < panelesBloques.length) {
            Bloque bloque = actual.getData();
            panelesBloques[index].setBackground(bloque.isOcupado() ? bloque.getColor() : Color.WHITE);

            actual = actual.getNext();
            index++;
        }

        revalidate();
        repaint();
    }
}
