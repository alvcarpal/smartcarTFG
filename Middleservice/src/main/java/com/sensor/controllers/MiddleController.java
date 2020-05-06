package com.sensor.controllers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Entity;
import com.sensor.dto.*;

@Controller
public class MiddleController {
	
	//Método de testing para generar informaciçon de contexto
	@RequestMapping(method = RequestMethod.POST, value="/sensor/info")
	@ResponseBody
	public void test(@RequestBody Sensor sensor){
	    			
		// Creamos la entidad correspondiente, en caso de existir se nos indicará que ha habido un conflicto
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.text("lat|"+sensor.getLatitud()+"|lon|"+sensor.getLongitud()+"|vel|"+sensor.getVelocidad());
        Response response = client.target("http://192.168.0.34:7896/iot/d?k=4jggokgpepnvsb2uv4s40d59ov&i=Sensor001")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .post(payload);
        
        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
        
	}
							

}
