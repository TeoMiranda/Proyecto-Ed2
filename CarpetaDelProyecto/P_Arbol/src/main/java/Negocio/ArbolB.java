/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Excepciones.ClaveNoExisteExcepcion;
import Excepciones.OrdenInvalidoException;
import java.util.Stack;

/**
 *
 * @author TEO
 * @param <K>
 * @param <V>
 */

public class ArbolB <K extends Comparable<K>, V> extends ArbolMViasBusqueda<K, V> {

    private final int nroMaxDeDatos;
    private final int nroMinDeDatos;
    private final int nroMinDeHijos;

    public ArbolB() {
        super(); // llama al constructor del padre   orden = 3 (nroMaxDeHijos)
        this.nroMaxDeDatos = 2;
        this.nroMinDeDatos = 1;
        this.nroMinDeHijos = 2;
    }

    public ArbolB(int orden) throws OrdenInvalidoException {
        super(orden);
        this.nroMaxDeDatos = super.orden - 1;
        this.nroMinDeDatos = this.nroMaxDeDatos / 2;
        this.nroMinDeHijos = this.nroMinDeDatos + 1;
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
            this.raiz = new NodoMVias<>(this.orden + 1, claveAInsertar, valorAsociado);
            return;
        }

        NodoMVias<K, V> nodoActual = this.raiz;

        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();

        do {
            int posicionDeClave = super.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != ArbolMViasBusqueda.POSICION_NO_VALIDA) {// si ya existe en arbol
                nodoActual.setValor(posicionDeClave, valorAsociado);
                nodoActual = NodoMVias.nodoVacio();
            } else if (nodoActual.esHoja()) {
                insertarClaveYValorOrdenadamenteEnNodo(nodoActual, claveAInsertar, valorAsociado);
                if (nodoActual.nroDeClavesNoVacias() > this.nroMaxDeDatos) {
                    dividir(nodoActual, pilaDeAncestros);
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                int posicionPorDondeBajar = obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                pilaDeAncestros.push(nodoActual);
                nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
            }
        } while (!NodoMVias.esNodoVacio(nodoActual));

    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncentros) {
        NodoMVias<K, V> nuevoNodoHijo = new NodoMVias<>(this.orden + 1);//Primer Hijo
        for (int i = 0; i < nroMinDeDatos; i++) {
            nuevoNodoHijo.setClave(nodoActual.getClave(i), i);
            nuevoNodoHijo.setValor(i, nodoActual.getValor(i));
            nuevoNodoHijo.setHijo(i, nodoActual.getHijo(i));
        }
        nuevoNodoHijo.setHijo(nroMinDeDatos, nodoActual.getHijo(nroMinDeDatos));

        int j = 0;
        NodoMVias<K, V> nuevoNodoHijo2 = new NodoMVias<>(this.orden + 1);//Segundo Hijo
        for (int i = nroMinDeDatos + 1; i < nodoActual.nroDeClavesNoVacias(); i++) {
            nuevoNodoHijo2.setClave(nodoActual.getClave(i), j);
            nuevoNodoHijo2.setValor(j, nodoActual.getValor(i));
            nuevoNodoHijo2.setHijo(j, nodoActual.getHijo(i));
            j++;
        }

        nuevoNodoHijo2.setHijo(j, nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));

        if (!pilaDeAncentros.isEmpty()) {
            NodoMVias<K, V> nodoAncestro = pilaDeAncentros.pop();
            if (!nodoAncestro.estanClavesLlenas()) {
                K claveInsertada = nodoActual.getClave(nroMinDeDatos);
                //con este metodo se inserta el dato en el lugar correcto
                insertarClaveYValorOrdenadamenteEnNodo(nodoAncestro, nodoActual.getClave(nroMinDeDatos), nodoActual.getValor(nroMinDeDatos));
                int posicion = obtenerPosicionDeClave(nodoAncestro, claveInsertada);
                nodoAncestro.setHijo(posicion, nuevoNodoHijo);
                nodoAncestro.setHijo(posicion + 1, nuevoNodoHijo2);
                if (nodoAncestro.nroDeClavesNoVacias() > this.nroMaxDeDatos) {
                    this.dividir(nodoAncestro, pilaDeAncentros);
                }
            } else {
                nodoActual.setClave(nodoActual.getClave(nroMinDeDatos), 0);
                nodoActual.setValor(0, nodoActual.getValor(nroMinDeDatos));
                int cantDeClavesNoVacias = nodoActual.nroDeClavesNoVacias();
                for (int i = 1; i < cantDeClavesNoVacias; i++) {
                    nodoActual.setClave((K) NodoMVias.datoVacio(), i);
                    nodoActual.setValor(i, (V) NodoMVias.datoVacio());
                    nodoActual.setHijo(i, NodoMVias.nodoVacio());
                }
                nodoActual.setHijo(0, nuevoNodoHijo);
                nodoActual.setHijo(1, nuevoNodoHijo2);
            }
        } else {//si la pila esta vacia quiere decir que no tiene ancestro el nodoActual, osea el nodoActual es la raiz
            nodoActual.setClave(nodoActual.getClave(nroMinDeDatos), 0);
            nodoActual.setValor(0, nodoActual.getValor(nroMinDeDatos));
            int cantDeClavesNoVacias = nodoActual.nroDeClavesNoVacias();
            for (int i = 1; i < cantDeClavesNoVacias; i++) {
                nodoActual.setValor(i, (V) NodoMVias.datoVacio());
                nodoActual.setClave((K) NodoMVias.datoVacio(), i);
                nodoActual.setHijo(i, NodoMVias.nodoVacio());
            }
            nodoActual.setHijo(0, nuevoNodoHijo);
            nodoActual.setHijo(1, nuevoNodoHijo2);
        }
    }

    private NodoMVias<K, V> buscarNodoDeLaClave(K claveAEliminar, Stack<NodoMVias<K, V>> pilaDePadres) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            boolean huboCambioDeNodo = false;
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias() && !huboCambioDeNodo; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveAEliminar.compareTo(claveActual) == 0) {
                    return nodoActual;
                }

                if (claveAEliminar.compareTo(claveActual) < 0) {
                    pilaDePadres.push(nodoActual);
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioDeNodo = true;
                }

            }
            if (!huboCambioDeNodo) {
                pilaDePadres.push(nodoActual);
                nodoActual = nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
            }
        }
        return NodoMVias.nodoVacio();
    }

    private void prestarseOFusionar(NodoMVias<K, V> nodoEnProblema, Stack<NodoMVias<K, V>> pilaDePadres, int posicionNodoEnProblema) {

        while ((nodoEnProblema.nroDeClavesNoVacias() < this.nroMinDeDatos) && (!pilaDePadres.isEmpty())) {

            NodoMVias<K, V> nodoPadre = pilaDePadres.pop();

            NodoMVias<K, V> nodoHermanoSig = nodoPadre.getHijo(posicionNodoEnProblema + 1);
            NodoMVias<K, V> nodoHermanoAnt;
            if (posicionNodoEnProblema == 0) {
                nodoHermanoAnt = NodoMVias.nodoVacio();
            } else {
                nodoHermanoAnt = nodoPadre.getHijo(posicionNodoEnProblema - 1);
            }

            //El nodoEnProblema se presta del hermano Siguiente,  si este es mayor al minimo de datos
            if (!NodoMVias.esNodoVacio(nodoHermanoSig) && nodoHermanoSig.nroDeClavesNoVacias() > this.nroMinDeDatos) {
                this.prestarseDelHermanoSig(nodoEnProblema, nodoPadre, nodoHermanoSig, posicionNodoEnProblema);
            } //caso contrario  se presta del hermano Anterior, si este es mayor al minimo de datos
            else if (!NodoMVias.esNodoVacio(nodoHermanoAnt) && nodoHermanoAnt.nroDeClavesNoVacias() > this.nroMinDeDatos) {
                this.prestarseDelHermanoAnt(nodoEnProblema, nodoPadre, nodoHermanoAnt, posicionNodoEnProblema);
            } //caso contrario se fuciona con el hermano siguiente si este no es vacio
            else if (!NodoMVias.esNodoVacio(nodoHermanoSig)) {
                posicionNodoEnProblema = fusionarseConHermanoSig(nodoPadre, posicionNodoEnProblema, nodoEnProblema, pilaDePadres, nodoHermanoSig);
                //caso contrario se fuciona con el hermano anterior
            } else {
                posicionNodoEnProblema = fusionarseConHermanoAnt(nodoPadre, posicionNodoEnProblema, nodoEnProblema, pilaDePadres, nodoHermanoAnt);

            }

            nodoEnProblema = nodoPadre;
        }
    }

    private int fusionarseConHermanoAnt(NodoMVias<K, V> nodoPadre, int posicionNodoEnProblema, NodoMVias<K, V> nodoEnProblema, Stack<NodoMVias<K, V>> pilaDePadres, NodoMVias<K, V> nodoHermanoAnt) {
        K claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema - 1);
        V valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema - 1);
        nodoPadre.setHijo(posicionNodoEnProblema, NodoMVias.nodoVacio());
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema - 1);
        this.insertarClaveYValorOrdenadamenteEnNodo(nodoHermanoAnt, claveDelNodoPadre, valorDelNodoPadre);

        for (int i = 0; i < nodoEnProblema.nroDeClavesNoVacias(); i++) {
            K claveNodoEnProblema = nodoEnProblema.getClave(i);
            V valorNodoEnProblema = nodoEnProblema.getValor(i);
            NodoMVias<K, V> hijosDelNodoEnProblema = nodoEnProblema.getHijo(i);
            nodoHermanoAnt.setHijo(nodoHermanoAnt.cantidadDeHijosNoVacios(), hijosDelNodoEnProblema);
            this.insertarClaveYValorOrdenadamenteEnNodo(nodoHermanoAnt, claveNodoEnProblema, valorNodoEnProblema);
        }
        NodoMVias<K, V> hijosDelNodoEnProblema = nodoEnProblema.getHijo(nodoEnProblema.nroDeClavesNoVacias());
        nodoHermanoAnt.setHijo(nodoHermanoAnt.cantidadDeHijosNoVacios(), hijosDelNodoEnProblema);

        if (!pilaDePadres.isEmpty()) {
            NodoMVias<K, V> nodoAbuelo = pilaDePadres.peek();
            posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(nodoAbuelo, claveDelNodoPadre);
        }

        if (super.raiz.nroDeClavesNoVacias() == 0) {
            super.raiz = nodoHermanoAnt;
        }
        return posicionNodoEnProblema;
    }

    private int fusionarseConHermanoSig(NodoMVias<K, V> nodoPadre, int posicionNodoEnProblema, NodoMVias<K, V> nodoEnProblema, Stack<NodoMVias<K, V>> pilaDePadres, NodoMVias<K, V> nodoHermanoSig) {
        K claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema);
        V valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema);
        nodoPadre.setHijo(posicionNodoEnProblema + 1, NodoMVias.nodoVacio());
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema);
        this.insertarClaveYValorOrdenadamenteEnNodo(nodoEnProblema, claveDelNodoPadre, valorDelNodoPadre);

        for (int i = 0; i < nodoHermanoSig.nroDeClavesNoVacias(); i++) {
            K claveHermanoSig = nodoHermanoSig.getClave(i);
            V valorHermanoSig = nodoHermanoSig.getValor(i);
            NodoMVias<K, V> hijosDelHermanoSig = nodoHermanoSig.getHijo(i);
            nodoEnProblema.setHijo(nodoEnProblema.cantidadDeHijosNoVacios(), hijosDelHermanoSig);
            this.insertarClaveYValorOrdenadamenteEnNodo(nodoEnProblema, claveHermanoSig, valorHermanoSig);
        }

        NodoMVias<K, V> hijosDelHermanoSig = nodoHermanoSig.getHijo(nodoHermanoSig.nroDeClavesNoVacias());
        nodoEnProblema.setHijo(nodoEnProblema.cantidadDeHijosNoVacios(), hijosDelHermanoSig);

        if (!pilaDePadres.isEmpty()) {
            NodoMVias<K, V> nodoAbuelo = pilaDePadres.peek();
            posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(nodoAbuelo, claveDelNodoPadre);
        }

        if (super.raiz.nroDeClavesNoVacias() == 0) {
            super.raiz = nodoEnProblema;
        }
        return posicionNodoEnProblema;
    }

    private void prestarseDelHermanoAnt(NodoMVias<K, V> nodoEnProblema, NodoMVias<K, V> nodoPadre, NodoMVias<K, V> nodoHermanoAnt, int posicionNodoEnProblema) {
        K claveHermanoAnt = nodoHermanoAnt.getClave(nodoHermanoAnt.nroDeClavesNoVacias() - 1);
        V valorHermanoAnt = nodoHermanoAnt.getValor(nodoHermanoAnt.nroDeClavesNoVacias() - 1);
        K claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema - 1);
        V valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema - 1);

        super.insertarClaveYValorOrdenadamenteEnNodo(nodoEnProblema, claveDelNodoPadre, valorDelNodoPadre);
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema - 1);
        super.insertarClaveYValorOrdenadamenteEnNodo(nodoPadre, claveHermanoAnt, valorHermanoAnt);
        super.eliminarClaveYValorDelNodo(nodoHermanoAnt, nodoHermanoAnt.nroDeClavesNoVacias() - 1);

    }

    private void prestarseDelHermanoSig(NodoMVias<K, V> nodoEnProblema, NodoMVias<K, V> nodoPadre, NodoMVias<K, V> nodoHermanoSig, int posicionNodoEnProblema) {
        K claveHermanoSig = nodoHermanoSig.getClave(0);
        V valorHermanoSig = nodoHermanoSig.getValor(0);
        K claveDelNodoPadre = nodoPadre.getClave(posicionNodoEnProblema);
        V valorDelNodoPadre = nodoPadre.getValor(posicionNodoEnProblema);

        super.insertarClaveYValorOrdenadamenteEnNodo(nodoEnProblema, claveDelNodoPadre, valorDelNodoPadre);
        super.eliminarClaveYValorDelNodo(nodoPadre, posicionNodoEnProblema);
        super.insertarClaveYValorOrdenadamenteEnNodo(nodoPadre, claveHermanoSig, valorHermanoSig);
        super.eliminarClaveYValorDelNodo(nodoHermanoSig, 0);
    }

    @Override
    public V eliminar(K claveAEliminar) throws ClaveNoExisteExcepcion {
        if (claveAEliminar == NodoMVias.datoVacio()) {
            throw new IllegalArgumentException("Clave a buscar no puede ser nula");
        }

        Stack<NodoMVias<K, V>> pilaDePadres = new Stack<>();
        NodoMVias<K, V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar, pilaDePadres);

        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new ClaveNoExisteExcepcion();
        }

        int posicionDeClaveAEliminar = super.obtenerPosicionPorDondeBajar(nodoActual, claveAEliminar) - 1;
        V valorARetornar = nodoActual.getValor(posicionDeClaveAEliminar);

        if (nodoActual.esHoja()) {
            super.eliminarClaveYValorDelNodo(nodoActual, posicionDeClaveAEliminar);
            if (nodoActual.nroDeClavesNoVacias() < this.nroMinDeDatos) {
                // si llego aca hay que ajustar
                if (pilaDePadres.isEmpty()) {
                    if (nodoActual.nroDeClavesNoVacias() == 0) {
                        //super.vaciarArbol();
                        super.Vaciar();
                    }
                } else {
                    NodoMVias<K, V> nodoPadre = pilaDePadres.peek();
                    //Posicion del nodo hijo en problema en el nodoPadre
                    int posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(nodoPadre, claveAEliminar);

                    this.prestarseOFusionar(nodoActual, pilaDePadres, posicionNodoEnProblema);
                }

            }
        } else {
            pilaDePadres.push(nodoActual);
            NodoMVias<K, V> nodoDelPredecesor = this.obtenerNodoDelPredecesor(nodoActual.getHijo(posicionDeClaveAEliminar), pilaDePadres);
            int posicionDelPredecesor = nodoDelPredecesor.nroDeClavesNoVacias() - 1;
            K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            V datoDelPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarClaveYValorDelNodo(nodoDelPredecesor, posicionDelPredecesor);
            NodoMVias<K, V> padreDelNodoPredecesoor = pilaDePadres.peek();
            //Posicion del nodo hijo en problema en el nodoPadreDelPredecesor
            int posicionNodoEnProblema = super.obtenerPosicionPorDondeBajar(padreDelNodoPredecesoor, clavePredecesora);

            nodoActual.setClave(clavePredecesora, posicionDeClaveAEliminar);
            nodoActual.setValor(posicionDeClaveAEliminar, datoDelPredecesor);

            if (nodoDelPredecesor.nroDeClavesNoVacias() < this.nroMinDeDatos) {
                this.prestarseOFusionar(nodoDelPredecesor, pilaDePadres, posicionNodoEnProblema);
            }

        }

        return valorARetornar;
    }

    private NodoMVias<K, V> obtenerNodoDelPredecesor(NodoMVias<K, V> nodoEnTurno, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        while (!nodoEnTurno.esHoja()) {
            pilaDeAncestros.push(nodoEnTurno);
            int ultimaPosicion = nodoEnTurno.nroDeClavesNoVacias();
            nodoEnTurno = nodoEnTurno.getHijo(ultimaPosicion);
        }
     
        return nodoEnTurno;
    }

}
