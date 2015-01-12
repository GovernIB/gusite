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
</head>

<body>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="encuestas.do"><bean:message key="menu.encuestas" /></a></li>
		<logic:present name="titencuesta">
			<li><a href="encuestaEdita.do?id=<bean:write name="preguntaForm" property="idencuesta"/>"><bean:write name="titencuesta"/></a></li>
		</logic:present>
		<logic:present name="titpregunta">
			<li><a href="preguntaEdita.do?id=<bean:write name="respuestaForm" property="idpregunta"/>"><bean:write name="titpregunta"/></a></li>
		</logic:present>				
	    <logic:present name="respuestaForm" property="id">
         		<li class="pagActual"><bean:write name="respuestaForm" property="traducciones[0].titulo" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="respuestaForm" property="id">
	         <li class="pagActual"><bean:message key="respuesta.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/encuestas.gif" alt="<bean:message key="respuesta.respuesta" />" />
	<bean:message key="respuesta.respuesta" /> :  
		<span>
		    <logic:present name="respuestaForm" property="id">
	         	<bean:write name="respuestaForm" property="traducciones[0].titulo" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="respuestaForm" property="id">
		        <bean:message key="respuesta.alta" />
		    </logic:notPresent>				
		</span>
	</h1>

		<!-- botonera -->
	<div id="botonera">
		<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
	</div>	


	<div style="font-weight:bold; color:#FF4400;">
	<html:errors/>
	</div>
	

<html:form action="/respuestaEdita.do" styleId="accFormularioLista">

		<logic:present name="respuestaForm" property="id">
		    <input type="hidden" name="modifica" value="Grabar">
			<html:hidden property="id" />
		</logic:present>
		<logic:notPresent name="respuestaForm" property="id">
			<input type="hidden" name="anyade" value="Crear">
		</logic:notPresent>

     	<html:hidden property="idpregunta" />

		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
				<table cellpadding="0" cellspacing="0" class="edicio">
		
					<tr class="par">
						<td class="etiqueta"><bean:message key="respuesta.tipo" /></td>
						<td colspan="3">
							<html:select property="tipo">
				   			    <html:option value="N"><bean:message key="respuesta.tipo.normal" /></html:option>
				    	        <html:option value="I"><bean:message key="respuesta.tipo.input" /></html:option>
					        </html:select>
					    </td>
					</tr>
			
					<tr>
						<td class="etiqueta"><bean:message key="respuesta.orden" /></td>
						<td><html:text property="orden"/></td>
						<td class="etiqueta" colspan="2"><bean:message key="respuesta.nrespuestas" />:&nbsp;&nbsp;<strong><bean:write name="respuestaForm" property="nrespuestas"/></strong><html:hidden property="nrespuestas"/></td>
						<td>&nbsp;</td>
					</tr>
					
					<tr>
					<td colspan="4">
			
						<ul id="submenu">
							<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
								<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
					        </logic:iterate>
						</ul>    
					
					    <logic:iterate id="traducciones" name="respuestaForm" property="traducciones" indexId="i" >
						<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
						
							<table cellpadding="0" cellspacing="0" class="tablaEdicio">
							<tr>
								<td class="etiqueta"><bean:message key="respuesta.titulo" />:</td>
								<td><html:text property="titulo" name="traducciones" size="100" maxlength="256" indexed="true" /></td>
							</tr>
							</table>
							
						</div>
					    </logic:iterate>
			
					</td>
					</tr>
				</table>
		
		</div>

</html:form>


</body>
</html>


<script type="text/javascript">
<!--



	function submitForm(){
		var accForm = document.getElementById('accFormularioLista');
		accForm.submit();
	}



	
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>