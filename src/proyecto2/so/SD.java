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
public class SD {
    private int bloquesmax;
    private int bloquesrestantes;
    private Lista<Bloque> bloques;

    public SD(int bloquesmax, int bloquesrestantes) {
        this.bloquesmax = bloquesmax;
        this.bloquesrestantes = bloquesrestantes;
        this.bloques = new Lista<>();
        
        for (int i = 1; i < bloquesmax; i++) {
            Bloque nuevo = new Bloque("");
            bloques.add(nuevo);
        }        
    }

    public int getBloquesmax() {
        return bloquesmax;
    }

    public void setBloquesmax(int bloquesmax) {
        this.bloquesmax = bloquesmax;
    }

    public int getBloquesrestantes() {
        return bloquesrestantes;
    }

    public void setBloquesrestantes(int bloquesrestantes) {
        this.bloquesrestantes = bloquesrestantes;
    }

    public Lista<Bloque> getBloques() {
        return bloques;
    }

    public void setBloques(Lista<Bloque> bloques) {
        this.bloques = bloques;
    }
    
    
}
