/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Excepciones.ClaveNoExisteExcepcion;
import java.util.List;

/**
 *
 * @author TEO
 * @param <K>
 * @param<V>
 */
public interface IArbolBusqueda <K extends Comparable<K> , V> {
    
    void insertar(K claveAInsertar , V valorAsociado); 
    V eliminar(K claveAEliminar) throws ClaveNoExisteExcepcion;
    V buscar(K clave); 
    boolean contiene(K clave); 
    int size(); 
    int altura();
    void vaciar();
    boolean esArbolVacio();
    boolean esHoja(K clave);
    byte getCantHoja();
    int nivel();
    List<K> recorridoInOrden();
    List<K> recorridoPreOrden();
    List<K> recorridoPostOrden();
    List<K> recorridoPorNiveles();
    
    
}