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

public class Usuario {
	
	// Identificador del usuario
	String user_id;
	// Tipo de vehiculo
	String tipo;
	// Matricula del vehiculo
	String matricula;
		
	public Usuario() {
		
	}
	
	

	public Usuario(String user_id, String tipo, String matricula) {
		super();
		this.user_id = user_id;
		this.tipo = tipo;
		this.matricula = matricula;
	}



	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	
}
	
	