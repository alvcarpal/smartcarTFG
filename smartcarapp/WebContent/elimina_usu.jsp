<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
          <meta charset='utf-8'>
          <meta name='viewport' content='width=device-width, initial-scale=1'>
          <title>Eliminar Usuario</title>
                                
          <link href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' rel='stylesheet'>
          <link rel="stylesheet" type="text/css" href="css/form.css">
                                
</head>
     
     
<body>

	<%
   			String token = request.getParameter("txtoken");
   		
   	%>
	
	<div class="container">
			 
			 <div class=" text-center mt-5 ">
        		<h1>Eliminar Usuario</h1>
    		</div>
   
   
   			 <div class="row ">
	       
	       <div class="col-lg-7 mx-auto">
	           <div class="card mt-2 mx-auto p-4 bg-light">
	               <div class="card-body bg-light">
	                   <div class="container">
	                       
	                       <form id="contact-form" role="form" action="Controlador">
	                           <div class="controls">
	                              
	                              <h5>Introduzca una matrícula válida: 
	                             </h3>
	                                   
	                               
	                               <div class="row">
	                               	   <div class="col-md-6">
	                                       <div class="form-group"> <label for="form_name">Identificador de Usuario *</label> <input id="form_name" class="form-control" type="text" name="txtuserid"   placeholder="Introduzca un identificador de usuario *" required="required" data-error="Una ID es requerido."> </div>
	                                   </div>
	                                   
	                                   <div class="col-md-6">
	                                       <div class="form-group"> <label for="form_name">Matrícula *</label> <input id="form_name" class="form-control" type="text" name="txtmat"   placeholder="Introduzca una matrícula *" required="required" data-error="Una matrícula es requerida."> </div>
	                                   </div>
	                                   
	                                    <div class="col-md-12"> <input type="hidden" name="txtoken" value="<%=token%>" /> </div>
	                                   <div class="col-md-12"> <input class="btn btn-success btn-send pt-2 btn-block" type="submit" name="accion" value="Eliminar"> </div>
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