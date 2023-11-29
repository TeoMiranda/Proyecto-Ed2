/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Excepciones.ClaveNoExisteExcepcion;
import Excepciones.ClaveNoExisteException;

/**
 *
 * @author TEO
 * @param <K>
 * @param <V>
 */
public class AVL <K extends Comparable<K>,V> extends ArbolBusquedaBinaria<K, V> {
    
    private static final int LIMITE_SUPERIOR = 1;
    private static final int LIMITE_INFERIOR = -1;

    public AVL() {
    }

    @Override
    public V eliminar(K claveAEliminar) throws ClaveNoExisteExcepcion {
        if (claveAEliminar == null) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        V valoreAsociado = buscar(claveAEliminar);
        if (valoreAsociado == null) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        this.raiz =eliminar(this.raiz, claveAEliminar);
        return valoreAsociado;
    }

    private NodoBinario<K, V> eliminar(NodoBinario<K, V> nodoActual, K claveAEliminar) {
        K claveActual = nodoActual.getClave();
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);

        }
        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<K, V> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return balancear(nodoActual);
        }

        //caso 1    si es Hoja 
        if (esHoja(nodoActual.getClave())) {
            return NodoBinario.nodoVacio();
        }

        //caso 2   si tiene un hijo  
        if (nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoDerecho();
        }
        if (!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoIzquierdo();
        }

        // caso 3   si tiene 2 hijos 
        NodoBinario<K, V> nodoDelSucesor = obtenerNodoDelSucesor(nodoActual.getHijoDerecho());

        NodoBinario<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        nodoActual.setHijoDerecho(supuestoNuevoHijo);

        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());

        return nodoActual;

    }

    @Override
    public void insertar(K claveAIsertar, V valorAsociado) {
        if (claveAIsertar == null) {

        }
        if (valorAsociado == null) {

        }

        this.raiz = insertar(this.raiz, claveAIsertar, valorAsociado);
    }

    private NodoBinario<K, V> insertar(NodoBinario<K, V> nodoActual, K claveAIsertar, V valorAsociado) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K, V> nuevoNodo = new NodoBinario<>(claveAIsertar, valorAsociado);
            return nuevoNodo;
        }

        K claveDelNodoActual = nodoActual.getClave();
        if (claveAIsertar.compareTo(claveDelNodoActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzq = insertar(nodoActual.getHijoIzquierdo(), claveAIsertar, valorAsociado);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzq);
            return balancear(nodoActual);
        }
        if (claveAIsertar.compareTo(claveDelNodoActual) > 0) {
            NodoBinario<K, V> supuestoNuevoHijoDer = insertar(nodoActual.getHijoDerecho(), claveAIsertar, valorAsociado);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDer);
            return balancear(nodoActual);
        }

        nodoActual.setClave(claveAIsertar);
        nodoActual.setValor(valorAsociado);
        return nodoActual;
    }

    private NodoBinario<K, V> balancear(NodoBinario<K, V> nodoActual) {
        int alturaPorIzq = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDer = altura(nodoActual.getHijoDerecho());
        
        int diferenciaDeAltura = alturaPorIzq - alturaPorDer;

        if (diferenciaDeAltura > AVL.LIMITE_SUPERIOR) {// rotacion hacia la derecha 
            NodoBinario<K, V> hijoIzqDelProblematico = nodoActual.getHijoIzquierdo();
            alturaPorIzq = altura(hijoIzqDelProblematico.getHijoIzquierdo());
            alturaPorDer = altura(hijoIzqDelProblematico.getHijoDerecho());

            if (alturaPorDer > alturaPorIzq) {
                return rotacionDoblePorDerecha(nodoActual);
            } else {
                return rotacionSimplePorDerecha(nodoActual);
            }
        } else if (diferenciaDeAltura < AVL.LIMITE_INFERIOR) {  // rotacion hacia la izquierda 
            NodoBinario<K, V> hijoDerDelProblematico = nodoActual.getHijoDerecho();
            alturaPorIzq = altura(hijoDerDelProblematico.getHijoIzquierdo());
            alturaPorDer = altura(hijoDerDelProblematico.getHijoDerecho());

            if (alturaPorDer > alturaPorIzq) {
                return rotacionSimplePorIzquierda(nodoActual);
            } else {
                return rotacionDoblePorIzquierda(nodoActual);   // cambiar a rotacion doble por izquierda
            }
        } else {
            return nodoActual;
        }

    }

    private NodoBinario<K, V> rotacionDoblePorDerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> primerNodoQueRota = rotacionSimplePorIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(primerNodoQueRota);
        return rotacionSimplePorDerecha(nodoActual);
    }

    private NodoBinario<K, V> rotacionSimplePorDerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());
        nodoQueRota.setHijoDerecho(nodoActual);
        return nodoQueRota;

    }

    //----------------------------------------------------------------------
    private NodoBinario<K, V> rotacionDoblePorIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> primerNodoQueRota = rotacionSimplePorDerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(primerNodoQueRota);
        return rotacionSimplePorIzquierda(nodoActual);
    }

    private NodoBinario<K, V> rotacionSimplePorIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());
        nodoQueRota.setHijoIzquierdo(nodoActual);
        return nodoQueRota;
    }
}
