/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.JOptionPane;
import proyecto2.so.Archivo;
import proyecto2.so.Bloque;
import proyecto2.so.Lista;
import proyecto2.so.Nodo;
import proyecto2.so.SD;

/**
 *
 * @author Ignacio
 */
public class Menu extends javax.swing.JFrame {
    private SD sd;
    private PanelDisco panelDisco;
    private Lista<Archivo> archivos;

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        iniciarSimulacion();
        cargarDesdeJSON();
    }

    private void iniciarSimulacion() {
        archivos = new Lista<>();
        sd = new SD(40, 40);
        panelDisco = new PanelDisco(sd);

        panelBloques.setLayout(new java.awt.BorderLayout());
        panelBloques.add(panelDisco);
        panelBloques.revalidate();
        panelBloques.repaint();
    }
    
    private void mostrarDialogoCrearArchivo() {
        CrearArchivoDialog dialog = new CrearArchivoDialog(this);
        dialog.setVisible(true);

        if (dialog.isAceptado()) {
            String nombre = dialog.getNombreArchivo();
            double tamano = dialog.getTamanoArchivo();
            Color color = dialog.getColorArchivo();

            if (nombre.isEmpty() || tamano <= 0) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (sd.getBloquesrestantes() < Math.ceil(tamano)) {
                JOptionPane.showMessageDialog(this, "No hay suficientes bloques disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Archivo nuevoArchivo = new Archivo(nombre, tamano, color);
            archivos.add(nuevoArchivo);
            sd.setBloquesrestantes((int) (sd.getBloquesrestantes() - tamano));

            sd.asignarBloques(nuevoArchivo);
            panelDisco.actualizarVista();
            guardarEnJSON();

            JOptionPane.showMessageDialog(this, "Archivo creado:\nNombre: " + nombre + "\nTamaño: " + tamano + " bloques\nColor: " + obtenerNombreColor(color));
            }
        }
    
    private void guardarEnJSON() {
        JSONArray jsonArray = new JSONArray();
        Nodo<Archivo> actualArchivo = archivos.getHead();

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

            if (archivo.getBloques().getHead() != null) {
                jsonArchivo.put("primerBloque", archivo.getBloques().getHead().getData().getNumero());
            } else {
                jsonArchivo.put("primerBloque", -1);  
            }

            jsonArray.put(jsonArchivo);
            actualArchivo = actualArchivo.getNext();
        }

        try (FileWriter file = new FileWriter("archivos.json")) {
            file.write(jsonArray.toString(4)); 
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarDesdeJSON() {
        File archivoJson = new File("archivos.json");
        if (!archivoJson.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoJson))) {
            StringBuilder jsonStr = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                jsonStr.append(linea);
            }

            JSONArray jsonArray = new JSONArray(jsonStr.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArchivo = jsonArray.getJSONObject(i);
                String nombre = jsonArchivo.getString("nombre");
                double bloquesAsignados = jsonArchivo.getDouble("bloquesAsignados");
                Color color = obtenerColorDesdeNombre(jsonArchivo.getString("color"));

                Archivo archivo = new Archivo(nombre, bloquesAsignados, color);

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

                archivos.add(archivo);
            }
            panelDisco.actualizarVista();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBloques = new javax.swing.JPanel();
        btnCrearArchivo = new javax.swing.JButton();
        btnModificarArchivo = new javax.swing.JButton();

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
        getContentPane().add(btnCrearArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, -1, -1));

        btnModificarArchivo.setText("Modificar Archivo");
        btnModificarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificarArchivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearArchivoActionPerformed
        mostrarDialogoCrearArchivo();
    }//GEN-LAST:event_btnCrearArchivoActionPerformed

    private void btnModificarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarArchivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificarArchivoActionPerformed

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
    private javax.swing.JButton btnCrearArchivo;
    private javax.swing.JButton btnModificarArchivo;
    private javax.swing.JPanel panelBloques;
    // End of variables declaration//GEN-END:variables
}
