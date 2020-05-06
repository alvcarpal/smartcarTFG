package com.sensor.dto;

public class Dato{

	private long id;
	private String vehiculos_matricula;
    private double velocidad;
    private double latitud;
    private double longitud;
    

    
    public Dato(){}

    public Dato(String matricula, 
    		double latitud,double longitud,double velocidad){
	this.vehiculos_matricula=matricula;
	this.velocidad=velocidad;
	this.latitud=latitud;
	this.longitud=longitud;
    }
    
    public Dato(long id, String matricula,double latitud,  double longitud, 
    		double velocidad){
    this.id=id;
	this.vehiculos_matricula=matricula;
	this.velocidad=velocidad;
	this.latitud=latitud;
	this.longitud=longitud;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVehiculos_matricula() {
		return vehiculos_matricula;
	}

	public void setVehiculos_matricula(String vehiculos_matricula) {
		this.vehiculos_matricula = vehiculos_matricula;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	
    
}