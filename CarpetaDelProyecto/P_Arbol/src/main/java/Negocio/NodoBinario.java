/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

/**
 *
 * @author TEO
 * @param <K>
 * @param <V>
 */
public class NodoBinario<K , V> {
    private V valor;
    private K clave;
    private NodoBinario hijoIzquierdo;
    private NodoBinario hijoDerecho;
    
    /**
     * Constructor parametrizado
     * @param clave
     * @param valor 
     */
    public NodoBinario(K clave, V valor){
        this.clave = clave;
        this.valor = valor;
    }
    /**
     * Funcion que devuelve V
     * @return Object V
    */
    public V getValor() {
        return valor;
    }

    /**
    * Funcion que devuelve K
    * @return Object K
    */
    public K getClave() {
        return clave;
    }

    /**
     * Funcion que retorna el nodo hijo izquierdo
     * @return NodoBinario
     */
    public NodoBinario getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public NodoBinario getHijoDerecho() {
        return hijoDerecho;
    }
    
    public static NodoBinario nodoVacio(){
        return null;
    }
    
    public static boolean esNodoVacio(NodoBinario nodito){
        return nodito == NodoBinario.nodoVacio();
    }

    public void setValor(V valor) {
        this.valor = valor;
    }

    public void setClave(K clave) {
        this.clave = clave;
    }

    public void setHijoIzquierdo(NodoBinario hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public void setHijoDerecho(NodoBinario hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }
    
    public boolean esVacioHijoIzquierdo(){
        return NodoBinario.esNodoVacio(this.getHijoIzquierdo());
    }
      
    public boolean esVacioHijoDerecho(){
        return NodoBinario.esNodoVacio(this.getHijoDerecho());
    }
    
    public static void main(String [] args){
        NodoBinario<Integer, String> nodito = new NodoBinario(5,"hola");
        
    }
}
