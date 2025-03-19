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
public class Directorio {
    private String nombre;
    private Lista<Directorio> subdirectorios;
    private Lista<Archivo> archivos;
    private Directorio padre;

    public Directorio(String nombre, Directorio padre) {
        this.nombre = nombre;
        this.padre = padre;
        this.subdirectorios = new Lista<>();
        this.archivos = new Lista<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Lista<Directorio> getSubdirectorios() {
        return subdirectorios;
    }

    public void setSubdirectorios(Lista<Directorio> subdirectorios) {
        this.subdirectorios = subdirectorios;
    }

    public Lista<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(Lista<Archivo> archivos) {
        this.archivos = archivos;
    }

    public Directorio getPadre() {
        return padre;
    }

    public void setPadre(Directorio padre) {
        this.padre = padre;
    }
    
    public void agregarSubdirectorio(Directorio dir) {
        subdirectorios.add(dir);
    }

    public void agregarArchivo(Archivo archivo) {
        archivos.add(archivo);
    }
    
    public void eliminarArchivo(Archivo archivo) {
        this.archivos.delete(archivo);
    }
}
