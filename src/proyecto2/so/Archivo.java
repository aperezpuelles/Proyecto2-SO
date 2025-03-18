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
    private Bloque primerBloque;
    private Color color;
    private Lista<Bloque> bloques;

    public Archivo(String nombre, double bloquesAsignados, Color color) {
        this.nombre = nombre;
        this.bloquesAsignados = bloquesAsignados;
        this.color = color;
        this.bloques = new Lista<>();
    }
    
    public void printBloques(){
        Nodo<Bloque> actual = bloques.getHead();
        while (actual != null) {
            System.out.println(actual.getData().getNumero()); 
            actual = actual.getNext(); 
        }
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

    public Bloque getPrimerBloque() {
        return primerBloque;
    }

    public void setPrimerBloque(Bloque primerBloque) {
        this.primerBloque = primerBloque;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Lista<Bloque> getBloques() {
        return bloques;
    }

    public void setBloques(Lista<Bloque> bloques) {
        this.bloques = bloques;
    }
    
    
}
