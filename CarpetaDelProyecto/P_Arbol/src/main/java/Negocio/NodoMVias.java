/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TEO
 * @param <K>
 * @param <V>
 */
public class NodoMVias<K, V> {
    
    private List<K> ListaDeClaves;
    private List<V> ListaDeValores;
    private List<NodoMVias> ListaDeHijos;
    
    public NodoMVias(int orden){
        this.ListaDeClaves=new ArrayList<>();
        this.ListaDeValores=new ArrayList<>();
        this.ListaDeHijos=new ArrayList<>();
        
        for (int i= 0;  i < orden -1; i++){
            this.ListaDeClaves.add((K) NodoMVias.datoVacio());
            this.ListaDeValores.add((V) NodoMVias.datoVacio());
            this.ListaDeHijos.add(NodoMVias.nodoVacio());
            
        }
        this.ListaDeHijos.add(NodoMVias.nodoVacio());
                
    }
    
    public NodoMVias( int orden, K clave, V valor){
        this(orden);//invocar l otro constructor solo en prmera linea
        
        this.ListaDeClaves.set(0,clave);
        this.ListaDeValores.set(0,valor);
      
    }
    
    public static NodoMVias nodoVacio(){
        return null;
    }
    
    public static Object datoVacio(){
        return null;
        
    }
    public static boolean esNodoVacio(NodoMVias nodoActual){
        return nodoActual == NodoMVias.nodoVacio();
    }
    
    public K getClave(int posicion){
        return this.ListaDeClaves.get(posicion);
    }
    
    public V getValor(int posicion){
        return this.ListaDeValores.get(posicion);
    }
    
    public NodoMVias <K, V> getHijo(int posicion){
        return this.ListaDeHijos.get(posicion);
    }
    
    public void setClave (K clave,int posicion){
        this.ListaDeClaves.set(posicion,clave);
    }
    
    public void setValor(int posicion, V Valor){
        this.ListaDeValores.set(posicion,Valor);
    }
    
    public void setHijo(int posicion,NodoMVias<K, V> hijo){
        this.ListaDeHijos.set(posicion,hijo);
    }
    
    public boolean esHijoVacio(int posicion){
        return NodoMVias.esNodoVacio(this.getHijo(posicion));
    }
    
    public boolean esClaveVacia(int posicion){
        return NodoMVias.datoVacio()==this.getClave(posicion);
    }
    
    public boolean esHoja(){
       // for (NodoMvias <K, V> unHijo: this.ListaDeHijos){
         //   if (NodoMVias.esNodoVacio(unHijo)){
           //     return false;
            //}
        //}
        for (int i = 0 ; i < this.ListaDeHijos.size(); i++){
            if (!this.esHijoVacio(i)){
              return false;  
            }
              
        }
        return true; 
    }
    public boolean estanClavesLlenas(){
        for (int i = this.ListaDeClaves.size() -1 ; i >= 0;i--){
            if (this.esClaveVacia(i)){
                return false;
            }
        }
        return true;
    }
    public int nroDeClavesNoVacias(){
        int cantidad=0;
        for(K unaclave : this.ListaDeClaves){
            if (unaclave != NodoMVias.datoVacio()){
                cantidad++;
            }
        }
        return cantidad;
    }
    
    public boolean hayClavesNoVacias(){
        return this.nroDeClavesNoVacias()!= 0;
    }
    
    @Override
    public String toString(){
        return this.ListaDeClaves.toString(); 
    }
    
    public int cantidadDeHijosNoVacios(){
        int cantidad = 0;
        for(int i = 0; i < this.ListaDeHijos.size (); i++){
            if(!this.esHijoVacio (i)){
                cantidad++;
            }
        }
        return cantidad;
    }
    
    public int nroDeHijosQuePuedeTener(){
        return this.ListaDeHijos.size();
    }
}
