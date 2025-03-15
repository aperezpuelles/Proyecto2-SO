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
public class NodoHash<K, V> {
    K clave;
    V valor;

    public NodoHash(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }
}
