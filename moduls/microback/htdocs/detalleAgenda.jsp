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
</head>

<body>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="agendas.do"><bean:message key="agenda.agenda" /></a></li>
	    <logic:present name="agendaForm" property="id">
         		<li class="pagActual"><bean:write name="agendaForm" property="traducciones[0].titulo" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="agendaForm" property="id">
	         <li class="pagActual"><bean:message key="agenda.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/agenda.gif" alt="<bean:message key="agenda.evento" />" />
	<bean:message key="agenda.evento" />:  
		<span>
		    <logic:present name="agendaForm" property="id">
	         	<bean:write name="agendaForm" property="traducciones[0].titulo" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="agendaForm" property="id">
		        <bean:message key="agenda.alta" />
		    </logic:notPresent>				
		</span>
	</h1>
	
	
	<bean:size id="tamano" name="actividadesCombo"/>
	<logic:equal name="tamano" value="0">
		<p>
			<div class="alerta" style="font-weight:bold; color:#FF1111;">
				<em><strong><bean:message key="agenda.actividad.nohay" />.</strong> <bean:message key="agenda.actividad.alerta" />.&nbsp;&nbsp;&nbsp;<button type="button" title="<bean:message key="actividad.crear" />" onclick="document.location='actividadesAcc.do?accion=crear';"><img src="imgs/botons/nou.gif" alt="<bean:message key="actividad.crear" />" /></button> </em><br/>
				<br/>
			</div>
		</p>					
	</logic:equal>	
	
	
	<logic:notEqual name="tamano" value="0">
		
		<!-- tinyMCE -->
		<script language="javascript" type="text/javascript" src="tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
		<script language="javascript" type="text/javascript">
		
			tinyMCE.init({
				mode : "textareas",
				theme : "advanced",
				plugins : "advlink",
				theme_advanced_buttons1 : "bold,italic,underline,separator,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,separator,outdent,indent,separator,link,unlink,forecolor,removeformat,cleanup,code",
				theme_advanced_buttons2 : "",		
				theme_advanced_buttons3 : "",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				theme_advanced_path_location : "bottom",
				file_browser_callback : "fileBrowserCallBack",		
				verify_html : true,
				theme_advanced_resizing : false,	
				accessibility_warnings : true,						
				language: "ca"	
			});
			
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
		
				window.open('/sacmicroback/recursos.do?tiny=true','recursos','scrollbars=yes,width=700,height=400');
		
			}	
			
		</script>
		<!-- /tinyMCE -->
	
<%session.setAttribute("action_path_key",null);%>
		<html:form action="/agendaEdita.do"  method="POST"  enctype="multipart/form-data"  styleId="accFormulario" >
		 
		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	
		
		 	<!-- botonera -->
			<input type="hidden" name="espera" value="si" id="espera" />
			<input type="hidden" name="accion" value=""/>
			<logic:present name="agendaForm" property="id">
				<input type="hidden" name="id" value="<bean:write name="agendaForm" property="id" />"/>
			</logic:present>
			
		    <!-- botonera -->
			<div id="botonera">
				<span class="grup">
					<button type="button" title='<bean:message key="agenda.volver"/>' onclick='document.location.href="agendas.do";'>
				   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="agenda.volver"/>' />
				   	</button> 
				</span>
				<logic:present name="agendaForm" property="id">
					<span class="grup">
						<button type="submit" title="<bean:message key="agenda.crear" />" onclick="submitForm('Crear');"><img src="imgs/botons/nou.gif" alt="<bean:message key="agenda.crear" />" /></button>
					</span>
					<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
						<span class="grup">				
							<button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
						   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
							</button>
						</span> 
				 	</logic:equal>
					<span class="grup">
						<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
					</span>	
				</logic:present>	
				
				<span class="grup">
					<button type="submit" title="<bean:message key="op.15" />" onclick="submitForm('Guardar');">
						<img src="imgs/botons/guardar.gif" alt="<bean:message key="op.15" />" /> &nbsp;<bean:message key="op.15" />
					</button>
				</span> 
				<logic:present name="agendaForm" property="id">
				 	<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('Borrar');">
				   		<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
				   </button> 
				</logic:present>
			</div>
			    
			
			<div id="formulario">
			 <logic:present name="accesibilidad">
				<a href="accesibilitat.do?tipo=a&iditem=<bean:write name="agendaForm" property="id"/>"><b> veure errors d'accessibilitat</b></a>
			  </logic:present>	
				<!-- las tablas están entre divs por un bug del FireFox -->
					<table cellpadding="0" cellspacing="0" class="edicio">
			
					<tr class="par">
						<td class="etiqueta"><bean:message key="agenda.finicio" /></td>
						<td>
							<html:text property="finicio" readonly="readonly" maxlength="16" />
						</td>
						<td class="etiqueta"><bean:message key="agenda.ffin" /></td>
						<td>
							<html:text property="ffin" readonly="readonly" maxlength="16" />
						</td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="agenda.visible" /></td>
						<td><label><html:radio property="visible" value="S" />&nbsp;S&iacute;</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visible" value="N" />&nbsp;No</label></td>
						<td class="etiqueta"><bean:message key="agenda.actividad" /></td>
						<td>
			                <html:select property="idActividad">
			                	<html:option value=""><bean:message key="agenda.selecactivi" /></html:option>
				                <html:options collection="actividadesCombo" labelProperty="traduccion.nombre" property="id"/>
			    	        </html:select>
						</td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="agenda.organizador" /></td>
						<td colspan="3">
							<html:text property="organizador" maxlength="256" size="60" />
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
			
			    <logic:iterate id="traducciones" name="agendaForm" property="traducciones" indexId="i" >
			    <bean:define id="idiomaahora" value="Catalan" type="java.lang.String" />
	            <logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
	            		<%if(j.intValue()==i.intValue()){%>
				  	<bean:define id="idiomaahora" name="lang" type="java.lang.String" />  
				<%}%>   	
				 
	            </logic:iterate>
			    
				<div id="capa_tabla<%=i%>" class="capaFormIdioma" <%=(i.intValue()==0?"":"style='display:none;'")%>>
					<html:hidden property="idioma" name="traducciones" value="<bean:write name='lang' property='idiomaahora' />" />
					<table cellpadding="0" cellspacing="0" class="edicio">
					<tr>
						<td class="etiqueta"><bean:message key="agenda.titulo" />:</td>
						<td><html:text property="titulo" name="traducciones" size="50" maxlength="256" indexed="true" /></td>
					</tr>
					<tr id="tinymceEditor<%=i%>">
						<td class="etiqueta"><bean:message key="agenda.descripcion" />:
						<p>
				<%
					Accesibilidad acce = (Accesibilidad)request.getAttribute("MVS_w3c_" + i);								
					if (acce!=null) out.println("<button type=\"button\" title='Errors dÂ´accessibilitat' onclick=\"document.location='visorw3c.do?id=" + acce.getId() + "';\"><img src=\"imgs/botons/taww3cButton.gif\" alt='Errors dÂ´accessibilitat' /></button>");
				%>						
						</p>						
						</td>
						<td><html:textarea  property="descripcion" name="traducciones" rows="5" cols="50" indexed="true" style="width:700px; height:300px;" /></td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="agenda.documento" /></td>
						<td colspan="3">
			
							<div style="text-align:left" id="microManagedFile<%=i%>">
								<html:hidden property="<%="ficherosid["+i+"]"%>" />
								<logic:notEmpty name="agendaForm" property="<%="ficherosnom["+i+"]"%>">
				                	<html:text property="<%="ficherosnom["+i+"]"%>"/>
									<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'<%="ficheros["+i+"]"%>','<%="ficherosid["+i+"]"%>','<%="ficherosbor["+i+"]"%>','<%="ficherosnom["+i+"]"%>');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
									<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="agendaForm" property="<%="ficherosid["+i+"]"%>"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
							    </logic:notEmpty>
							    <logic:empty name="agendaForm" property="<%="ficherosnom["+i+"]"%>">
								    <html:file property="<%="ficheros["+i+"]"%>" size="30"/>
				   			    </logic:empty>
			   			    </div>    			    
			   			    
						</td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="agenda.imagen" /></td>
						<td colspan="3">
			   			    
							<div style="text-align:left" id="microManagedFileImg<%=i%>">
								<html:hidden property="<%="imagenesid["+i+"]"%>" />
								<logic:notEmpty name="agendaForm" property="<%="imagenesnom["+i+"]"%>">
				                	<html:text property="<%="imagenesnom["+i+"]"%>"/>
									<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'<%="imagenes["+i+"]"%>','<%="imagenesid["+i+"]"%>','<%="imagenesbor["+i+"]"%>','<%="imagenesnom["+i+"]"%>');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
									<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="agendaForm" property="<%="imagenesid["+i+"]"%>"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
							    </logic:notEmpty>
							    <logic:empty name="agendaForm" property="<%="imagenesnom["+i+"]"%>">
								    <html:file property="<%="imagenes["+i+"]"%>" size="30"/>
				   			    </logic:empty>
			   			    </div>    			    
			   			    
			   			    
						</td>
					</tr>
					<tr>
						<td class="etiqueta"><bean:message key="url.adicional" />:</td>
						<td></td>
					</tr>
					<tr>						
						<td class="etiqueta"><bean:message key="agenda.url" />:</td>
						<td><html:text property="url" name="traducciones" size="50" maxlength="256" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].url', 'traducciones[<%=i%>].urlnom','<bean:write name="idiomaahora"/>');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
					</tr>
					<tr>						
						<td class="etiqueta"><bean:message key="agenda.urlnom" />:</td>
						<td><html:text property="urlnom" name="traducciones" size="114" maxlength="512" indexed="true" /></td>
					</tr>					
					</table>
				</div>
			    </logic:iterate>
			
			</div>
			
		</html:form>
	</logic:notEqual>

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
	
	
	function previsualizar() {
		<logic:present name="agendaForm" property="id">
			
			var tempdate= '<bean:write name="agendaForm" property="finicio"/>';
			var date = tempdate.substring(6, 10) + tempdate.substring(3, 5) + tempdate.substring(0, 2);

			
			abrirWindow('<bean:message key="url.aplicacion" />agenda.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&cont='+date+'&stat=no');

			</logic:present>	
	}


	var RcajatempUrl;
	var RcajatempDesc;

    function Rpopupurl(objurl, objdesc, idioma ) 
    {

      RcajatempUrl =document.agendaForm[objurl];
      RcajatempDesc =document.agendaForm[objdesc];
      window.open('recursos.do?lang='+idioma,'recursos','scrollbars=yes,width=700,height=400');

    }

      function Rmeterurl(laurl, descr) 
    {
            RcajatempUrl.value=laurl;
			RcajatempDesc.value=descr;

    }

  

// -->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
