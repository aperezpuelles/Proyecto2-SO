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
public class TablaHash<K, V> {
    private static final int TAMANO = 10; 
    private Nodo<NodoHash<K, V>>[] tabla; 


    public TablaHash() {
        tabla = new Nodo[TAMANO]; 
    }


    private int funcionHash(K clave) {
        int hash = clave.hashCode(); 
        return Math.abs(hash) % TAMANO; 
    }


    public void put(K clave, V valor) {
        int indice = funcionHash(clave); 
        Nodo<NodoHash<K, V>> actual = tabla[indice];

        if (actual == null) {
           
            tabla[indice] = new Nodo<>(new NodoHash<>(clave, valor));
        } else {
            
            while (actual != null) {
                if (actual.getData().clave.equals(clave)) {
                    actual.getData().valor = valor; 
                    return;
                }
                actual = actual.getNext();
            }

            Nodo<NodoHash<K, V>> nuevoNodo = new Nodo<>(new NodoHash<>(clave, valor));
            nuevoNodo.setNext(tabla[indice]);
            tabla[indice] = nuevoNodo;
        }
    }


    public V get(K clave) {
        int indice = funcionHash(clave);
        Nodo<NodoHash<K, V>> actual = tabla[indice];

        while (actual != null) {
            if (actual.getData().clave.equals(clave)) {
                return actual.getData().valor; 
            }
            actual = actual.getNext();
        }
        return null; 
    }
}
