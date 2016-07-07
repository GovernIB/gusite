<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>	
		<!-- molla pa -->
		<ul id="mollapa">
			<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
			<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
			<li><bean:message key="menu.ferramentes" /></li>
			<li class="pagActual"><bean:message key="menu.indexar.principal" /></li>
		</ul>
		<!-- titol pagina -->
		<h1><img src="imgs/titulos/indexar.gif" alt="<bean:message key="menu.indexar.principal" />" />
		<bean:message key="menu.indexar.principal" /> </h1>

		<!-- continguts -->
		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.todo" />" onclick='indexarTodo()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.todo" />" /> &nbsp;<bean:message key="menu.indexar.todo" /></button>
		</div>
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.pendientes" />" onclick='indexarPendientes()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.pendientes" />" /> &nbsp;<bean:message key="menu.indexar.pendientes" /></button>
		</div>		
		<div id="botonera">
				<button type="button" name="fichero" title="<bean:message key="menu.indexar.unidad.administrativa" />" onclick='indexarByUA()'><img src="imgs/botons/indexar.gif" alt="<bean:message key="menu.indexar.unidad.administrativa" />" /> &nbsp;<bean:message key="menu.indexar.unidad.administrativa" /></button>  								
                <select id="unidadAdministrativa" >				       	
			       	<logic:iterate id="i" name="listaUA">
			       		<option value='<bean:write name="i" property="idUnidad"/>'><bean:write name="i" property="nombre"/></option>
					</logic:iterate>
			    </select>	
								
				
		</div>			
		
		<p>
		<strong>
			<bean:message key="indexador.p1" /><br/> 
			<bean:message key="indexador.p2" /><br/>
			<bean:message key="indexador.p3" /><br/>
		</strong>
		</p>	
		<logic:notEmpty name="ok">			
		<div class="alerta" style="font-weight:bold; color:#FF1111;">
			<html:messages id="message" message="true">
			<%= message %><br/>
			</html:messages>	
			
		</div>
		</logic:notEmpty>
</body>
</html>

<script>
	function indexarTodo(){
		document.location="indexarPrincipal.do?indexar=todo";
		
	}

	function indexarByUA(){
		
		// Plain old JavaScript
		var sel = document.getElementById('unidadAdministrativa');
		var selected = sel.options[sel.selectedIndex];
		var idUA = selected.value;
		if (idUA == null){
			return;
		}
		
		document.location.href="indexarPrincipal.do?indexar=byUA&uaId="+idUA;
		
	
		
	}
    function indexarPendientes(){
		
		document.location.href="indexarPrincipal.do?indexar=pendientes";
		
	}
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
