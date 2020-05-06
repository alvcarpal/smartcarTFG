package com.sensor.dto;

// Clase para representar la tabla vehiculos
public class Vehiculo {
	private String matricula;
	private String tipos_tipo;
    
    public Vehiculo(){}

    public Vehiculo(String matricula, String tipo){
	this.matricula=matricula;
	this.tipos_tipo=tipo;
    }
    
    public String getMatricula(){
	return matricula;
    }
    public void setMatricula(String matricula){
    	this.matricula=matricula;
    }

	public String getTipos_tipo() {
		return tipos_tipo;
	}

	public void setTipos_tipo(String tipos_tipo) {
		this.tipos_tipo = tipos_tipo;
	}
    
    
    
}