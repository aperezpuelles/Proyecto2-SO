/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2.so;

/**
 *
 * @author casti
 */
public class Lista<T> {
    private Nodo<T> head;
    private int size;

    public Lista() {
        this.head = null;
        this.size = 0;
    }

    public Nodo<T> getHead() {
        return head;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T data) {
        Nodo<T> newNode = new Nodo<>(data);
        newNode.setNext(head);
        head = newNode;
        size++;
    }
    
    public void addLast(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (head == null) {
            head = nuevoNodo;
        } else {
            Nodo<T> actual = head;
            while (actual.getNext() != null) {
                actual = actual.getNext();
            }
            actual.setNext(nuevoNodo);
        }
    }

    public T get(int index) {
        Nodo<T> current = head;
        int count = 0;
        while (current != null) {
            if (count == index) {
                return current.getData();
            }
            count++;
            current = current.getNext();
        }
        return null;
    }

    public int size() {
        return size;
    }
    
    public void set(int index, T data) {
        Nodo<T> current = head;
        int count = 0;
        while (current != null) {
            if (count == index) {
                current.setData(data);
                return;
            }
            count++;
            current = current.getNext();
        }
    }
    
    public void print() {
        Nodo<T> actual = head;
        while (actual != null) {
            System.out.println(actual.getData()); 
            actual = actual.getNext(); 
        }
    }
}
