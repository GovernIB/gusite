<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><bean:message key="usuari.listado" /> - Microsites - Govern de les Illes Balears</title>
	
	<!-- estils -->
	<link href="css/estils-v.4.1.css" rel="stylesheet" type="text/css" media="screen" />
	<!-- /estils -->
	
	<!-- js -->
	<script type="text/javascript" src="js/jquery-1.2.6.pack.js"></script>
	<script type="text/javascript" src="js/comuns.js"></script>
	<script type="text/javascript" src="js/llistat.js"></script>
    <script type="text/javascript" src="js/subMenus.js"></script>
	<script type="text/javascript">
	<!--
		// enllaços
		var pagCrear = "usuariosAcc.do?accion=crear";
		var pagMicros = "microUsuarios.do?accion=lista&id=";
		var pagSeleccionar = "usuarioEdita.do?id=";
		var pagEliminar = "usuariosAcc.do?accion=borrar&id=";
		// textos
		var txtNoSeleccionat = '<bean:message key="usuari.seleccionar" />';
		var txtAlertEliminar = '<bean:message key="usuari.alert3"/>';
		var txtCerca = '<bean:message key="menu2.microsites.general.buscar.cerca"/>';
		var txtCercador = '<bean:message key="menu2.microsites.general.buscar.alert"/>';
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
			
		<h1><img src="imgs/titulos/usuaris.gif" alt="Llistats" /><bean:message key="usuari.gestion" /></h1>
			
			<logic:equal name="parametros_pagina" property="nreg" value="0">
			
				<!-- No hay ningun elemento -->

					<p>
						<bean:message key="usuari.vacio" /><br/>
						<br/>&nbsp;&nbsp;<html:link href="usuarios.do"><bean:message key="usuari.volver" /></html:link>
					</p>
				
					<ul id="botonera">
							<li><button id="btCrear" type="button" title="<bean:message key="usuari.crear" />"><img src="imgs/botons/crear.gif" alt="" /></button></li> 
					</ul>
			

					<html:form action="/usuariosAcc.do" styleId="accFormularioLista" >
					  <html:hidden property="accion" />
					</html:form>	
				
				
			</logic:equal>
			
			
			<logic:notEqual name="parametros_pagina" property="nreg" value="0">  
				
				
				<!-- total -->
				<p><bean:message key="encontrados" /> <strong><bean:write name="parametros_pagina" property="nreg" /> <bean:message key="usuari.plural" /> </strong>. <bean:message key="mostrados" /> <strong><bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /></strong>.
				<logic:present name="BuscaOrdenaUsuarioActionForm">
				<logic:notEmpty name="BuscaOrdenaUsuarioActionForm" property="filtro">
				&nbsp;<a href="usuarios.do?filtro=">Veure tots els usuaris</a>.
				</logic:notEmpty>
				</logic:present>
				</p>
				<!-- /total -->
				
				<!-- botonera -->
				<ul id="botonera">
					<li><button id="btCrear" type="button" title="Crear"><img src="imgs/botons/crear.gif" alt="" /></button></li>
					<li><button id="btSeleccionar" type="button" title="Seleccionar"><img src="imgs/botons/seleccionar.gif" alt="" /></button></li>
					<li class="sep"><button id="btCercar" type="button" title="<bean:message key="op.3" />"><img src="imgs/botons/cercar.gif" alt="" /></button></li>
					<li class="sep"><button id="btEliminar" type="button" title="Eliminar"><img src="imgs/botons/eliminar.gif" alt="" /></button></li>
					<li class="sep"><button id="btMicros" type="button" title="Microsites"><img src="imgs/botons/urls.gif" alt="" /></button></li>
				</ul>
				<!-- /botonera -->
				
				<!-- form -->
				<html:form action="/usuarios.do"  styleId="formulari">
					<html:hidden property="ordenacion" styleId="ordenacion" />
					<html:hidden property="filtro" styleId="filtro" />
				</html:form>
				<!-- /form -->
				
				<!-- tabla listado -->
				<table id="llistat">
					<thead>
						<tr>
							<th class="id">Identificador</th>
							<th><bean:message key="micro.usuario.usuario" /></th>
							<th><bean:message key="usuari.eticolumna2" /></th>
							<th><bean:message key="usuari.eticolumna3" /></th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<td colspan="4">
						        <logic:present name="parametros_pagina" property="inicio">
						            &lt;&lt;<html:link action="/usuarios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="inicio"><bean:message key="op.7" /></html:link>&nbsp;&nbsp;
						        </logic:present>
						        <logic:present name="parametros_pagina" property="anterior">
						            &lt;<html:link action="/usuarios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="anterior"><bean:message key="op.8" /></html:link>&nbsp;&nbsp;      
						        </logic:present>
						        - <bean:write name="parametros_pagina" property="cursor" /> <bean:message key="pagina.al" /> <bean:write name="parametros_pagina" property="cursor_final" /> <bean:message key="pagina.de" /> <bean:write name="parametros_pagina" property="nreg" /> -  
						        <logic:present name="parametros_pagina" property="siguiente">
						            <html:link action="/usuarios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="siguiente"><bean:message key="op.9" /></html:link>&gt;&nbsp;&nbsp;      
						        </logic:present>
						        <logic:present name="parametros_pagina" property="final">
						            <html:link action="/usuarios.do" paramId="pagina" paramName="parametros_pagina" paramProperty="final"><bean:message key="op.10" /></html:link>&gt;&gt;&nbsp;&nbsp;      
						        </logic:present>
							</td>
						</tr>
					</tfoot>
					<tbody>
						<logic:iterate id="i" name="listado" indexId="indice">
						<tr>
							<td class="id"><bean:write name="i" property="id"/></td>
							<td class="usuari"><bean:write name="i" property="username"/></td>
							<td class="nomUs"><bean:write name="i" property="nombre"/></td>
							<td class="perfil"><bean:write name="i" property="perfil"/></td>
						</tr>
						</logic:iterate>
	
					</tbody>
				</table>
				<!-- /tabla listado -->
			
			</logic:notEqual>
		
		</div>
		<!-- /continguts -->
		<!-- peu -->
		<jsp:include page="peu.jsp"/>
		<!-- /peu -->			
	</div>
	<!-- contenidor -->

</body>

</html>