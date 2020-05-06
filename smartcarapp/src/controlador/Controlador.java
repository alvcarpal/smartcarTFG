/***************************************************************
*  Autor: Álvaro Carmona Palomares                             *
*  Versión: v3                                                 *
*  Fecha: 03/04/2020                                           *
*                                                              *
*  Descripción: La clase controlador actuará como controlador  *
*  de un modelo-vista-controlador, permitiendo a la vista      *
*  mostrar los datos procedentes del modelo                    *
*                                                              *
*                                                              *
***************************************************************/

package controlador;


import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Servlet implementation class Controlador
 */
@WebServlet("/Controlador")
public class Controlador extends HttpServlet {
	
	// Correo del usuario
	String mail = null;
	// Contraseña del usuario
	String pass = null;
	// String usado para almacenar el token en caso de ser usuario válido o de denegar el acceso si es Unauthorized
	String auth=null;
	// String usado para almacenar un oauth2 token de usuario
	String oauth=null;
	
	private static final long serialVersionUID = 1L;
	private static final String AUTENTICAR = "Ingresar";
	private static final String NO_AUTORIZADO = "Unauthorized";
	private static final String ADMIN = "true";
	private static final String ALTA_USUARIO = "Crear";
	
	// Creamos los parámetros necesarios para generar el fichero log
	private static final Logger LOGGER = Logger.getLogger(Controlador.class.getName());  

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
					
					// Una vez pulsemos input de index.jsp con nombre accion comprobamos si su valor es Ingresar
					response.setContentType("text/html;charset=UTF-8");
			    	String accion = request.getParameter("accion");
			    	
			    	// En caso de ser Ingresar significará que se ha pulsado el botón, por lo que hacemos la petición http al endpoint del SW Restful para validar el usuario
			    	if (accion.equals(AUTENTICAR)) {
				    	
			    		
			    		// Correo del usuario
				    	mail = request.getParameter("txtcorreo");
				    	// Contraseña del usuario
				    	pass = request.getParameter("txtpass");
				    	// String que será pasado en la petición POST
				    	String myString = null;
				    	
				    	// Llamamos al método cadenaJSON para crear nuestra cadena con formato JSON
				    	myString=cadenaJSON(mail,pass,null);
				    	
				    	// Creamos el httpClient a usar
			    		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			    		
			    		// Hacemos la petición POST al endpoint del SW Rest encargado de pedir el token de acceso
			    		HttpPost peticion = new HttpPost("http://localhost:8080/keyrock/token");
			    		StringEntity params = new StringEntity(myString);
			    		// Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
			    		peticion.addHeader("content-type", "application/json");
			    		peticion.setEntity(params);
			    		    
			    		// Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
			    		HttpResponse respuesta=httpClient.execute(peticion);
			    		    
			    		// Creamos un httpentity para poder obtener el body de la respuesta
			    		HttpEntity httpEntity = respuesta.getEntity();
			    		    // Comprobamos si hay respuesta
			    		    if (httpEntity != null) {
			    		    	// Convertimos la respuesta en string para poder parsearlo 
			    		    	String responseBody = EntityUtils.toString(httpEntity);
			    		    	
			    		    	// Respuesta de la función con el token o con Unauthorized si no está registrado el usuario en Keyrock
			    		    	auth = parserJSON(responseBody,6,"[ {},:]");
			    		    	
			    		    	// El usuario no es valido, debemos volver a index.jsp y mostrar cuadro indicado la no autorización
			    		    	if(auth.equals(NO_AUTORIZADO)) {
			    		    			
			    		    		// Redirigimos a index.jsp
			    		    		request.getRequestDispatcher("index.jsp").forward(request, response);			    		    					    		    		
			    		    	} else {
			    		    		// Suponemos que en caso de ser cualquier otro caracter es un token valido por lo que debemos dar paso
			    		    		// Ahora debemos consultar mediante Keyrock si el usuario con acceso es admin o no 
			    		    		
			    		    	
					    			// Hacemos la petición POST al endpoint del SW Rest encargado de pedir la info del user
					    			peticion = new HttpPost("http://localhost:8080/keyrock/info");
					    		    params = new StringEntity(auth);
					    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
					    		    peticion.addHeader("content-type", "application/json");
					    		    peticion.setEntity(params);
					    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
					    		    respuesta=httpClient.execute(peticion);
					    		    
					    		    
					    		    httpEntity = respuesta.getEntity();
					    		    // Comprobamos si hay respuesta
					    		    if (httpEntity != null) {
					    		    	// Convertimos la respuesta en string para poder parsearlo 
					    		    	responseBody = EntityUtils.toString(httpEntity);
					    		    	
					    		    	if (responseBody.equals(ADMIN)) {
					    		    		
					    		    		// Obtenemos un oauth2 token para el usuario
					    		    		oauth=getOauth2Token(mail,pass);
					    		    		
					    		    					    		    		
					    		    		// Agregamos los parámetros necesarios a rquest
					    		            request=buildrequest(request,auth,mail,pass,"Administrador",oauth,"Sesión iniciada correctamente");
					    		        	// También debemos de configurar la sesión correspondiente al usuario administrador
					    		    		HttpSession sesion=request.getSession(true);					    		    				    		    		
					    		            sesion.setAttribute("admin",mail);
					    		    	
					    		    		// Usuario administrador
					    		    		// Redirigimos a admin.jsp
					    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
					    		    		
					    		    	} else if (responseBody.equals("false")) {
					    		   					    		    		
					    		            oauth=getOauth2Token(mail,pass);
					    		    		
					    		    		// Agregamos los parámetros necesarios a rquest
					    		            request=buildrequest(request,auth,mail,pass,"Usuario",oauth,"Sesión iniciada correctamente");
					    		            // También debemos de configurar la sesión correspondiente al usuario estandar
					    		    		HttpSession sesion=request.getSession(true);
					    		            sesion.setAttribute("usuario",mail);
					    		    							    		    		
					    		    		//Usuario no administrador
					    		    		// Redirigimos a usu.jsp
					    		    		request.getRequestDispatcher("user/usuario/usu.jsp").forward(request, response);
					    		    		
					    		    	} else if (responseBody.equals("error")) {
					    		    		
					    		    		// Redirigimos a index.jsp
					    		    		request.getRequestDispatcher("index.jsp").forward(request, response);
					    		    	}
					    		    }
			    		    	}
			    		    }
			    	}
			    	
			    	// 	En caso de ser crear significará que el administrador quiere crear un usuario a partir de los parámetros dados en el form
			    	if (accion.equals(ALTA_USUARIO)) {
			    			
				    		// String que será pasado en la petición POST
					    	String myString = null;
					    	// Nombre del usuario
					    	String nombre =null;
					    	// Cooreo del usuario a crear
					    	String correo =null;
					    	// Contraseña del usuario a crear
					    	String password =null;
					    	// Matricula del usuario
					    	String matricula=null;
					    	// Tipo de vehiculo
					    	String tipo=null;
					    	
					    	
					    	// Obtenemos los valores introducidos en el formulario
					    	nombre = request.getParameter("txtnombre");
					    	correo = request.getParameter("txtcorreo");
					    	password = request.getParameter("txtpass");
					    	matricula=request.getParameter("txtmat");
					    	tipo=request.getParameter("txtipo");
					    						    	
					    	// Creamos el httpClient correspondiente
					    	CloseableHttpClient httpClient = HttpClientBuilder.create().build();
					    	
			    			// Convertimos el JSON en un string
			    			myString =cadenaJSON(correo,password,auth,nombre);
			    			
			    			// Hacemos la petición POST al endpoint del SW Rest encargado de pedir el token de acceso
			    			HttpPost peticion = new HttpPost("http://localhost:8080/keyrock/alta");
			    		    StringEntity params = new StringEntity(myString);
			    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
			    		    peticion.addHeader("content-type", "application/json");
			    		    peticion.setEntity(params);
			    		    
			    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
			    		    HttpResponse respuesta=httpClient.execute(peticion);
			    		    
			    		    // Creamos un httpentity para poder obtener el body de la respuesta
				    		HttpEntity httpEntity = respuesta.getEntity();
				    		
				    		// Comprobamos si hay respuesta
				    		if (httpEntity != null) {
				    		    	
				    			// Convertimos la respuesta en string para poder parsearlo 
				    		    String responseBody = EntityUtils.toString(httpEntity);
				    		   		    		    
				    		    if (responseBody.equals("error")) {
				    		    	
				    		    	// Agregamos los parámetros necesarios a rquest
			    		            request=buildrequest(request,auth,mail,pass,"Administrador",oauth,"Error en la creación del usuario");
				    		   			    		 	    		    		
			    		    		// Usuario administrador
			    		    		// Redirigimos a admin.jsp
			    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
			    		    		
			    		    	} else {
			    		    		
			    		    		// Guardamos el id de usuario y hacemos la petición a Wilma para crear el nuevo usuario
							    	// Llamamos al método cadenaJSON para crear nuestra cadena con formato JSON
							    	myString=cadenawilmaJSON(responseBody,tipo,matricula);
							    	
							    	// Creamos el httpClient a usar
						    		httpClient = HttpClientBuilder.create().build();
						    		
						    		// Hacemos la petición POST al endpoint del SW Rest encargado de pedir el token de acceso
						    		peticion = new HttpPost("http://localhost:1027/sensor/alta");
						    		params = new StringEntity(myString);
						    		// Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
						    		peticion.addHeader("content-type", "application/json");
						    		peticion.addHeader("Authorization:", "Bearer "+oauth);
						    		peticion.setEntity(params);
						    		    
						    		// Obtenemos la respuesta del servicio con el exito o no de la creación de usuario
						    		respuesta=httpClient.execute(peticion);
						    		httpEntity = respuesta.getEntity();
						    		
						    		if (httpEntity != null) {
						    			// Convertimos la respuesta en string para poder parsearlo 
					    		    	 responseBody = EntityUtils.toString(httpEntity);
							    		// Comprobamos si hay respuesta
							    		if (responseBody.equals("true")) {
							    			
							    			// Agregamos los parámetros necesarios a rquest
					    		            request=buildrequest(request,auth,mail,pass,"Administrador",oauth,"Creación de usuario realizada correctamente");
							    								    		    		
							    			// Redirigimos a admin.jsp
					    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
							    		} else {
							    			
							    			// Agregamos los parámetros necesarios a rquest
					    		            request=buildrequest(request,auth,mail,pass,"Administrador",oauth,"Vehículo existente");
					    		    							    			
							    			// Redirigimos a usu.jsp
					    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
							    		}
						    			
						    		} else {
						    			
						    			// Agregamos los parámetros necesarios a rquest
				    		            request=buildrequest(request,auth,mail,pass,"Administrador",oauth,"Error en la creación del usuario");
				    		 	    		    		
				    		    		// Usuario administrador
				    		    		// Redirigimos a admin.jsp
				    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
				    		    		
						    		}
						    		
						    		
			    		    	}
				    		}
			    	}
			    	
			    	// En caso de ser modificar significará que el usuario quiere cambiar su información personal
			    	if (accion.equals("Modificar")) {
			    		
			    		// String que será pasado en la petición POST
				    	String myString = null;
				    	
				    	// Información del usuario
				    	String nombre =null;
				        String token=null;
				    	String mail =null;
				    	String tipo =null;
				    	
				    	// Obtenemos los valores introducidos en el formulario
				    	nombre = request.getParameter("txtnombre");
				    	mail = request.getParameter("txtcorreo");
				    	token = request.getParameter("txtoken");
				    	
				    	
				    	// Creamos el httpClient correspondiente
				    	CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				    	
		    			// Convertimos el JSON en un string
		    			myString =cadenaJSON(mail,null,token,nombre);
		    			
		    			// Hacemos la petición POST al endpoint del SW Rest encargado de pedir el token de acceso
		    			HttpPost peticion = new HttpPost("http://localhost:8080/keyrock/dato_modificado");
		    		    StringEntity params = new StringEntity(myString);
		    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
		    		    peticion.addHeader("content-type", "application/json");
		    		    peticion.setEntity(params);
		    		    
		    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
		    		    HttpResponse respuesta=httpClient.execute(peticion);
		    		    
		    		    // Creamos un httpentity para poder obtener el body de la respuesta
			    		HttpEntity httpEntity = respuesta.getEntity();
			    		
			    		 if (httpEntity != null) {
			    			 
			    			// Convertimos la respuesta en string para poder parsearlo 
		    		    	 String responseBody = EntityUtils.toString(httpEntity);
				    		// Comprobamos si hay respuesta
				    		if (responseBody.equals("admin")) {
				    			
				    			// Agregamos los parámetros necesarios a rquest
		    		            request=buildrequest(request,auth,mail,pass,"Administrador",oauth,"Modificación de usuario realizada correctamente");
				    			    		    		
				    			// Redirigimos a admin.jsp
		    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
				    		} else {
				    			
				    			// Agregamos los parámetros necesarios a rquest
		    		            request=buildrequest(request,auth,mail,pass,"Usuario",oauth,"Modificación de usuario realizada correctamente");
				    				    			
				    			// Redirigimos a usu.jsp
		    		    		request.getRequestDispatcher("user/usuario/usu.jsp").forward(request, response);
				    		}
			    		 } 
			    	}
			    	
			    	
			    	// En caso de Solicitar el usuario solicitará información de su matrícula
			    	if (accion.equals("Solicitar")) {
			    		
			    		// Obtenemos los parámetros del formulario
			    		// Información del usuario
				    	String matricula =null;
				        String token=null;
				    	String userid =null;
				    	String myString=null;
				    	String tipo=null;
				    	
				    	// Obtenemos los valores introducidos en el formulario
				    	matricula = request.getParameter("txtmat");
				    	userid = request.getParameter("txtuserid");
				    	token = request.getParameter("txtoken");
				    	tipo = request.getParameter("txtipo");			    	
				    	
				    	// Hacemos la petición a wilma para eliminar el usuario de PostgreSQL
	    		    	myString =cadenawilmaJSON(userid,null,matricula);
	    		    	
	    		    	// Hacemos la petición al servicio web encargado de las comunicaciones con Keyrock
				    	CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		 					    			
		    			// Si el usuario tiene rol owner es administrador, por lo que tiene permisos para eliminar a un usuario de Keyrock
	    		    	HttpPost peticion = new HttpPost("http://localhost:1027/sensor/info");
	    		    	StringEntity params = new StringEntity(myString);
		    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
		    		    peticion.addHeader("content-type", "application/json");
		    		    peticion.addHeader("Authorization: ","Bearer "+oauth);
		    		    peticion.setEntity(params);
		    		    
		    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
		    		    HttpResponse respuesta=httpClient.execute(peticion);
		    		    // Creamos un httpentity para poder obtener el body de la respuesta
		    		    HttpEntity  httpEntity = respuesta.getEntity();
		    		    
		    		    if (httpEntity != null) {
			    			 
			    			// Convertimos la respuesta en string para poder parsearlo 
		    		    	 String responseBody = EntityUtils.toString(httpEntity);
		    		    	 
		    		    	 if(tipo.equals("Administrador")) {
		    		    		// Agregamos los parámetros necesarios a rquest
					    		request=buildrequest(request,auth,mail,pass,"Administrador",oauth,responseBody);
							    			    		    		
					    		// Redirigimos a admin.jsp
					    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
		    		    	 } else  {
		    		    		// Agregamos los parámetros necesarios a rquest
				    		    request=buildrequest(request,auth,mail,pass,"Usuario",oauth,responseBody);
						    			    		    		
				    		    // Redirigimos a admin.jsp
				    		    request.getRequestDispatcher("user/usuario/usu.jsp").forward(request, response);
		    		    	 }
				    		
		    		    	
			    		 } 
			    	}
			    	
			    	
			    	
			    	// En caso de Eliminar el administrador desea eliminar un usuario de Keyrock y la base de datos PostgreSQL
			    	if (accion.equals("Eliminar")) {
			    		
			    		// Obtenemos los parámetros del formulario
			    		// Información del usuario
				    	String matricula =null;
				        String token=null;
				    	String userid =null;
				    	String myString=null;
				    	
				    	// Obtenemos los valores introducidos en el formulario
				    	matricula = request.getParameter("txtmat");
				    	userid = request.getParameter("txtuserid");
				    	token = request.getParameter("txtoken");
				    	
				    	// Hacemos la petición al servicio web encargado de las comunicaciones con Keyrock
				    	CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				    	
		    			// Hacemos la petición POST al endpoint del SW Rest encargado de comprobar el rol de usuario
		    			HttpPost peticion = new HttpPost("http://localhost:8080/keyrock/permisos");
		    		    StringEntity params = new StringEntity(token);
		    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
		    		    peticion.addHeader("content-type", "application/json");
		    		    peticion.setEntity(params);
		    		    
		    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
		    		    HttpResponse respuesta=httpClient.execute(peticion);
		    		    
		    		    HttpEntity httpEntity = respuesta.getEntity();
		    		    
		    		    // Comprobamos si hay respuesta
		    		    if (httpEntity != null) {
		    		    	// Convertimos la respuesta en string para poder parsearlo 
		    		    	 String responseBody = EntityUtils.toString(httpEntity);
			    		
			    		
							if (responseBody.equals("owner")) {
				    		
				    			myString =cadenaJSON(userid,null,token);
				    			
				    			// Si el usuario tiene rol owner es administrador, por lo que tiene permisos para eliminar a un usuario de Keyrock
				    			peticion = new HttpPost("http://localhost:8080/keyrock/baja");
				    		    params = new StringEntity(myString);
				    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
				    		    peticion.addHeader("content-type", "application/json");
				    		    peticion.setEntity(params);
				    		    
				    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
				    		    respuesta=httpClient.execute(peticion);
				    		    
				    		    // Creamos un httpentity para poder obtener el body de la respuesta
				    		    httpEntity = respuesta.getEntity();
				    		    // Convertimos la respuesta en string para poder parsearlo 
			    		    	responseBody = EntityUtils.toString(httpEntity);
			    		    	
				    		    // Comprobamos si el usuario tiene los privilegios adecuados
				    		    if (responseBody.equals("OK")) {
				    		    	
				    		    	// Hacemos la petición a wilma para eliminar el usuario de PostgreSQL
				    		    	myString =cadenawilmaJSON(userid,null,matricula);
					 					    			
					    			// Si el usuario tiene rol owner es administrador, por lo que tiene permisos para eliminar a un usuario de Keyrock
					    			peticion = new HttpPost("http://localhost:1027/sensor/baja");
					    		    params = new StringEntity(myString);
					    		    // Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
					    		    peticion.addHeader("content-type", "application/json");
					    		    peticion.addHeader("Authorization: ","Bearer "+oauth);
					    		    peticion.setEntity(params);
					    		    
					    		    // Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
					    		    respuesta=httpClient.execute(peticion);
					    		    // Creamos un httpentity para poder obtener el body de la respuesta
					    		    httpEntity = respuesta.getEntity();
					    		    responseBody = EntityUtils.toString(httpEntity);
					    		  
					    		    // Comprobamos la respuesta
					    		    if (responseBody.equals("true")) {
					    		    	
					    		    	// Agregamos los parámetros necesarios a rquest
				    		            request=buildrequest(request,token,mail,pass,"Administrador",oauth,"Se ha dado de baja al usuario con identificador "+userid+" correctamente");
				    		            		    		    	
						    			// Redirigimos a admin.jsp
				    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
					    		    } else {
					    		    	// Agregamos los parámetros necesarios a rquest
				    		            request=buildrequest(request,token,mail,pass,"Administrador",oauth,"Error en la eliminación del usuario con ID "+userid);
				    		            		    		  				    		    		
						    			// Redirigimos a admin.jsp
				    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
					    		    }
					    		  
				    		    	
				    		    } else {
				    		    	// Agregamos los parámetros necesarios a rquest
			    		            request=buildrequest(request,token,mail,pass,"Administrador",oauth,"Error en la eliminación del usuario con ID "+userid);
			    		            	
					    			// Redirigimos a admin.jsp
			    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
				    		    	
					    		}
				    		    
				    					
				    		} else {
				    			// Agregamos los parámetros necesarios a rquest
		    		            request=buildrequest(request,token,mail,pass,"Administrador",oauth,"No se tienen los privilegios necesarios");
		    		            	
				    			// Redirigimos a admin.jsp
		    		    		request.getRequestDispatcher("user/administrador/admin.jsp").forward(request, response);
				    		  }
		    		    }
			    	}

		    	}
	
	
	
	
	/**
	 * Método para agregar los parámetros necesarios a la request
	 * que se devolverá al jsp correspondiente.
	 * 
	 */
	protected HttpServletRequest buildrequest (HttpServletRequest request, String token, String mail,String pass, String tipo, String oauth, String info)   {
		
		// Agregamos los parámetros a request
		request.setAttribute("token",token);
		request.setAttribute("user",mail);	
		request.setAttribute("tipo",tipo);
		request.setAttribute("pass",pass);	
		request.setAttribute("oauth",oauth);
		request.setAttribute("info",info);
		
		
		return request;
		
	}
	
	/**
	 * Método para hacer la petición al servicio rest,
	 * el cual se encarga de pedir un oauth2 
	 * @throws IOException 
	 * 
	 * @Param String mail: Correo del usuario
	 *        String password: Contraseña del usuario
	 *        
	 * @return String responseBody: Bearer token obtenido
	 * 
	 */
	protected String getOauth2Token(String mail, String password) throws IOException  {
		
		// String que será pasado en la petición POST
    	String myString = null;
    	
    	// Llamamos al método cadenaJSON para crear nuestra cadena con formato JSON
    	myString=cadenaJSON(mail,pass,null);
    	
    	// Creamos el httpClient a usar
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		// Hacemos la petición POST al endpoint del SW Rest encargado de pedir el token de acceso
		HttpPost peticion = new HttpPost("http://localhost:8080/keyrock/bearertoken");
		StringEntity params = new StringEntity(myString);
		// Añadimos todos los campos necesarios a la peticion POST y la ejecutamos finalmente
		peticion.addHeader("content-type", "application/json");
		peticion.setEntity(params);
		    
		// Obtenemos la respuesta del servicio web para ver si el usuario es autorizado o no
		HttpResponse respuesta=httpClient.execute(peticion);
		    
		// Creamos un httpentity para poder obtener el body de la respuesta
		HttpEntity httpEntity = respuesta.getEntity();
		    
		// Comprobamos si hay respuesta
		if (httpEntity != null) {
		    	
			// Convertimos la respuesta en string para poder parsearlo 
		    String responseBody = EntityUtils.toString(httpEntity);
		    	
		    return responseBody;
		    	
		}
		
		
		return null;
	}
	
	/**
	 * Método para crear una cadena con formato JSON
	 * En caso de querer más de una posición debemos llamar varias veces a la función
	 * 
	 */
	protected String cadenaJSON(String mail, String password, String token) throws IOException {
	
		//Cadena devuelta
		String cadena=null;
		
		try {	
			// Creamos el objeto JSON con los campos mail y password
			JSONObject obj = new JSONObject();
			obj.put("mail", mail);
			obj.put("password", password);
			obj.put("token", token);
			
			// Convertimos el JSON en un string
			cadena =obj.toString();
			
			return cadena;
		} catch (JSONException e) {
			System.out.println(e);
		}
		
		return cadena;
	}
	
	/**
	 * Método para crear una cadena con formato JSON
	 * En caso de querer más de una posición debemos llamar varias veces a la función
	 * 
	 */
	protected String cadenawilmaJSON(String id, String tipo, String matricula) throws IOException {
	
		//Cadena devuelta
		String cadena=null;
		
		try {	
			// Creamos el objeto JSON con los campos mail y password
			JSONObject obj = new JSONObject();
			obj.put("user_id", id);
			obj.put("tipo", tipo);
			obj.put("matricula", matricula);
			
			// Convertimos el JSON en un string
			cadena =obj.toString();
			
			return cadena;
		} catch (JSONException e) {
			System.out.println(e);
		}
		
		return cadena;
	}
	
	/**
	 * Método para crear una cadena con formato JSON con el nombre de usuario aobreescribimos el método
	 * En caso de querer más de una posición debemos llamar varias veces a la función
	 * 
	 */
	protected String cadenaJSON(String mail, String password, String token,String nombre) throws IOException {
	
		//Cadena devuelta
		String cadena=null;
		
		try {	
			// Creamos el objeto JSON con los campos mail y password
			JSONObject obj = new JSONObject();
			obj.put("mail", mail);
			obj.put("password", pass);
			obj.put("token", token);
			obj.put("nombre", nombre);
			
			// Convertimos el JSON en un string
			cadena =obj.toString();
			
			return cadena;
		} catch (JSONException e) {
			System.out.println(e);
		}
		
		return cadena;
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

