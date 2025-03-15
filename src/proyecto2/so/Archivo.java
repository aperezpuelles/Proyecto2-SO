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
public class Archivo {
    private String nombre;
    private double bloquesAsignados;
    private int primerBloque;
    private Color color;

    public Archivo(String nombre, double bloquesAsignados, Color color) {
        this.nombre = nombre;
        this.bloquesAsignados = bloquesAsignados;
        this.primerBloque = 0;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getBloquesAsignados() {
        return bloquesAsignados;
    }

    public void setBloquesAsignados(double bloquesAsignados) {
        this.bloquesAsignados = bloquesAsignados;
    }

    public int getPrimerBloque() {
        return primerBloque;
    }

    public void setPrimerBloque(int primerBloque) {
        this.primerBloque = primerBloque;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
}
