<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><bean:message key="micro.usuario.titulo" /> - Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/jsListados.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li class="pagActual"><bean:message key="micro.usuario.titulo" /></li>
	</ul>
	<!-- titol pagina -->
	<h1><bean:message key="micro.usuario.titulo" />. </h1>

	<html:form action="/microUsuarios.do"  styleId="accFormSearch">
		<html:hidden property="ordenacion" />
		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
			</span>
			
		    	<select name="iduser" id="iduser" >
                    <logic:iterate id="i" name="listatodos" >
                    <option value='<bean:write name="i" property="id" />'><bean:write name="i" property="nombre" /> (<bean:write name="i" property="username" />)</option>
                    </logic:iterate>
                </select>        
		    	&nbsp;&nbsp;
				<button type="button" title="<bean:message key="micro.usuario.crear" />" onclick="nuevouser();"><img src="imgs/botons/nou.gif" alt="<bean:message key="micro.usuario.crear" />" /></button>
			
		</div>		
		</html:form>


		<html:form action="/microusuariosAcc.do" styleId="accFormularioLista">
		   <table cellpadding="0" cellspacing="0" class="llistat">
			    <thead>
				<tr>
					<th class="check">&nbsp;</th>
					<th><bean:message key="micro.usuario.usuario" /></th>
					<th><bean:message key="micro.usuario.nombre" /></th>
				</tr>
				</thead>
				<tbody>
					    <logic:iterate id="i" name="listado" indexId="indice">
					      <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
					      <td class="check">
					        <html:multibox property="seleccionados" styleClass="radio"> 
					            <bean:write name="i" property="id"/>
					        </html:multibox>
					      </td>
					      <td><bean:write name="i" property="username" /></td>
					      <td><bean:write name="i" property="nombre" ignore="true" /></td>
					    </tr>
					    </logic:iterate>
				</tbody>
		  </table>
		  
		    
		  <html:hidden property="accion" />
		
		</html:form>



</body>
</html>


<script>
<!--

var uriEdicion="#";
var alert1="<bean:message key="micro.usuario.alert1"/>";
var alert2="<bean:message key="micro.usuario.alert2"/>";

	function nuevouser() {
		var acciduser = document.getElementById('iduser');

	    var url = 'microUsuarios.do?accion=nuevouser&iduser='+acciduser.value;
	    document.location = url;
	}
	
-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
