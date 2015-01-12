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
	<script type="text/javascript" src="moduls/funcions.js"></script>		
	<script type="text/javascript" src="js/jsListados.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>		
		<li><a href="ldistribucion.do"><bean:message key="menu.ldistribucion" /></a></li>
		<logic:present name="ldistribucionForm" property="id">
         	<li class="pagActual"><bean:write name="correoForm" property="nombre" ignore="true" /></li>
	    </logic:present>
	    <logic:notPresent name="correoForm" property="correo">
	        <li class="pagActual"><bean:message key="ldistribucio.alta" /></li>
	    </logic:notPresent>
	</ul>
			
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/convocatorias.gif" alt="<bean:message key="menu.editarpagina" />"/>  
		<span>
		    <logic:present name="correoForm" property="correo">
	         	<bean:message key="correo.modificacion" />
		    </logic:present>		
		    <logic:notPresent name="correoForm" property="correo">
		        <bean:message key="correo.alta" />
		    </logic:notPresent>
		</span>
	</h1>

<%session.setAttribute("action_path_key",null);%>
	<jsp:include page="/moduls/mensajes.jsp"/>
	<!-- botonera -->
	<div id="botonera">
		<span class="grup">
			<button type="button" title='<bean:message key="correo.volver"/>' onclick='document.location.href="ldistribucionEdita.do?id=<bean:write name="correoForm" property="id"/>";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="formu.volver"/>' />
		   	</button> 
		</span>  
		<logic:present name="correoForm" property="correo">
			<span class="grup">
				<button type="submit" title="<bean:message key="correo.crear" />" onclick="submitForm('crear');"><img src="imgs/botons/nou.gif" alt="<bean:message key="correo.crear" />" /></button>
			</span>
		</logic:present>		
		<span class="grup">
			<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('guardar');">
				<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
			</button>
		</span> 
	</div>	
	
	
	<!-- las tablas estÃÂ¡n entre divs por un bug del FireFox -->
	
	<html:form action="/correoEdita.do"  method="post" enctype="multipart/form-data"  styleId="accFormularioLista">
		<div id="formulario">	
			<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->

		    <input type="hidden" name="espera" value="si" id="espera" />
			<input type="hidden" name="accion" value=""/>			
			<table cellpadding="0" cellspacing="0" class="edicio">			
				<tr>
					<td class="etiqueta"><bean:message key="correo.email"/></td>
					<td colspan="2">
						<logic:present name="correoForm" property="correo">
							<html:text property="correo" readonly="true"/>
						</logic:present>
						<logic:notPresent name="correoForm" property="correo">
							<html:text property="correo"/>
						</logic:notPresent>		
					</td>
				</tr>
				<tr>					
					<td class="etiqueta"><bean:message key="correo.nombre"/></td>
					<td colspan="2"><html:text property="nombre"/></td>
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="correo.apellidos"/></td>
					<td colspan="2"><html:text property="apellidos"/></td>
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="correo.emailvalid"/></td>
					<td colspan="2">
						<html:radio property="noEnviar" value="N">Si</html:radio>
						<html:radio property="noEnviar" value="S">No</html:radio>
					</td>
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="correo.ultimoenvio"/></td>
					<td colspan="2"><bean:write name="correoForm" property="ultimoEnvio"/></td>
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="correo.intentsErronis"/></td>
					<td colspan="2">
						<html:text property="intentoEnvio" readonly="true"/><html:button property="Inicializa" onclick="inicializaIntento();">
							<bean:message key="correo.inicializa"/>
						</html:button>
					</td>
				</tr>
			</table>
		</div>
	</html:form>

<script>
	function submitForm(nom_accio){
		var accForm = document.getElementById('accFormularioLista');
		if(accForm.correo.value != ""){
			accForm.accion.value= nom_accio;		
			accForm.submit();
		}else{
			alert('<bean:message key="ldistribucio.alert8"/>');
		}
	}

	function validar(correo){
		if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})$/.test(correo))
			return true;
		else
			return false;
	}

	function inicializaIntento(){
		document.getElementById("accFormularioLista").intentoEnvio.value=0;
	}
	
</script>

</body>
</html>

