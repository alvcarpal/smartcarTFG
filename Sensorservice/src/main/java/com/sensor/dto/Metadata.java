package com.sensor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//class para los metadatos del context broker
public class Metadata{
	
  @JsonProperty("unidades")
  private Attribute unidades;
	
  public Metadata(){}
  
  public Metadata(Attribute unidades){
  this.unidades=unidades;
  }
  
  public Attribute getUnidades(){
	return unidades;
  }
  public void setUnidades(Attribute unidades){
  	this.unidades=unidades;
  }
  
}