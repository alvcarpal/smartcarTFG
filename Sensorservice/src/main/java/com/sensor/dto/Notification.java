package com.sensor.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

//POJO class para las notificaciones del context broker
public class Notification{

	@JsonProperty("data")
	private List<Entity> data;
	@JsonProperty("subscriptionId")
    private String subscriptionId;
	
    public Notification(){}
    
    public Notification(List<Entity> data, String subscriptionId){
    this.data=data;
	this.subscriptionId=subscriptionId;
    }

    
    public String getSubscriptionId(){
	return subscriptionId;
    }
    public void setSubscriptionId(String subscriptionId){
    	this.subscriptionId=subscriptionId;
    }
    
    public List<Entity> getData(){
	return data;
    }
    
    public void setData(List<Entity> data){
    	this.data=data;
    }
}

