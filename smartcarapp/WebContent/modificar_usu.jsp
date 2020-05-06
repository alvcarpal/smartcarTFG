<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
          <meta charset='utf-8'>
          <meta name='viewport' content='width=device-width, initial-scale=1'>
          <title>Nuevo Usuario</title>
                                
          <link href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' rel='stylesheet'>
          <link rel="stylesheet" type="text/css" href="css/form.css">
                                
</head>
     
     
<body>
	
	
	<div class="container">
			 
			 <div class=" text-center mt-5 ">
        		<h1>Modificar Usuario</h1>
    		</div>
   
   		<%
   			String token = request.getParameter("txtoken");
   		
   		%>
   		
   		<div class="row ">
	       
	       <div class="col-lg-7 mx-auto">
	           <div class="card mt-2 mx-auto p-4 bg-light">
	               <div class="card-body bg-light">
	                   <div class="container">
	                       
	                       <form id="contact-form" role="form" action="Controlador">
	                           <div class="controls">
	                               <div class="row">
	                                   <div class="col-md-6">
	                                       <div class="form-group"> <label for="form_name">Nombre *</label> <input id="form_name" class="form-control" type="text" name="txtnombre"   placeholder="Introduzca un nombre *" required="required" data-error="Un nombre es requerido."> </div>
	                                   </div>
	                                  
	                               </div>
	                               <div class="row">
	                                   <div class="col-md-6">
	                                       <div class="form-group"> <label for="form_email">Email *</label> <input id="form_email" type="email"  name="txtcorreo" class="form-control" placeholder="Introduzca un correo *" required="required" data-error="Un email es requerido."> </div>
	                                   </div>
	                                   
	                               </div>
	                               
	                               <div class="row">
	                                   
	                                    <div class="col-md-12"> <input type="hidden" name="txtoken" value="<%=token%>" /> </div>
	                                   <div class="col-md-12"> <input class="btn btn-success btn-send pt-2 btn-block" type="submit" name="accion" value="Modificar"> </div>
	                               </div>

	                           </div>
	                       </form>
	                   </div>
	               </div>
	           </div> 
	       </div> 
	   </div>
	</div>
                       
</body>     
</html>