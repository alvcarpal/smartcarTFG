package com.sensor.database;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.sensor.dto.Dato;
import com.sensor.dto.Entity;
import com.sensor.dto.Notification;
import com.sensor.dto.User;
import com.sensor.dto.Usuarios_has_vehiculos;
import com.sensor.dto.Vehiculo;

import java.util.Iterator;
import java.util.List;




public class DataBase {

	// Método para dar de baja a un usuario y su matrícula asociada
		public boolean crearUsuario(String mat,String userid,String tipo) {
				
			   boolean creado = false;
				
			    String matriocb = "urn:ngsi-ld:Sensor:"+mat;
			    
			    //Obtenemos una sesión para trabajar con Hibernate e iniciamos una transacción
				SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession();
				session.beginTransaction();		
				
				// El primer paso será comprobar si el vehículo exite previamente					
				// Creamos la matrícula de la relación vehiculos
				Vehiculo matricula = new Vehiculo();				
				matricula.setMatricula(matriocb);
				matricula.setTipos_tipo(tipo);
				session.save(matricula);
					
				// Creamos el usuario con el id pasado como parámetro
				User usu = new User();
				usu.setUsername(userid);
				usu.setRole("usuario");
				usu.setOrganizacion("Andalucia");
				session.save(usu);
					
				// El último paso es crear la entrada en la relación que relaciona al usuario y su matrícula
				Usuarios_has_vehiculos usu_has_veh = new Usuarios_has_vehiculos();
				usu_has_veh.setUsuarios_username(userid);
				usu_has_veh.setVehiculos_matricula(matriocb);
				session.save(usu_has_veh);
					
				creado = true;
					
	
				// Cerrarmos los recursos utilizados
				session.getTransaction().commit(); 
				session.close(); 
				sessionFactory.close();
				
			
			   return creado;
			}
	
	// Método para pedir información de un usuario 
	public List<Dato> consultarDatos(String mat) {
		
	        //Comenzamos una transacción con la base de datos
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			//Configuramos los parámetros necesarios para las consultas dinámicas
			Criteria cr = session.createCriteria(Dato.class);
			
			//Añadimos las condiciones dinámicas según los parámetros introducidos
			if(mat!=null) {
				cr.add(Restrictions.eq("vehiculos_matricula", mat));
			}
			
			List<Dato> datos = cr.list();
			
			session.getTransaction().commit(); //Terminamos transacción
			session.close(); //Cerramos sesión
			sessionFactory.close(); //Cerramos conexiones con BD
			
			return datos;		
	}
		
	// Método para insertar datos recibidos de los sensores mediante las notificaciones 
	public void insertarDatos(Notification notificacion) {
		
		System.out.println("entro");
		// Objetos necesarios para poder almacenar la información obtenida mediante la subscripción
		Dato nuevo=new Dato();
		Entity entity=null;
		
		//Comenzamos una transacción con la base de datos
		SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		/*Necesitamos un bucle para que guarde todos las entidades de la notificacion inicial*/
		for(Iterator<Entity> i=notificacion.getData().iterator();i.hasNext();) {
			
			// Tomamos la entidad que ha cambiado
			entity=i.next();
			
			// Obtenemos los datos de la entidad y realizamos las conversiones
			double latitud = Double.parseDouble((String) entity.getLatitud().getValue());
			double longitud = Double.parseDouble((String) entity.getLongitud().getValue());
			double velocidad = Double.parseDouble((String) entity.getVelocidad().getValue());
			
			nuevo = new Dato((String) entity.getId(),
					latitud,
					longitud,
					velocidad
					);
			
			session.save(nuevo);			
		}
		
		
		session.getTransaction().commit(); //Terminamos transacción
		session.close(); //Cerramos sesión
		sessionFactory.close(); //Cerramos conexiones con BD
		
	}
	
	// Método para dar de baja a un usuario y su matrícula asociada
	public boolean eliminarDatos(String mat,String userid) {
			
		   
			boolean eliminado = false;
			 String matriocb = "urn:ngsi-ld:Sensor:"+mat;
		   
		    //Obtenemos una sesión para trabajar con Hibernate e iniciamos una transacción
			SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();		
			
			// Para no entrar en conflicto con las claves externas debemos eliminar primero la relación matricula-usuario de usuarios_has_vehiculos
			// Para comprobar si el vehículo o usuario existe haremos una consulta antes de borrar la fila de la relación correspondiente
			Usuarios_has_vehiculos usu_has_veh = new Usuarios_has_vehiculos();
			
			// Creamos el objeto correspondiente para eliminar la fila correspondiente a los valores pasados
			
			usu_has_veh = session.get(Usuarios_has_vehiculos.class, userid);
			
			if(usu_has_veh != null) {
				usu_has_veh.setUsuarios_username(userid);
				usu_has_veh.setVehiculos_matricula(matriocb);
				
				// Se elimina la fila correspondiente
				session.delete(usu_has_veh);
				
				// Los siguientes pasos serán eliminar el usuario y la matrícula del resto de relaciones sin violar las claves externa
				User usu = new User();
				usu.setUsername(userid);
				// Se elimina el usuario de la relación Usuarios
				session.delete(usu);
								
				// Elimina la matrícula de la relación vehiculos
				Vehiculo matricula = new Vehiculo();
				matricula.setMatricula(matriocb);
				session.delete(matricula);
				
				// Se ha podido dar de alta al usuario
				eliminado=true;
				
			}
			
			// Cerrarmos los recursos utilizados
			session.getTransaction().commit(); 
			session.close(); 
			sessionFactory.close(); 		
		
			return eliminado;
		}
}
