package com.sensor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//POJO class para las notificaciones del context broker
public class Attribute<T>{

	@JsonProperty("metadata")
	private Metadata metadata;
	@JsonProperty("type")
	private String type;
	@JsonProperty("value")
	private T value;
  
  public Attribute(){}
  
  public Attribute(Metadata metadata, String type, T value){
  this.metadata=metadata;
	this.type=type;
	this.value=value;
  }
  
  public Metadata getMetadata(){
	return metadata;
  }
  public void setMetadata(Metadata metadata){
  	this.metadata=metadata;
  }
  
  public String getType(){
	return type;
  }
  public void setType(String type){
  	this.type=type;
  }
  
  public T getValue(){
	return value;
  }
  public void setValue(T value){
  	this.value=value;
  }
}
