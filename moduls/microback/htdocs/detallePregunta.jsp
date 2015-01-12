<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
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
		<li><a href="encuestas.do"><bean:message key="menu.encuestas" /></a></li>
		<logic:present name="titencuesta">
			<li><a href="encuestaEdita.do?id=<bean:write name="preguntaForm" property="idencuesta"/>"><bean:write name="titencuesta"/></a></li>
		</logic:present>		
	    <logic:present name="preguntaForm" property="id">
         		<li class="pagActual"><bean:write name="preguntaForm" property="traducciones[0].titulo" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="preguntaForm" property="id">
	         <li class="pagActual"><bean:message key="pregunta.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/encuestas.gif" alt="<bean:message key="pregunta.pregunta" />" />
	<bean:message key="pregunta.pregunta" /> :  
		<span>
		    <logic:present name="preguntaForm" property="id">
	         	<bean:write name="preguntaForm" property="traducciones[0].titulo" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="preguntaForm" property="id">
		        <bean:message key="pregunta.alta" />
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

	<html:form action="/preguntaEdita.do" enctype="multipart/form-data" styleId="accFormularioLista">
	
	
		<logic:present name="preguntaForm" property="id">
		    <input type="hidden" name="modifica" value="Grabar">
			<html:hidden property="id" />
		</logic:present>
		<logic:notPresent name="preguntaForm" property="id">
			<input type="hidden" name="anyade" value="Crear">
		</logic:notPresent>
	
	    <html:hidden property="idencuesta" />
	    
		<script>
			var DHTML = (document.getElementById || document.all || document.layers);
			function ap_getObj(name) {
				if (document.getElementById){
					return document.getElementById(name).style;
				} else if (document.all){
					return document.all[name].style;
				} else if (document.layers){
					return document.layers[name];
				}
			}
					
			function ap_showDiv(flag){
				if (!DHTML) return;
				var x = ap_getObj("minMaxResp");
				var y = ap_getObj("obligatori");
				x.visibility = (flag=='S') ? 'visible':'hidden';
				y.visibility = (flag=='S') ? 'hidden':'visible';
				if(!document.getElementById){
					if(document.layers){
						x.left=280/2;
						return true;
					}
				}
			}
		</script>

		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
			<table cellpadding="0" cellspacing="0" class="edicio">
		
				<tr class="par">
					<td width="25%" class="etiqueta"><bean:message key="pregunta.imagen" /></td>
					<td width="75%" colspan="3">
						<div style="text-align:left" id="microManagedFile">
							<html:hidden property="imagenid" />
							<logic:notEmpty name="preguntaForm" property="imagennom">
			                	<html:text property="imagennom"/>
								<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'imagen','imagenid','imagenbor','imagennom');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
								<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="preguntaForm" property="imagenid"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>									
						    </logic:notEmpty>
						    <logic:empty name="preguntaForm" property="imagennom">
							    <html:file property="imagen" size="30"/>
			   			    </logic:empty>
		   			    </div>
					</td>
				</tr>			
				<tr>
					<td width="25%" class="etiqueta"><bean:message key="pregunta.multiresp" /></td>
					<td width="25%"><label><html:radio property="multiresp" value="S" onchange="ap_showDiv('S');"/>&nbsp;Sí</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="multiresp" value="N" onchange="ap_showDiv('N');"/>&nbsp;No</label></td>
					<td width="15%" class="etiqueta"><bean:message key="pregunta.visiblecmp" /></td>
					<td width="35%"><label><html:radio property="visiblecmp" value="S"/>&nbsp;Sí</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visiblecmp" value="N" />&nbsp;No</label></td>
				</tr> 
		
				<tr class="par">
					<td width="25%" class="etiqueta"><bean:message key="pregunta.visible" /></td>
					<td width="25%"><label><html:radio property="visible" value="S" />&nbsp;Sí</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visible" value="N" />&nbsp;No</label></td>
					<td width="15%" class="etiqueta"><bean:message key="pregunta.orden" /></td>
					<td width="35%"><html:text property="orden"/></td>
				</tr>					
				<tr>
					<td colspan="4">
						<div id="minMaxResp">
							<table cellpadding="0" cellspacing="0" class="edicio">
								<tr class="par">
									<td width="25%"></td>
									<td width="75%" colspan="3"><bean:message key="pregunta.nresp"/></td>
								</tr>
								<tr class="par">
									<td width="25%" class="etiqueta"><bean:message key="pregunta.numresp.min" /></td>
									<td width="25%"><html:text property="minContestadas"/></td>
									<td width="15%" class="etiqueta"><bean:message key="pregunta.numresp.max" /></td>
									<td width="35%"><html:text property="maxContestadas"/></td>
								</tr>
							</table>
						</div>
						<div id="obligatori" style="position:relative;">
							<table cellpadding="0" cellspacing="0" class="edicio">
								<tr class="par">
									<td width="25%"></td>
									<td width="75%" colspan="3"><bean:message key="pregunta.obligatorietat"/></td>
								</tr>
								<tr class="par">
									<td width="25%" class="etiqueta"><bean:message key="pregunta.obligatori" /></td>
									<td width="35%"><label><html:radio property="obligatoria" value="1"/>&nbsp;Sí</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="obligatoria" value="0" />&nbsp;No</label></td>
									<td/>
									<td/>
								</tr>
							</table>
						</div>
					</td>								
				</tr>
				<tr>
					<td width="25%" class="etiqueta">&nbsp;</td>
					<td width="25%"><html:hidden property="obligatorio" value="S" /></td>
					<td width="15%" class="etiqueta" colspan="2"><bean:message key="pregunta.nrespuestas" />:&nbsp;&nbsp;<strong><bean:write name="preguntaForm" property="nrespuestas"/></strong><html:hidden property="nrespuestas"/></td>
					<td width="35%">&nbsp;</td>
				</tr>				
				<tr>
					<td width="100%" colspan="4">
			
						<ul id="submenu">
							<logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
								<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
					        </logic:iterate>
						</ul>    
					
					    <logic:iterate id="traducciones" name="preguntaForm" property="traducciones" indexId="i" >
						<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
						
							<table cellpadding="0" cellspacing="0" class="tablaEdicio">
							<tr>
								<td class="etiqueta"><bean:message key="pregunta.titulo" />:</td>
								<td><html:text property="titulo" name="traducciones" size="100" maxlength="256" indexed="true" /></td>
							</tr>
							</table>
							
						</div>
					    </logic:iterate>
			
					</td>
				</tr>
			</table>

			<!-- ********************************** -->
			<!-- Lista de Respuestas de la pregunta -->
			<!-- ********************************** -->
			
			<logic:present name="preguntaForm" property="id">
			
				
					<div style="background:#FFFAF5; margin:40px 50px 40px 50px;">
							
							<!-- subtitol: respostes -->
							<h1>
								<span>
								    <bean:message key="preg.campos" />				
								</span>
							</h1>
							<!-- botonera respostes-->
							<div id="botonera">
								<button type="button" title="<bean:message key="preg.anadirrespuesta" />" onclick="nuevaresp('<bean:write name="preguntaForm" property="id"/>');"><img src="imgs/botons/nou.gif" alt="<bean:message key="preg.anadirrespuesta" />" /></button> 
								<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 					
								<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
							</div>	
						
						
							<logic:empty name="preguntaForm" property="respuestas">	
								<table id="llistatcoses" cellpadding="0" cellspacing="0" class="llistat">
								<tr>
								<td></td>
								</tr>
								</table>
							</logic:empty>
						
							<logic:notEmpty name="preguntaForm" property="respuestas">	
							
								<table id="llistatcoses" cellpadding="0" cellspacing="0" class="llistat">
								<thead>
								<tr>
									<th class="check">&nbsp;</th>
									<th><bean:message key="preg.nombrerespuesta" /></th>
									<th><bean:message key="respuesta.orden" /></th>								
									<th><bean:message key="preg.tipo" /></th>
								</tr>
								</thead>
								<tbody>	
									<logic:iterate id="i" name="preguntaForm" property="respuestas" indexId="indice">
								    <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
								      	<td class="check">
								        	<html:multibox property="seleccionados" styleClass="radio"> 
								            	<bean:write name="i" property="id"/>
									        </html:multibox>
								    	</td>
								   	    <td>
									   	    <logic:present name="i" property="traduccion.titulo">
										   	    <bean:write name="i" property="traduccion.titulo" />
								   		    </logic:present>
											<logic:empty name="i" property="traduccion.titulo">
												[<bean:message key="encu.noetiq" />]
											</logic:empty>								   		    
								   	    </td>
										<td>
								    		<bean:write name="i" property="orden" />
										</td>							   	    
									    <td>
								    		<logic:match name="i" property="tipo" value="N"><bean:message key="respuesta.tipo.normal" /></logic:match>
											<logic:notMatch name="i" property="tipo" value="N"><bean:message key="respuesta.tipo.input" /></logic:notMatch>
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



<logic:present name="preguntaForm" property="id">
	var uriBorrar="preguntaEdita.do?modifica=null&";
</logic:present>
<logic:notPresent name="preguntaForm" property="id">
	var uriBorrar="preguntaEdita.do?anyade=null&";
</logic:notPresent>

var uriEdicion="respuestaEdita.do?id=";
var alert1="<bean:message key="preg.respalert1"/>";
var alert2="<bean:message key="preg.respalert2"/>";

	function nuevaresp(idpreg) {
		document.location.href="respuestaEdita.do?idpreg="+idpreg;
	}

	function submitForm(){
		var accForm = document.getElementById('accFormularioLista');
		accForm.submit();
	}


	function borravarios() {
	
		<logic:present name="encuestaForm" property="id">
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
		    
		    if (!confirm(alert2))
		            return;
		
		    accFormLista.action=uriBorrar + "borrar";
		    accFormLista.submit();
		</logic:present>  
	
	}	
	
	
// -->
	ap_showDiv("<bean:write name="preguntaForm" property="multiresp"/>");
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
