<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
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
		<li><bean:message key="menu.configuracion" /></li>
		<li><a href="frqssis.do"><bean:message key="menu.formulariosqssi" /></a></li>
	    <logic:present name="frqssiForm" property="id">
         		<li class="pagActual"><bean:write name="frqssiForm" property="traducciones[0].nombre" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="frqssiForm" property="id">
	         <li class="pagActual"><bean:message key="frqssi.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.formulariosqssi" />" />
	<bean:message key="menu.formulariosqssi" />. <span>Propiedades del listado</span></h1>
	<!-- botonera -->
	<div id="botonera">
		<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
	</div>
	

	<div style="font-weight:bold; color:#FF4400;">
	<html:errors/>
	</div>

<html:form action="/frqssiEdita.do"  styleId="accFormulario">

		     <logic:present name="frqssiForm" property="id">
			     <input type="hidden" name="modifica" value="Grabar">
		         <html:hidden property="id" />
		     </logic:present>
			 <logic:notPresent name="frqssiForm" property="id">
			  	<input type="hidden" name="anyade" value="Crear">
			 </logic:notPresent> 
	
	
	<div id="formulario">
		<!-- las tablas están entre divs por un bug del FireFox -->
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr>
				<td class="etiqueta"><bean:message key="frqssi.centro" /></td>
				<td>
					<html:text property="centro" maxlength="25" size="15"/>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			
			<tr>
				<td class="etiqueta"><bean:message key="frqssi.tipoescrito" /></td>
				<td>
					<html:text property="tipoescrito" maxlength="25" size="15"/>
				</td>
				<td class="etiqueta">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>								
			<tr>
			<td colspan="4">
	
		<ul id="submenu">
			<logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
				<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
	        </logic:iterate>
		</ul>    
	
	    <logic:iterate id="traducciones" name="frqssiForm" property="traducciones" indexId="i" >
		<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
			<table cellpadding="0" cellspacing="0" class="tablaEdicio">
			<tr>
				<td class="etiqueta"><bean:message key="frqssi.nombre" />:</td>
				<td><html:text property="nombre" name="traducciones" size="40" maxlength="100" indexed="true" /></td>
			</tr>
			</table>
		</div>
	    </logic:iterate>		
		</td></tr>
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
