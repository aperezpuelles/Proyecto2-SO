/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.so;

/**
 *
 * @author Ignacio
 */
import javax.swing.*;
import java.awt.*;

public class PanelDisco extends JPanel {
    private SD sd;

    public PanelDisco(SD sd) {
        this.sd = sd;
        setLayout(new GridLayout(5, 8, 5, 5));
        actualizarVista();
    }

    public void actualizarVista() {
        removeAll();

        Nodo<Bloque> actual = sd.getBloques().getHead();
        int index = 1;

        while (actual != null) {
            Bloque bloque = actual.getData();

            JPanel panelBloque = new JPanel(new BorderLayout());
            panelBloque.setPreferredSize(new Dimension(50, 50));
            panelBloque.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            Color color = bloque.isOcupado() ? Color.RED : Color.GREEN;
            panelBloque.setBackground(color);

            JLabel label = new JLabel(String.valueOf(index), SwingConstants.CENTER);
            panelBloque.add(label, BorderLayout.SOUTH);

            add(panelBloque);
            actual = actual.getNext();
            index++;
        }

        revalidate();
        repaint();
    }
}
