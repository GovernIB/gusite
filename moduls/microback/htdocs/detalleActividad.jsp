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
		<li><a href="agendas.do"><bean:message key="agenda.agenda" /></a></li>
		<li><a href="actividades.do"><bean:message key="actividad.lista" /></a></li>		
	    <logic:present name="actividadForm" property="id">
         		<li class="pagActual"><bean:write name="actividadForm" property="traducciones[0].nombre" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="actividadForm" property="id">
	         <li class="pagActual"><bean:message key="actividad.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/agenda.gif" alt="<bean:message key="actividad.eticolumna1" />" />
	<bean:message key="actividad.eticolumna1" />: 
		<span>
		    <logic:present name="actividadForm" property="id">
	         	<bean:write name="actividadForm" property="traducciones[0].nombre" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="actividadForm" property="id">
		        <bean:message key="actividad.alta" />
		    </logic:notPresent>				
		</span>
	</h1>
	
	
<%session.setAttribute("action_path_key",null);%>
	<html:form action="/actividadEdita.do" method="POST" enctype="multipart/form-data" styleId="accFormulario" >
		
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
		
		<!-- botonera -->
		<div id="botonera">
			<span class="grup">
				<button type="button" title='<bean:message key="agenda.activitat.volver"/>' onclick='document.location.href="actividades.do";'>
			   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="agenda.activitat.volver"/>' />
			   	</button> 
			</span>
			<logic:present name="actividadForm" property="id">
				<span class="grup">
					<button type="submit" title="<bean:message key="actividad.crear" />" onclick="submitForm('Crear');"><img src="imgs/botons/nou.gif" alt="<bean:message key="actividad.crear" />" /></button>
				</span>
				<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
					<span class="grup">				
						<button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
						   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
						</button>
					</span> 
				 </logic:equal>
			</logic:present>	
			<span class="grup">
				<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('Guardar');">
					<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
				</button>
			</span> 
			<logic:present name="actividadForm" property="id">
				 	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('Borrar');">
				   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
				   </button> 
			</logic:present>
					   	
		</div>	
		
		<input type="hidden" name="espera" value="si" id="espera" />
		<input type="hidden" name="accion" value=""/>
		<logic:present name="actividadForm" property="id">
			<input type="hidden" name="id" value="<bean:write name="actividadForm" property="id" />"/>
		</logic:present>

		

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
				
				    <logic:iterate id="traducciones" name="actividadForm" property="traducciones" indexId="i" >
					<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
						<table cellpadding="0" cellspacing="0" class="edicio">
						<tr>
							<td class="etiquetaleft"><bean:message key="actividad.nombre" />:</td>
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

<logic:present name="alert">
alert("<bean:write name='alert' />");
</logic:present>

	function submitForm(){
		var accForm = document.getElementById('accFormulario');
		accForm.submit();
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
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>


