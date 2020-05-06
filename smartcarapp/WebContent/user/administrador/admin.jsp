 <!--
 
 Autor: Álvaro Carmona Palomares
 Fecha: 15-02-2019
 Versión: v1
 
 Descripción: Admin.jsp contiene las opciones de administrador, las cuales
 serán mostradas mediante un menu vertical
 
 -->


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    <!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description"
        content="Responsive sidebar template with sliding effect and dropdown menu based on bootstrap 3">
    <title>Menu Administrador</title>

    <!-- online links -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
        integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css"
        integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
    <link rel="stylesheet" href="//malihu.github.io/custom-scrollbar/jquery.mCustomScrollbar.min.css">

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/sidebar-themes.css">
    <link rel="shortcut icon" type="image/png" href="img/favicon.png" />
    
</head>

<body>
    <div class="page-wrapper default-theme sidebar-bg bg1 toggled">
        <nav id="sidebar" class="sidebar-wrapper">
            <div class="sidebar-content">
                <!-- sidebar-header  -->
                <div class="sidebar-item sidebar-header d-flex flex-nowrap">
                    <div class="user-pic">
                        <img class="img-responsive img-rounded" src="img/user.jpg" alt="User picture">
                    </div>
                    <div class="user-info">
                        <span class="user-name">${user}
                        </span>
                        <span class="user-role">Administrator</span>
                        <span class="user-status">
                            <i class="fa fa-circle"></i>
                            <span>Online</span>
                        </span>
                    </div>
                </div>
                
                <!-- sidebar-search  -->
                <div class="sidebar-item sidebar-search">
                    <div>
                        <div class="input-group">
                            <input type="text" class="form-control search-menu" placeholder="Buscar...">
                            <div class="input-group-append">
                                <span class="input-group-text">
                                    <i class="fa fa-search" aria-hidden="true"></i>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- sidebar-menu  -->
                <div class=" sidebar-item sidebar-menu">
                    <ul>
                        <li class="header-menu">
                            <span>General</span>
                        </li>
                        <li class="sidebar-dropdown">
                            <a href="#">
                                <i class="fas fa-users"></i>
                                <span class="menu-text">Usuario</span>
                                
                            </a>
                            <div class="sidebar-submenu">
                                <ul>
                                    <li>
                                        <a href="Controlador?txtcorreo=${user}&txtpass=${pass}&accion=Ingresar">Refrescar Token</a>
                                    </li>
                                    <li>
                                        <a href="modificar_usu.jsp?txtoken=${token}">Actualizar Información
                                        <span class="badge badge-pill badge-warning">Nuevo</span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                                               
                        <li class="sidebar-dropdown">
                            <a href="#">
                                <i class="fas fa-book"></i>
                                <span class="menu-text">Documentación</span>
                            </a>
                            <div class="sidebar-submenu">
                                <ul>
                                    <li>
                                        <a href="#">Manual Usuario</a>
                                    </li>
                                    <li>
                                        <a href="#">Manual Administrador
                                        
                                        <span class="badge badge-pill badge-danger">Admin</span>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        
                       
                        <li class="sidebar-dropdown">
                            <a href="#">
                                <i class="fas fa-info"></i>
                                <span class="menu-text">Mi Información</span>
                            </a>
                            <div class="sidebar-submenu">
                                <ul>
                                    <li>
                                        <a href="solicitar_info.jsp?txtoken=${token}&txtipo=${tipo}">Consultar Datos</a>
                                    </li>
                                   
                                </ul>
                            </div>
                        </li>
                        
                        
                        <li class="header-menu">
                            <span>Administrador</span>
                        </li>
                        <li class="sidebar-dropdown">
                            <a href="#">
                                <i class="fas fa-tools"></i>
                                <span class="menu-text">Configuración</span>
                                <span class="badge badge-pill badge-danger">Admin</span>
                            </a>
                            <div class="sidebar-submenu">
                                <ul>
                                    <li>
                                        <a href="nuevo_usu.jsp">Crear Nuevo Usuario

                                        </a>
                                    </li>
                                    <li>
                                        <a href="elimina_usu.jsp?txtoken=${token}">Eliminar Usuario</a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
                
            </div>
            
            <!-- sidebar-footer  -->
            <div class="sidebar-footer">
                
                <div class="dropdown">
                    <a href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fa fa-cog"></i>
                        <span class="badge-sonar"></span>
                    </a>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuMessage">
                        <a class="dropdown-item" href="#">Mi perfil</a>
                        <a class="dropdown-item" href="#">Ayuda</a>
                        <a class="dropdown-item" href="#">Configuración</a>
                    </div>
                </div>
                
                <div>
                    <a href="CerrarSesion">
                        <i class="fa fa-power-off"></i>
                    </a>
                </div>
                <div class="pinned-footer">
                    <a href="#">
                        <i class="fas fa-ellipsis-h"></i>
                    </a>
                </div>
            </div>
        </nav>
        
        <!-- page-content  -->
        <main class="page-content pt-2">
            <div id="overlay" class="overlay"></div>
            <div class="container-fluid p-5">
                <div class="row">
                    <div class="form-group col-md-12">
                        <h2>Información de Usuario</h2>
                        <p>Información que identifica a un usuario dentro de la aplicación.
                        <h4 align="right"> Keyrock Token: ${token}</h4>
						<h4 align="right"> Bearer Token: ${oauth}</h4>
						<h4 align="right"> Notificación: ${info}</h4>
						
						</p>

                    </div>
                    
                    <div class="form-group col-md-12">
                        <a id="toggle-sidebar" class="btn btn-secondary rounded-0" href="#">
                            <span>Ampliar</span>
                        </a>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="form-group col-md-12">
                        <h3>Personalizar</h3>
                        <p>Temas para personalización de la interfaz</p>
                    </div>
                </div>

                <div class="row">
                    <div class="form-group col-md-12">
                        <a href="#" data-theme="default-theme" class="theme default-theme selected"></a>
                        <a href="#" data-theme="chiller-theme" class="theme chiller-theme"></a>
                        <a href="#" data-theme="legacy-theme" class="theme legacy-theme"></a>
                        <a href="#" data-theme="ice-theme" class="theme ice-theme"></a>
                        <a href="#" data-theme="cool-theme" class="theme cool-theme"></a>
                        <a href="#" data-theme="light-theme" class="theme light-theme"></a>
                    </div>
                    <div class="form-group col-md-12">
                        <p>Imagen de fondo </p>
                    </div>
                    <div class="form-group col-md-12">
                        <a href="#" data-bg="bg1" class="theme theme-bg selected"></a>
                        <a href="#" data-bg="bg2" class="theme theme-bg"></a>
                        <a href="#" data-bg="bg3" class="theme theme-bg"></a>
                        <a href="#" data-bg="bg4" class="theme theme-bg"></a>
                    </div>
                    <div class="form-group col-md-12">
                        <div class="custom-control custom-switch">
                            <input type="checkbox" class="custom-control-input" id="toggle-bg" checked>
                            <label class="custom-control-label" for="toggle-bg">Habilitar imagen de fondo</label>
                        </div>
                    </div>
                    
                </div>


                <hr>
                <div class="row ">
                    <div class="form-group col-md-12">
                        <small> <span
                                class="text-secondary font-weight-bold"> Álvaro
                                Carmona Palomares </span></small>
                    </div>
                    <div class="form-group col-md-12">
                        <a href="https://github.com/azouaoui-med" target="_blank"
                            class="btn btn-sm bg-secondary shadow-sm rounded-0 text-light mr-3 mb-3">
                            <i class="fab fa-github" aria-hidden="true"></i>
                        </a>
                    </div>
                </div>
            </div>
        </main>
       
    </div>
  
    <!--scripts -->	
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"
        integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous">
    </script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
        integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous">
    </script>
    <script src="//malihu.github.io/custom-scrollbar/jquery.mCustomScrollbar.concat.min.js"></script>


    <script src="js/main.js"></script>
    
</body>
    