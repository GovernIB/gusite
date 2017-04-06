<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="es.caib.gusite.utilities.property.GusitePropertiesUtil"  %>

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
	<script type="text/javascript"	src="js/jquery/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="js/checkUri.js"></script>
	<script type="text/javascript" src="js/jscolor.min.js"></script>
	<% if(GusitePropertiesUtil.getKeyGooglemaps()!=null && GusitePropertiesUtil.getKeyGooglemaps()!=""){ %>
	<link href="css/modalMaps.css" rel="stylesheet" type="text/css" />	
	<%} %>
</head>

<body>

		<bean:define id="tipovisible" type="java.lang.Object" >0</bean:define>

		<logic:present name="MVS_tipolistado">
		<bean:define id="tipovisible" name="MVS_tipolistado" property="tipoelemento" />
		</logic:present>
				
<% if(GusitePropertiesUtil.getKeyGooglemaps()!=null && GusitePropertiesUtil.getKeyGooglemaps()!=""){ %>		
		
<!-- ventana Modal para google maps MODALMAPS -->
<div id="modalMaps" class="modal">
  <div class="modal-content">
    <span class="close_modal">&times;</span>
	<div id="modal_cabecera">
		<span>Dirección:</span> 
		<input type="text" id="modal_direccion"/> 
		<input type="button" id="modal_buscarDireccion" onclick="buscarDireccion()" value="<bean:message key="boton.buscar.direccion"/>"/> 
		<input type="button" id="modal_centrar" onclick="centrarMapa()" value="<bean:message key="boton.centrar.mapa"/>"/> 
	</div>
	<div id="modal_map">mapa</div>
	<div id="modal_footer">
		<input type="button" id="modal_guardar" onclick="guardarPosicion();cerrarModal();" value="<bean:message key="boton.usar.ubicacion"/>"/> 
		<input type="button" id="modal_borrar" onclick="borrarPosicion();cerrarModal();" value="<bean:message key="boton.borrar.ubicacion"/>"/> 
		<input type="button" id="modal_volver" onclick="cerrarModal();" value="<bean:message key="boton.volver"/>"/> 	
	</div>   
  </div>
</div>
<%} %>	
	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li><bean:message key="menu.recursos" /></li>
		<li><a href="tipos.do?mntnmnt=yes"><bean:message key="menu.listados" /></a></li>
		<logic:present name="MVS_tipolistado"><li><a href="noticias.do"><bean:write name="MVS_tipolistado" property="traduce.nombre" ignore="true" /></a></li></logic:present>
	    <logic:present name="noticiaForm" property="id">
         		<li class="pagActual"><bean:write name="noticiaForm" property="traducciones[0].titulo" ignore="true" /></li>
	    </logic:present>		
	    <logic:notPresent name="noticiaForm" property="id">
	         <li class="pagActual"><bean:message key="noticia.alta" /></li>
	    </logic:notPresent>				
	</ul>
	<!-- titol pagina -->
	<h1><img src="imgs/titulos/fichas.gif" alt="<logic:present name="MVS_tipolistado"><bean:write name="MVS_tipolistado" property="traduce.nombre" ignore="true" /></logic:present>" />
	<logic:present name="MVS_tipolistado">Elemento '<bean:write name="MVS_tipolistado" property="traduce.nombre" ignore="true" />'. </logic:present>
		<span>
		    <logic:present name="noticiaForm" property="id">
	         	<bean:write name="noticiaForm" property="traducciones[0].titulo" ignore="true" />
		    </logic:present>		
		    <logic:notPresent name="noticiaForm" property="id">
		        <bean:message key="noticia.alta" />
		    </logic:notPresent>				
		</span>
	</h1>

	<!-- tinyMCE. Editar codigo segun el rol (siendo 1 el valor si puede editar codigo). -->
		<script language="javascript" type="text/javascript">
					var editarCodigo = "";
		</script>
		<logic:equal name="MVS_usuario" property="permisosTiny" value="1">
			<script language="javascript" type="text/javascript">
					editarCodigo = "code";
			</script>
		</logic:equal>
		
		<!-- tinyMCE -->
		<script language="javascript" type="text/javascript" src="tinymce/tinymce.min.js"></script>
		<script language="javascript" type="text/javascript">
		
		
		//Paso 1. Inicializamos tinyMCE.
		tinymce.init({
		    selector: 'textarea.editorTinyMCE',
			language: 'ca',
			plugins: "code, compat3x, link, textcolor, acheck ,paste"
			,toolbar1: 'bold italic underline | alignleft aligncenter alignright alignjustify bullist numlist | outdent indent | link unlink forecolor removeformat cleanup '+editarCodigo+' acheck  | cut copy paste'
			,menubar: false
			,external_plugins: {
				"acheck": "plugins/acheck/editor_plugin.js"
			}
			<logic:notEqual name="MVS_usuario" property="permisosTiny" value="1">		
				,paste_as_text: true
				,invalid_elements: 'br'
			</logic:notEqual>
			, file_browser_callback : function(field_name, url, type, win){
                
                Rcajatemp_tiny=field_name;
                Rwin_tiny=win;
                <logic:notEmpty name="contenidoForm">
                               window.open("/sacmicroback/recursos.do?tiny=true&id=<bean:write name='contenidoForm' property='id'/>&idMenu=<bean:write name='contenidoForm' property='idMenu'/>",'recursos','scrollbars=yes,width=700,height=400');
                </logic:notEmpty>
                <logic:empty name="contenidoForm">
                               window.open("/sacmicroback/recursos.do?tiny=true",'recursos','scrollbars=yes,width=700,height=400');
                </logic:empty>
                return false;
			}
		  });
		
			var Rcajatemp_tiny;
		   	var Rwin_tiny;
			
			function Rmeterurl_tiny(laurl) {
				document.getElementById(Rcajatemp_tiny).value = laurl;
			}	
		</script>
		<!-- /tinyMCE -->

<%session.setAttribute("action_path_key",null);%>
<html:form action="/noticiaEdita.do" method="POST" enctype="multipart/form-data"  styleId="accFormulario">

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>

		<input type="hidden" name="accion" value=""/>
		
		<!-- botonera -->
		<div id="botonera">
			<span class="grup">
			<button type="button" title='<bean:message key="tipo.volvermantenimiento.noticias"/>' onclick='document.location.href="noticias.do?tipo=<bean:write name="MVS_idtipo"/>";'>
			  		<img src="imgs/botons/tornar.gif" alt='<bean:message key="tipo.volvermantenimiento.noticias"/>' />
			</button> 
			</span>
			<logic:present name="noticiaForm" property="id">
				<button type="submit" title="<bean:message key="noticia.crear" />" onclick="submitForm('Crear');"><img src="imgs/botons/nuevaPagina.gif" alt="<bean:message key="noticia.crear" />" /></button>
			<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
			   <button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
			   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
			   </button>
			</logic:equal>

			</logic:present>
		    <button type="button" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
		   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
		   	</button>
		   	<logic:present name="noticiaForm" property="id">
		   	<logic:present name="MVS_tipolistado">
			<logic:equal name="MVS_tipolistado" property="tipoelemento" value="0">
					<button type="button" title="<bean:message key="op.12" />" onclick="previsualizar();"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
			</logic:equal>
			</logic:present>
				<span class="grup">
				<button type="submit" title='<bean:message key="operacion.borrar" />' onclick="submitForm('Borrar');">
					<img src="imgs/menu/esborrar.gif" alt='<bean:message key="operacion.borrar" />' />
				</button> 
				</span>
			</logic:present>
		</div>
		
		<input type="hidden" name="espera" value="si" id="espera" />
		
		     <logic:present name="noticiaForm" property="id">
			     <input type="hidden" name="modifica" value="Grabar">
		         <html:hidden property="id" />
		         <input type="hidden" name="idmicrosite" value='<bean:write name="idmicrosite"/>' />
		     </logic:present>
			 <logic:notPresent name="noticiaForm" property="id">
			  	<input type="hidden" name="anyade" value="Crear">
			  	<input type="hidden" name="idmicrosite" value='<bean:write name="idmicrosite"/>' />
			 </logic:notPresent>      

	<div id="formulario">
		  <logic:present name="accesibilidad">
			<a href="accesibilitat.do?tipo=n&iditem=<bean:write name="noticiaForm" property="id"/>"><b> veure errors d'accessibilitat</b></a>
		  </logic:present>	
		<!-- las tablas estan entre divs por un bug del FireFox -->
			<table cellpadding="0" cellspacing="0" class="edicio">
	
			<tr class="par" style="display:block;">
				<td class="etiqueta"><bean:message key="noticia.fpublicacion" /></td>
				<td>
					<html:text property="fpublicacion" readonly="readonly" maxlength="16" />
				</td>
				<td class="etiqueta"><bean:message key="noticia.fcaducidad" /></td>
				<td>
					<html:text property="fcaducidad" readonly="readonly" maxlength="16" />
				</td>
			</tr>
	
			<tr style="display:block;">
				<td class="etiqueta"  <%= tipovisible.equals("5")?"style=\"display:none;\"":""%> ><bean:message key="noticia.orden" /></td>
				<td <%= tipovisible.equals("5")?"style=\"display:none;\"":""%> ><html:text property="orden"/></td>
				<td class="etiqueta"><bean:message key="noticia.visible" /></td>
				<td style="min-width: 120px"><label><html:radio property="visible" value="S" />&nbsp;Sí­</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visible" value="N" />&nbsp;No</label></td>
			</tr>
			

			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("5")?"block":"none"%>;" id="CamposMapa" class="par">
				<td class="etiqueta"><bean:message key="noticia.latitud" /></td>
				<td><html:text property="latitud" styleId="latitud"/></td>
				<td class="etiqueta"><bean:message key="noticia.longitud" /></td>
				<td>
					<html:text property="longitud" styleId="longitud"/> 
					<% if(GusitePropertiesUtil.getKeyGooglemaps()!=null && GusitePropertiesUtil.getKeyGooglemaps()!=""){ %>
					<button type="button" title="<bean:message key="boton.ubicarenmapa" />" onclick="ubicarEnMapa();"><img src="imgs/botons/ubicacion.gif" alt="<bean:message key="boton.ubicarenmapa" />" /></button>
					<%} %>
				</td>
			</tr>
			
			<tr style="display:<%= tipovisible.equals("5")?"block":"none"%>;" id="CamposMapa2">				
				<td class="etiqueta"><bean:message key="noticia.color" /></td>
				<% if(tipovisible.equals("5")){ %>
				<td><html:text property="colorIcono" styleClass="jscolor" styleId="colorIcono" /></td>
				<%}else{ %>
				<td><html:text property="colorIcono" styleClass="jscolor {required:false}" styleId="colorIcono" /></td>
				<%} %>
			</tr>

						
			<tr class="par" style="display:none;">
				<td class="etiqueta"><bean:message key="noticia.tipo" /></td>
				<td>
	                <html:select property="idTipo">
	                	<html:option value=""><bean:message key="noticia.selectipo" /></html:option>
		                <html:options collection="tiposCombo" labelProperty="traduccion.nombre" property="id"/>
	    	        </html:select>
				</td>				
				<td class="etiqueta"><bean:message key="noticia.visibleweb" /></td>
				<td><label><html:radio property="visibleweb" value="S" />&nbsp;Sí­</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visibleweb" value="N" />&nbsp;No</label></td>				
			</tr>
	
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("4") || tipovisible.equals("5")?"block":"none"%>;" <%= tipovisible.equals("5")?"class=\"par\"":""%> >
				
				<td class="etiqueta"><bean:message key="noticia.imagen" /></td>
				<td>
					<div style="text-align:left" id="microManagedFile">
						<html:hidden property="imagenid" />
						<logic:notEmpty name="noticiaForm" property="imagennom">
		                	<html:text property="imagennom"/>
							<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'imagen','imagenid','imagenbor','imagennom');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
							<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="noticiaForm" property="imagenid"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
					    </logic:notEmpty>
					    <logic:empty name="noticiaForm" property="imagennom">
						    <html:file property="imagen" size="30"/>
		   			    </logic:empty>
	   			    </div>
				</td>
				<td class="etiqueta"></td>
				<td>&nbsp;</td>
			</tr>
	
			<tr>
			<td colspan="4">
	
		<ul id="submenu">
			<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
				<li<%=(j.intValue()==0?" class='selec'":"")%>>
					<a href="#" onclick="mostrarForm(this);">
					<bean:message name="lang" />
					</a>
				</li>
	        </logic:iterate>
		</ul>    
	
	    <logic:iterate id="traducciones" name="noticiaForm" property="traducciones" indexId="i" >
	     <bean:define id="idiomaahora" value="Catalan" type="java.lang.String" />

            <logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
            		<%if(j.intValue()==i.intValue()){%>
			  	<bean:define id="idiomaahora" name="lang" type="java.lang.String" />  
			<%}%>   	
			 
            </logic:iterate>

	    
		<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:block;":"display:none;")%>">
		
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr style="display:block;">
				<td class="etiqueta"><bean:message key="noticia.titulo" />:</td>
				<td><html:text property="titulo" name="traducciones" styleId="<%="titulo"+i%>" size="50" maxlength="512" indexed="true" /></td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("4") || tipovisible.equals("5")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="noticia.subtitulo" />:</td>
				<td><html:text property="subtitulo" name="traducciones" size="50" maxlength="256" indexed="true" /></td>
			</tr>
			<tr style="display:block;">
				<td class="etiqueta"><bean:message key="noticia.uri"/>:</td>
				<td><html:text property="uri" name="traducciones" styleId="<%="uri"+i%>" size="50" maxlength="256" indexed="true" /></td>
				<input type="hidden" name="type" value="nid_uri" />
			</tr>
			<tr id="tinymceEditor<%=i%>" style="display:block;">
				<td class="etiqueta"><bean:message key="noticia.texto" />:
				<p>
				</p>				
				</td>
				<td><html:textarea  property="texto" name="traducciones" styleClass="editorTinyMCE" rows="5" cols="50" indexed="true" style="width:700px; height:300px;"/></td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("4")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="noticia.fuente" />:</td>
				<td><html:text property="fuente" name="traducciones" size="50" maxlength="256" indexed="true" /></td>
			</tr>							
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("2")|| tipovisible.equals("4") || tipovisible.equals("5")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="noticia.documento" /></td>
				<td colspan="3">
	
					<div style="text-align:left" id="microManagedFile<%=i%>">
						<html:hidden property="<%="ficherosid["+i+"]"%>" />
						<logic:notEmpty name="noticiaForm" property="<%="ficherosnom["+i+"]"%>">
		                	<html:text property="<%="ficherosnom["+i+"]"%>"/>
							<button type="button" title="<bean:message key="boton.eliminar" />" onclick="borraFile(this,'<%="ficheros["+i+"]"%>','<%="ficherosid["+i+"]"%>','<%="ficherosbor["+i+"]"%>','<%="ficherosnom["+i+"]"%>');"><img src="imgs/botons/borrar.gif" alt="<bean:message key="boton.eliminar" />" /></button>
							<button type="button" title="<bean:message key="op.12" />" onclick="abrirDoc('<bean:write name="noticiaForm" property="<%="ficherosid["+i+"]"%>"/>');"><img src="imgs/botons/previsualitzar.gif" alt="<bean:message key="op.12" />" /></button>
					    </logic:notEmpty>
					    <logic:empty name="noticiaForm" property="<%="ficherosnom["+i+"]"%>">
						    <html:file property="<%="ficheros["+i+"]"%>" size="30"/>
		   			    </logic:empty>
	   			    </div>    
	   			    
				</td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("1") || tipovisible.equals("4")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="url.adicional" />:</td>
				<td></td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("1") || tipovisible.equals("4")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="noticia.laurl" />:</td>
				<td><html:text property="laurl" name="traducciones"  size="45" maxlength="512" indexed="true" />&nbsp;<button type="button" title="<bean:message key="micro.verurl"/>" onclick="javascript:Rpopupurl('traducciones[<%=i%>].laurl', 'traducciones[<%=i%>].urlnom','<bean:write name="idiomaahora"/>');"><img src="imgs/botons/urls.gif" alt="<bean:message key="micro.verurl"/>" /></button></td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("1") || tipovisible.equals("4")?"block":"none"%>;">				
				<td class="etiqueta"><bean:message key="noticia.urlnom" />:</td>
				<td><html:text property="urlnom" name="traducciones" size="114" maxlength="512" indexed="true" /></td>				
			</tr>
			</table>
		</div>
	    </logic:iterate>
	
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
		<% if(tipovisible.equals("5") || tipovisible.equals("0") ){ %>
		if (nom_accio== "Guardar"){//solo validamos al guardar
			if (!validaCampos(<%= tipovisible.equals("5")?"true":"false"%>)){
				return false;
			}
		}
		<%}%>
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
		abrirWindow('<bean:message key="url.aplicacion" />noticia.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&cont=<bean:write name="noticiaForm" property="id"/>&stat=no');
	}
	
	var RcajatempUrl;
	var RcajatempDesc;

    function Rpopupurl(objurl, objdesc, idioma ) 
    {

      RcajatempUrl =document.noticiaForm[objurl];
      RcajatempDesc =document.noticiaForm[objdesc];
      window.open('recursos.do?lang='+idioma,'recursos','scrollbars=yes,width=700,height=400');

    }

      function Rmeterurl(laurl, descr) 
    {
            RcajatempUrl.value=laurl;
			RcajatempDesc.value=descr;

    } 
	
	
// -->
</script>

<script type="text/javascript" src="js/modalMaps.js"></script>

<script type="text/javascript">
<!--
//labels por defecto	 
var lb_coordenadasInvalidas = "<bean:message key="error.coordenadas.invalidas"/>";
var lb_colorInvalido = "<bean:message key="error.color.invalido"/>";
var lb_completeDireccion = "<bean:message key="error.complete.direccion"/>";
var lb_DireccionNoEncontrada = "<bean:message key="error.direccion.erronea"/>";	
//-->
</script>

<% if(GusitePropertiesUtil.getKeyGooglemaps()!=null && GusitePropertiesUtil.getKeyGooglemaps()!=""){ %>
<script src="https://maps.googleapis.com/maps/api/js?key=<%=GusitePropertiesUtil.getKeyGooglemaps() %>&callback=initialize" async defer></script>
<% } %>
