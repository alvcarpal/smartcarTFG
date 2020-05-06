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

package com.example.dto;

public class Usuario {
	
	// Correo para la consulta a Keyrock
	String mail;
	// Contraseña para la consulta a Keyrcok
	String password;
	// Variable para indicar si un usuario es autorizado o no
	String token;
	// Nombre del usuario en el gestor de identidad
	String nombre;
	// Identificador de usuario
	String user_id;
		
	public Usuario() {
		
	}
	
	public Usuario(String mail, String password, String auth, String nombre, String token, String bearer) {
		super();
		this.mail = mail;
		this.password = password;
		this.token = token;
		this.nombre = nombre;
		
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
	
	
}
