/****************************************************
*  Autor: Álvaro Carmona Palomares                  *
*  Versión: v3                                      *
*  Fecha: 18/04/2020                                *
*                                                   *
*  Descripción: Controlador que escucha peticiones  *
*  POST y realiza la petición del token a Keyrock   *
*  con los parámetros pasados mail y password       *
*                                                   *
*                                                   *
****************************************************/
package com.example.controllers;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.*;
import com.nimbusds.jose.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;



@Controller
public class TokenController {
	
	// Obtenemos las variables de interés
	@Value("${ip_port_keyrock}")
	private String ip_port_keyrock;
	@Value("${client_id}")
	private String client_id;
	@Value("${client_secret}")
	private String client_secret;
	@Value("${application_id}")
	private String application_id;
	@Value("${organization_id}")
	private String organization_id;
	
	// Establecemos los códigos Http
	private static final int OK = 200;
	private static final int CREADO = 201;
	private static final int NO_CONTENT = 204;
	private static final int Unauthorized = 401;

	
	/* Método para pedir token para un usuario 
	 * 
	 * @param: 
	 * 		   Usuario user: Objeto POJO serializable. Se recibe una cadena en formato JSON y se traduce directamente
	 * 		   en un objeto POJO Usuario.
	 * 
	 * @return: 
	 * 		   ResponseEntity<Usuario>:Objeto Usuario con el token o mensaje de error en el campo auth.
	 *
	 */
	@RequestMapping(method = RequestMethod.POST, value="/keyrock/token")
	@ResponseBody 
	public ResponseEntity<Usuario> createTokenPassword(@RequestBody Usuario user) throws IOException {
		
		// Obtenemos un usuario mediante el método getKeyrockToken, el cual devuelve un Usuario con el token o mensaje de no autorizado en el parámetro auth
		Usuario usu = null;
		usu=getKeyrockToken(user.getMail(),user.getPassword());
		
		// Obtenemos el token y comprobamos si es valido o no 
		if(usu.getToken()=="Unauthorized") {
			return new ResponseEntity<Usuario>(usu, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<Usuario>(usu, HttpStatus.OK);
		}
	}
	
	
	/* Método para solicitar información de usuario a partir de su token 
	 * 
	 * @param: 
	 * 			String token:Token del usuario del cual se quiere obtener la información para determinar 
	 * 			si el usuario es administrador o no.
	 * 
	 * @return: 
	 * 			ResponseEntity<String>(admin, HttpStatus.x): Valor de admin indicando si el usuario es administrador o no.
	 *
	 */
	@RequestMapping(method = RequestMethod.POST, value="/keyrock/info")
	@ResponseBody
	public ResponseEntity<String> userInfo(@RequestBody String token) throws IOException {
		
		// String usado para devolver si es admin o no, usamos string en vez de boolean para permitir un 3 caso pudiendo indicar si la respuesta de keyrock falla
		String admin=null;
		Client client = ClientBuilder.newClient();
		
		// Hacemos una peticion GET para obtener los datos del usuario asociados al token correspondiente
		Response response = client.target("http://"+ip_port_keyrock+"/v1/auth/tokens")
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .header("X-Auth-token",token)
		  .header("X-Subject-token",token)
		  .get();
		
				
		// Comprobamos si la respuesta obtenida es correcta y procedemos en caso positivo a parsear la respuesta para obtener el campo admin
		if (response.getStatus()== OK) {
						
						// Llamamos a la función de parseo pasando el user
						// Obtenemos si el usuario es admin o no
						admin=parserJSON(response.readEntity(String.class).toString(),26,"[,: {} ]");						
		} else {
			// Error en la respuesta, debemos volver a index.jsp y avisar del error
			admin="error";
		}
		
		return new ResponseEntity<String>(admin, HttpStatus.OK);
		
		
	}
	
	
	/* Método para dar de alta a un usuario 
	 * 
	 * @param: 
	 * 			String token:Token del usuario del cual se quiere obtener la información para determinar 
	 * 			si el usuario es administrador o no.
	 * 
	 * @return: 
	 * 			ResponseEntity<String>(admin, HttpStatus.x): Valor de admin indicando si el usuario es administrador o no.
	 *
	 */
	@RequestMapping(method = RequestMethod.POST, value="/keyrock/alta")
	@ResponseBody
	public ResponseEntity<String> createUser(@RequestBody Usuario user) throws IOException {
		
		// Respuesta devuelta
		String respuesta=null;
		
		// Creamos el usuario a partir de su nombre email y correo. Se comprueba que el token sea correcto y que ese usuario tenga permisos de creación
		Client client = ClientBuilder.newClient();
		   
		Entity payload = Entity.json("{  \"user\": {    \"username\": \""+user.getNombre()+"\",    \"email\": \""+user.getMail()+"\",    \"password\": \""+user.getPassword()+"\"  }}");
		Response response = client.target("http://"+ip_port_keyrock+"/v1/users")
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .header("X-Auth-token",user.getToken())
			  .post(payload);
		
		// Analizamos el resultado de la petición
		if (response.getStatus()==CREADO) {
			
			// Si se ha creado el usuario correctamente, debemos crear un permiso con su matrícula, el ID de la aplicación cambiará en caso de crear una nueva app
	        Usuario nuevo_usu=new Usuario();
	        String user_token=null;
	        String user_id=null;
	        
	        // Obtenemos un token para el nuevo usuario, para poder obtener su id, el cual será devuelto al servlet
	        // Es importante destacar que necesitamos por un lado el token del administrador y por otro lado un token del usuario creado para poder pedir su id
			nuevo_usu=getKeyrockToken(user.getMail(),user.getPassword());
			user_token=nuevo_usu.getToken();
			// Obtenemos el identificador
			user_id=getUserID(user_token);
			
			// Asignamos al nuevo usuario el rol miembro de la organización de nuestro sistema, limitando asi los recursos a los que tendrá acceso
			if (user_id != null) {
				payload = Entity.json("");
				Response response3 = client.target("http://"+ip_port_keyrock+"/v1/organizations/"+organization_id+"/users/"+user_id+"/organization_roles/member")
					  .request(MediaType.APPLICATION_JSON_TYPE)
					  .header("X-Auth-token", user.getToken())
					  .put(payload);
	
					return  new ResponseEntity<String>(user_id, HttpStatus.OK);	
										
			} else {
				// Error en la respuesta, debemos volver a index.jsp y avisar del error
				
				return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
			}
			
		} else {
			return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
		}
				
	}
	

	/* Método para modificar los datos de un usuario
	 * 
	 * @param: 
	 * 			Usuarios user: Clase POJO con los datos del usuario a modificar.
	 * 
	 * @return: 
	 * 			ResponseEntity<String>(admin, HttpStatus.x): Valor de admin indicando si el usuario es administrador o no.
	 *
	 */ 
		@RequestMapping(method = RequestMethod.POST, value="/keyrock/dato_modificado")
		@ResponseBody
		public ResponseEntity<String> modificarUsu(@RequestBody Usuario user) throws IOException {
			
			// Información de usuario
			String user_id=null;
			
			// Necesitamos obtener el user_id del usuario a partir de la información de su token
			Client client = ClientBuilder.newClient();
			
			Response response = client.target("http://"+ip_port_keyrock+"/v1/auth/tokens")
						  .request(MediaType.TEXT_PLAIN_TYPE)
						  .header("X-Auth-token",user.getToken())
						  .header("X-Subject-token",user.getToken())
						  .get();
						
			// Comprobamos si la respuesta obtenida es correcta y procedemos en caso positivo a parsear la respuesta para obtener el campo admin
			if (response.getStatus()==OK) {
							
							user_id=getUserID(user.getToken());
							
							// Modificamos el usuario con los datos aportados
							client = ClientBuilder.newClient().property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
							
							response = client.target("http://"+ip_port_keyrock+"/v1/users/"+user_id)
							  .request(MediaType.APPLICATION_JSON_TYPE)
							  .header("X-Auth-token", user.getToken())
							  .build("PATCH", Entity.json("{  \"user\": {    \"username\": \""+user.getNombre()+"\",    \"email\": \""+user.getMail()+"\"  }}"))
							  .invoke();
					
							return  new ResponseEntity<String>(user_id, HttpStatus.OK);
			}
					
						
			return  new ResponseEntity<String>("ERROR", HttpStatus.OK);
		}
		
		
		/* Método para dar de baja a un usuario
		 * 
		 * @param: 
		 * 			Usuarios user: Clase POJO con los datos del usuario a eliminar.
		 * 
		 * @return: 
		 * 			ResponseEntity<String>(response, HttpStatus.x): Respuesta de la operación.
		 *
		 */  
		@RequestMapping(method = RequestMethod.POST, value="/keyrock/baja")
		@ResponseBody
		public ResponseEntity<String> eliminarUsu(@RequestBody Usuario user) throws IOException {
		
			// Hacemos la petición a Keyrock para eliminar al usuario
			Client client = ClientBuilder.newClient();
			Response response = client.target("http://"+ip_port_keyrock+"/v1/users/"+user.getMail())
			  .request(MediaType.TEXT_PLAIN_TYPE)
			  .header("X-Auth-token", user.getToken())
			  .delete();
			
			if (response.getStatus()==NO_CONTENT) {
				
				// Exito al eliminar el usuario
				return  new ResponseEntity<String>("OK", HttpStatus.OK);
			} else {
				
				// No se pudo eliminar el usuario
				return  new ResponseEntity<String>("ERROR", HttpStatus.OK);
			}
			
		}
		
		/* Método para dar leer los permisos de un usuario
		 * 
		 * @param: 
		 * 			Usuarios user: Clase POJO con los datos del usuario.
		 * 
		 * @return: 
		 * 			ResponseEntity<String>(response, HttpStatus.x): Rol del usuario.
		 *
		 */   
		@RequestMapping(method = RequestMethod.POST, value="/keyrock/permisos")
		@ResponseBody
		public ResponseEntity<String> readPermission(@RequestBody String token ) throws IOException {
			
			// Obtenemos los parámetros de la cadena JSON pasada 
			String userid=null;
			String role=null;
						
			// Obtenemos el user_id a partir del token dado
			userid=getUserID(token);
				
			// Realizamos la petición a Keyrock y obtenemos el role del usuario dentro de la organización dada de alta en la aplicación
			Client client = ClientBuilder.newClient();
			Response response = client.target("http://"+ip_port_keyrock+"/v1/organizations/"+organization_id+"/users/"+userid+"/organization_roles")
			  .request(MediaType.TEXT_PLAIN_TYPE)
			  .header("X-Auth-token", token)
			  .get();
			
			if (response.getStatus()==OK) {
				
				// Exito al obtener el role del usuario		
				role=parserJSON(response.readEntity(String.class),8,"[ {},:]");
				
			} else {
				
				// Usuario no encontrador en la organización
				role="Error";
			}

			return  new ResponseEntity<String>(role, HttpStatus.OK);
		}
		
	
		/* Método para pedir un bearer token mediante flujo Oauth v2 a partir de un flujo de password
		 * 
		 * @param: 
		 * 			Usuario user: Clase POJO con los datos del usuarios previamente autenticado.
		 * 
		 * @return: 
		 * 			String token: Bearer token obtenido para el usuario.
		 *
		 */
		@RequestMapping(method = RequestMethod.POST, value="/keyrock/bearertoken")
		@ResponseBody
		public ResponseEntity<String> getOauth2token(@RequestBody Usuario user) throws IOException {
			
			// Obtenemos el token a partir del correo y contraseña de un usuario previamente autenticado
			String token=null;
			
			token=getToken(user.getMail(),user.getPassword());
			token=parserJSON(token,2,"[ {},:]");
			
			
			return new ResponseEntity<String>(token, HttpStatus.OK);			
			
	}
	
	
	/**
	* Método para obtener el id de un usuario a partir de su token 
	* @throws IOException 
	* 
	* @param String token: Token del usuario del cual se quiero obtener su ID de usuario.
	* 
	* @return String user_id: Identificador de usuario, obtenido a partir de su token.
	* 
	**/
	protected String getUserID(String token) throws IOException{
		
		// Necesitamos obtener el user_id del usuario a partir de la información de su token
		Client client = ClientBuilder.newClient();
		
		Response response = client.target("http://"+ip_port_keyrock+"/v1/auth/tokens")
					.request(MediaType.TEXT_PLAIN_TYPE)
					.header("X-Auth-token",token)
					.header("X-Subject-token",token)
					.get();
		
		// Comprobamos si la respuesta obtenida es correcta y procedemos en caso positivo a parsear la respuesta para obtener el identificador
		if (response.getStatus()==OK) {
			
			// Obtenemos el identificador de usuario
			String user_id=parserJSON(response.readEntity(String.class).toString(),14,"[,: {} ]");
			
			return user_id;
		}
			
		return null;	
	}
		
	/**
	* Método para obtener un token de Keyrock que nos autentique dentro del idm 
	* @throws IOException 
	* 
	* @param  String mail: Correo del usuario que quiere autenticarse.
	*		  String password: Contraseña del usuario que quiere autenticarse.
	*
	* @return Usuario usu: Objeto POJO con los datos del usuario.
	* 
	*/
	protected Usuario getKeyrockToken(String mail, String password) throws IOException  {
		
				String token=null;
		
				// Hacemos la petición a la URL correspondiente de Keyrock, pasando el user y password pasados mediante el formulario
				Client client = ClientBuilder.newClient();
				Entity payload = Entity.json("{  \"name\": \""+mail+"\",  \"password\": \""+password+"\"}");
				// Realizamos la petición POST
				Response response = client.target("http://"+ip_port_keyrock+"/v1/auth/tokens")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .post(payload);
				
				// Usuario estandar para poder devolverlo vacio en caso de error
				Usuario usu =new Usuario();
				
				// Una vez tenemos respuesta a la petición, comprobamos si tenemos acceso a Keyrock (201) o no tenemos acceso (401)
				if (response.getStatus() == CREADO) {
					
					// Obtenemos el token
					token=parserJSON(response.getHeaders().toString(),69,"[,:= \\[ ]");
					
					// Hacemos un último parse para obtener el token final
					String delimresult = "[\\] ]";
					String[] iterador = token.split(delimresult);
					
					// Guardamos el token final en la respuesta que devuelve el servicio web 
					for (int j = 0; j < iterador.length; j++) {
						token=iterador[j];
					}
									
				} else if (response.getStatus() == Unauthorized) {
					
					// En caso de ser no autorizado devolvemos un string con no autorizado para mostar al usuario
					token="Unauthorized";	
				}
		
				// Rellenamos el usuario con sus datos
				usu.setMail(mail);
				usu.setPassword(password);
				usu.setToken(token);
				
				return usu;
			     		
	}
		
		
		

	/**
	* Método para obtener un oauth2 token según un flujo de password
	* 
	* @param String username: Correo del usuario autenticado en el sistema.
	*        String password: Contraseá del usuario autenticado en el sistema.
	*        
	* @return String result: Bearer token obtenido para el usuario.
	* 
	*/
	protected String getToken(String username, String password)  {
			
		// Variables a enviar en la petición para obtener un oauth2 token con flujo de password
		// La url donde escucha el idm Keyrock
		String urlString = "http://"+ip_port_keyrock+"/oauth2/token";
		// Codificamos client_id y client_secret en base64
		String idColonSecret = client_id+":"+client_secret;
		String basicAuthPayload = "Basic " + Base64.encode(idColonSecret);
			
		// Creamos los elementos necesarios para hacer la peitición http
		URL url = null;
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        
        
         try {
        	 
        	 // Creamos el urlConnection, al cual debemos añadir todos los parámetros correspondientes de la petición
             url = new URL(urlString);
             urlConnection = (HttpURLConnection) url.openConnection();
             urlConnection.setRequestMethod("POST");
             urlConnection.setRequestProperty("Authorization", basicAuthPayload);
             urlConnection.setDoOutput(true);

             // Creamos la cadena data con el formato a seguir en un body urlencoded
             String data = URLEncoder.encode("password", "UTF-8")
                     + "=" + URLEncoder.encode(password, "UTF-8");

             data += "&" + URLEncoder.encode("grant_type", "UTF-8") + "="
                     + URLEncoder.encode("password", "UTF-8");
             
             data += "&" + URLEncoder.encode("client_id", "UTF-8") + "="
                     + URLEncoder.encode(client_id, "UTF-8");
             
             data += "&" + URLEncoder.encode("client_secret", "UTF-8") + "="
                     + URLEncoder.encode(client_secret, "UTF-8");
             
             data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                     + URLEncoder.encode(username, "UTF-8");
             
             // Establecemos la petición 
             urlConnection.connect();

             // Obtenemos la salida
             OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
             wr.write(data);
             wr.flush();

             // Debemos guardar el body de la respuesta para poder llamar a nuestra función parser, la cual parseara el json resultado para obtener únicamente el bearer token
             stream = urlConnection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
             String result = reader.readLine();
                      
             // Cerramos la conexión
             urlConnection.disconnect();
             
             return result;

         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (urlConnection != null) {
                 urlConnection.disconnect();
             }
         }
		return null;
			
		}
	
	/**
	 * Método para parsear una cadena y obtener la posición dada como parámetro
	 * 
	 * @param String body: Cadena JSON que se desea parsear.
	 *        String posicion: Lugar del parametro dentro de la cadena que se desea obtener.
	 *        String delims: Delimitadores para realizar el parseo.
	 *  
	 * @return String result: Parametro de la cadena JSON parseado.
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
	
