<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html xhtml="true">

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><bean:message key="usuari.datos" /> - Microsites</title>
	
	<!-- estils -->
        <link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<!-- /estils -->
	
	<!-- js -->
	<script type="text/javascript" src="js/jquery-1.2.6.pack.js"></script>
	<script type="text/javascript" src="js/comuns.js"></script>
	<script type="text/javascript" src="js/detall.js"></script>
	<script type="text/javascript" src="js/form.js"></script>
	<script type="text/javascript" src="js/llistat.js"></script>
	<script type="text/javascript">
	<!--
		var pagMicros = "microUsuarios.do?accion=lista&id=";
		// enllaços
		var pagTornar = "usuarios.do";
		// textos
	-->
	</script>
	<!-- /js -->
	
	<!--[if lt IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estils-v.4.1_ie6.css" media="screen" />
	<![endif]-->

</head>

<body>
	
	<!-- contenidor -->
	<div id="contenidor">
	
		<!-- cap -->
		<jsp:include page="cabecera.jsp"/>
		<!-- /cap -->
		
		<!-- marc lateral -->
		<jsp:include page="menuLateralIzq.jsp"/>
		<!-- /marc lateral -->
		
		<!-- continguts -->
		<div id="continguts">

			<ul id="mollapa">
				 <li><a href="usuarios.do"><bean:message key="usuari.gestion" /></a></li>
				 <li class="pagActual"><bean:message key="usuari.datos"/></li>
		  	</ul>
			
			<h1><img src="imgs/titulos/usuari.gif" alt="Detall" /><bean:message key="usuari.datos" /></h1>


			
			<!-- total -->
			<p><bean:message key="usuari.insertar.datos" /> <em><bean:message key="usuari.guardar" /></em>.</p>
			<!-- /total -->
			
			<logic:present name="errores">
			<div id="textError">
				<html:errors/>
			</div>
			</logic:present>
			<!-- botonera -->
			<ul id="botonera">
				<li>
					<button id="btGuardar" type="button" title="<bean:message key="op.15" />"><img src="imgs/botons/guardar.gif" alt="" /></button>
				</li>
				<logic:present name="usuarioForm" property="id">
				
				    <li class="sep">
					<button   type="button" title="Microsites"  onclick="document.location ='microUsuarios.do?accion=lista&amp;id=<bean:write name="usuarioForm" property="id"  filter="false"/>'" ><img src="imgs/botons/urls.gif" alt="" /></button>
				</li>
					
				</logic:present>
				
			</ul>
			<!-- /botonera -->
			
			<!-- formulari -->
			<html:form action="/usuarioEdita.do" styleId="form" styleClass="formulari" >
			
				<logic:present name="usuarioForm" property="id">
				    <input type="hidden" name="modifica" value="Grabar" />
					<html:hidden property="id" />
				</logic:present>
				<logic:notPresent name="usuarioForm" property="id">
					<input type="hidden" name="anyade" value="Crear" />
				</logic:notPresent>   
			
				<!-- fila -->
				<div class="fila">
					
					<!-- element -->
					<div class="element t30">
					
						<div class="etiqueta"><label for="user">Usuari</label></div>
						<div class="control">
							<html:text property="username" styleId="user" tabindex="1" maxlength="128" />
						</div>
						
					</div>
					<!-- /element -->
					
					<!-- element -->
					<div class="element t30">
					
						<div class="etiqueta"><label for="pass">Contrasenya</label></div>
						<div class="control">
							<html:password property="password" styleId="pass" tabindex="2" maxlength="128" />
						</div>
						
					</div>
					<!-- /element -->
					<!-- element -->
					<div class="element t30">
					
						<div class="etiqueta"><label for="pass2">Repetir contrasenya</label></div>
						<div class="control">
							<html:password property="repitepwd" styleId="pass2" tabindex="2" maxlength="128" />
						</div>
						
					</div>
					<!-- /element -->
					
				</div>
				<!-- /fila -->
				
				<!-- fila -->
				<div class="fila">
					
					<!-- element -->
					<div class="element t60">
					
						<div class="etiqueta"><label for="nom">Nom i Llinatges</label></div>
						<div class="control">
							<html:text property="nombre" styleId="nom" tabindex="4" maxlength="256" />
						</div>
						
					</div>
					<!-- /element -->
					
					<!-- element -->
					<div class="element t30">
					
						<div class="etiqueta"><label for="perfil">Perfil</label></div>
						<div class="control">
							
							<select name="perfil" tabindex="5" id="perfil">
								<option value="0">Escull un perfil</option>
								<logic:equal name="usuarioForm" property="perfil" value="sacadmin">
									<option value="sacadmin" selected="selected">Administrador</option>
								</logic:equal>
								<logic:notEqual name="usuarioForm" property="perfil" value="sacadmin">
									<option value="sacadmin">Administrador</option>
								</logic:notEqual>
								
								<logic:equal name="usuarioForm" property="perfil" value="sacsuper">
									<option value="sacsuper" selected="selected">Supervisor</option>
								</logic:equal>
								<logic:notEqual name="usuarioForm" property="perfil" value="sacsuper">
									<option value="sacsuper">Supervisor</option>
								</logic:notEqual>
								
								<logic:equal name="usuarioForm" property="perfil" value="sacoper">
									<option value="sacoper" selected="selected">Operador</option>
								</logic:equal>
								<logic:notEqual name="usuarioForm" property="perfil" value="sacoper">
									<option value="sacoper">Operador</option>
								</logic:notEqual>
								

							</select>
						</div>
						
					</div>
					<!-- /element -->
					
				</div>
				<!-- /fila -->
				
				<!-- fila -->
				<div class="fila">
					
					<!-- element -->
					<div class="element t25">
					
						<div class="etiqueta"><label for="descripcio">Descripció</label></div>
						<div class="control">
							<html:textarea  property="observaciones" styleId="descripcio" tabindex="6" cols="70" rows="4" />
						</div>
						
					</div>
					<!-- /element -->
				
				</div>
				<!-- /fila -->
			
			</html:form>
			<!-- /formulari -->
		
		</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->			
	</div>
	<!-- contenidor -->

</body>

</html:html>