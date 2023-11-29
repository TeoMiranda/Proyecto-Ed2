/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Excepciones.ClaveNoExisteExcepcion;
import Excepciones.OrdenInvalidoException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author TEO
 * @param <K>
 * @param <V>
 */
public class ArbolMViasBusqueda<K extends Comparable<K>, V> implements IArbolBusqueda<K, V>{
    
     protected NodoMVias<K, V> raiz;
    protected final int orden;

    protected static final int POSICION_NO_VALIDA = -1;
    protected static final int ORDEN_MINIMO = 3;

    public ArbolMViasBusqueda() {
        this.orden = ArbolMViasBusqueda.ORDEN_MINIMO;
    }

    public ArbolMViasBusqueda(int orden) throws OrdenInvalidoException {//llevar al paquete exepciones
        if (orden < ArbolMViasBusqueda.ORDEN_MINIMO) {
            throw new OrdenInvalidoException();
        }
        this.orden = orden;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("clave invalidad, no se aceptan claves nulas");
        }
        if (valorAsociado == null) {
            throw new IllegalArgumentException("valor invalido, no se aceptan valores nulos");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
            return;
        }

        NodoMVias<K, V> nodoActual = this.raiz;
        do {
            int posicionDeClave = this.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != ArbolMViasBusqueda.POSICION_NO_VALIDA) {// si ya existe en arbol
                nodoActual.setValor(posicionDeClave, valorAsociado);
                nodoActual = NodoMVias.nodoVacio();
            } else if (nodoActual.esHoja()) {
                //System.out.println("llenas ? " + nodoActual.estanClavesLlenas());
                //System.out.println(nodoActual.toString());
                if (nodoActual.estanClavesLlenas()) {
                    int posicionPorDondeBajar = obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);//---TAREA--------------
                    NodoMVias<K, V> nuevoNodo = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
                    //System.out.println("bajar por " + posicionPorDondeBajar);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoNodo);
                } else {
                    insertarClaveYValorOrdenadamenteEnNodo(nodoActual, claveAInsertar, valorAsociado);   // TAREA----------------- 
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posicionPorDondeBajar = obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                    NodoMVias<K, V> nuevoNodo = new NodoMVias<>(this.orden, claveAInsertar, valorAsociado);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoNodo);
                    //nodoActual = NodoMVias.nodoVacio();    // por que en vacio ? 
                    //System.out.println("se hizo un break ");
                    break;
                } else {
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        } while (!NodoMVias.esNodoVacio(nodoActual));

    }

    protected int obtenerPosicionDeClave(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveEnTurno = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveEnTurno) == 0) {
                return i;
            }
        }

        return ArbolMViasBusqueda.POSICION_NO_VALIDA;
    }

    public void insertarClaveYValorOrdenadamenteEnNodo(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAsociado) {
        int i = 0;
        while (i < nodoActual.nroDeClavesNoVacias() && (claveAInsertar.compareTo(nodoActual.getClave(i)) > 0)) {
            i++;
        }
        if (i == nodoActual.nroDeClavesNoVacias()) {
            nodoActual.setClave(claveAInsertar, i);
            nodoActual.setValor(i, valorAsociado);
        } else {
            for (int j = nodoActual.nroDeClavesNoVacias(); j > i; j--) {
                nodoActual.setClave(nodoActual.getClave(j - 1), j);
                nodoActual.setValor(j, nodoActual.getValor(j - 1));
            }
            nodoActual.setClave(claveAInsertar, i);
            nodoActual.setValor(i, valorAsociado);
        }

    }

//    public int obtenerPosicionPorDondeBajar(NodoMVias<K, V> nodoActual, K claveAInsertar) {
//        int i = 0;
//        while (i < nodoActual.nroDeHijosQuePuedeTener() - 1 && (claveAInsertar.compareTo(nodoActual.getClave(i)) > 0)) {
//            i++;
//        }
//        return i;
//    }
    protected int obtenerPosicionPorDondeBajar(NodoMVias<K, V> nodoActual, K claveAInsertar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            if (claveAInsertar.compareTo(nodoActual.getClave(i)) < 0) {//si la clave a insertar es menor 
                return i;
            }
        }
        
        //si llego hasta aqui quiere decir que la clave a insertar es mayor que la ultima clave del nodo
        return (nodoActual.nroDeClavesNoVacias()); 
    }

    @Override
    public V eliminar(K claveAEliminar) throws ClaveNoExisteExcepcion {
        if (claveAEliminar == null) {
            throw new IllegalArgumentException("clave invalidad, no se aceptan claves nulas");
        }
        V valorAsociado = buscar(claveAEliminar);
        if (valorAsociado == null) {
            throw new ClaveNoExisteExcepcion();
        }

        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAsociado;
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveEnTurno = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveEnTurno) == 0) {
                if (nodoActual.esHoja()) {
                    eliminarClaveDePosicion(nodoActual, i); //----------------------------------------TAREA--------------------
                    if (!nodoActual.hayClavesNoVacias()) {
                        return NodoMVias.nodoVacio();
                    } else {
                        return nodoActual;
                    }
                } else {
                    K claveDeReemplazo;
                    if (hayHijosNoVaciosMasAdelante(nodoActual, i)) {//----------------------TAREA----------------------------
                        System.out.println("si hay mas hijo ");
                        claveDeReemplazo = obtenerSucesorInOrden(nodoActual, i); //----------------------TAREA----------------------------
                    } else {
                        System.out.println("no hay mas hijo ");
                        claveDeReemplazo = obtenerPredecesorInOrden(nodoActual, i);//----------------------TAREA----------------------------
                    }

                    V valorDeReemplazo = buscar(claveDeReemplazo);
                    nodoActual = eliminar(nodoActual, claveDeReemplazo);
                    nodoActual.setValor(i, valorDeReemplazo);
                    return nodoActual;
                }
            }
            if (claveAEliminar.compareTo(claveEnTurno) < 0) {
                NodoMVias<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        NodoMVias<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), claveAEliminar);
        nodoActual.setHijo(nodoActual.nroDeClavesNoVacias(), supuestoNuevoHijo);
        return nodoActual;
    }

    protected void eliminarClaveYValorDelNodo(NodoMVias<K, V> nodoActual, int i) {
        //el nodo es hoja por lo tanto no tienen hijos y no hace falta moverlos
        int cantDeClavesNoVacias = nodoActual.nroDeClavesNoVacias();
        nodoActual.setClave((K)NodoMVias.datoVacio(),i);
        nodoActual.setValor(i, (V)NodoMVias.datoVacio());

        if (i+1 < cantDeClavesNoVacias) {//verifica que tenga mas claves adelante
            //traemos las claves y valores de derecha hacia izq
            for (int j = i; j < cantDeClavesNoVacias-1; j++) {
                nodoActual.setClave( nodoActual.getClave(j+1),j);
                nodoActual.setValor(j, nodoActual.getValor(j+1));
            }

            //eliminar la clave y valor de la ultima posicion
            if (cantDeClavesNoVacias > 0) {
                nodoActual.setClave( (K)NodoMVias.datoVacio(),cantDeClavesNoVacias-1);
                nodoActual.setValor(cantDeClavesNoVacias-1, (V)NodoMVias.datoVacio());
            }
        }
    }
    
    public void eliminarClaveDePosicion(NodoMVias<K, V> nodoActual, int posicion) { //---------------------------------------
        int posicionDeLaClaveAEliminar = this.obtenerPosicionDeClave(nodoActual, nodoActual.getClave(posicion));
        if (nodoActual.estanClavesLlenas()) {
            for (int i = posicionDeLaClaveAEliminar; i < nodoActual.nroDeClavesNoVacias() - 1; i++) {
                nodoActual.setClave(nodoActual.getClave(i + 1), i);
            }
            nodoActual.setClave(null, nodoActual.nroDeHijosQuePuedeTener() - 2);
        } else {
            for (int i = posicionDeLaClaveAEliminar; i < nodoActual.nroDeClavesNoVacias(); i++) {
                nodoActual.setClave(nodoActual.getClave(i + 1), i);
            }
        }

    }

    public boolean hayHijosNoVaciosMasAdelante(NodoMVias<K, V> nodoActual, int i) {
        for (int j = i + 1; j < nodoActual.nroDeClavesNoVacias(); j++) {
            if (!nodoActual.esHijoVacio(j)) {
                return true;
            }
        }
        return false;
    }

    private K obtenerSucesorInOrden(NodoMVias<K, V> nodoActual, int i) {

        return nodoActual.getClave(i);
    }

    private K obtenerPredecesorInOrden(NodoMVias<K, V> nodoActual, int i) {

        return nodoActual.getClave(i);
    }

    @Override
    public V buscar(K claveABuscar) {
        if (!this.esArbolVacio()) {

            NodoMVias<K, V> nodoActual = this.raiz;

            do {
                boolean cambiaElNodoActual = false;
                for (int i = 0; i < nodoActual.nroDeClavesNoVacias()
                        && !cambiaElNodoActual; i++) {

                    K claveDelNodoActual = nodoActual.getClave(i);
                    if (claveABuscar.compareTo(claveDelNodoActual) == 0) {
                        return nodoActual.getValor(i);
                    }
                    if (claveABuscar.compareTo(claveDelNodoActual) < 0) {
                        nodoActual = nodoActual.getHijo(i);
                        cambiaElNodoActual = true;
                    }
                }

                if (!cambiaElNodoActual) {
                    nodoActual = nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
                }
            } while (!NodoMVias.esNodoVacio(nodoActual));
        }
        return null;
    }

    @Override
    public int size() {
        int cantidad = 0;
        if (!this.esArbolVacio()) {
            Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            do {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();    // retorna el valor y despues lo elimina de la cola
                cantidad++;
                for (int i = 0; i < orden; i++) {
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(orden));
                    }
                }

            } while (!colaDeNodos.isEmpty());
        }
        return cantidad;
    }

    public NodoMVias getNodo() {  // eliminar despues 
        return this.raiz;
    }

    public int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias() + 1; i++) {
            int alturaDeHijoActual = altura(nodoActual.getHijo(i));
            if (alturaDeHijoActual > alturaMayor) {
                alturaMayor = alturaDeHijoActual;
            }
        }
        return alturaMayor + 1;

    }

    public void Vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean contiene(K claveAVerificar) {
        return this.buscar(claveAVerificar) != null;
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public boolean esHoja(K clave) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public byte getCantHoja() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
    public int obtenerNivel(K clave) {
        return obtenerNivelRecursivo(raiz, clave, 0);
    }

    private int obtenerNivelRecursivo(NodoMVias<K, V> nodo, K clave, int nivel) {
        if (NodoMVias.esNodoVacio(nodo)) {
            return -1;
        }

        int posicionDeClave = obtenerPosicionDeClave(nodo, clave);

        if (posicionDeClave != POSICION_NO_VALIDA) {
            return nivel;
        } else {
            int posicionPorDondeBajar = obtenerPosicionPorDondeBajar(nodo, clave);
            return obtenerNivelRecursivo(nodo.getHijo(posicionPorDondeBajar), clave, nivel + 1);
        }
    }

    public List<K> mostrarClavesDeUnNivelDado(int nivelObjetivo) {  //modelo de examen ------------------------------------
        List<K> lista = new LinkedList<>();
        mostrarClavesDeUnNivelDado(this.raiz, lista, 0, nivelObjetivo);
        return lista;
    }

    private void mostrarClavesDeUnNivelDado(NodoMVias<K, V> nodoActual, List<K> lista, int nivelActual, int nivelObjetivo) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            if (nivelActual == nivelObjetivo) {
                lista.add((K) nodoActual.getClave(i));
            }

            mostrarClavesDeUnNivelDado(nodoActual.getHijo(i), lista, nivelActual + 1, nivelObjetivo);

        }
        mostrarClavesDeUnNivelDado(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), lista, nivelActual + 1, nivelObjetivo);
    }
    //-----------------------------------------------------------------------------------------------------------------------

    @Override
    public List<K> recorridoInOrden() {
        List<K> lista = new LinkedList<>();
        recorridoEnInOrden(this.raiz, lista);
        return lista;
    }

    private void recorridoEnInOrden(NodoMVias<K, V> nodoActual, List<K> lista) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), lista);
            lista.add((K) nodoActual.getClave(i));
        }
        recorridoEnInOrden(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), lista);
    }

    @Override
    public List<K> recorridoPreOrden() {

        List<K> lista = new LinkedList<>();
        recorridoEnPreOrden(this.raiz, lista);
        return lista;

    }

    private void recorridoEnPreOrden(NodoMVias<K, V> nodoActual, List<K> lista) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            lista.add((K) nodoActual.getClave(i));
            recorridoEnPreOrden(nodoActual.getHijo(i), lista);
        }
        recorridoEnPreOrden(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), lista);

    }

    @Override
    public List<K> recorridoPostOrden() {
        List<K> lista = new LinkedList<>();
        recorridoEnPostOrden(this.raiz, lista);
        return lista;

    }

    private void recorridoEnPostOrden(NodoMVias<K, V> nodoActual, List<K> lista) {
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        recorridoEnPostOrden(nodoActual.getHijo(orden), lista);
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), lista);
            lista.add((K) nodoActual.getClave(i));

        }

    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()) {
            Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);

            do {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();    // retorna el valor y despues lo elimina de la cola
                for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {

                    recorrido.add(nodoActual.getClave(i));
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }
                if (!nodoActual.esHijoVacio(nodoActual.nroDeClavesNoVacias())) {
                    colaDeNodos.offer(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
                }

            } while (!colaDeNodos.isEmpty());
        }
        return recorrido;
    }

    @Override
    public int altura() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    public List<V> recorridoEnInOrdenDeValores() {
        List<V> lista = new LinkedList<>();
        recorridoEnInOrdenDeValores(this.raiz, lista);
        return lista;
    }
    
    private void recorridoEnInOrdenDeValores(NodoMVias<K, V> nodoActual, List<V> lista) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoEnInOrdenDeValores(nodoActual.getHijo(i), lista);
            lista.add((V) nodoActual.getValor(i));
        }
        recorridoEnInOrdenDeValores(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()), lista);
    }

    

}