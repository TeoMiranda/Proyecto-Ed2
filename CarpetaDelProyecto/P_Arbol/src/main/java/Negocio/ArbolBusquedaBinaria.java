/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Excepciones.ClaveNoExisteExcepcion;
import java.util.LinkedList;
import java.util.Stack;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TEO
 * @param <K>
 * @param <V>
 */
public class ArbolBusquedaBinaria<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {

    NodoBinario raiz;

    public ArbolBusquedaBinaria() {
    }
    
    public ArbolBusquedaBinaria(List<K> clavesInOrden, List<V> valoresInOrden,
                                List<K> clavesNoInOrden, List<V> valoresNoInOrden,
                                boolean conPreOrden) {
        if (conPreOrden){
            this.raiz = reconstruirConPreOrden(clavesInOrden, valoresInOrden,
                                               clavesNoInOrden, valoresNoInOrden);
        }else { 
            this.raiz = reconstruirConPostOrden(clavesInOrden, valoresInOrden,
                                               clavesNoInOrden, valoresNoInOrden);
        }
    }
    
    private NodoBinario<K, V> reconstruirConPreOrden(List<K> clavesInOrden, List<V> valoresInOrden,
                                List<K> clavesPreOrden, List<V> valoresPreOrden){
        if (clavesInOrden.isEmpty())
            return NodoBinario.nodoVacio();
        K clave = clavesPreOrden.get(0);
        V valor = valoresPreOrden.get(0);
        NodoBinario<K, V> nodito = new NodoBinario<>(clave, valor);
        int n = clavesInOrden.size();
        if (n == 1){
            return nodito;
        }
        int indiceClaveValor = getPosicion(clave, clavesInOrden);
        if (indiceClaveValor == 0){
            
            List<K> clavesDerInOrden = clavesInOrden.subList(indiceClaveValor + 1, n);
            List<V> valoresDerInOrden = valoresInOrden.subList(indiceClaveValor + 1, n);
            
            List<K> clavesDerPreOrden = clavesPreOrden.subList(1, n);
            List<V> valoresDerPreOrden = valoresPreOrden.subList(1, n);
            
             NodoBinario<K, V> hijoDerecho = reconstruirConPreOrden(clavesDerInOrden, 
                                                                    valoresDerInOrden,
                                                                    clavesDerPreOrden,
                                                                    valoresDerPreOrden);
            
            nodito.setHijoDerecho(hijoDerecho);
            return nodito;
        }
        if (indiceClaveValor == n-1){
            
            List<K> clavesIzqInOrden = clavesInOrden.subList(0, indiceClaveValor);
            List<V> valoresIzqInOrden = valoresInOrden.subList(0, indiceClaveValor);
            
            List<K> clavesIzqPreOrden = clavesPreOrden.subList(1, n);
            List<V> valoresIzqPreOrden = valoresPreOrden.subList(1, n);
            
            NodoBinario<K, V> hijoIzquierdo = reconstruirConPreOrden(clavesIzqInOrden, 
                                                                 valoresIzqInOrden,
                                                                 clavesIzqPreOrden,
                                                                 valoresIzqPreOrden);
            nodito.setHijoIzquierdo(hijoIzquierdo);
            return nodito;
        }
        List<K> clavesIzqInOrden = clavesInOrden.subList(0, indiceClaveValor);
        List<V> valoresIzqInOrden = valoresInOrden.subList(0, indiceClaveValor);
        List<K> clavesDerInOrden = clavesInOrden.subList(indiceClaveValor + 1, n);
        List<V> valoresDerInOrden = valoresInOrden.subList(indiceClaveValor + 1, n);
        
        List<K> clavesIzqPreOrden = clavesPreOrden.subList(1, clavesIzqInOrden.size() + 1);
        List<V> valoresIzqPreOrden = valoresPreOrden.subList(1, valoresIzqInOrden.size() + 1);
        List<K> clavesDerPreOrden = clavesPreOrden.subList(clavesIzqInOrden.size() +1, n);
        List<V> valoresDerPreOrden = valoresPreOrden.subList(valoresIzqInOrden.size() +1, n);
        
        NodoBinario<K, V> hijoIzquierdo = reconstruirConPreOrden(clavesIzqInOrden, 
                                                                 valoresIzqInOrden,
                                                                 clavesIzqPreOrden,
                                                                 valoresIzqPreOrden);
        nodito.setHijoIzquierdo(hijoIzquierdo);
        NodoBinario<K, V> hijoDerecho = reconstruirConPreOrden(clavesDerInOrden, 
                                                                 valoresDerInOrden,
                                                                 clavesDerPreOrden,
                                                                 valoresDerPreOrden);
        nodito.setHijoDerecho(hijoDerecho);
        return nodito;
    }
    
    public int getPosicion(K clave, List<K> claves){
        int pos = 0;
        while (clave.compareTo(claves.get(pos)) != 0){
            pos++;
        }
        return pos;
    }
    
    private NodoBinario<K, V> reconstruirConPostOrden(List<K> clavesInOrden, List<V> valoresInOrden,
                                List<K> clavesPostOrden, List<V> valoresPostOrden){
        if (clavesInOrden.isEmpty())
            return NodoBinario.nodoVacio();
        int n = clavesInOrden.size();
        K clave = clavesPostOrden.get(n-1);
        V valor = valoresPostOrden.get(n-1);
        NodoBinario<K, V> nodito = new NodoBinario<>(clave, valor);
        if (n == 1){
            return nodito;
        }
        int indiceClaveValor = getPosicion(clave, clavesInOrden);
        if (indiceClaveValor == 0){
            
            List<K> clavesDerInOrden = clavesInOrden.subList(indiceClaveValor + 1, n);
            List<V> valoresDerInOrden = valoresInOrden.subList(indiceClaveValor + 1, n);
            
            List<K> clavesDerPostOrden = clavesPostOrden.subList(0, n-1);
            List<V> valoresDerPostOrden = valoresPostOrden.subList(0, n-1);
            
            NodoBinario<K, V> hijoDerecho = reconstruirConPostOrden(clavesDerInOrden, 
                                                                    valoresDerInOrden,
                                                                    clavesDerPostOrden,
                                                                    valoresDerPostOrden);
            
            nodito.setHijoDerecho(hijoDerecho);
            return nodito;
        }
        if (indiceClaveValor == n-1){
            
            List<K> clavesIzqInOrden = clavesInOrden.subList(0, indiceClaveValor);
            List<V> valoresIzqInOrden = valoresInOrden.subList(0, indiceClaveValor);
            
            List<K> clavesIzqPostOrden = clavesPostOrden.subList(0, n-1);
            List<V> valoresIzqPostOrden = valoresPostOrden.subList(0, n-1);
            
            NodoBinario<K, V> hijoIzquierdo = reconstruirConPostOrden(clavesIzqInOrden, 
                                                                 valoresIzqInOrden,
                                                                 clavesIzqPostOrden,
                                                                 valoresIzqPostOrden);
            nodito.setHijoIzquierdo(hijoIzquierdo);
            return nodito;
        }
        List<K> clavesIzqInOrden = clavesInOrden.subList(0, indiceClaveValor);
        List<V> valoresIzqInOrden = valoresInOrden.subList(0, indiceClaveValor);
        List<K> clavesDerInOrden = clavesInOrden.subList(indiceClaveValor + 1, n);
        List<V> valoresDerInOrden = valoresInOrden.subList(indiceClaveValor + 1, n);
        
        List<K> clavesIzqPostOrden = clavesPostOrden.subList(0, clavesIzqInOrden.size());
        List<V> valoresIzqPostOrden = valoresPostOrden.subList(0, valoresIzqInOrden.size());
        List<K> clavesDerPostOrden = clavesPostOrden.subList(clavesIzqInOrden.size(), n-1);
        List<V> valoresDerPostOrden = valoresPostOrden.subList(valoresIzqInOrden.size(), n-1);
        
        NodoBinario<K, V> hijoIzquierdo = reconstruirConPostOrden(clavesIzqInOrden, 
                                                                 valoresIzqInOrden,
                                                                 clavesIzqPostOrden,
                                                                 valoresIzqPostOrden);
        nodito.setHijoIzquierdo(hijoIzquierdo);
        NodoBinario<K, V> hijoDerecho = reconstruirConPostOrden(clavesDerInOrden, 
                                                                 valoresDerInOrden,
                                                                 clavesDerPostOrden,
                                                                 valoresDerPostOrden);
        nodito.setHijoDerecho(hijoDerecho);
        return nodito;
    }
    
    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(claveAInsertar, valorAsociado);
        } else {
            NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
            NodoBinario<K, V> nodoActual = this.raiz;
            do {
                K claveDelNodoActual = nodoActual.getClave();
                nodoAnterior = nodoActual;
                if (claveAInsertar.compareTo(claveDelNodoActual) < 0) {
                    nodoActual = nodoActual.getHijoIzquierdo();
                } else if (claveAInsertar.compareTo(claveDelNodoActual) > 0) {
                    nodoActual = nodoActual.getHijoDerecho();
                } else {
                    nodoActual.setValor(valorAsociado);
                    return;
                }
            } while (!NodoBinario.esNodoVacio(nodoActual));
            NodoBinario<K, V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAsociado);
            if (nodoAnterior.getClave().compareTo(claveAInsertar) < 0) {
                nodoAnterior.setHijoDerecho(nuevoNodo);
            } else {
                nodoAnterior.setHijoIzquierdo(nuevoNodo);
            }
        }
    }

    @Override
    public V eliminar(K claveAEliminar) throws ClaveNoExisteExcepcion {
        if(claveAEliminar == null){
            throw new ClaveNoExisteExcepcion("No se aceptan claves nulas");
        }else {
            V valorARetornar = this.buscar(claveAEliminar);
            if(valorARetornar == null)
                throw new ClaveNoExisteExcepcion("valor nulo");
            this.raiz = this.eliminarRecursivo(this.raiz, claveAEliminar);
            return valorARetornar;
        }
    }
    
    private NodoBinario<K, V> eliminarRecursivo(NodoBinario<K, V> nodoActual, K claveAEliminar){
        K claveActual = nodoActual.getClave();
        if (claveAEliminar.compareTo(claveActual) < 0){
            NodoBinario<K, V> supuestoNuevoNodoHijoIzq = this.eliminarRecursivo(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoNodoHijoIzq);
            return nodoActual;
        }
        if (claveAEliminar.compareTo(claveActual) > 0){
            NodoBinario<K, V> supuestoNuevoNodoHijoDer = this.eliminarRecursivo(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoNodoHijoDer);
            return nodoActual;
        }
        //Caso 1
        if (this.esHoja(nodoActual)){
            return NodoBinario.nodoVacio();
        }
        
        //Caso 2
        if (!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho())
            return nodoActual.getHijoIzquierdo();
        if (nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho())
            return nodoActual.getHijoDerecho();
        
        //Caso 3
        NodoBinario<K, V> nodoDelSucesor = this.getSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K, V> supuestoNuevoHijoDerecho = this.eliminarRecursivo(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
        return nodoActual;
    }
    
    private NodoBinario<K, V> getSucesor(NodoBinario<K, V> nodito){
        while (!nodito.esVacioHijoIzquierdo())
            nodito = nodito.getHijoIzquierdo();
        return nodito;
    }
    
    public V eliminarIterativo(K claveAEliminar) throws ClaveNoExisteExcepcion {
       throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody 
    }

    @Override
    public V buscar(K clave) {
        if (!this.esArbolVacio()) {
            NodoBinario<K, V> nodoActual = this.raiz;
            do {
                K claveActual = nodoActual.getClave();
                if (clave.compareTo(claveActual) > 0) {
                    nodoActual = nodoActual.getHijoDerecho();
                } else {
                    if (clave.compareTo(claveActual) < 0) {
                        nodoActual = nodoActual.getHijoIzquierdo();
                    } else {
                        return nodoActual.getValor();
                    }
                }
            } while (!NodoBinario.esNodoVacio(nodoActual));
        }
        return null;
    }

    @Override
    public boolean contiene(K clave) {
        return (buscar(clave) != null);
    }

    @Override
    public int size() {
            return sizeMetodoCompanero(this.raiz);       
    }
    
    protected int sizeMetodoCompanero(NodoBinario<K, V> nodito){
        if (NodoBinario.esNodoVacio(nodito)){
            return 0;
        } else {
            return (sizeMetodoCompanero(nodito.getHijoDerecho()) + 
                    sizeMetodoCompanero(nodito.getHijoIzquierdo()) + 1);
        }
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }
    
    protected int altura(NodoBinario<K, V> nodoActual){
        if (nodoActual == NodoBinario.nodoVacio()){
            return 0;
        } else {
            int alturaXHijoIzq = this.altura(nodoActual.getHijoIzquierdo());
            int alturaXHijoDer = this.altura(nodoActual.getHijoDerecho());
            return (alturaXHijoIzq > alturaXHijoDer)? alturaXHijoIzq+1 : alturaXHijoDer+1;
        }
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public boolean esHoja(K clave){
        if (!this.esArbolVacio()) {

            NodoBinario<K, V> nodoActual = this.raiz;

            do {
                K claveDelNodoActual = nodoActual.getClave();
                if (clave.compareTo(claveDelNodoActual) < 0) {
                    nodoActual = nodoActual.getHijoIzquierdo();
                } else if (clave.compareTo(claveDelNodoActual) > 0) {
                    nodoActual = nodoActual.getHijoDerecho();
                } else {
                    return nodoActual.getHijoIzquierdo() == null && nodoActual.getHijoDerecho() == null;
                }

            } while (!NodoBinario.esNodoVacio(nodoActual));
        }
        return false;
    }
    
    public boolean esHoja(NodoBinario<K, V> nodito) {
        if (nodito == NodoBinario.nodoVacio())
            try {
                throw new ClaveNoExisteExcepcion("nodo nulo");
        } catch (ClaveNoExisteExcepcion ex) {
            Logger.getLogger(ArbolBusquedaBinaria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ((nodito.esVacioHijoIzquierdo()) && (nodito.esVacioHijoDerecho()));
    }

    @Override
    public byte getCantHoja() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<K> recorridoInOrden() {
        List<K> recorrido = new LinkedList<>();
        if (!this.esArbolVacio()) {
            //Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
            Stack<NodoBinario<K, V>> pilaDeNodo = new Stack<>();
            NodoBinario<K, V> nodoActual = this.raiz;

            apilarParaInOrden(pilaDeNodo, nodoActual);

            while (!pilaDeNodo.isEmpty()) {
                nodoActual = pilaDeNodo.pop();
                recorrido.add(nodoActual.getClave());
                if (!nodoActual.esVacioHijoDerecho()) {
                    nodoActual = nodoActual.getHijoDerecho();
                    apilarParaInOrden(pilaDeNodo, nodoActual);

                }
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoPreOrden() {
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(raiz);
        List<K> recorrido = new LinkedList<>();
        NodoBinario<K, V> nodito;
        do{
            nodito = pilaDeNodos.pop();
            recorrido.add(nodito.getClave());
            if (!nodito.esVacioHijoDerecho()){
                pilaDeNodos.push(nodito.getHijoDerecho());
            }
            if (!nodito.esVacioHijoIzquierdo()){
                pilaDeNodos.push(nodito.getHijoIzquierdo());
            }
        }while (!pilaDeNodos.isEmpty());
        return recorrido;
    }

    @Override
    public List<K> recorridoPostOrden() {
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        List<K> recorrido = new LinkedList<>();
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoDelTope;
        do {
            while (!NodoBinario.esNodoVacio(nodoActual)) {
                pilaDeNodos.add(nodoActual);
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    nodoActual = nodoActual.getHijoIzquierdo();
                } else {
                    nodoActual = nodoActual.getHijoDerecho();
                }
            } 
            
            nodoActual = (NodoBinario<K, V>)pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!pilaDeNodos.isEmpty()){
                nodoDelTope = pilaDeNodos.peek();
                if ((!nodoDelTope.esVacioHijoDerecho()) && (nodoDelTope.getHijoDerecho() != nodoActual)){
                    nodoActual = nodoDelTope.getHijoDerecho();
                } else {
                    nodoActual = NodoBinario.nodoVacio();
                } 
            }
        } while (!pilaDeNodos.isEmpty());
        return recorrido;
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> lista = new LinkedList<>();
        Queue<NodoBinario<K, V>> cola = new LinkedList<>();
        NodoBinario<K, V> nodito;
        cola.add(this.raiz);
        do {
            nodito = cola.poll();
            lista.add(nodito.getClave());
            if (!nodito.esVacioHijoIzquierdo()){
                cola.add(nodito.getHijoIzquierdo());
            }
            if (!nodito.esVacioHijoDerecho()){
                cola.add(nodito.getHijoDerecho());
            }
        }while (!cola.isEmpty());
        return lista;
    }
    
    private void apilarParaInOrden(Stack<NodoBinario<K, V>> pilaDeNodo, NodoBinario<K, V> nodoActual) {
        do {
            pilaDeNodo.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        } while (!NodoBinario.esNodoVacio(nodoActual));
    }
    
    public List<V> recorridoEnInOrdenDeValores() {
         List<V> recorrido = new LinkedList<>();
        if (!this.esArbolVacio()) {
            //Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
            Stack<NodoBinario<K, V>> pilaDeNodo = new Stack<>();
            NodoBinario<K, V> nodoActual = this.raiz;

            apilarParaInOrden(pilaDeNodo, nodoActual);

            while (!pilaDeNodo.isEmpty()) {
                nodoActual = pilaDeNodo.pop();
                recorrido.add(nodoActual.getValor());
                if (!nodoActual.esVacioHijoDerecho()) {
                    nodoActual = nodoActual.getHijoDerecho();
                    apilarParaInOrden(pilaDeNodo, nodoActual);

                }
            }
        }
        return recorrido;
    }
    
    protected NodoBinario<K, V> obtenerNodoDelSucesor(NodoBinario<K, V> unNodo) {
        NodoBinario<K, V> nodoAnterior;
        do {
            nodoAnterior = unNodo;
            unNodo = unNodo.getHijoIzquierdo();
        } while (!NodoBinario.esNodoVacio(unNodo));

        return nodoAnterior;
    }

    
    public static void main(String [] args) throws ClaveNoExisteExcepcion{
//        List<Integer> clavesInOrden = new LinkedList<>();
//        clavesInOrden.add(12);
//        clavesInOrden.add(15);
//        clavesInOrden.add(16);
//        clavesInOrden.add(17);
//        clavesInOrden.add(18);
//        clavesInOrden.add(20);
//        clavesInOrden.add(22);
//        clavesInOrden.add(25);
//        clavesInOrden.add(26);
//        clavesInOrden.add(28);
//        List<String> valoresInOrden = new LinkedList<>();
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        valoresInOrden.add("");
//        List<Integer> clavesPreOrden = new LinkedList<>();
//        clavesPreOrden.add(12);
//        clavesPreOrden.add(16);
//        clavesPreOrden.add(18);
//        clavesPreOrden.add(17);
//        clavesPreOrden.add(15);
//        clavesPreOrden.add(22);
//        clavesPreOrden.add(26);
//        clavesPreOrden.add(28);
//        clavesPreOrden.add(25);
//        clavesPreOrden.add(20);
//        List<String> valoresPreOrden = new LinkedList<>();
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        valoresPreOrden.add("");
//        ArbolBusquedaBinaria<Integer, String> arbolito = new ArbolBusquedaBinaria<>(clavesInOrden, valoresInOrden,
//                                                                                    clavesPreOrden, valoresPreOrden, false);

//System.out.println(arbolito.recorridoInOrden());
        ArbolBusquedaBinaria<Integer, String> arbolito = new ArbolBusquedaBinaria();
        arbolito.insertar(20, "raiz");
        arbolito.insertar(15, "15");
        arbolito.insertar(25, "25");
        arbolito.insertar(28, "28");
        arbolito.insertar(22, "22");
        arbolito.insertar(26, "26");
        arbolito.insertar(12, "12");
        arbolito.insertar(17, "17");
        arbolito.insertar(16, "soy el 16");
        arbolito.insertar(18, "18");
        System.out.println(arbolito.recorridoInOrden());
//        System.out.println(arbolito.buscar(16));
//        System.out.println(arbolito.recorridoPostOrden());
//        arbolito.eliminarRecursivo(15);
//        System.out.println(arbolito.recorridoPostOrden());
//        System.out.println(arbolito.size());
//        System.out.println(arbolito.recorridoPreOrden());
    }

}
