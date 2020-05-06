package com.sensor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//POJO class para las entidades del context broker
public class Entity{

	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
	@JsonProperty("latitud")
	private Attribute latitud;
	@JsonProperty("longitud")
	private Attribute longitud;
	@JsonProperty("velocidad")
	private Attribute velocidad;

  public Entity(){}
  
  public Entity(String id, Attribute latitud, Attribute longitud, 
  		Attribute velocidad, String type){
  this.id=id;
	this.latitud=latitud;
	this.longitud=longitud;
	this.velocidad=velocidad;
	this.type=type;
  }

  public String getId(){
	return id;
  }
  public void setId(String id){
  	this.id=id;
  }
  
  public Attribute getLatitud(){
	return latitud;
  }
  public void setLatitud(Attribute latitud){
  	this.latitud=latitud;
  }

  public Attribute getLongitud(){
	return longitud;
  }
  public void setLongitud(Attribute longitud){
  	this.longitud=longitud;
  }

  public Attribute getVelocidad(){
	return velocidad;
  }
  public void setVelocidad(Attribute velocidad){
  	this.velocidad=velocidad;
  }
  
  public String getType(){
	return type;
  }
  public void setType(String type){
  	this.type=type;
  }
  
}
