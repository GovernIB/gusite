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
		<li><bean:message key="menu.configuracion" /></li>
		<li><a href="componentes.do"><bean:message key="menu.componentes" /></a></li>
	    <logic:present name="componenteForm" property="id">
         		<li class="pagActual"><bean:write name="componenteForm" property="nombre" /></li>
	    </logic:present>		
	    <logic:notPresent name="componenteForm" property="id">
	         <li class="pagActual"><bean:message key="compo.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/configuracion.gif" alt="<bean:message key="menu.componentes" />" />
	<bean:message key="menu.componentes" />. <span>Propiedades del componente</span></h1>

<%session.setAttribute("action_path_key",null);%>	
	<html:form action="/componenteEdita.do" method="POST" enctype="multipart/form-data"  styleId="accFormulario">

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
		
		<input type="hidden" name="accion" value=""/>
		
		<!-- botonera -->
		<div id="botonera">
			<span class="grup">
			<button type="button" title='<bean:message key="tipo.volvermantenimiento.componentes"/>' onclick='document.location.href="componentes.do";'>
			  		<img src="imgs/botons/tornar.gif" alt='<bean:message key="tipo.volvermantenimiento.componentes"/>' />
			</button> 
			</span>
			<logic:present name="componenteForm" property="id">
			<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
			   <span class="grup">
			   <button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
			   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
			   </button>
			   </span> 
			</logic:equal>
			</logic:present>
		   <button type="submit" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
		   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
		   	</button>
		</div>

		<input type="hidden" name="espera" value="si" id="espera" />
	
	     <logic:present name="componenteForm" property="id">
		     <input type="hidden" name="modifica" value="Grabar">
	         <html:hidden property="id" />
	     </logic:present>
		 <logic:notPresent name="componenteForm" property="id">
		  	<input type="hidden" name="anyade" value="Crear">
		 </logic:notPresent>     
	
	
		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
				<table cellpadding="0" cellspacing="0" class="edicio">
		
				<tr>
					<td class="etiqueta" >
						<bean:message key="compo.nombre" />
					</td>
					<td>	
						<html:text property="nombre" maxlength="256" />
					</td>
				</tr>
		
				<tr class="par">
					<td class="etiqueta" >
						<bean:message key="compo.tipo" />
					</td>
					<td>
		                <html:select property="idTipo" onchange="javascript:controlaClase();">
			                <html:options collection="tiposCombo" labelProperty="traduccion.nombre" property="id"/>
		    	        </html:select>
					</td>
				</tr>
				</table>
				
				
					<div id="diferenteExterno">
						<table cellpadding="0" cellspacing="0" class="edicio">
						<tr>
							<td class="etiqueta"><bean:message key="compo.orden" /></td>
							<td>
								<html:select property="orden">
				   	    	        <html:option value="0"><bean:message key="tipo.orden0" /></html:option>
				   	    	        <html:option value="1"><bean:message key="tipo.orden1" /></html:option>
				   	    	        <html:option value="2"><bean:message key="tipo.orden2" /></html:option>
				   	    	        <html:option value="3"><bean:message key="tipo.orden3" /></html:option>
						        </html:select>
							</td>
							<td class="etiqueta"><bean:message key="compo.numelem" /></td>
							<td><html:text property="numelem"/></td>
						</tr>
				
						<tr class="par">
							<td class="etiqueta"><bean:message key="compo.soloimagen" /></td>
							<td><html:radio property="soloimg" value="S" />&nbsp;Sí­<html:radio property="soloimg" value="N" />&nbsp;No</td>
							<td class="etiqueta"><bean:message key="compo.filas" /></td>
							<td><html:radio property="filas" value="S" />&nbsp;Sí­<html:radio property="filas" value="N" />&nbsp;No</td>
						</tr>
				
						<tr>
							<td class="etiqueta"><bean:message key="compo.visualizacion" /></td>
							<td>
								<html:select property="visualizacion">
				   	    	        <html:option value="L"><bean:message key="compo.visualizacion.listado" /></html:option>
				   	    	        <html:option value="B"><bean:message key="compo.visualizacion.boton" /></html:option>
						        </html:select>
							</td>
						</tr>
						
						<tr class="par">
							<td class="etiqueta"><bean:message key="compo.imagen" /></td>
							<td>
								<div style="text-align:left" id="microManagedFile">
									<html:hidden property="imagenid" />
									<logic:notEmpty name="componenteForm" property="imagennom">
					                	<html:text property="imagennom"/>
										<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'imagen','imagenid','imagenbor','imagennom');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
										<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="componenteForm" property="imagenid"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
										
								    </logic:notEmpty>
								    <logic:empty name="componenteForm" property="imagennom">
									    <html:file property="imagen" size="30"/>
					   			    </logic:empty>
				   			    </div>
							</td>
						</tr>
						</table>
					</div>
				
				
				<table cellpadding="0" cellspacing="0" class="edicio">
		
				<tr>
				<td colspan="4">
		
					<ul id="submenu">
						<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
							<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
				        </logic:iterate>
					</ul>    
				
				    <logic:iterate id="traducciones" name="componenteForm" property="traducciones" indexId="i" >
					<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
					
						<table cellpadding="0" cellspacing="0" class="tablaEdicio">
						<tr>
							<td class="etiqueta"><bean:message key="compo.titulo" />:</td>
							<td><html:text property="titulo" name="traducciones" size="50" maxlength="256" indexed="true" /></td>
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

	function submitForm(nom_accio){
		var accForm = document.getElementById('accFormulario');
		accForm.accion.value= nom_accio;
		 if (nom_accio== "Traduir") {
			 accForm.accion.value="<bean:message key='operacion.traducir'/>";
		 }else if (nom_accio== "Guardar"){
			 accForm.accion.value="<bean:message key='operacion.guardar'/>";
		} else if (nom_accio== "Borrar"){
			if(confirm("<bean:message key='conte.alert6' />"))
			 accForm.accion.value="<bean:message key='operacion.borrar' />";
			 else return false;
		}
		accForm.submit();
	}

		//muestra/esconde el editor
	function controlaClase() {
		var obj;var valor;var obj2;var obj3;
		obj = document.getElementsByName("idTipo")[0];
		var esexterno='N';
		var elseleccionado = obj.options[obj.selectedIndex].value;
		//recorrer los que son externos
		var listadoExternosOMapa = new Array( '-1'
		<logic:present name="tiposCombo">
			<logic:iterate name="tiposCombo" id="clase">
						<logic:match name="clase" property="tipoelemento" value="3">
							,'<bean:write name="clase" property="id"/>'
						</logic:match>	
						<logic:match name="clase" property="tipoelemento" value="5">
							,'<bean:write name="clase" property="id"/>'
						</logic:match>
			</logic:iterate>
		</logic:present>
		);
		
		for(f=0;f<listadoExternosOMapa.length;f++) {
			if (parseInt(listadoExternosOMapa[f])==parseInt(elseleccionado))
				esexterno='S';				
		};
		
		if(esexterno == 'S') {
	 		obj = document.getElementById("diferenteExterno");
	 		obj.style.display="none";
	 	} else {
	 		obj = document.getElementById("diferenteExterno");
	 		obj.style.display="block";
	 	}
		
	}
	
	
	controlaClase();
	
// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>