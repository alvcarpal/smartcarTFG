/***************************************************************
*  Autor: Álvaro Carmona Palomares                             *
*  Versión: v2                                                 *
*  Fecha: 04/04/2020                                           *
*                                                              *
*  Descripción: Clase con anotaciones Spring para ejercer de   *
*  controlador, la cual se encargará de la comunicación con    *
*  la base de datos que almacena la información de los         *
*  usuarios                                                    *
*                                                              *
***************************************************************/

package com.sensor.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.sensor.database.DataBase;
import com.sensor.dto.Dato;
import com.sensor.dto.Notification;
import com.sensor.dto.Tipo;
import com.sensor.dto.User;
import com.sensor.dto.Usuario;
import com.sensor.dto.Vehiculo;

import net.minidev.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Entity;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.hibernate.boot.model.relational.Database;

@Controller
public class AppController {

	// Obtenemos las variables de interés
	@Value("${admin_id}")
	private String admin_id;
	@Value("${ip_port_contextbroker}")
	private String ip_port_contextbroker;
	@Value("${ip_port_sensorapp}")
	private String ip_port_sensorapp;
	
	private static final int CREADO = 201;
	
	// Método para dar de alta a un usuario y a su matrícula correspondiente
			@RequestMapping(method = RequestMethod.POST, value="/sensor/alta")
			@ResponseBody
			public ResponseEntity<String> crearusu(@RequestHeader("X-NICK-NAME") String username, @RequestBody Usuario user) throws IOException {
				
				// Comprobamos si es el administrador
				if (username.equals("admin")) {
					
					// Realizamos la consulta a la base de datos
			    	DataBase bd = new DataBase();
			    	boolean datos = bd.crearUsuario(user.getMatricula(), user.getUser_id(),user.getTipo());
			    						
			    	if (datos == true) {
			    		// Los datos se han creado correctamente, se crea la entidad correspondiente al nuevo vehículo
			    		crearsensor(user.getMatricula(),user.getTipo());
			    		
			    		// Nos subscribimos a la nueva matrícula, previamente creada por IoT Agent
			    		if (suscribir(user.getMatricula(),user.getTipo()) == true) {
			    			return  new ResponseEntity<String>("true", HttpStatus.OK);
			    		} else {
			    			return  new ResponseEntity<String>("error_suscripción", HttpStatus.OK);
			    		}
			    	} else {
			    		return  new ResponseEntity<String>("false", HttpStatus.OK);
			    	}
				}
				
							
				return new ResponseEntity<String>("false", HttpStatus.OK);
		}
			
			
		
	// Método para dar de alta a un usuario y a su matrícula correspondiente
		@RequestMapping(method = RequestMethod.POST, value="/sensor/info")
		@ResponseBody
		public ResponseEntity<String> pedirinfo(@RequestHeader("X-NICK-NAME") String username, @RequestBody Usuario user) throws IOException {
		
			System.out.println(username+":"+user.getUser_id());
	    	String error="Usuario Incorrecto";
			
			//Comprobamos que el usuario es quien dice ser
			if(username.equals(user.getUser_id())) {
				// Creamos los parámetros necesarios para almacenar las mediciones consultadas
				Dato dato;
		    	int count=1;
		    	
		    	String mensaje="No hay datos de viaje para los valores introducidos";
				
		    	// Hacemos la consulta al método de DataBase
		    	DataBase bd = new DataBase();
		    	List<Dato> viaje = bd.consultarDatos(user.getMatricula());
		    	
		    	//Recorremos los datos del viaje
		    	for(Iterator<Dato> i=viaje.iterator();i.hasNext();count++) {
		    		dato=i.next();
		    			    		
		    		if(count==1) {
		    			mensaje="Viaje "+count
		        				+"\nLa velocidad alcanzada es: "+dato.getVelocidad()+" km/h"
		        				+"\nLa latitud es: "+dato.getLatitud()
		        				+"\nLa longitud es: "+dato.getLongitud()+"\n";
		        	
		    		} else {
		    			
		    			mensaje=mensaje
		    					+"\nViaje "+count
		        				+"\nLa velocidad alcanzada es: "+dato.getVelocidad()+" km/h"
		        				+"\nLa latitud es: "+dato.getLatitud()
		        				+"\nLa longitud es: "+dato.getLongitud()+"\n";
		    		}
		    	}
		    	
		    	
		    	return new ResponseEntity<String>(mensaje, HttpStatus.OK);
			}
			
			return new ResponseEntity<String>(error, HttpStatus.OK);
		 							
		}
				
				
				
				
	
	// Método para dar de baja a un usuario y a su matrícula correspondiente
		@RequestMapping(method = RequestMethod.POST, value="/sensor/baja")
		@ResponseBody
		public ResponseEntity<String> eliminarusu(@RequestHeader("X-NICK-NAME") String username, @RequestBody Usuario user) throws IOException {
			
			// Hacemos una comprobación adicional para saber si es el usuario administrador
			if (username.equals("admin")) {
			  	
				System.out.println(user.getMatricula()+user.getUser_id());
				// Realizamos la consulta a la base de datos
		    	DataBase bd = new DataBase();
		    	boolean datos = bd.eliminarDatos(user.getMatricula(), user.getUser_id());
		    	
		    	if (datos == true) {
		    		
		    		// Los datos se han eliminado correctamente
		    		return  new ResponseEntity<String>("true", HttpStatus.OK);
		    	} else {
		    		return  new ResponseEntity<String>("false", HttpStatus.OK);
		    	}
			}
						
			return null;
		}
		
	//Recibe notificaciones del context broker y añade los nuevos datos en la base de datos PostgreSQL
		@RequestMapping(method = RequestMethod.POST, value="/sensor/notificaciones")
		@ResponseBody
	    public void notificaciones(@RequestBody Notification notificacion){
	    	
			System.out.println(notificacion.getSubscriptionId());
			
	    	//Introducimos los datos de la notificación en la base de datos
			if (notificacion.getData().iterator().hasNext()) {
							
				DataBase bd= new DataBase();
				bd.insertarDatos(notificacion);
			}
	    }
				
	// Crea la entidad correspondiente a un sensor 
		public void crearsensor(String mat, String tipo) throws IOException {
			
			// Creamos el id de la entidad
			String matricula=null;
			matricula="urn:ngsi-ld:Sensor:"+mat;
			// Creamos el identificador
			String device_id="Sensor008";
			
			// Creamos la entidad correspondiente, en caso de existir se nos indicará que ha habido un conflicto
	        Client client = ClientBuilder.newClient();
	        Entity payload = Entity.json("{  \"devices\": [    {      \"device_id\": \""+device_id+"\",       \"entity_name\": \""+matricula+ "\",      \"entity_type\": \"Sensor\",      \"timezone\": \"Europe/Madrid\",      \"attributes\": [        {          \"object_id\": \"lat\",          \"name\": \"latitud\",          \"type\": \"Float\"        } , {          \"object_id\": \"lon\",          \"name\": \"longitud\",          \"type\": \"Float\"        } , {          \"object_id\": \"vel\",          \"name\": \"velocidad\",          \"type\": \"Float\"        }        ],      \"static_attributes\": [        {          \"name\": \"tipo\",          \"type\": \"string\",          \"value\": \""+tipo+"\"        }      ]    }  ]}");
	        
	        // Hacemos la petición correspondiente al puerto norte del IoT-Agent, el cual se encargará de comunicarle a OCB la creación de la entidad
	        Response response = client.target("http://localhost:4041/iot/devices")
	                .request(MediaType.APPLICATION_JSON_TYPE)
	                .header("Fiware-Service", "smartcar")
	                .header("Fiware-ServicePath", "/")
	                .post(payload);
	        
	        System.out.println("status: " + response.getStatus());
	        System.out.println("headers: " + response.getHeaders());
	        System.out.println("body:" + response.readEntity(String.class));
	        			
		}
		    	
	//Suscribe una matrícula 
	    public boolean suscribir(String mat, String tipo) throws IOException {
	        
			// Id para realizar la subscripción
			String matricula=null;
			matricula="urn:ngsi-ld:Sensor:"+mat;
				
			// Mensaje en caso de error en la subscripción
		    boolean mensaje = false;
		        
		     
		    Client client = ClientBuilder.newClient();
		    Entity payload = Entity.json("{\"description\": \"A subscription to get info about GPS\", "
	        		+ "\"subject\": {\"entities\": [{\"idPattern\": \""+matricula+"\", \"typePattern\": \"Sensor\"}], "
	        		+ "\"condition\": {\"attrs\": [\"latitud\", \"longitud\", \"altitud\"]}}, " //Si cambia alguno se envia notificacion
	        		+ "\"notification\": {\"http\": {\"url\": \"http://"+ip_port_sensorapp+"/sensor/notificaciones\"}, "
	        		+ "\"attrs\": [], " //Si no se especifican atributos se notifican todos
	        		+ "\"attrsFormat\": \"normalized\"}}");
		    	
	    	Response response = client.target("http://"+ip_port_contextbroker+"/v2/subscriptions")
	    	       .request(MediaType.APPLICATION_JSON_TYPE)
	    	       .header("fiware-service","smartcar")
	    	       .header("fiware-servicepath","/")
	    	       .post(payload);
	
	    	 
	    	// Se crea la suscripción correctamente
	    	 if (response.getStatus()==CREADO) {
	    		 mensaje = true;
	    	 }
	    	            
	    	 return mensaje;
	 }	
	    	
	
	/**
	 * Método para parsear una cadena y obtener la posición dada como parámetro
	 * 
	 */
	protected String parserJSON(String body,int posicion, String delims) throws IOException {
		
		// Cadena a devolver
		String result=null;
		
		
		String[] tokens = body.split(delims);
		
    	for (int i = 0; i < tokens.length; i++)
		{   
    		// Obtenemos la posición pasada a la función donde se encuentra el dato de interés
			if (i==posicion) {
			
				result=tokens[i];
				// Finalmente debemos eliminar el carácter " para poder tener nuestro string final 
				result=result.replaceAll("\"", "");
			}
		}
    	
		return result;
	
	}
				
}
