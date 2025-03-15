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
public class Bloque {
    private String nombre;
    private int numero;
    private boolean ocupado;
    private Color color;

    public Bloque(String nombre, int numero) {
        this.nombre = nombre;
        this.numero = numero;
        this.ocupado = false;
        this.color = Color.WHITE;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
