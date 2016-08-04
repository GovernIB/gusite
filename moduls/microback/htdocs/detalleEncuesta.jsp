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
	<script type="text/javascript"	src="js/jquery/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="js/checkUri.js"></script>
</head>

<body>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="encuestas.do"><bean:message key="menu.encuestas" /></a></li>
	    <logic:present name="encuestaForm" property="id">
         		<li class="pagActual"><bean:write name="encuestaForm" property="traducciones[0].titulo" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="encuestaForm" property="id">
	         <li class="pagActual"><bean:message key="encuesta.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/encuestas.gif" alt="<bean:message key="encuesta.encuesta" />" />
	<bean:message key="encuesta.encuesta" />:  
		<span>
		    <logic:present name="encuestaForm" property="id">
	         	<bean:write name="encuestaForm" property="traducciones[0].titulo" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="encuestaForm" property="id">
		        <bean:message key="encuesta.alta" />
		    </logic:notPresent>				
		</span>
	</h1>

	<!-- botonera -->
	<div id="botonera">
		<logic:present name="encuestaForm" property="id">
			<span class="grup">
				<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
			</span>	
	    </logic:present>	
		<span class="grup">	    
			<button type="button" title="<bean:message key="op.15" />" onclick="submitForm();"><img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" /></button>
		</span>			
		<logic:present name="encuestaForm" property="id">			
			<button type="button" title="<bean:message key="stat.encuesta.ver" />" onclick="verStat('<bean:write name="encuestaForm" property="id"/>');"><img src="imgs/botons/stat.gif" alt="<bean:message key="stat.encuesta.ver" />" /></button>
		</logic:present>
		
	</div>	


	<div style="font-weight:bold; color:#FF4400;">
	<html:errors/>
	</div>

	<html:form action="/encuestaEdita.do" enctype="multipart/form-data" styleId="accFormularioLista">
		
		
		<logic:present name="encuestaForm" property="id">
		    <input type="hidden" name="modifica" value="Grabar">
			<html:hidden property="id" />			
			<input type="hidden" name="idmicrosite" value='<bean:write name="idmicrosite"/>' />
		</logic:present>
		
		<logic:notPresent name="encuestaForm" property="id">
			<input type="hidden" name="anyade" value="Crear">
			<input type="hidden" name="idmicrosite" value='<bean:write name="idmicrosite"/>' />
		</logic:notPresent>
		
		<div id="formulario">
			<!-- las tablas están entre divs por un bug del FireFox -->
				<table cellpadding="0" cellspacing="0" class="edicio">
		
				<tr class="par">
					<td class="etiqueta"><bean:message key="encuesta.fpublicacion" /></td>
					<td>
						<html:text property="fpublicacion" readonly="readonly" maxlength="16" />
					</td>
					<td class="etiqueta"><bean:message key="encuesta.fcaducidad" /></td>
					<td>
						<html:text property="fcaducidad" readonly="readonly" maxlength="16" />
					</td>
				</tr>
		
				<tr>
					<td class="etiqueta"><bean:message key="encuesta.visible" /></td>
					<td><label><html:radio property="visible" value="S" />&nbsp;Si</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visible" value="N" />&nbsp;No</label></td>
					<td class="etiqueta"><bean:message key="encuesta.mostrar" /></td>
					<td><label><html:radio property="mostrar" value="S" />&nbsp;Si</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="mostrar" value="N" />&nbsp;No</label></td>					
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="encuesta.identificacion" /></td>
					<td><label><html:radio property="identificacion" value="S" />&nbsp;Si</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="identificacion" value="N" />&nbsp;No</label></td>
					<td></td>
					<td></td>
				</tr>
				<tr class="par">
					<td class="etiqueta">&nbsp;</td>
					<td><html:hidden property="paginacion" value="10"/><html:hidden property="indivisible" value="S"/></td>
					<td class="etiqueta">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
		
				<tr>
				<td colspan="4">
					<ul id="submenu">
						<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
							<li<%=(j.intValue()==0?" class='selec'":"")%>><a href="#" onclick="mostrarForm(this);"><bean:message name="lang" /></a></li>
				        </logic:iterate>
					</ul>    
				
				    <logic:iterate id="traducciones" name="encuestaForm" property="traducciones" indexId="i" >
					<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
					
						<table cellpadding="0" cellspacing="0" class="tablaEdicio">
						<tr>
							<td class="etiqueta"><bean:message key="encuesta.titulo" />:</td>
							<td><html:text property="titulo" name="traducciones" styleId="<%="titulo"+i%>" size="100" maxlength="256" indexed="true" /></td>
						</tr>
						<tr>
							<td class="etiqueta"><bean:message key="encuesta.uri" />:</td>
							<td><html:text property="uri" name="traducciones" styleId="<%="uri"+i%>" size="100" maxlength="256" indexed="true" /></td>
							<input type="hidden" name="type" value="eid_uri" />
						</tr>
						</table>
					</div>
				    </logic:iterate>
				</td>
				</tr>
				</table>
		
		
			<!-- ********************************* -->
			<!-- Lista de Preguntas de la encuesta -->
			<!-- ********************************** -->
			
			
		<logic:present name="encuestaForm" property="id">
			
				
				<div style="background:#FFFAF5; margin:40px 50px 40px 50px;">
					<!-- subtitol: preguntes -->
					<h1>
						<span>
						    <bean:message key="encu.campos" />				
						</span>
					</h1>
					<!-- botonera preguntas-->
					<div id="botonera">
						<span class="grup">
							<button type="button" title="<bean:message key="encu.anadirpregunta" />" onclick="nuevapreg('<bean:write name="encuestaForm" property="id"/>');"><img src="imgs/botons/nou.gif" alt="<bean:message key="encu.anadirpregunta" />" /></button> 
							<button type="button" title="<bean:message key="op.6" />" onclick="editar();"><img src="imgs/menu/editar.gif" alt="<bean:message key="op.6" />" /></button> 					
							<button type="button" title="<bean:message key="op.2" />" onclick="borravarios();"><img src="imgs/botons/borrar.gif" alt="<bean:message key="op.2" />" /></button>
						</span>
					</div>			
				
				<logic:empty name="encuestaForm" property="preguntas">	
					<table id="llistatcoses" cellpadding="0" cellspacing="0" class="llistat">
					<tr>
					<td></td>
					</tr>
					</table>
				</logic:empty>	
				<logic:notEmpty name="encuestaForm" property="preguntas">	
					<table id="llistatcoses" cellpadding="0" cellspacing="0" class="llistat">
					<thead>
						<tr>
							<th class="check">&nbsp;</th>
							<th width="60%"><bean:message key="encu.nombrepregunta" /></th>
							<th><bean:message key="pregunta.orden" /></th>
							<th><bean:message key="encu.visible" /></th>
							<th><bean:message key="pregunta.visiblecmp" /></th>
							<th><bean:message key="encu.obligatorio" /></th>
						</tr>
					</thead>
					<tbody>
						<logic:iterate id="i" name="encuestaForm" property="preguntas" indexId="indice">
					    <tr class="<%=((indice.intValue()%2==0) ? "par" : "")%>">
					      	<td class="check">
					        	<html:multibox property="seleccionados" styleClass="radio"> 
					            	<bean:write name="i" property="id"/>
						        </html:multibox>
					    	</td>
					   	    <td>
							   	    <logic:notEmpty name="i" property="traduccion.titulo">
										<bean:write name="i" property="traduccion.titulo" />
									</logic:notEmpty>
									<logic:empty name="i" property="traduccion.titulo">
										[<bean:message key="encu.noetiq" />]
									</logic:empty>
					   	    </td>
						    <td>
					    		<bean:write name="i" property="orden" />
							</td>
						    <td>
					    		<logic:match name="i" property="visible" value="S">Si</logic:match>
								<logic:notMatch name="i" property="visible" value="S">No</logic:notMatch>
							</td>
						    <td>
					    		<logic:match name="i" property="visiblecmp" value="S">Si</logic:match>
								<logic:notMatch name="i" property="visiblecmp" value="S">No</logic:notMatch>
							</td>
							
					  	    <td>
						  	    <logic:match name="i" property="obligatorio" value="S">Si</logic:match>
								<logic:notMatch name="i" property="obligatorio" value="S">No</logic:notMatch>
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



<logic:present name="encuestaForm" property="id">
	var uriBorrar="encuestaEdita.do?modifica=null&";
</logic:present>
<logic:notPresent name="encuestaForm" property="id">
	var uriBorrar="encuestaEdita.do?anyade=null&";
</logic:notPresent>

var uriEdicion="preguntaEdita.do?id=";
var alert1="<bean:message key="encu.pregalert1"/>";
var alert2="<bean:message key="encu.pregalert2"/>";

	function nuevapreg(idencu) {
		document.location.href="preguntaEdita.do?idenc="+idencu;
	}

	function submitForm(){
		var accForm = document.getElementById('accFormularioLista');
		accForm.submit();
	}

	function previsualizar() {
	<logic:present name="encuestaForm" property="id">
		abrirWindow('<bean:message key="url.aplicacion" />encuesta.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&cont=<bean:write name="encuestaForm" property="id"/>&stat=no');
	</logic:present>
	}

	function verStat(idencu) {
		document.location.href="estadisticaenc.do?idenc="+idencu;
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
</script>
<jsp:include page="/moduls/pieControl.jsp"/>