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
	<script type="text/javascript" src="js/jsListados.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.estadisticas" /></li>
		<li class="pagActual"><bean:message key="stat.encuesta.mollapa" /></li>
	</ul>


	<!-- titol pagina -->
	<h1><img src="imgs/titulos/estadisticas.gif" alt="<bean:message key="stat.encuesta.listado" />" />
	<bean:message key="stat.encuesta.listado" />.</h1>
	
		<div id="botonera">

				<button type="button" title="<bean:message key="stat.encuesta.ver" />" onclick="editar();"><img src="imgs/botons/stat.gif" alt="<bean:message key="stat.encuesta.ver" />" /></button>

		</div>		

		<p><bean:message key="encuesta.dobleclic" />.</p>


		<form action="estadisticaenc.do" id="accFormularioLista">
			  <table cellpadding="0" cellspacing="0" class="llistat">
				  	<thead>
						<tr>
							<th class="check">&nbsp;</th>
							<th>
					            <bean:message key="stat.encuesta.marco.titulo" />
					        </th>
							<th>
								<bean:message key="stat.encuesta.marco.fecha" />
					        </th>
							<th>
								<bean:message key="stat.encuesta.marco.respostes" />
					        </th>
						</tr>
					</thead>

					<tbody>
					    <logic:iterate id="i" name="MVS_lista_encuesta" indexId="indice">
					    <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
					      <td class="check">
					        <input type="checkbox" name="seleccionados" value="<bean:write name="i" property="url"/>" class="radio">
					      </td>
					      <td>
							<bean:write name="i" property="titulo" />
					      </td>
						  <td><bean:write name="i" property="tipo" /></td>  
					      <td><bean:write name="i" property="head" /></td>   
					    </tr>
					    </logic:iterate>
					</tbody>
			  </table>
		
		</form>


</body>
</html>

<script>
<!--

var uriEdicion="estadisticaenc.do?idenc=";
var alert1="<bean:message key="encuesta.alert1"/>";
var alert2="<bean:message key="encuesta.alert2"/>";


-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
