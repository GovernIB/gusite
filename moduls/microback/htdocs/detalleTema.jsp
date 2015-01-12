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
		<li><a href="faqs.do"><bean:message key="faq.faqs" /></a></li>
		<li><a href="temas.do"><bean:message key="menu.temas" /></a></li>		
	    <logic:present name="temaForm" property="id">
         		<li class="pagActual"><bean:write name="temaForm" property="traducciones[0].nombre" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="temaForm" property="id">
	         <li class="pagActual"><bean:message key="tema.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/faqs.gif" alt="<bean:message key="tema.eticolumna1" />" />
	<bean:message key="tema.eticolumna1" />: 
		<span>
		    <logic:present name="temaForm" property="id">
	         	<bean:write name="temaForm" property="traducciones[0].nombre" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="temaForm" property="id">
		        <bean:message key="tema.alta" />
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

	<html:form action="/temaEdita.do" enctype="multipart/form-data"  styleId="accFormulario">
		
		<logic:present name="temaForm" property="id">
		    <input type="hidden" name="modifica" value="Grabar">
			<html:hidden property="id" />
		</logic:present>
		<logic:notPresent name="temaForm" property="id">
			<input type="hidden" name="anyade" value="Crear">
		</logic:notPresent> 	     
		
		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
				<table cellpadding="0" cellspacing="0" class="edicio">
				<tr>
					<td colspan="4">
			
						<ul id="submenu">
							<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
								<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
					        </logic:iterate>
						</ul>    
					
					    <logic:iterate id="traducciones" name="temaForm" property="traducciones" indexId="i" >
						<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
							<table cellpadding="0" cellspacing="0" class="edicio">
							<tr>
								<td class="etiqueta"><bean:message key="tema.nombre" />:</td>
								<td><html:text property="nombre" name="traducciones" size="40" maxlength="256" indexed="true" /></td>
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
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
	}
	
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>

