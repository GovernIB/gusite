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
	<script type="text/javascript" src="js/jsListadosSeleccionLDistrib.js"></script>
</head>

<body>
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>		
		<li><a href="ldistribucion.do"><bean:message key="menu.ldistribucion" /></a></li>
		<logic:present name="ldistribucionForm" property="id">
         	<li class="pagActual"><bean:write name="ldistribucionForm" property="nombre" ignore="true" /></li>
	    </logic:present>
	    <logic:notPresent name="ldistribucionForm" property="id">
	        <li class="pagActual"><bean:message key="ldistribucio.alta" /></li>
	    </logic:notPresent>
	</ul>
			
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/convocatorias.gif" alt="<bean:message key="menu.editarpagina" />"/>
		<span>
		    <logic:present name="ldistribucionForm" property="id">
	         	<bean:message key="ldistribucio.modificacion" />
		    </logic:present>
		    <logic:notPresent name="ldistribucionForm" property="id">
		        <bean:message key="ldistribucio.alta" />
		    </logic:notPresent>
		</span>
	</h1>

<%session.setAttribute("action_path_key",null);%>
	<jsp:include page="/moduls/mensajes.jsp"/>
	<!-- botonera -->
	<div id="botonera">
		<span class="grup">
			<button type="button" title='<bean:message key="ldistribucio.volver"/>' onclick='document.location.href="ldistribucion.do";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="formu.volver"/>' />
		   	</button> 
		</span>  
		<logic:present name="ldistribucionForm" property="id">
			<span class="grup">
				<button type="submit" title="<bean:message key="ldistribucio.crear" />" onclick="submitForm('crearDistrib');"><img src="imgs/botons/nou.gif" alt="<bean:message key="ldistribucio.crear" />" /></button>
			</span>
		</logic:present>
		<span class="grup">
			<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('guardarDistrib');">
				<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
			</button>
		</span> 
		<logic:present name="ldistribucionForm" property="id">
		 	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('borrarDistrib');">
		   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
		    </button> 
		</logic:present>
	</div>	
	
	
	<!-- las tablas están entre divs por un bug del FireFox -->
	
	<html:form action="/ldistribucionEdita.do"  method="POST" enctype="multipart/form-data"  styleId="accFormularioLista">
		<div id="formulario">	
			<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->

		    <input type="hidden" name="espera" value="si" id="espera" />
			<input type="hidden" name="accion" value=""/>
			<logic:present name="ldistribucionForm" property="id">
				<input type="hidden" name="id" value="<bean:write name="ldistribucionForm" property="id" />"/>
			</logic:present>
			<logic:notPresent name="ldistribucionForm" property="id">
				<input type="hidden" name="id" value=""/>
			</logic:notPresent>		
			<table cellpadding="0" cellspacing="0" class="edicio">
				<tr class="par">
					<td class="etiqueta"><bean:message key="ldistribucio.datos"/></td>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td width="30%" class="etiqueta" width="30%"><bean:message key="ldistribucio.eticolumna1" /></td>
					<td width="10%"><html:text property="nombre" size="15" maxlength="20"/></td>
					<td width="10%" class="etiqueta"><bean:message key="ldistribucio.eticolumna2" /></td>
					<td width="50%"><html:text property="descripcion" size="60" maxlength="200"/></td>					
				</tr>
				<tr>
					<td width="30%" class="etiqueta" width="30%"><bean:message key="ldistribucio.eticolumna3" /></td>
					<td>
						<html:radio property="publico" value="S"/>&nbsp;Si&nbsp;
						<html:radio property="publico" value="N" />&nbsp;No
					</td>
					<td colspan="2"/>
				</tr>
				<tr>
					<td class="etiqueta" colspan="3">
						<bean:message key="correo.nMaxIntentsErronis"/>
					</td>
					<td>
						<%=System.getProperty("es.caib.gusite.intentosMailing") %>
					</td>
			    </tr>
			</table>
					
			<div style="background:#FFFAF5; margin:40px 50px 40px 50px;">
				
				<div id="botonera">			
					<span class="grup">
						<button type="button" title="<bean:message key="ldistribucio.guardar.correo" />" onclick="guardarCorreo();">
								<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="ldistribucio.guardar.correo" />
						</button>
						<html:text property="email" size="30" maxlength="200" styleId="email"/>					    				 				
					</span>
					<logic:present name="ldistribucionForm" property="id">
						<span>
							<button type="button" title='<bean:message key="ldistribucio.importar" />' onclick="importar();">
						   		<img src="imgs/iconos/ico_entraredicion.gif" alt='<bean:message key="ldistribucio.importar" />' />
						    </button>
							<html:file property="upLoad" styleId="upload">
				   			    <img src="imgs/iconos/ico_entraredicion.gif" alt='<bean:message key="ldistribucio.importar" />' />
							</html:file>
						</span>
					</logic:present>
					<logic:greaterThan name="nreg" value="0">
						<span>
						 	<button type="button" title='<bean:message key="operacion.borrar" />' onclick="borravarios();">
						   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
						    </button>
						    <button type="button" title='<bean:message key="ldistribucio.exportar" />' onclick="exportar();">
						   		<img src="imgs/iconos/ico_saliredicion.gif" alt='<bean:message key="ldistribucio.exportar" />' />
						    </button>
					    </span>
					</logic:greaterThan>
				</div>	
				<table cellpadding="0" cellspacing="0" class="llistat" id="seleccion">
					<thead>
						<tr>
							<th class="check">&nbsp;</th>
							<th><bean:message key="correo.email" /></th>
							<th><bean:message key="correo.nombre" /></th>
							<th><bean:message key="correo.apellidos" /></th>
							<th><bean:message key="correo.emailvalid" /></th>
							<th><bean:message key="correo.intentsErronis" /></th>
						</tr>
					</thead>		
					<tbody>
						<logic:present name="ldistribucionForm" property="destinatarios">			
						    <logic:iterate id="i" name="ldistribucionForm" property="destinatarios" indexId="indice">
								<tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
									<td class="check">
										<html:multibox property="seleccionados" styleClass="radio"> 
											<bean:write name="i" property="correo"/>
										</html:multibox>
									</td>
									<td><bean:write name="i" property="correo" /></td>
									<td><bean:write name="i" property="nombre" /></td>
									<td><bean:write name="i" property="apellidos" /></td>
									<td>
										<logic:present name="i" property="noEnviar">
											<logic:equal name="i" property="noEnviar" value="false">
												<img src="imgs/accessibilitat/ok.gif"/>
											</logic:equal>
											<logic:equal name="i" property="noEnviar" value="true">
												<img src="imgs/accessibilitat/mal.gif"/>
											</logic:equal>
										</logic:present>
									</td>
									<td>
										<logic:present name="i" property="intentoEnvio">
											<bean:write name="i" property="intentoEnvio"/>
										</logic:present>
									</td>               	
								</tr>
						    </logic:iterate>
					    </logic:present>			    
					</tbody>
				</table>			
			</div>
		</div>
	</html:form>

<script>
	function submitForm(nom_accio){
		var accForm = document.getElementById('accFormularioLista');
		if(accForm.nombre.value != ""){
			accForm.accion.value= nom_accio;		
			accForm.submit();
		}else{
			alert('<bean:message key="ldistribucio.alert8"/>');
		}
	}

	function exportar(){
	    nombre = window.open('/sacmicroback/ldistribucionEdita.do?accion=exportar&id=<bean:write name="ldistribucionForm" property="id"/>','','');
	}

	function importar(){
		var accForm = document.getElementById('accFormularioLista');
		if(accForm.upload.value != ""){
			accForm.accion.value= 'importar';		
			accForm.submit();
		}else{
			alert('<bean:message key="ldistribucio.alert7"/>');
		}	
	}

	function guardarCorreo(){
		var accForm = document.getElementById('accFormularioLista');
		if(accForm.id.value != ""){
			if(validar(document.getElementById("email").value)){
				accForm.accion.value= 'guardarCorreo';
				accForm.submit();
			}else{
				alert('<bean:message key="ldistribucio.alert4"/>');
			}
		}else{
			alert("<bean:message key="ldistribucio.alert3"/>");
		}
	}

	function validar(correo){
		if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})$/.test(correo))
			return true;
		else
			return false;
	}
	
	var uriEdicion="correoEdita.do?id=<bean:write name="ldistribucionForm" property="id"/>&correo=";
	var alert1="<bean:message key="ldistribucio.alert5"/>";
	var alert2="<bean:message key="ldistribucio.alert6"/>";
	
</script>

</body>
</html>

