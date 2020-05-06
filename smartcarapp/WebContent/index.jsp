 <!--
 
 Autor: Álvaro Carmona Palomares
 Fecha: 29-01-2019
 Versión: v1
 
 Descripción: Index.jsp contiene el formulario de acceso, cuyos campos
 serán los pasados en la petición http al servicio web
 
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
			<link rel="stylesheet" type="text/css" href="css/estilos.css">
			
			<title>Formulario Acceso</title>
	</head>


	<body>
		
			<div class="container col-lg-3">
				<form action="Controlador" method="post">
					<div class="form-group text-center">
						<img src="img/User-icon.png" height="80" width="80"/>
						<p><strong>Formulario de Acceso</strong></p>
					</div>
				
					<div class="form-group">
						<label>Usuario</label>
						<input class="form-control" type="email" name="txtcorreo" placeholder="example@gmail.com">
						
					</div>
					
					<div class="form-group">
						<label>Contraseña</label>
						<input class="form-control" type="password" name="txtpass" placeholder="al45jL12">
						
					</div>
					<input class="btn btn-danger btn-block" type="submit" name="accion" value="Ingresar">
					
				
				</form>
				
			</div>

			<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
			<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
			<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	
	
	</body>
</html>