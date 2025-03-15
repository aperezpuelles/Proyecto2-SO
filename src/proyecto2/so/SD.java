/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2.so;

import java.awt.Color;

/**
 *
 * @author Ignacio
 */
public class SD {
    private int bloquesmax;
    private double bloquesrestantes;
    private Lista<Bloque> bloques;

    public SD(int bloquesmax, double bloquesrestantes) {
        this.bloquesmax = bloquesmax;
        this.bloquesrestantes = bloquesrestantes;
        this.bloques = new Lista<>();
        
        for (int i = bloquesmax; i >= 0; i--) {
            Bloque nuevo = new Bloque("", i);
            bloques.add(nuevo);
        }        
    }
    
    public void asignarBloques(Archivo archivo) {
        double tamano = archivo.getBloquesAsignados();
        Color color = archivo.getColor();

        Nodo<Bloque> actual = bloques.getHead();
        while (actual != null && tamano > 0) {
            Bloque bloque = actual.getData();

            if (!bloque.isOcupado()) {
                if (tamano >= 1) {
                    bloque.setColor(color);
                } else {
                    bloque.setColor(new Color(
                        (color.getRed() + 255) / 2,
                        (color.getGreen() + 255) / 2,
                        (color.getBlue() + 255) / 2
                    ));
                }
                bloque.setOcupado(true);
                tamano--; 
            }
            actual = actual.getNext();
        }
    }

    public int getBloquesmax() {
        return bloquesmax;
    }

    public void setBloquesmax(int bloquesmax) {
        this.bloquesmax = bloquesmax;
    }

    public double getBloquesrestantes() {
        return bloquesrestantes;
    }

    public void setBloquesrestantes(double bloquesrestantes) {
        this.bloquesrestantes = bloquesrestantes;
    }

    public Lista<Bloque> getBloques() {
        return bloques;
    }

    public void setBloques(Lista<Bloque> bloques) {
        this.bloques = bloques;
    }
    
    
}
