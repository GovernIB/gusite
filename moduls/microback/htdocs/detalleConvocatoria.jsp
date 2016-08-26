<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript"	src="js/jquery/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="js/fillOption.js"></script>
	
	<script>
		<logic:present name="convocatoriaForm" property="id">
			var idEncuesta = <bean:write name="convocatoriaForm" property="encuesta"/>;
			var idResCorreo = <bean:write name="convocatoriaForm" property="resCorreo"/>;
			var idPreConfirmacion = <bean:write name="convocatoriaForm" property="preConfirmacion"/>;
			var idResConfirmacion = <bean:write name="convocatoriaForm" property="resConfirmacion"/>;
		</logic:present>
		<logic:notPresent name="convocatoriaForm" property="id">
			var idEncuesta = 0;
			var idResCorreo = 0;
			var idPreConfirmacion = 0;
			var idResConfirmacion = 0;
		</logic:notPresent>
		
		$(document).ready(inicializarDesplegables);
		function inicializarDesplegables(){
			$("#encuesta").change(chgEncuesta);
			$("#preConfirmacion").change(chgPreConfirmacion);
			fillOption("/sacmicroback/convocatoriaEdita.do","detalleEncuestas", "<bean:write name="MVS_microsite" property="id"/>", "#encuesta", idEncuesta);
		}
		
		function chgEncuesta(){
			fillOption("/sacmicroback/convocatoriaEdita.do","detalleResCorreo", $("#encuesta").val(), "#resCorreo", idResCorreo);
			fillOption("/sacmicroback/convocatoriaEdita.do","detallePreConfirmacion", $("#encuesta").val(), "#preConfirmacion", idPreConfirmacion);										
		}
	
		function chgPreConfirmacion(){
			fillOption("/sacmicroback/convocatoriaEdita.do","detalleResConfirmacion", $("#preConfirmacion").val(), "#resConfirmacion", idResConfirmacion);		
		}
	</script>
</head>

<body>
	<!-- tinyMCE -->
	<script language="javascript" type="text/javascript" src="tinymce/tiny_mce.js"></script>
	<script language="javascript" type="text/javascript">
	
		tinyMCE.init({
			mode : "textareas",
			theme : "advanced",
			plugins: "personalizaMailing",
			theme_advanced_buttons1 : "bold,italic,underline,separator,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,separator,outdent,indent,separator,link,unlink,forecolor,removeformat,cleanup,code,addname,addcognom,addurlencuesta,addbaixa",			
			theme_advanced_buttons2 : "",		
			theme_advanced_buttons3 : "",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_path_location : "bottom",
			fill_content_callback : "mailingCallback",		
			verify_html : true,
			theme_advanced_resizing : false,	
			accessibility_warnings : true,		
			language: "ca"	
		});	
	
		function mailingCallback(type){
			txt = "";
			switch (type) { 
            	case "addurlencuesta":
                	if($('#resCorreo').val()!=""){
	            		txt = "<%=System.getProperty("es.caib.gusite.portal.url")%>/sacmicrofront/encuesta.do?";
	        			txt = txt + "idsite=<bean:write name="MVS_microsite" property="id"/>&";
	        			txt = txt + "cont=" + $('#encuesta').val() + "&resp" + $('#resCorreo').val() + "={correu}";
                	}else{
                    	alert('<bean:message key="mensa.rescorreoconvocatoria"/>')
                    } 
					break;
            	case "addname":
                	txt = " {nom}";
                	break;
            	case "addcognom":
                	txt = " {cognom}";
                	break;
            	case "addbaixa":
            		if($('#resCorreo').val()!=""){
	            		txt = "<%=System.getProperty("es.caib.gusite.portal.url")%>/sacmicrofront/operacioMailing.do?";
	        			txt = txt + "idsite=<bean:write name="MVS_microsite" property="id"/>&";
	        			txt = txt + "accio=baixa&email={correu}";
                	}else{
                    	alert('<bean:message key="mensa.rescorreoconvocatoria"/>')
                    } 
                	break;
			}
			return txt;
		}
	
	</script>
	<!-- /tinyMCE
	<div style="font-weight:bold; color:#FF4400;"> <html:errors/></div> -->



	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>		
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="convocatorias.do"><bean:message key="menu.convocatorias" /></a></li>
		<logic:present name="convocatoriaForm" property="id">
         		<li class="pagActual"><bean:write name="convocatoriaForm" property="nombre" ignore="true" /></li>
	    </logic:present>
	    <logic:notPresent name="convocatoriaForm" property="id">
	         <li class="pagActual"><bean:message key="convocatoria.alta" /></li>
	    </logic:notPresent>
	</ul>
			
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/convocatorias.gif" alt="<bean:message key="menu.editarpagina" />"/>  
		<span>
		    <logic:present name="convocatoriaForm" property="id">
	         	<bean:message key="convocatoria.modificacion" />
		    </logic:present>
		    <logic:notPresent name="convocatoriaForm" property="id">
		        <bean:message key="convocatoria.alta" />
		    </logic:notPresent>
		</span>
	</h1>
	<logic:present name="convocatoriaForm" property="id">
		<html:form action="/escogerDistribConvoc.do" method="get" styleId="chgDistribForm">	
			<html:hidden name="convocatoriaForm" property="id"/>
		</html:form>
	</logic:present>

	<%session.setAttribute("action_path_key",null);%>
	<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
	<jsp:include page="/moduls/mensajes.jsp"/>	
	
	<!-- botonera -->
	<div id="botonera">
		<span class="grup">
			<button type="button" title='<bean:message key="convocatoria.volver"/>' onclick='document.location.href="convocatorias.do";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="formu.volver"/>' />
		   	</button>
		</span>
		<logic:present name="convocatoriaForm" property="id">
			<span class="grup">
				<button type="submit" title="<bean:message key="convocatoria.crear" />" onclick="submitForm('CrearConv');"><img src="imgs/botons/nou.gif" alt="<bean:message key="convocatoria.crear" />" /></button>
			</span>
		</logic:present>
		<span class="grup">
			<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('GuardarConv');">
				<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
			</button>
		</span>
		<logic:present name="convocatoriaForm" property="id">
		 	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('BorrarConv');">
		   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
		    </button>		    
		</logic:present>
	</div>	

	<html:form action="/convocatoriaEdita.do"  method="post" enctype="multipart/form-data"  styleId="accFormulario">
		<!-- las tablas están entre divs por un bug del FireFox -->
		<div id="formulario">
			<input type="hidden" name="espera" value="si" id="espera" />
			<input type="hidden" name="accion" value=""/>	
			<html:hidden name="convocatoriaForm" property="id"/>
					
			<table cellpadding="3" cellspacing="0" class="edicio">
				<tr class="par">
					<td class="etiqueta"><bean:message key="convocatoria.datos"/></td>
					<td colspan="4"></td>
				</tr>
				<tr>
					<td width="30%" class="etiqueta" width="30%"><bean:message key="convocatoria.nombre" /></td>
					<td width="10%"><html:text property="nombre" size="15" maxlength="255"/></td>
					<td width="10%" class="etiqueta"><bean:message key="convocatoria.descripcion" /></td>
					<td width="50%"><html:text property="descripcion" size="60" maxlength="1000"/></td>
					<td/>
				</tr>
				<tr>
					<td class="etiqueta">
						<bean:message key="convocatoria.envioerror" />
					</td>
					<td>
						<html:radio property="envioError" value="S"/>&nbsp;Sí­&nbsp;
						<html:radio property="envioError" value="N" />&nbsp;No
					</td>
					<td class="etiqueta">
						
					</td>
					<td>
						
					</td>
					<td/>				
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="convocatoria.ultimoEnvio" />:</td>
					<td colspan="3">
						<logic:present name="convocatoriaForm" property="id">
							<bean:write name="convocatoriaForm" property="ultimoEnvio" />
						</logic:present>
					</td>
					<td/>
				</tr>
				<tr class="par">
					<td class="etiqueta"><bean:message key="convocatoria.preguntas" /></td>
					<td colspan="4" ></td>				
				</tr>			
				<tr>
					<td class="etiqueta"><bean:message key="convocatoria.encuesta" /></td>
					<td colspan="3">
						<html:select property="encuesta" styleId="encuesta"/>										
						<br/>
						<bean:message key="convocatoria.elegible"/>
					</td>
					<td>
						<button type="button" title="<bean:message key="boton.seleccionar" />" 
								onclick="window.open('<%=System.getProperty("es.caib.gusite.portal.url")%>/sacmicrofront/encuesta.do?idsite=<bean:write name="MVS_microsite" property="id"/>&cont=' + $('#encuesta').val(),'ventana','width=700,height=540');">
							<img alt="<bean:message key="boton.seleccionar" />" src="imgs/botons/cercar.gif"/>
						</button>
					</td>
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="convocatoria.respuestaCorreo" /></td>
					<td colspan="3">
						<html:select property="resCorreo" styleId="resCorreo"/>					
					</td>
					<td/>				
				</tr>			
				<tr>
					<td class="etiqueta">
						<bean:message key="convocatoria.preguntaConfirmacion" />
					</td>
					<td colspan="3">
						<html:select property="preConfirmacion" styleId="preConfirmacion"/>					
					</td>
					<td/>				
				</tr>
				<tr>
					<td class="etiqueta">
						<bean:message key="convocatoria.respuestaConfirmacion" />
					</td>
					<td colspan="3">
						<html:select property="resConfirmacion" styleId="resConfirmacion"/>
					</td>
					<td/>				
				</tr>
				<tr class="par">
					<td class="etiqueta">
						<bean:message key="convocatoria.mailing" />
					</td>
					<td colspan="4" ></td>				
				</tr>
				<tr>
					<td class="etiqueta">
						<bean:message key="convocatoria.llistes" />
					</td>
					<td colspan="3">
						<logic:iterate id="i" name="convocatoriaForm" property="destinatarios">&lt<bean:write name="i" property="nombre"/>&gt </logic:iterate>
					</td>
					<td>										
						<button type="button" title="<bean:message key="boton.seleccionar" />" onclick="escogerDistrib();">
							<img alt="<bean:message key="boton.seleccionar" />" src="imgs/botons/cercar.gif"/>
						</button>
					</td>								
				</tr>
				<tr>
					<td class="etiqueta">
						<bean:message key="convocatoria.destinatarios" />
					</td>
					<td colspan="3">
						<html:text property="otrosDestinatarios" size="100" maxlength="200"/>
					</td>
					<td/>
				</tr>
				<tr>
					<td class="etiqueta"><bean:message key="convocatoria.asunto"/></td>
					<td colspan="3">
						<html:text property="txtAsunto" size="100" maxlength="200"/>
					</td>
					<td/>
				</tr>
				<tr>
					<td class="etiqueta" valign="top">
						<bean:message key="convocatoria.texte"/>
					</td>
					<td colspan="3">
						<html:textarea  property="txtMensaje" rows="5" cols="50" style="width:700px; height:300px;"/>
					</td>
					<td/>
				</tr>			
			</table>				
		</div>
	
	</html:form>

<script>
	function submitForm(nom_accio){
		var strError = "";
		var accForm = document.getElementById('accFormulario');
		accForm.accion.value= nom_accio;	
		
		if (accForm.nombre.value == ""){
			strError = strError + "<bean:message key="mensa.nombreconvocatoria"/>\n";
		}
		if (accForm.encuesta.value == ""){
			strError = strError + "<bean:message key="mensa.encuestaconvocatoria"/>\n";
		}
		if (accForm.resCorreo.value == ""){
			strError = strError + "<bean:message key="mensa.rescorreoconvocatoria"/>\n";
		}
		if (accForm.preConfirmacion.value == ""){
			strError = strError + "<bean:message key="mensa.preconfirmacionconvocatoria"/>\n";
		}
		if (accForm.resConfirmacion.value == ""){
			strError = strError + "<bean:message key="mensa.resconfirmacionconvocatoria"/>\n";
		}
		if (!validar(accForm.otrosDestinatarios.value)){ 
			strError = strError + "<bean:message key="mensa.altresDestinataris"/>\n";
		}
		if (accForm.txtAsunto.value == ""){
			strError = strError + "<bean:message key="mensa.txtasuntoconvocatoria"/>\n";
		}
		if (strError ==""){
			accForm.submit();
		}else{
			alert(strError);
		}
	}

	function escogerDistrib(){
		<logic:present name="convocatoriaForm" property="id">
			var distribForm = document.getElementById('chgDistribForm');
			distribForm.submit();
		</logic:present>
		<logic:notPresent name="convocatoriaForm" property="id">
			alert("<bean:message key="convocatoria.alert3"/>");
		</logic:notPresent>
	}

	function validar(listaCorreos){
		if (/^(\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+(,\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+)*){0,1}$/.test(listaCorreos))
			return true;
		else
			return false;
	}
</script>

</body>
</html>

