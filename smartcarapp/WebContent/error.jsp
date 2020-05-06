<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset='UTF-8'">
    <link rel="stylesheet" href="../webjars/bootstrap/4.0.0/css/bootstrap.css">
    <link rel="stylesheet" href="../stylesBootstrap/bootstrap-superhero-dorado.css">
    <script type="text/javascript" src="../webjars/jquery/3.3.1/jquery.js"></script>
    <script type="text/javascript" src="../webjars/bootstrap/4.0.0/js/bootstrap.js"></script>
    <script type="text/javascript" src="../webjars/bootstrap/4.0.0/js/bootstrap.js"></script>
    <title>Error de Autenticación</title>
</head>
<body>
    <form action="index.jsp">
        <div class="container">
            <div class="row">
                <div class="col-md-12"> &nbsp; </div>
            </div>
            <div class="row">
                <div class="col-md-12"> &nbsp; </div>
            </div>
            <div class="row">
                <div class="col-md-12"> &nbsp; </div>
            </div>
            <div class="card text-white bg-danger col-mb-8">
               <div class="card-header">Usuario no autenticado</div>
               <div class="card-body">
                 <h4 class="card-title">Aviso:</h4>
                 <p class="card-text">El usuario y/o contraseña no es(son) valido(s). Verifique los datos introducidos</p>
                 <button type="submit" class="btn btn-success">Ir al inicio de sesión</button>
               </div>
             </div>
          </div>
     </form>
</body>
</html>