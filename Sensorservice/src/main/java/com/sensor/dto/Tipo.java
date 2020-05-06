package com.sensor.dto;

// Clase para representar la tabla tipos
public class Tipo {
	private String tipo;
	private int vmax;
    
    public Tipo(){}

    public Tipo(String tipo, int vmax){
	this.tipo=tipo;
	this.vmax=vmax;
    }
    
    public String getTipo(){
	return tipo;
    }
    public void setTipo(String tipo){
    	this.tipo=tipo;
    }
    
    public int getVmax(){
	return vmax;
    }
    public void setVmax(int vmax){
    	this.vmax=vmax;
    }
    
}
