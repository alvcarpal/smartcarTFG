package com.sensor.dto;

public class Sensor {

	String latitud;
	String longitud;
	String velocidad;
	String sensor;
	
	public Sensor() {
		
	}
	
	
	public Sensor(String latitud, String longitud, String velocidad, String sensor) {
		super();
		this.latitud = latitud;
		this.longitud= longitud;
		this.velocidad = velocidad;
		this.sensor = sensor;
	}
	
	public String getSensor() {
		return sensor;
	}


	public void setSensor(String sensor) {
		this.sensor = sensor;
	}


	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(String velocidad) {
		this.velocidad = velocidad;
	}


	public String getLongitud() {
		return longitud;
	}


	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	
	
	
	
}
