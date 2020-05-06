package com.sensor.dto;

public class Usuarios_has_vehiculos {

	private String usuarios_username;
	private String vehiculos_matricula;
	
	public Usuarios_has_vehiculos() {
		
	}

	public Usuarios_has_vehiculos(String usuarios_username, String vehiculos_matricula) {
		super();
		this.usuarios_username = usuarios_username;
		this.vehiculos_matricula = vehiculos_matricula;
	}

	public String getUsuarios_username() {
		return usuarios_username;
	}

	public void setUsuarios_username(String usuarios_username) {
		this.usuarios_username = usuarios_username;
	}

	public String getVehiculos_matricula() {
		return vehiculos_matricula;
	}

	public void setVehiculos_matricula(String vehiculos_matricula) {
		this.vehiculos_matricula = vehiculos_matricula;
	}
	
	
}
