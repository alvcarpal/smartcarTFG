/*************************************************************
*  Autor: Álvaro Carmona Palomares                           *
*  Versión: v1                                               *
*  Fecha: 06/02/2020                                         *
*                                                            *
*  Descripción: Un usuario se identificará por su contraseña *
*  y su correo                                               *
*                                                            *
*                                                            *
*************************************************************/

package com.sensor.dto;

public class User {
	
	// Nombre de usuario
	String username;
	// Role
	String role;
	// Organizacion
	String organizacion;
		
	public User() {
		
	}
	
	

	public User(String username, String role, String organizacion) {
		super();
		this.username = username;
		this.role = role;
		this.organizacion = organizacion;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}
	
	
	
}
	
