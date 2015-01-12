<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
</head>

<body>	
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li class="pagActual"><bean:message key="menu.mensaje" />.</li>
	</ul>
	<!-- titol pagina -->
	<h1><bean:message key="menu.mensaje" />. </h1>
	
 
	<div id="botonera">
	   	<span class="grup">
	   	<button type="button" name="action" value='<bean:message key="mensa.volverarbol.titol"/>' title='<bean:message key="mensa.volverarbol.titol"/>' onclick='document.location.href="menuEdita.do";'>
	   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="mensa.volverarbol.titol"/>' /> 
	   </button> 
	   </span>
	   
	   	<logic:present name="contenidoForm" property="id">
	   	<logic:present name="W3C">
	   	<button type="button" name="action" value='<bean:message key="mensa.editarconte.titol"/>' title='<bean:message key="mensa.editarconte.titol"/>' onclick='document.location.href="contenidoEdita.do?id=<bean:write name="contenidoForm" property="id"/>";'>
	   		<img src="imgs/botons/editar.gif" alt='<bean:message key="mensa.editarconte.titol"/>' />
	   </button> 
	   </logic:present>
	   </logic:present>
	   
	</div>

		<div class="alerta" style="color:#FF1111;">
			<html:messages id="message" message="true">
			<%= message %><br/>
			</html:messages>	
			<logic:present name="SVS_otrainfo">
					<p><pre><bean:write name="SVS_otrainfo"/></pre></p>
				</logic:present>	
		</div>


</body>
</html>