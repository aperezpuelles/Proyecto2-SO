/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import proyecto2.so.Archivo;
import proyecto2.so.Bloque;
import proyecto2.so.Directorio;
import proyecto2.so.Lista;
import proyecto2.so.Nodo;
import proyecto2.so.SD;

/**
 *
 * @author Ignacio
 */
public class Menu extends javax.swing.JFrame {
    private DefaultTableModel tablaArchivos;
    private SD sd;
    private PanelDisco panelDisco;
    private Lista<Archivo> archivos;
    private Directorio raizSD;
    private DefaultTreeModel modeloArbol;

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        iniciarSimulacion();
        cargarEstructuraJson();
        configurarTablas();
        
        arbolDirectorio.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { 
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) arbolDirectorio.getLastSelectedPathComponent();

                if (selectedNode != null && selectedNode.isLeaf()) { 
                    String nombreArchivo = selectedNode.getUserObject().toString().replace(".txt", "");

                    Archivo archivoSeleccionado = buscarArchivoPorNombre(nombreArchivo);
                    if (archivoSeleccionado != null) {
                        mostrarDialogoArchivo(archivoSeleccionado);
                    }
                }
            }
        }
    });
    }

    private void iniciarSimulacion() {
        archivos = new Lista<>();
        sd = new SD(40, 40);
        panelDisco = new PanelDisco(sd);
        
        raizSD = new Directorio("SD", null);
        DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode("SD");
        modeloArbol = new DefaultTreeModel(nodoRaiz);
        arbolDirectorio.setModel(modeloArbol);

        panelBloques.setLayout(new java.awt.BorderLayout());
        panelBloques.add(panelDisco);
        panelBloques.revalidate();
        panelBloques.repaint();
    }
    
    private void configurarTablas() {
        tablaArchivos = new DefaultTableModel(new String[]{"Nombre", "Bloque Inicial", "Longitud", "Color"}, 0);
        tblArchivos.setModel(tablaArchivos);
        actualizarTablas();
    }
    
    private void actualizarTablas() {
        tablaArchivos.setRowCount(0);
        if (archivos.getHead() == null){
            return;
        }
        Nodo<Archivo> actual = archivos.getHead();
        while (actual != null){
            Archivo archivo = actual.getData();
            tablaArchivos.addRow(new Object[]{archivo.getNombre(), archivo.getPrimerBloque().getNumero(), archivo.getBloquesAsignados(), obtenerNombreColor(archivo.getColor())});
            actual = actual.getNext();
        }
    }
    
    private void mostrarDialogoArchivo(Archivo archivo) {
        String mensaje = "Nombre: " + archivo.getNombre() + "\nTamaño: " + archivo.getBloquesAsignados();

        JOptionPane.showMessageDialog(this, mensaje, "Información del Archivo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarDialogoCrearArchivo() {
        Lista<String> listaDirectorios = obtenerListaDirectorios(raizSD, "SD");
        String[] directoriosDisponibles = convertirListaAArray(listaDirectorios);
        CrearArchivoDialog dialog = new CrearArchivoDialog(this, directoriosDisponibles);
        dialog.setVisible(true);

        if (dialog.isAceptado()) {
            String nombre = dialog.getNombreArchivo();
            double tamano = dialog.getTamanoArchivo();
            Color color = dialog.getColorArchivo();
            String directorioDestino = dialog.getDirectorioSeleccionado();

            if (nombre.isEmpty() || tamano <= 0) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sd.getBloquesrestantes() < Math.ceil(tamano)) {
                JOptionPane.showMessageDialog(this, "No hay suficientes bloques disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Directorio dirDestino = buscarDirectorioPorRuta(raizSD, directorioDestino);
            if (dirDestino == null) {
                JOptionPane.showMessageDialog(this, "Error: No se encontró el directorio seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Archivo nuevoArchivo = new Archivo(nombre, tamano, color);
            dirDestino.agregarArchivo(nuevoArchivo);
            archivos.add(nuevoArchivo); 

            sd.asignarBloques(nuevoArchivo);
            panelDisco.actualizarVista();
            actualizarTablas();
            actualizarJTree();
            guardarEstructuraJson(); 
            
            JOptionPane.showMessageDialog(this, "Archivo creado en " + directorioDestino + ":\nNombre: " + nombre + "\nTamaño: " + tamano + " bloques\nColor: " + obtenerNombreColor(color));
        }
    }
    
    private void mostrarDialogoModificarArchivo() {
        if (archivos.getHead() == null) { 
            JOptionPane.showMessageDialog(this, "No hay archivos creados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] nombresArchivos = obtenerListaNombresArchivos();
        String seleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona un archivo para modificar:",
                "Modificar Archivo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresArchivos,
                nombresArchivos[0]
        );

        if (seleccionado == null) return;

        Archivo archivoAModificar = buscarArchivoPorNombre(seleccionado);
        if (archivoAModificar == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModificarArchivoDialog dialog = new ModificarArchivoDialog(this, archivoAModificar.getNombre());
        dialog.setVisible(true);

        if (dialog.isAceptado()) {
            String nuevoNombre = dialog.getNombreArchivo();

            if (nuevoNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            archivoAModificar.setNombre(nuevoNombre);
            actualizarTablas();
            actualizarJTree();
            guardarEstructuraJson();

            JOptionPane.showMessageDialog(this, "Archivo modificado con éxito.");
        }
    }
    
    private void mostrarDialogoBorrarArchivo() {
        if (archivos.getHead() == null) { 
            JOptionPane.showMessageDialog(this, "No hay archivos creados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] nombresArchivos = obtenerListaNombresArchivos();
        String seleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona un archivo para borrar:",
                "Borrar Archivo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresArchivos,
                nombresArchivos[0]
        );

        if (seleccionado == null) return;

        Archivo archivoABorrar = buscarArchivoPorNombre(seleccionado);
        if (archivoABorrar == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Nodo<Bloque> actual = archivoABorrar.getBloques().getHead();
        int bloquesLiberados = 0;

        while (actual != null) {
            if (actual.getData().isOcupado()) {
                actual.getData().setOcupado(false);
                bloquesLiberados++;
            }
            actual = actual.getNext();
        }

        archivoABorrar.getBloques().clear(); 
        sd.aumentarBloquesRestantes(bloquesLiberados);

        Directorio dirPadre = buscarDirectorioQueContieneArchivo(raizSD, archivoABorrar);
        if (dirPadre != null) {
            dirPadre.eliminarArchivo(archivoABorrar);
        }

        archivos.delete(archivoABorrar);

        actualizarTablas();
        panelDisco.actualizarVista();
        actualizarJTree();
        guardarEstructuraJson(); 

        JOptionPane.showMessageDialog(this, "Archivo eliminado con éxito.");
    }
    
    private void mostrarDialogoCrearDirectorio() {
        Lista<String> listaDirectorios = obtenerListaDirectorios(raizSD, "SD");
        String[] directoriosDisponibles = convertirListaAArray(listaDirectorios);

        CrearDirectorioDialog dialog = new CrearDirectorioDialog(this, directoriosDisponibles);
        dialog.setVisible(true);

        if (dialog.isAceptado()) {
            String nombre = dialog.getNombreDirectorio();
            String padreRuta = dialog.getDirectorioPadre();

            Directorio dirPadre = buscarDirectorioPorRuta(raizSD, padreRuta);
            if (dirPadre != null) {
                Directorio nuevoDir = new Directorio(nombre, dirPadre);
                dirPadre.agregarSubdirectorio(nuevoDir);
                actualizarJTree();
                guardarEstructuraJson();
                JOptionPane.showMessageDialog(this, "Directorio creado en " + dirPadre.getNombre() + "s\nNombre: " + nombre);
            } else {
                JOptionPane.showMessageDialog(this, "Error: No se encontró el directorio padre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarDialogoBorrarDirectorio() {
        if (raizSD.getSubdirectorios().getHead() == null) { 
            JOptionPane.showMessageDialog(this, "No hay directorios creados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Lista<String> listaDirectorios = obtenerListaDirectorios(raizSD, "SD");
        String[] directoriosDisponibles = convertirListaAArray(listaDirectorios);
        String seleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona un directorio para borrar:",
                "Borrar Directorio",
                JOptionPane.QUESTION_MESSAGE,
                null,
                directoriosDisponibles,
                directoriosDisponibles[0]
        );

        if (seleccionado == null) return;

        Directorio directorioABorrar = buscarDirectorioPorRuta(raizSD, seleccionado);
        if (directorioABorrar == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el directorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        liberarBloquesDeArchivos(directorioABorrar);

        Directorio dirPadre = buscarDirectorioPadre(raizSD, directorioABorrar);
        if (dirPadre != null) {
            dirPadre.eliminarSubdirectorio(directorioABorrar);
        }

        actualizarTablas();
        panelDisco.actualizarVista();
        actualizarJTree();
        guardarEstructuraJson(); 

        JOptionPane.showMessageDialog(this, "Directorio eliminado con éxito.");
    }
    
    private void mostrarDialogoModificarDirectorio() {
        if (raizSD.getSubdirectorios().getHead() == null) { 
            JOptionPane.showMessageDialog(this, "No hay directorios creados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Lista<String> listaDirectorios = obtenerListaDirectorios(raizSD, "SD");
        String[] directoriosDisponibles = convertirListaAArray(listaDirectorios);
        String seleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona un directorio para modificar:",
                "Modificar Directorio",
                JOptionPane.QUESTION_MESSAGE,
                null,
                directoriosDisponibles,
                directoriosDisponibles[0]
        );

        if (seleccionado == null) return;

        Directorio directorioAModificar = buscarDirectorioPorRuta(raizSD, seleccionado);
        if (directorioAModificar == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el directorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModificarDirectorioDialog dialog = new ModificarDirectorioDialog(this, directorioAModificar.getNombre());
        dialog.setVisible(true);

        if (dialog.isAceptado()) {
            String nuevoNombre = dialog.getNombreDirectorio();

            if (nuevoNombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            directorioAModificar.setNombre(nuevoNombre);
            actualizarTablas();
            actualizarJTree();
            guardarEstructuraJson();

            JOptionPane.showMessageDialog(this, "Directorio modificado con éxito.");
        }
    }
    
    private JSONObject convertirDirectorioAJson(Directorio dir) {
        JSONObject jsonDir = new JSONObject();
        jsonDir.put("nombre", dir.getNombre());

        JSONArray jsonSubdirectorios = new JSONArray();
        Nodo<Directorio> actualDir = dir.getSubdirectorios().getHead();
        while (actualDir != null) {
            jsonSubdirectorios.put(convertirDirectorioAJson(actualDir.getData())); 
            actualDir = actualDir.getNext();
        }
        jsonDir.put("subdirectorios", jsonSubdirectorios);

        JSONArray jsonArchivos = new JSONArray();
        Nodo<Archivo> actualArchivo = dir.getArchivos().getHead();
        while (actualArchivo != null) {
            Archivo archivo = actualArchivo.getData();
            JSONObject jsonArchivo = new JSONObject();
            jsonArchivo.put("nombre", archivo.getNombre());
            jsonArchivo.put("bloquesAsignados", archivo.getBloquesAsignados());
            jsonArchivo.put("color", obtenerNombreColor(archivo.getColor()));

            JSONArray jsonBloques = new JSONArray();
            Nodo<Bloque> actualBloque = archivo.getBloques().getHead();
            while (actualBloque != null) {
                jsonBloques.put(actualBloque.getData().getNumero());
                actualBloque = actualBloque.getNext();
            }
            jsonArchivo.put("bloques", jsonBloques);

            if (archivo.getPrimerBloque() != null) {
                jsonArchivo.put("primerBloque", archivo.getPrimerBloque().getNumero());
            } else {
                jsonArchivo.put("primerBloque", -1);
            }

            jsonArchivos.put(jsonArchivo);
            actualArchivo = actualArchivo.getNext();
        }
        jsonDir.put("archivos", jsonArchivos);

        return jsonDir;
    }
    
    private void guardarEstructuraJson() {
        JSONObject jsonRaiz = convertirDirectorioAJson(raizSD);

        try (FileWriter file = new FileWriter("directorios.json")) {
            file.write(jsonRaiz.toString(4));
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarEstructuraJson() {
        File archivoJson = new File("directorios.json");
        if (!archivoJson.exists()) return; 

        archivos = new Lista<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoJson))) {
            StringBuilder jsonStr = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                jsonStr.append(linea);
            }

            JSONObject jsonRaiz = new JSONObject(jsonStr.toString());
            raizSD = convertirJsonADirectorio(jsonRaiz, null);
            panelDisco.actualizarVista();
            actualizarJTree(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Directorio convertirJsonADirectorio(JSONObject jsonDir, Directorio padre) {
        Directorio dir = new Directorio(jsonDir.getString("nombre"), padre);

        JSONArray jsonSubdirectorios = jsonDir.getJSONArray("subdirectorios");
        for (int i = 0; i < jsonSubdirectorios.length(); i++) {
            dir.agregarSubdirectorio(convertirJsonADirectorio(jsonSubdirectorios.getJSONObject(i), dir));
        }

        JSONArray jsonArchivos = jsonDir.getJSONArray("archivos");
        for (int i = 0; i < jsonArchivos.length(); i++) {
            JSONObject jsonArchivo = jsonArchivos.getJSONObject(i);
            String nombre = jsonArchivo.getString("nombre");
            double tamano = jsonArchivo.getDouble("bloquesAsignados");
            Color color = obtenerColorDesdeNombre(jsonArchivo.getString("color"));

            Archivo archivo = new Archivo(nombre, tamano, color);

            JSONArray jsonBloques = jsonArchivo.getJSONArray("bloques");
            for (int j = 0; j < jsonBloques.length(); j++) {
                int numBloque = jsonBloques.getInt(j);
                Bloque bloque = sd.obtenerBloquePorNumero(numBloque);
                archivo.getBloques().add(bloque);
                bloque.setOcupado(true);
                bloque.setColor(color);
            }

            int primerBloqueNum = jsonArchivo.getInt("primerBloque");
            if (primerBloqueNum != -1) {
                archivo.setPrimerBloque(sd.obtenerBloquePorNumero(primerBloqueNum));
            }

            dir.agregarArchivo(archivo);
            archivos.add(archivo);
        }

        return dir;
    }
    
    private String obtenerNombreColor(Color color) {
        if (color.equals(Color.RED)) return "Rojo";
        if (color.equals(Color.GREEN)) return "Verde";
        if (color.equals(Color.BLUE)) return "Azul";
        if (color.equals(Color.YELLOW)) return "Amarillo";
        if (color.equals(new Color(255, 165, 0))) return "Naranja"; 
        if (color.equals(new Color(128, 0, 128))) return "Morado"; 
        return "Personalizado (RGB: " + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }

    private Color obtenerColorDesdeNombre(String nombre) {
        switch (nombre) {
            case "Rojo": return Color.RED;
            case "Verde": return Color.GREEN;
            case "Azul": return Color.BLUE;
            case "Amarillo": return Color.YELLOW;
            case "Naranja": return new Color(255, 165, 0);
            case "Morado": return new Color(128, 0, 128);
            default:
                String[] partes = nombre.replace("Personalizado (", "").replace(")", "").split(",");
                return new Color(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
        }
    
    }
    
    private String[] obtenerListaNombresArchivos() {
        Nodo<Archivo> actual = archivos.getHead();
        int size = archivos.size();
        String[] nombres = new String[size];

        int i = 0;
        while (actual != null) {
            nombres[i] = actual.getData().getNombre();
            actual = actual.getNext();
            i++;
        }

        return nombres;
    }
    
    private Archivo buscarArchivoPorNombre(String nombre) {
        Nodo<Archivo> actual = archivos.getHead();
        while (actual != null) {
            if (actual.getData().getNombre().equals(nombre)) {
                return actual.getData();
            }
            actual = actual.getNext();
        }
        return null;
    }
    
    private Lista<String> obtenerListaDirectorios(Directorio raiz, String ruta) {
        Lista<String> lista = new Lista<>();
        lista.add(ruta);

        Nodo<Directorio> actual = raiz.getSubdirectorios().getHead();
        while (actual != null) {
            Lista<String> subdirectorios = obtenerListaDirectorios(actual.getData(), ruta + "/" + actual.getData().getNombre());
            concatenarListas(lista, subdirectorios); 
            actual = actual.getNext();
        }

        return lista;
    }

    private void concatenarListas(Lista<String> listaPrincipal, Lista<String> listaAgregar) {
        Nodo<String> actual = listaAgregar.getHead();
        while (actual != null) {
            listaPrincipal.add(actual.getData()); 
            actual = actual.getNext();
        }
    }


    private String[] convertirListaAArray(Lista<String> lista) {
        int size = lista.size(); 
        String[] array = new String[size];

        Nodo<String> actual = lista.getHead();
        int index = 0;
        while (actual != null) {
            array[index++] = actual.getData();
            actual = actual.getNext();
        }

        return array;
    }
    
    private Directorio buscarDirectorioPorRuta(Directorio raiz, String ruta) {
        String[] partes = ruta.split("/"); 
        Directorio actual = raiz;

        for (int i = 1; i < partes.length; i++) { 
            Nodo<Directorio> nodo = actual.getSubdirectorios().getHead();
            boolean encontrado = false;

            while (nodo != null) {
                if (nodo.getData().getNombre().equals(partes[i])) {
                    actual = nodo.getData();
                    encontrado = true;
                    break;
                }
                nodo = nodo.getNext();
            }

            if (!encontrado) return null; 
        }

        return actual;
    }
    
    private void actualizarJTree() {
        DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode("SD");
        agregarNodos(raizSD, nodoRaiz);
        modeloArbol.setRoot(nodoRaiz);
        modeloArbol.reload();
    }

    private void agregarNodos(Directorio dir, DefaultMutableTreeNode nodoPadre) {
        Nodo<Directorio> actualDir = dir.getSubdirectorios().getHead();
        while (actualDir != null) {
            DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(actualDir.getData().getNombre());
            nodoPadre.add(nodo);
            agregarNodos(actualDir.getData(), nodo);
            actualDir = actualDir.getNext();
        }

        Nodo<Archivo> actualArchivo = dir.getArchivos().getHead();
        while (actualArchivo != null) {
            DefaultMutableTreeNode nodoArchivo = new DefaultMutableTreeNode(actualArchivo.getData().getNombre() + ".txt");
            nodoPadre.add(nodoArchivo);
            actualArchivo = actualArchivo.getNext();
        }
    }
    
    private Directorio buscarDirectorioQueContieneArchivo(Directorio raiz, Archivo archivo) {
        Nodo<Archivo> actualArchivo = raiz.getArchivos().getHead();
        while (actualArchivo != null) {
            if (actualArchivo.getData().equals(archivo)) {
                return raiz;
            }
            actualArchivo = actualArchivo.getNext();
        }

        Nodo<Directorio> actualDir = raiz.getSubdirectorios().getHead();
        while (actualDir != null) {
            Directorio encontrado = buscarDirectorioQueContieneArchivo(actualDir.getData(), archivo);
            if (encontrado != null) {
                return encontrado;
            }
            actualDir = actualDir.getNext();
        }

        return null; 
    }
    
    private void liberarBloquesDeArchivos(Directorio dir) {
        Nodo<Archivo> actualArchivo = dir.getArchivos().getHead();
        while (actualArchivo != null) {
            Archivo archivo = actualArchivo.getData();
            archivo.liberarBloques(sd);
            archivos.delete(archivo); 
            actualArchivo = actualArchivo.getNext();
        }

        Nodo<Directorio> actualDir = dir.getSubdirectorios().getHead();
        while (actualDir != null) {
            liberarBloquesDeArchivos(actualDir.getData());
            actualDir = actualDir.getNext();
        }
    }
    
    private Directorio buscarDirectorioPadre(Directorio raiz, Directorio hijo) {
        Nodo<Directorio> actual = raiz.getSubdirectorios().getHead();

        while (actual != null) {
            if (actual.getData().equals(hijo)) {
                return raiz; 
            }

            Directorio resultado = buscarDirectorioPadre(actual.getData(), hijo);
            if (resultado != null) {
                return resultado;
            }

            actual = actual.getNext();
        }

        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        panelBloques = new javax.swing.JPanel();
        btnCrearArchivo = new javax.swing.JButton();
        btnModificarArchivo = new javax.swing.JButton();
        btnBorrarArchivo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblArchivos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        arbolDirectorio = new javax.swing.JTree();
        btnCrearDirectorio = new javax.swing.JButton();
        btnBorrarDirectorio = new javax.swing.JButton();
        btnModificarDirectorio = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane4 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 500));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout panelBloquesLayout = new javax.swing.GroupLayout(panelBloques);
        panelBloques.setLayout(panelBloquesLayout);
        panelBloquesLayout.setHorizontalGroup(
            panelBloquesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        panelBloquesLayout.setVerticalGroup(
            panelBloquesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(panelBloques, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 340, 150));

        btnCrearArchivo.setText("Crear Archivo");
        btnCrearArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(btnCrearArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 450, -1, -1));

        btnModificarArchivo.setText("Modificar Archivo");
        btnModificarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 450, -1, -1));

        btnBorrarArchivo.setText("Borrar Archivo");
        btnBorrarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 450, -1, -1));

        tblArchivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblArchivos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 340, -1, 90));

        jScrollPane3.setViewportView(arbolDirectorio);

        jScrollPane2.setViewportView(jScrollPane3);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 480, 250));

        btnCrearDirectorio.setText("Crear Directorio");
        btnCrearDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearDirectorioActionPerformed(evt);
            }
        });
        getContentPane().add(btnCrearDirectorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, -1));

        btnBorrarDirectorio.setText("Borrar Directorio");
        btnBorrarDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarDirectorioActionPerformed(evt);
            }
        });
        getContentPane().add(btnBorrarDirectorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 120, -1));

        btnModificarDirectorio.setText("Modificar Directorio");
        btnModificarDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarDirectorioActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificarDirectorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, -1, -1));
        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        getContentPane().add(jTabbedPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearArchivoActionPerformed
        mostrarDialogoCrearArchivo();
    }//GEN-LAST:event_btnCrearArchivoActionPerformed

    private void btnModificarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarArchivoActionPerformed
        mostrarDialogoModificarArchivo();
    }//GEN-LAST:event_btnModificarArchivoActionPerformed

    private void btnBorrarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarArchivoActionPerformed
        mostrarDialogoBorrarArchivo();
    }//GEN-LAST:event_btnBorrarArchivoActionPerformed

    private void btnCrearDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearDirectorioActionPerformed
        mostrarDialogoCrearDirectorio();
    }//GEN-LAST:event_btnCrearDirectorioActionPerformed

    private void btnBorrarDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarDirectorioActionPerformed
        mostrarDialogoBorrarDirectorio();
    }//GEN-LAST:event_btnBorrarDirectorioActionPerformed

    private void btnModificarDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarDirectorioActionPerformed
        mostrarDialogoModificarDirectorio();
    }//GEN-LAST:event_btnModificarDirectorioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree arbolDirectorio;
    private javax.swing.JButton btnBorrarArchivo;
    private javax.swing.JButton btnBorrarDirectorio;
    private javax.swing.JButton btnCrearArchivo;
    private javax.swing.JButton btnCrearDirectorio;
    private javax.swing.JButton btnModificarArchivo;
    private javax.swing.JButton btnModificarDirectorio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JPanel panelBloques;
    private javax.swing.JTable tblArchivos;
    // End of variables declaration//GEN-END:variables
}
