/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author TEO
 */
public class OrdenInvalidoException extends Exception{
    
    public OrdenInvalidoException(){
        super("El orden mínimo es 3");
    }

    public OrdenInvalidoException(String message) {
        super(message);
    }
    
}
