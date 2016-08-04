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
		<li><a href="formularios.do"><bean:message key="menu.contactos" /></a></li>
		<li><a href="formularioEdita.do?id=<bean:write name="lineaFormularioForm" property="idcontacto" />"><bean:message key="formu.datosgenerales" /></a></li>
	    <logic:present name="lineaFormularioForm" property="id">
       		<li class="pagActual"><bean:message key="formu.linnlin" /></li>
	    </logic:present>		
	    <logic:notPresent name="lineaFormularioForm" property="id">
	         <li class="pagActual"><bean:message key="formu.linea.alta" /></li>
	    </logic:notPresent>				
	</ul>
	
	<!-- titol pagina -->
	<h1><bean:message key="formu.linea.datos" />:  
		<span>
		    <logic:present name="lineaFormularioForm" property="id">
	         	<bean:message key="formu.linea.modificacion" />
		    </logic:present>		
		    <logic:notPresent name="lineaFormularioForm" property="id">
		        <bean:message key="formu.linea.alta" />
		    </logic:notPresent>				
		</span>
	</h1>

<%session.setAttribute("action_path_key",null);%>
<html:form action="/lineaformularioEdita.do"  method="POST" enctype="multipart/form-data"  styleId="accFormulario">

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	

     <html:hidden name="lineaFormularioForm" property="idcontacto" />
     
     <input type="hidden" name="espera" value="si" id="espera" />
	<input type="hidden" name="accion" value=""/>
	<logic:present name="lineaFormularioForm" property="id">
			<input type="hidden" name="id" value="<bean:write name="lineaFormularioForm" property="id" />"/>
	</logic:present>
		

	<!-- botonera -->
	<div id="botonera">
		<span class="grup">
			<button type="button" title='<bean:message key="formu.volver"/>' onclick="volver('<bean:write name="lineaFormularioForm" property="idcontacto" />');">
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="formu.volver"/>' />
		   	</button> 
		</span>  
		<logic:present name="lineaFormularioForm" property="id">
			<span class="grup">
					<button type="submit" title="<bean:message key="formu.linea.crear" />" onclick="submitForm('Crear');"><img src="imgs/botons/nou.gif" alt="<bean:message key="formu.linea.crear" />" /></button>
			</span>
		</logic:present>
		<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
			<logic:present name="lineaFormularioForm" property="id">
				<span class="grup">				
					<button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
				 		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
					</button>
				</span> 
			</logic:present>
		</logic:equal>
		<span class="grup">
			<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('Guardar');">
					<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
			</button>
		</span> 
		<logic:present name="lineaFormularioForm" property="id">
		 	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('Borrar');">
		   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
		    </button> 
		</logic:present>
	</div>	

	<!-- las tablas están entre divs por un bug del FireFox -->
	<div id="formulario">
	
		<table cellpadding="0" cellspacing="0" class="edicio">
		<tr>
			<td class="etiqueta"><bean:message key="formu.linvisible" /></td>
			<td><html:radio property="visible" value="S"/>&nbsp;Sí­&nbsp;<html:radio property="visible" value="N" />&nbsp;No</td>
			<td class="etiqueta"><bean:message key="formu.linorden" /></td>
			<td><html:text property="orden" size="2" maxlength="10" /></td>
			<td class="etiqueta"><bean:message key="formu.linobliga" /></td>
			<td><html:radio property="obligatorio" value="1"/>&nbsp;Sí­&nbsp;<html:radio property="obligatorio" value="0" />&nbsp;No</td>
		</tr>
		<tr class="par">
			<td class="etiqueta"><bean:message key="formu.linetiq" /></td>
			<td colspan="5">
			<table>
 		 	<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
	 		<tr>
 		 		<td><bean:message name="lang" /></td>
				<td><html:text property="<%="etiq["+j+"]"%>" size="50" maxlength="512"/></td>
			</tr>
       		</logic:iterate>
       		</table>
       		</td>
 		</tr>
		<tr>
			<td class="etiqueta" style="vertical-align:top;"><bean:message key="formu.lintipo" /></td>
			<td colspan="6">
				<html:select styleId="tipo" property="tipo" onchange="triarTipusFormContacte();" >
					<html:option value="1"><bean:message key="formu.lincampo1" /></html:option>
                	<html:option value="2"><bean:message key="formu.lincampo2" /></html:option>
                	<html:option value="3"><bean:message key="formu.lincampo3" /></html:option>
                	<html:option value="4"><bean:message key="formu.lincampo4" /></html:option>
                	<html:option value="5"><bean:message key="formu.lincampo5" /></html:option>
    	        </html:select>

				<%int indice =30; %>
				<span id="tipusCampSpan">
					<span style="display:none;"></span>
					<span style="display:none;">
						<bean:message key="formu.lintamano" /> <html:text property="tamano" size="2" maxlength="10" styleId="tamano"/>
					</span>
					<span style="display:none;">
						<bean:message key="formu.linnlin" /> <html:text property="lineas" size="2" maxlength="10" styleId="lineas"/>
					</span>
						<bean:define id="numoptions" value="0" type="java.lang.String"/>
						<logic:present name="lineaFormularioForm" property="numOptions">
							<logic:notEqual name="lineaFormularioForm" property="numOptions" value="">
									<bean:define id="numoptions"> <bean:write name="lineaFormularioForm" property="numOptions"/> </bean:define>
							</logic:notEqual>
						</logic:present>
					<span style="display:none;">
						<bean:message key="formu.filasselec" />
						<html:select styleId="numSelectors" property="numSelectors" onchange="triaNumSelectors();">
									<% 	for (int ix=0;ix <= indice;ix++) { %>
									    <%if(ix == 0){ %>
									    	<html:option value="<%=""+ ix%>" ><%=ix %></html:option>
									   <%}else if (ix > 1){ 
									   		if(ix == (new Integer(numoptions)).intValue()){  %>
									   			<option value="<%=""+ ix%>" selected="selected"><%=ix %></option>
									   <%	}else{		%>
									     		<html:option value="<%=""+ ix%>" ><%=ix %></html:option>
									   <%	}
										} %>

								<%} %>
								
						</html:select>
					</span>
					<span style="display:none;">
						<bean:message key="formu.filasselec" />
						<html:select styleId="numSelectors2" property="numSelectors2" onchange="triaNumSelectors2();">
								<% 	for (int ix=0;ix <= indice;ix++) { %>
									    <%if(ix == 0){ %>
									    	<html:option value="<%=""+ ix%>" ><%=ix %></html:option>
									   <%}else if (ix > 1){ 
									   		if(ix == (new Integer(numoptions)).intValue()){  %>
									   			<option value="<%=""+ ix%>" selected="selected"><%=ix %></option>
									   <%	}else{		%>
									     		<html:option value="<%=""+ ix%>" ><%=ix %></html:option>
									   <%	}
										} %>
								<%} %>
						</html:select>
					</span>
				</span>

				<div id="llistatNumSelectors">
				
				
					<% 	for (int ix=0;ix < indice;ix++) { %>
						<span>
						<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
		 		 			<bean:write name="lang" />: <html:text property="<%="textos["+(j.intValue()+(ix*5))+"]"%>" size="15" maxlength="100"/>
		 		 		</logic:iterate>
						</span>
									
					<%} %>
				
				</div>					
			</td>
			</tr>
		</table>
	
</div>


</html:form>

</body>
</html>


<script>

triarTipusFormContacte();

<logic:present name="lineaFormularioForm" property="id">
	var tipo='<bean:write name="lineaFormularioForm" property="tipo"/>';
	
	if (tipo==4) triaNumSelectors();
	if (tipo==5) triaNumSelectors2();

</logic:present>
	
function submitForm(){
	var accForm = document.getElementById('accFormulario');
	accForm.submit();
}
function volver(id) {
	document.location.href="formularioEdita.do?accion="+ "<bean:message key='operacion.editar'/>" +"&id="+id;
}
function submitForm(nom_accio){
	var accForm = document.getElementById('accFormulario');
	accForm.accion.value= nom_accio;
	
	 if (nom_accio== "Traduir") {
		 accForm.accion.value="<bean:message key='operacion.traducir'/>";
	 }else if (nom_accio== "Crear"){
		  accForm.accion.value="<bean:message key='operacion.crear'/>";
	 }else if (nom_accio== "Guardar"){
		 accForm.accion.value="<bean:message key='operacion.guardar'/>";
	} else if (nom_accio== "Borrar"){
		if(confirm("<bean:message key='conte.alert6' />"))
		 accForm.accion.value="<bean:message key='operacion.borrar' />";
		 else return false;
	}
	accForm.submit();
}

</script>

