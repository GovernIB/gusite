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
	<script type="text/javascript" src="js/jsEncuestas.js"></script>
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
	    <logic:present name="formularioconForm" property="id">
       		<li class="pagActual"><bean:write name="formularioconForm" property="email" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="formularioconForm" property="id">
	         <li class="pagActual"><bean:message key="formu.alta" /></li>
	    </logic:notPresent>				
	</ul>
	
	<!-- titol pagina -->
	<h1><bean:message key="formu.datos" />:  
		<span>
		    <logic:present name="formularioconForm" property="id">
	         	<bean:message key="formu.datos" />
		    </logic:present>		
		    <logic:notPresent name="formularioconForm" property="id">
		        <bean:message key="formu.alta" />
		    </logic:notPresent>				
		</span>
	</h1>

<%session.setAttribute("action_path_key",null);%>
<html:form action="/formularioEdita.do" method="POST"  enctype="multipart/form-data"  styleId="accFormularioLista">
		
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
			
			<!-- botonera -->
		<input type="hidden" name="espera" value="si" id="espera" />
		<input type="hidden" name="accion" value=""/>
		<logic:present name="formularioconForm" property="id">
				<input type="hidden" name="id" value="<bean:write name="formularioconForm" property="id" />"/>
		</logic:present>
		<div id="botonera"> 
				<span class="grup">
					<button type="button" title='<bean:message key="formu.volver"/>' onclick='volver();'>
				   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="formu.volver"/>' />
				   	</button> 
				</span>  
				<logic:present name="formularioconForm" property="id">
					<span class="grup">
						<button type="submit" title="<bean:message key="agenda.crear" />" onclick="submitForm('Crear');"><img src="imgs/botons/nou.gif" alt="<bean:message key="agenda.crear" />" /></button>
					</span>
				</logic:present>
				<span class="grup">
					<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('Guardar');">
						<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
					</button>
				</span> 
				<logic:present name="formularioconForm" property="id">
				 	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('Borrar');">
				   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
				   </button> 
				</logic:present>
		
		</div>	
	

	<!-- ******************************* -->
	<!-- Datos Generales del Formulario  -->
	<!-- ******************************* -->

	<!-- las tablas están entre divs por un bug del FireFox -->
	<div id="formulario">
		<table cellpadding="0" cellspacing="0" class="edicio">
		<tr>
			<td class="etiqueta"><bean:message key="formu.visible" /></td>
			<td><html:radio property="visible" value="S" />&nbsp;Sí­&nbsp;<html:radio property="visible" value="N" />&nbsp;No</td>
			<td class="etiqueta"><bean:message key="formu.correo" /></td>
			<td><html:text property="email" maxlength="256" size="50" /></td>
		</tr>
		<tr>
			<td class="etiqueta"><bean:message key="formu.anexArch" /></td>
			<td><html:radio property="anexarch" value="S" />&nbsp;Sí­&nbsp;<html:radio property="anexarch" value="N" />&nbsp;No</td>
			<td></td>
			<td></td>
		</tr>
		</table>
	
	<!-- ****************************** -->
	<!-- Lista de Campos del Formulario -->
	<!-- ****************************** -->
	
	<logic:present name="formularioconForm" property="id">
	
	
		<div style="background:#FFFAF5; margin:40px 50px 40px 50px;">
		<h1>
			<span>
			    <bean:message key="formu.campos" />				
			</span>
		</h1>

		<div id="botonera">
			<span class="grup">
				<button type="button" title="<bean:message key="formu.anadircampo" />" onclick="nuevalinea('<bean:write name="formularioconForm" property="id"/>');"><img src="imgs/botons/nou.gif" alt="<bean:message key="formu.anadircampo" />" /></button> 
				<logic:notEmpty name="formularioconForm" property="lineasdatocontacto">
					<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button>
					<button type="button" title="<bean:message key="op.2" />" onclick="borralineas();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
				</logic:notEmpty>
			</span>
		</div>			
	
		<logic:empty name="formularioconForm" property="lineasdatocontacto">	
			<table id="llistatcoses" cellpadding="0" cellspacing="0" class="llistat">
			<tr>
			<td></td>
			</tr>
			</table>
		</logic:empty>	
	
		<logic:notEmpty name="formularioconForm" property="lineasdatocontacto">
	
			<table id="llistatcoses" cellpadding="0" cellspacing="0" class="llistat">
			<thead>
				<tr>
					<th class="check">&nbsp;</th>
					<th><bean:message key="formu.linvisible" /></th>
					<th width="30%"><bean:message key="formu.linetiq" /></th>
					<th><bean:message key="formu.linorden" /></th>
					<th><bean:message key="formu.linobliga" /></th>
					<th><bean:message key="formu.lintipo" /></th>
				</tr>
			</thead>
			<tbody>
	
			<logic:iterate id="i" name="formularioconForm" property="lineasdatocontacto" indexId="indice">
	    	<tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
	    		<td class="check">
				   	<html:multibox property="seleccionados" styleClass="radio"> 
			   			<bean:write name="i" property="id"/>
			    	</html:multibox>
				</td>
		    	<td>
	    			<logic:match name="i" property="visible" value="S">Si</logic:match>
					<logic:notMatch name="i" property="visible" value="S">No</logic:notMatch>
				</td>
	   	    	<td>
			   	    <logic:notEmpty name="i" property="traduccion.texto">
						  <bean:define id="textoetiquetas"><bean:write name="i" property="traduccion.texto" /></bean:define>
						  <% String[] vector= textoetiquetas.split("#"); %>
						 <bean:define id="textolinea"><%=vector[0] %></bean:define>
						 <bean:write name="textolinea"/>
					</logic:notEmpty>
					<logic:empty name="i" property="traduccion.texto">
						[<bean:message key="formu.noetiq" />]
					</logic:empty>
	   	    	</td>
		    	<td><bean:write name="i" property="orden"/></td>
	  	    	<td>
			  	    <logic:match name="i" property="obligatorio" value="1">Si</logic:match>
					<logic:notMatch name="i" property="obligatorio" value="1">No</logic:notMatch>
	  	    	</td>
	  	    	<td>
		  	    	<logic:match name="i" property="tipo" value="1">
						<bean:message key="formu.lincampo1" />
					</logic:match>
					<logic:match name="i" property="tipo" value="2">
						<bean:message key="formu.lincampo2" />&nbsp;
						<bean:message key="formu.lintamano" />:<bean:write name="i" property="tamano"/>
					</logic:match>
					<logic:match name="i" property="tipo" value="3">
						<bean:message key="formu.lincampo3" />&nbsp;
						<bean:message key="formu.linnlin" />:<bean:write name="i" property="lineas"/>
					</logic:match>
					<logic:match name="i" property="tipo" value="4">
						<bean:message key="formu.lincampo4" />&nbsp;
					</logic:match>
					<logic:match name="i" property="tipo" value="5">
						<bean:message key="formu.lincampo5" />&nbsp;
					</logic:match>
	  	    	</td>
			</tr>
	    	</logic:iterate>
		
			</tbody>
			</table>
				
		</logic:notEmpty>
				
	</div>
		
	</logic:present>
		
</div>
	
	</html:form>

</body>
</html>

<script type="text/javascript">
<!--

var uriEdicion="lineaformularioEdita.do?accion="+"<bean:message key='operacion.editar'/>&idcontacto=";
var uriBorrarLinea="formularioEdita.do?&accion=borrarlinea";

var alert1='<bean:message key="formu.linalert1"/>';
var alert2='<bean:message key="formu.linalert2"/>';


function submitForm(nom_accio){
	var accForm = document.getElementById('accFormularioLista');
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

function nuevalinea(idcontacto) {
		document.location.href="lineaformularioEdita.do?idcontacto="+idcontacto+"&accion="+"<bean:message key='operacion.crear'/>";
}
function volver() {
	document.location.href="formularios.do";
}

function borralineas() {
var nselec=0;


		var accFormLista = document.getElementById('accFormularioLista');
		var nselec=0;
		
	    if (accFormLista.seleccionados.length==undefined) {
	        if (accFormLista.seleccionados.checked) nselec=1;
	    } else {
	        for (var i=0;i<accFormLista.seleccionados.length;i++)
	            if (accFormLista.seleccionados[i].checked) nselec++;
	    }
		    
	    if (nselec==0) {
            alert (alert1);
            return;
	    }
		    
	    if (!confirm(alert2))	return;

	    accFormLista.accion.value="<bean:message key='operacion.borrarlinea' />";

		accFormLista.submit();

}

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>