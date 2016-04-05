<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="es.caib.gusite.micromodel.Accesibilidad"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
	<script type="text/javascript" src="js/rArxius.js"></script>
	<script type="text/javascript"	src="js/jquery/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="js/checkUri.js"></script>
</head>

<body>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.contenidos" /></li>
		<li><a href="menuEdita.do"><bean:message key="menu.arbol" /></a></li>
		<li class="pagActual"><bean:write name="migapan" /></li>
	</ul>

	<!-- titol pagina -->
	<h1><img src="imgs/titulos/contenido.gif" alt="<bean:message key="menu.editarpagina" />" />
	<bean:message key="menu.editarpagina" />. 
		<span>
		    <logic:present name="contenidoForm" property="id">
	         		<bean:message key="menu.paginacontenidos" />
		    </logic:present>		
		    <logic:notPresent name="contenidoForm" property="id">
		         <bean:message key="menu.paginacontenidosalta" />
		    </logic:notPresent>					
		</span>
	</h1>


<!-- tinyMCE -->
<script language="javascript" type="text/javascript" src="tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
<script language="javascript" type="text/javascript">
	tinyMCE.init({
		mode : "textareas",
		theme : "advanced",
		plugins : "media ,style,layer,table,advhr,advimage,advlink,iespell,searchreplace,print,contextmenu,paste,directionality,fullscreen,tipoarchivos,insertararchivos,template,componentesmicros",
		//plugins : "media ,style,layer,table,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,searchreplace,print,contextmenu,paste,directionality,fullscreen,tipoarchivos,insertararchivos,template,componentesmicros",
		theme_advanced_buttons1_add_before : "newdocument,separator,insertararchivos,tipoarchivos,componentesmicros,template,separator",
		theme_advanced_buttons2_add : "",
		//theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview",
		theme_advanced_buttons2_add_before: "cut,copy,paste,pastetext,pasteword,separator,search,replace,separator",
		theme_advanced_buttons3_add_before : "tablecontrols,separator,forecolor,backcolor,separator",
		//theme_advanced_buttons3_add : "emotions,flash,advhr,separator,fullscreen",
		theme_advanced_buttons3_add : "media,advhr,separator,fullscreen",
		theme_advanced_buttons4 : "",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_path_location : "bottom",
		verify_html : false,
		content_css : "<bean:write name="MVS_css_tiny" filter="false" ignore="true"/>",
		plugin_insertdate_dateFormat : "%d/%m/%Y",
		plugin_insertdate_timeFormat : "%H:%M:%S",
		extended_valid_elements : "hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]",
		onpageload  : "initCaib",
		template_external_list_url : "tinymce/jscripts/tiny_mce/microsites_template_list.js",
		file_browser_callback : "fileBrowserCallBack",
		theme_advanced_resizing : false,
		nonbreaking_force_tab : true,
		apply_source_formatting : true,		
		convert_urls: false,			
		relative_urls: true,		
		language: "ca"
		<logic:present name="contenidoForm" property="id">		
			,idform:<bean:write name="contenidoForm" property="id"/>
		</logic:present>
	});
	
	function initCaib() {		
		tinyMCE.addI18n('ca.advanced',{
			h1:"Títol",
			h2:"Subtítol nivell 1",
			h3:"Subtítol nivell 2",
			h4:"Subtítol nivell 3",
			h5:"Subtítol nivell 4",
			h6:"Subtítol nivell 5"
		});	
				
	}
	
   	var Rcajatemp_tiny;
   	var Rwin_tiny;
	
	function Rmeterurl_tiny(laurl) {
		Rwin_tiny.document.forms[0].elements[Rcajatemp_tiny].value = laurl;
	}	

	// Método que recoge las variables de la ventana que la llama y abre un popup con el listado de recursos url.
	// Ãste método es la implementación personalizada del tiny
	function fileBrowserCallBack(field_name, url, type, win) {
		Rcajatemp_tiny=field_name;
		Rwin_tiny=win;
		<logic:notEmpty name="contenidoForm">
			window.open("/sacmicroback/recursos.do?tiny=true&id=<bean:write name='contenidoForm' property='id'/>&idMenu=<bean:write name='contenidoForm' property='idMenu'/>",'recursos','scrollbars=yes,width=700,height=400');
		</logic:notEmpty>
		<logic:empty name="contenidoForm">
			window.open("/sacmicroback/recursos.do?tiny=true",'recursos','scrollbars=yes,width=700,height=400');
		</logic:empty>
	}
	
	var valor="";


    <logic:present name="contenidoForm" property="id">
	
	function eliminarContenido(alert) {
		if(confirm(alert)) document.location.href="contenidoEdita.do?accion=Esborrar&id=<bean:write name='contenidoForm' property='id'/>";
		else 				return false;
	}

	</logic:present>
	
	
</script>

<!-- /tinyMCE -->

<div id="formulario">

		<!-- las tablas están entre divs por un bug del FireFox -->

		<table cellpadding="0" cellspacing="0" class="edicio">
		
<%session.setAttribute("action_path_key",null);%>

		<html:form action="/contenidoEdita.do" method="POST" enctype="multipart/form-data"  styleId="accFormulario">

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
		
		<input type="hidden" name="accion" value=""/>
		<div id="botonera">
		   	<span class="grup">
		   	<button type="button" title='<bean:message key="mensa.volverarbol.titol"/>' onclick='document.location.href="menuEdita.do";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="mensa.volverarbol.titol"/>' />
		   </button> 
		   </span>
		   <logic:present name="contenidoForm" property="id">
		   <button type="button" title='<bean:message key="menu.botonnuevocontenido" />' onclick='document.location.href="contenidosAcc.do?op=crear&idmenu=<bean:write name="contenidoForm" property="idMenu"/>";'>
		   		<img src="imgs/botons/nuevaPagina.gif" alt='<bean:message key="menu.botonnuevocontenido" />' />
		   </button>	   
		  <logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
		   <button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
		   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
		   </button>
		   </logic:equal>
		   </logic:present>	   
		   <button type="submit" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
		   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
		   	</button>
		   	<logic:present name="contenidoForm" property="id">
		   	<span class="grup">
		   	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('Borrar');">
		   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
		   </button> 
		   </span>
		   </logic:present>
		</div>

		<input type="hidden" name="espera" value="si" id="espera" />

	 	<logic:present name="contenidoForm" property="id">
	 		<input type="hidden" name="modifica" />
        	<html:hidden property="id" />
			<html:hidden property="idMenu" />
     	</logic:present>
     
     	<logic:notPresent name="contenidoForm" property="id">
	     	<input type="hidden" name="anyade" />
			<input type="hidden" name="idMenu" value='<bean:write name="menu"/>' />
     	</logic:notPresent>
     	
     	<logic:present name="contenidoForm" property="id">
	 		<input type="hidden" name="modifica" />
        	<html:hidden property="id" />
			<input type="hidden" name="idmicrosite" value='<bean:write name="idmicrosite"/>' />
     	</logic:present>
     
     	<logic:notPresent name="contenidoForm" property="id">
	     	<input type="hidden" name="anyade" />
			<input type="hidden" name="idmicrosite" value='<bean:write name="idmicrosite"/>' />
     	</logic:notPresent>

		<tr class="par">
			<td class="etiqueta"><bean:message key="conte.fpublicacion" /></td>
			<td>
				<html:text property="fpublicacion" readonly="readonly" maxlength="16" />
			</td>
			<td class="etiqueta"><bean:message key="conte.fcaducidad" /></td>
			<td>
				<html:text property="fcaducidad" readonly="readonly" maxlength="16" />
			</td>
		</tr>

		<tr>
			<td class="etiqueta"><bean:message key="conte.visible" /></td>
			<td><label><html:radio property="visible" value="S" />&nbsp;Sí­&nbsp;&nbsp;&nbsp;</label><label><html:radio property="visible" value="N" />&nbsp;No</label></td>
			<td class="etiqueta"><bean:message key="conte.orden" /></td>
			<td>
				<html:text property="orden" maxlength="4" size="8" />
			</td>
		</tr>

		<tr>
		<td colspan="4">

			<ul id="submenu">
				<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
					<li<%=(j.intValue()==0?" class='selec'":"")%>>
						<a href="#" onclick="mostrarForm(this);">
						<bean:message name="lang" />						
						<%
						Accesibilidad acceLang = (Accesibilidad)request.getAttribute("MVS_w3c_" + j);											
						if (acceLang!=null) out.println("<b><i>(amb errors)</i></b>");
						%>							
						</a>			
					</li>
        		</logic:iterate>
			</ul>    

    		<logic:iterate id="traducciones" name="contenidoForm" property="traducciones" indexId="i" >
			<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:true;":"display:none;")%>">
	
				<table cellpadding="0" cellspacing="0" class="tablaEdicio">
					<tr>
						<td class="etiqueta"><bean:message key="conte.titulo" />:</td>
						<td><html:text property="titulo" name="traducciones" styleId="<%="titulo"+i%>" size="50" maxlength="256" indexed="true" /></td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="conte.url" />:</td>
						<td><html:text property="url" name="traducciones" styleId="<%="url"+i%>" size="60" maxlength="512" indexed="true" onchange="javascript:controlaEditor();"/>
						&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].url','microManamegedURL<%=i%>');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button>
						&nbsp; &nbsp;<button type="button" title="Informació" onclick='alert("<bean:message key="conte.aviso.url" />");'><img src="imgs/botons/info.gif" alt="<bean:message key="conte.aviso.url" />" /></button> 
						
						</td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="conte.uri" />:</td>
						<td><html:text property="uri" name="traducciones" styleId="<%="uri"+i%>" size="60" maxlength="512" indexed="true"/>
						&nbsp;
						</td>
						<input type="hidden" name="type" value="cid_uri" />
					</tr>
					
				</table>
				
				<table id="tinymceEditor<%=i%>" cellpadding="0" cellspacing="0" class="tablaEdicio">
					<tr>
						<td class="etiqueta"><bean:message key="conte.textoalfa" />
						<p><button type="button" title='<bean:message key="conte.visualizaalfa"/>' onclick="abrir('<bean:message key="url.aplicacion" />contenido.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="contenidoForm" property="id"/>&stat=no&tipo=alfa&previsual', '', 700, 500);"><img src="imgs/botons/previsualitzar.gif" alt='<bean:message key="conte.visualizaalfa"/>' /></button></p>
						<p>
				<%
					Accesibilidad acce = (Accesibilidad)request.getAttribute("MVS_w3c_" + i);				
					if (acce!=null) out.println("<button type=\"button\" title='Errors dÂ´accessibilitat' onclick=\"document.location='visorw3c.do?id=" + acce.getId() + "';\"><img src=\"imgs/botons/taww3cButton.gif\" alt='Errors dÂ´accessibilitat' /></button>");
				%>						
						</p>
						</td>
						<td>
						<html:textarea property="texto" name="traducciones" rows="5" cols="50" indexed="true"  style="width:700px; height:300px;" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<logic:present name="contenidoForm" property="id">
								<table id="mceTextoBeta_alt<%=i%>">
								<tr>
									<td class="etiqueta">&nbsp;</td>
									<td>
											<bean:message key="conte.visualizarbeta"/>&nbsp;&nbsp;
											<button type="button" name="cambioaBeta" onclick='visualizaPrototipo();'><img src="imgs/botons/copias.gif" alt="" /></button> &nbsp; 
									</td>
								</tr>
								</table>

								<table id="mceTextoNoBeta_alt<%=i%>">
								<tr>
									<td class="etiqueta">&nbsp;</td>
									<td>
											<bean:message key="conte.ocultarbeta"/>&nbsp;&nbsp;
											<button type="button" name="cambioaNoBeta" onclick='escondePrototipo();'><img src="imgs/botons/copias.gif" alt="" /></button> &nbsp; 
									</td>
								</tr>
								</table>

								<table id="mceBotonesBeta<%=i%>">
								<tr>
									<td class="etiqueta">&nbsp;</td>
									<td>
										<center>			
										<button type="button" name="botonP[<%=i%>]" class="alpha_beta" title='<bean:message key="conte.publicar" />' onclick='publica("<%=i%>")'><bean:message key="conte.publica" /><br /><img src="imgs/botons/betaToAlfa.gif" alt="" /><br /><bean:message key="conte.prototipo" /></button> &nbsp; &nbsp; 
										<button type="button" name="botonI[<%=i%>]" class="alpha_beta" title='<bean:message key="conte.intercambiar" />' onclick='intercambia("<%=i%>")'><bean:message key="conte.publica" /><br /><img src="imgs/botons/alfaBeta.gif" alt="" /><br /><bean:message key="conte.prototipo" /></button> &nbsp; &nbsp; 
										<button type="button" name="botonC[<%=i%>]" class="alpha_beta" title='<bean:message key="conte.copiar" />' onclick='copia("<%=i%>")'><bean:message key="conte.publica" /><br /><img src="imgs/botons/alfaToBeta.gif" alt="" /><br /><bean:message key="conte.prototipo" /></button>
										</center>
									</td>
								</tr>
								</table>

								<table id="mceTextoBeta<%=i%>">
								<tr>
									<td class="etiqueta"><bean:message key="conte.textobeta" />:
									<p><button type="button" title='<bean:message key="conte.visualizabeta"/>' onclick="abrir('<bean:message key="url.aplicacion" />contenido.do?lang=ca&idsite=<bean:write name="MVS_microsite" property="id"/>&amp;cont=<bean:write name="contenidoForm" property="id"/>&stat=no&tipo=beta&previsual', '', 700, 500);"><img src="imgs/botons/previsualitzar.gif" alt='<bean:message key="conte.visualizabeta"/>' /></button></p>
									</td>
									<td>
									<html:textarea property="txbeta" name="traducciones" rows="5" cols="50" indexed="true"   style="width:700px; height:300px;" />
									</td>
								</tr>
								</table>
							</logic:present>
						</td>
					</tr>
				</table>

				<table id="tinymceEditor_alt<%=i%>" cellpadding="0" cellspacing="0" class="tablaEdicio">
					<tr>
						<td class="etiqueta">&nbsp;</td>
						<td>
							<br/><div id="microManamegedURL<%=i%>" style="font-weight:bold; align:left; color:#8a4700; "><bean:write name="MVS_HS_URL_migapan" property="<%=""+i%>" ignore="true"/></div><br/>
							<em><bean:message key="mensa.editor" /></em>
						</td>
					</tr>
				</table>
			</div>
    		</logic:iterate>

		</td>
		</tr>
		</html:form>
		
		<!-- ********************************* -->
		<!-- Lista de documentos del contenido -->
		<!-- ********************************* -->
		
	<logic:present name="contenidoForm" property="id">
		<tr id="archivos">
		
		<td colspan="4">
					
		<html:form action="/docsEdita.do" enctype="multipart/form-data" >
		
		<input type="hidden" name="idPagina" value='<bean:write name="contenidoForm" property="id"/>'>
			
		<p><strong><bean:message key="conte.archidispo" /></strong> 
		<!-- botonera -->
		<div id="botonera">
			<span class="grup">
				<button type="button" title='<bean:message key="conte.nuevoarchi"/>' onclick="mostarGuardarArxiu('divGuardarArxiu','nou');"><img src="imgs/botons/nou.gif" alt='<bean:message key="conte.nuevoarchi"/>' /></button> 
				<button type="button" title='<bean:message key="boton.modif"/>' onclick="mostarGuardarArxiu('divGuardarArxiu','modificar');"><img src="imgs/botons/modificar.gif" alt='<bean:message key="boton.modif"/>' /></button>
			</span>
			<!-- 
			<span class="grup">
				<button type="button" title='<bean:message key="op.11" />' onclick="incluir();"><img src="imgs/icones/07incloure.gif" alt='<bean:message key="op.11" />' /></button> 
			</span>
			 -->
			<button type="button" title='<bean:message key="boton.ver"/>' onclick="verArchivo();"><img src="imgs/botons/ver.gif" alt='<bean:message key="boton.ver"/>' /></button> 
			<button name="esborrar" type="button" title='<bean:message key="op.2"/>' onclick=" borrar();" ><img src="imgs/botons/borrar.gif" alt='<bean:message key="op.2"/>' /></button>
		
			<div id="divGuardarArxiu">
				<h2><bean:message key="conte.nuevoarchi"/></h2>
				<p><bean:message key="conte.nuevoarchimensa"/></p>
				<html:hidden property="id" value="0"/>
				<html:file property="archi" size="50"/>
				<div class="botonera">
					<button name="subirficheros" type="button" title='<bean:message key="op.15"/>' onclick="guardar()"><img src="imgs/botons/guardar.gif" alt='<bean:message key="op.15"/>' /> &nbsp;<bean:message key="op.15"/></button> 
					&nbsp; &nbsp; <button type="button" title='<bean:message key="boton.cerrar"/>' onclick="formEsconder();"><img src="imgs/botons/cerrar.gif" alt='<bean:message key="boton.cerrar"/>' /></button>
				</div>
				<html:hidden property="operacion" value=""/>
			</div>
		</div>

			<logic:notEmpty name="listaDocs">
			<table cellpadding="0" cellspacing="0" class="llistat">
			<thead>
				<tr>
					<th class="check"></th>
					<th><bean:message key="conte.tipoarchi" /></th>
					<th><bean:message key="conte.nombrearchi" /></th>
					<th><bean:message key="conte.pesoarchi" /></th>
					<th><bean:message key="conte.url" /></th>
				</tr>
			</thead>	
			<tbody id="listadoArchivos">
			
			<logic:iterate id="i" name="listaDocs" indexId="indice">
			    <tr>
			    <td class="check">

    				<bean:define id="tipomime" name="i" property="mime" />
					<% String tipomime2 = (String)tipomime; tipomime2=tipomime2.toLowerCase(); %>
					<% if (tipomime2.indexOf("image")!=-1) { %>
				    	<input name="seleccionados" onclick="incluirImg('<bean:write name="i" property="id"/>','<bean:write name="i" property="nombre"/>');" id="c<bean:write name="i" property="id"/>" type="checkbox" value="<bean:write name="i" property="id"/>" />
				    <% } else { %>
		    			<input name="seleccionados" onclick="incluirDoc('<bean:write name="i" property="id"/>','<bean:write name="i" property="nombre"/>','<%=tipomime2%>');" id="c<bean:write name="i" property="id"/>" type="checkbox" value="<bean:write name="i" property="id"/>" />
				    <% } %>
			    	
			    </td>
				<bean:define id="icono" value="stream.gif"/>
				<logic:match name="i" property="mime" value="EXCEL"><bean:define id="icono" value="xls.gif"/></logic:match>
				<logic:match name="i" property="mime" value="POWERPOINT"><bean:define id="icono" value="powerpoint.gif"/></logic:match>							    
				<logic:match name="i" property="mime" value="ZIP"><bean:define id="icono" value="zip.gif"/></logic:match>							    							    
				<logic:match name="i" property="mime" value="WORD"><bean:define id="icono" value="word.gif"/></logic:match>							    							    
	    		<logic:match name="i" property="mime" value="RTF"><bean:define id="icono" value="word.gif"/></logic:match>							    							      							    
	    		<logic:match name="i" property="mime" value="PDF"><bean:define id="icono" value="pdf.gif"/></logic:match>							    							    
	    		<logic:match name="i" property="mime" value="PLAIN"><bean:define id="icono" value="txt.gif"/></logic:match>							    							    
	    		<logic:match name="i" property="mime" value="HTM"><bean:define id="icono" value="html.gif"/></logic:match>
	    		<logic:match name="i" property="mime" value="CSS"><bean:define id="icono" value="css.gif"/></logic:match>
	    		<logic:match name="i" property="mime" value="AUDIO"><bean:define id="icono" value="media.gif"/></logic:match>
				<logic:match name="i" property="mime" value="MEDIA"><bean:define id="icono" value="media.gif"/></logic:match>
	    		<logic:match name="i" property="mime" value="VIDEO"><bean:define id="icono" value="media.gif"/></logic:match>
	    		<logic:match name="i" property="mime" value="IMAGE"><bean:define id="icono" value="image.gif"/></logic:match>							    
				<logic:match name="i" property="mime" value="FLASH"><bean:define id="icono" value="flash.gif"/></logic:match>		
				<logic:match name="i" property="mime" value="OPENDOCUMENT"><bean:define id="icono" value="odt.gif"/></logic:match>		
				<logic:match name="i" property="mime" value="SUN.XML.WRITER"><bean:define id="icono" value="odt.gif"/></logic:match>		
				<logic:match name="i" property="mime" value="STARDIVISION"><bean:define id="icono" value="odt.gif"/></logic:match>				
				
				<td><img src='imgs/arxius/<bean:write name="icono"/>' alt='<bean:write name="i" property="mime"/>' /></td>
				<td><bean:write name="i" property="nombre"/></td>
				<td><bean:write name="i" property="peso"/>&nbsp;Kb</td>
				<td>archivopub.do?ctrl=MCRST<bean:write name="MVS_microsite" property="id"/>ZI<bean:write name="i" property="id"/>&id=<bean:write name="i" property="id"/></td>
				</tr>
			</logic:iterate>
			</tbody>
		    </table>
			</logic:notEmpty>
	
		</html:form>
	
	
		</td>
		</tr>
	</logic:present>		

	</table>


</div>


</body>
</html>


<script type="text/javascript">
<!--

var alert1='<bean:message key="conte.alertamasdeuno"/>';
var alert2='<bean:message key="conte.alertaelegirarchi"/>';
var alert3='<bean:message key="conte.nuevoarchi"/>';
var alert4='<bean:message key="conte.alertaborrararchivos"/>';
var mensa1='<bean:message key="conte.modifarchi"/>';
var mensa2='<bean:message key="conte.alertamodifarchi"/>';
var mensa3='<bean:message key="conte.nuevoarchimensa"/>';

<logic:notEmpty name="listaDocs">
	addEvent(window,'load',iniciarMenu,true);
</logic:notEmpty>

	// Intercambia ALFA y BETA
	function intercambia (idioma) {
		var txtalfa, txtbeta, txttmp;

		if (confirm('<bean:message key="conte.mensaintercambio" />')) {
			txtalfa = tinyMCE.get('traducciones['+idioma+'].texto').getBody().innerHTML;
			txtbeta = tinyMCE.get('traducciones['+idioma+'].txbeta').getBody().innerHTML;

			txttmp =txtalfa;
			txtalfa=txtbeta;
			txtbeta=txttmp;
		
			tinyMCE.get('traducciones['+idioma+'].texto').getBody().innerHTML = txtalfa;
			tinyMCE.get('traducciones['+idioma+'].txbeta').getBody().innerHTML = txtbeta;
			tinyMCE.triggerSave(); 
		}
	}
	
	// Copia ALFA en BETA
	function copia (idioma) {
	var txtalfa;
		if (confirm('<bean:message key="conte.mensacopia" />')) {
			txtalfa = tinyMCE.get('traducciones['+idioma+'].texto').getBody().innerHTML;
			tinyMCE.get('traducciones['+idioma+'].txbeta').getBody().innerHTML = txtalfa;
			tinyMCE.triggerSave(); 
		}
	}
	
	// Copia BETA en ALFA
	function publica (idioma) {
	var txtbeta;
		if (confirm('<bean:message key="conte.mensacopia1" />')) {
			txtbeta = tinyMCE.getInstanceById('traducciones['+idioma+'].txbeta').getBody().innerHTML;
			tinyMCE.getInstanceById('traducciones['+idioma+'].texto').getBody().innerHTML = txtbeta;
			tinyMCE.triggerSave(); 
		}
	}
	

    var Rcajatemp;
    var divContenedor;
    function Rpopupurl(obj,divcontenedor) {
    	divContenedor=document.getElementById(divcontenedor);
    	Rcajatemp=document.contenidoForm[obj];
    	<logic:notEmpty name="contenidoForm">
			window.open("/sacmicroback/recursos.do?id=<bean:write name='contenidoForm' property='id'/>&idMenu=<bean:write name='contenidoForm' property='idMenu'/>",'recursos','scrollbars=yes,width=700,height=400');
		</logic:notEmpty>
		<logic:empty name="contenidoForm">
			window.open("/sacmicroback/recursos.do",'recursos','scrollbars=yes,width=700,height=400');
		</logic:empty>
    }
	
	function Rmeterurl(laurl) {
		divContenedor.innerHTML="";
		Rcajatemp.value=laurl;
	}	
	
	function incluir() {
	
		comprobarSeleccionado();
		if(inputsSel.length > 1) {
			alert(alert1);
			return false;
		} else if(inputsSel.length == 0) {
			alert(alert2);
			return false;
		}
		
		inputsSel[0].onclick();
		
	}
	
	function incluirDoc(id, nom, mime) {

		var url = "<img src=\"imgs/archivos_prev/";
		if(mime=="application/msword" || mime=="application/rtf") url+="doc.gif";
		else if(mime=="application/pdf" || mime=="application/x-pdf") url+="pdf.gif";
		else if(mime=="application/vnd.ms-powerpoint" || mime=="application/powerpoint") url+="ppt.gif";
		else if(mime=="application/vnd.ms-excel" || mime=="application/x-msexcel" || mime=="application/ms-excel" || mime=="application/msexcel" || mime=="application/x-excel") url+="xls.gif";
		else if(mime=="application/zip" || mime=="application/x-gtar" || mime=="application/x-tar") url+="zip.gif";
		else if(mime=="text/plain") url+="txt.gif";
		else if(mime=="text/html") url+="htm.gif";
		else if(mime.indexOf("audio")!=-1 || mime.indexOf("video")!=-1) url+="media.gif";
		else if(mime=="application/vnd.sun.xml.writer" || mime=="application/vnd.oasis.opendocument.text" || mime=="application/vnd.stardivision.writer") url+="odt.gif";
		else url+="file.gif";
		
		url+="\">";

		<logic:present name="contenidoForm" property="id">
				url+="<a href=\"archivopub.do?ctrl=CNTSP";
				url+=document.contenidoForm.id.value+"ZI"+id+"&id="+id+"\">"+nom+"</a>";		
		</logic:present>
		<logic:notPresent name="contenidoForm" property="id">
				url+="<a href=\"archivopub.do?ctrl=MCRST";
				url+="<bean:write name="MVS_microsite" property="id"/>ZI"+id+"&id="+id+"\">"+nom+"</a>";		
		</logic:notPresent>

		var orden;
		
		<logic:iterate id="traducciones" name="contenidoForm" property="traducciones" indexId="i" >
			if(document.getElementById("capa_tabla<%=i%>").style.display!="none") {tinyMCE.execInstanceCommand("mce_editor_<%=i%>", "mceFocus");orden="<%=i%>";}
		</logic:iterate>
		
		if(document.getElementById("tinymceEditor"+orden).style.display!="none") {tinyMCE.execCommand('mceInsertContent',false,url);}
	}

	function incluirImg(id, nom) {	
	    
		<logic:present name="contenidoForm" property="id">
			var url="<img src=\"archivopub.do?ctrl=CNTSP";
			url+=document.contenidoForm.id.value+"ZI"+id+"&id="+id+"\">";
		</logic:present>
		<logic:notPresent name="contenidoForm" property="id">
			var url="<img src=\"archivopub.do?ctrl=MCRST";
			url+="<bean:write name="MVS_microsite" property="id"/>ZI"+id+"&id="+id+"\">";
		</logic:notPresent>
		
		var orden;
		
		<logic:iterate id="traducciones" name="contenidoForm" property="traducciones" indexId="i" >
			if(document.getElementById("capa_tabla<%=i%>").style.display!="none") {tinyMCE.execInstanceCommand("mce_editor_<%=i%>", "mceFocus");orden="<%=i%>";}
		</logic:iterate>
		
		if(document.getElementById("tinymceEditor"+orden).style.display!="none") {tinyMCE.execCommand('mceInsertContent',false,url);}
	}

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

	function guardar() {
		
		tinyMCE.triggerSave(); 
		if (check_cambio()!=valor)
			if (!confirm('<bean:message key="conte.alert5"/>')) return;
		
		if (document.docsForm.id.value=='0')  document.docsForm.operacion.value='crear';
		else  document.docsForm.operacion.value='modificar';
		document.docsForm.submit();

	}
	
	function borrar() {

		tinyMCE.triggerSave(); 
		if (check_cambio()!=valor)
			if (!confirm('<bean:message key="conte.alert4"/>')) return;

		if (!esborrarArxiu()) return;
		document.docsForm.operacion.value='borrar';
		document.docsForm.submit();
	}

	
	//control de los selects
	function comprueba(obj) {
		objs = document.getElementsByTagName("select");
		for (var i=0;i<objs.length;i++)
		{
			if(obj!=objs[i] && obj.selectedIndex!=0) objs[i].selectedIndex=0;
		}
	}
	
	window.onload=controlaEditor;
	
	//muestra/esconde el editor
	function controlaEditor() {
		var obj;var valor;var obj2;var obj3;
		
		<logic:iterate id="traducciones" name="contenidoForm" property="traducciones" indexId="i" >
	 		obj = document.getElementById("url<%=i%>");
			valor = obj.value;
			obj2 = document.getElementById("tinymceEditor<%=i%>");
			obj3 = document.getElementById("tinymceEditor_alt<%=i%>");			
			if (valor.length>0) {obj2.style.display="none";obj3.style.display="block";}
			else {obj2.style.display="block";obj3.style.display="none";}
		</logic:iterate>

	}


	function check_cambio() {
		
		var ret="";
		
		var inputs = document.forms[0].getElementsByTagName('input');
		var textarea = document.forms[0].getElementsByTagName('textarea');
     	
     	for(var i =0;i< inputs.length;i++) 	if (inputs[i].type=='text')	ret+=inputs[i].value;
     	
     	for(var i =0;i< textarea.length;i++) ret+=textarea[i].value;

		return ret;

	}

	valor=check_cambio();

	   <logic:present name="contenidoForm" property="id">
		escondePrototipo();
	   </logic:present>		

	
	function visualizaPrototipo() {
		//función que visualiza lo del prototipo y esconde el texto explicativo
				
		<logic:iterate id="traducciones" name="contenidoForm" property="traducciones" indexId="i" >
			obj1 = document.getElementById("mceTextoBeta_alt<%=i%>");
			obj2 = document.getElementById("mceTextoNoBeta_alt<%=i%>");		
			obj3 = document.getElementById("mceBotonesBeta<%=i%>");
			obj4 = document.getElementById("mceTextoBeta<%=i%>");		
			obj1.style.display="none";
			obj2.style.display="block";
			obj3.style.display="block";
			obj4.style.display="block";
		</logic:iterate>
	}
	
	function escondePrototipo() {
		//función que esconde lo del prototipo y visualiza el texto explicativo
				
		<logic:iterate id="traducciones" name="contenidoForm" property="traducciones" indexId="i" >
			obj1 = document.getElementById("mceTextoBeta_alt<%=i%>");
			obj2 = document.getElementById("mceTextoNoBeta_alt<%=i%>");		
			obj3 = document.getElementById("mceBotonesBeta<%=i%>");
			obj4 = document.getElementById("mceTextoBeta<%=i%>");		
			obj1.style.display="block";
			obj2.style.display="none";
			obj3.style.display="none";
			obj4.style.display="none";
		</logic:iterate>
	}
	
	

// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
