<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.ibit.rol.sac.micromodel.Accesibilidad"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/funciones.js"></script>
	<script type="text/javascript" src="moduls/funcions.js"></script>
</head>

<body>

		<bean:define id="tipovisible" type="java.lang.Object" >0</bean:define>

		<logic:present name="MVS_tipolistado">
		<bean:define id="tipovisible" name="MVS_tipolistado" property="tipoelemento" />
		</logic:present>


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

	<!-- tinyMCE -->
	<script language="javascript" type="text/javascript" src="tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
	<script language="javascript" type="text/javascript">
	
		tinyMCE.init({
			mode : "textareas",
			theme : "advanced",
			plugins : "advlink,paste",
			theme_advanced_buttons1 : "bold,italic,underline,separator,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,separator,outdent,indent,separator,link,unlink,forecolor,removeformat,cleanup,code,separator,cut,copy,paste,pastetext,pasteword",
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
		// Este método es la implementación personalizada del tiny
		function fileBrowserCallBack(field_name, url, type, win) {
			Rcajatemp_tiny=field_name;
			Rwin_tiny=win;
			
			window.open('/sacmicroback/recursos.do?tiny=true','recursos','scrollbars=yes,width=700,height=400');
	
		}
	
	</script>
	<!-- /tinyMCE
	<div style="font-weight:bold; color:#FF4400;"> <html:errors/></div> -->

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
		    <button type="submit" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
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
		     </logic:present>
			 <logic:notPresent name="noticiaForm" property="id">
			  	<input type="hidden" name="anyade" value="Crear">
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
				<td class="etiqueta"><bean:message key="noticia.orden" /></td>
				<td><html:text property="orden"/></td>
				<td class="etiqueta"><bean:message key="noticia.visible" /></td>
				<td><label><html:radio property="visible" value="S" />&nbsp;Sí</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visible" value="N" />&nbsp;No</label></td>
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
				<td><label><html:radio property="visibleweb" value="S" />&nbsp;Sí</label>&nbsp;&nbsp;&nbsp;<label><html:radio property="visibleweb" value="N" />&nbsp;No</label></td>				
			</tr>
	
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("4")?"block":"none"%>;">
				
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
			<logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
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
	
	    <logic:iterate id="traducciones" name="noticiaForm" property="traducciones" indexId="i" >
	     <bean:define id="idiomaahora" value="Catalan" type="java.lang.String" />

            <logic:iterate id="lang" name="org.ibit.rol.sac.microback.LANGS_KEY" indexId="j">
            		<%if(j.intValue()==i.intValue()){%>
			  	<bean:define id="idiomaahora" name="lang" type="java.lang.String" />  
			<%}%>   	
			 
            </logic:iterate>

	    
		<div id="capa_tabla<%=i%>" class="capaFormIdioma" style="<%=(i.intValue()==0?"display:block;":"display:none;")%>">
		
			<table cellpadding="0" cellspacing="0" class="edicio">
			<tr style="display:block;">
				<td class="etiqueta"><bean:message key="noticia.titulo" />:</td>
				<td><html:text property="titulo" name="traducciones" size="50" maxlength="512" indexed="true" /></td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("4")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="noticia.subtitulo" />:</td>
				<td><html:text property="subtitulo" name="traducciones" size="50" maxlength="256" indexed="true" /></td>
			</tr>
			<tr id="tinymceEditor<%=i%>" style="display:block;">
				<td class="etiqueta"><bean:message key="noticia.texto" />:
				<p>
				<%
					Accesibilidad acce = (Accesibilidad)request.getAttribute("MVS_w3c_" + i);				
					if (acce!=null) out.println("<button type=\"button\" title='Errors dï¿½accessibilitat' onclick=\"document.location='visorw3c.do?id=" + acce.getId() + "';\"><img src=\"imgs/botons/taww3cButton.gif\" alt='Errors dï¿½accessibilitat' /></button>");
				%>						
				</p>				
				</td>
				<td><html:textarea  property="texto" name="traducciones" rows="5" cols="50" indexed="true" style="width:700px; height:300px;"/></td>
			</tr>
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("4")?"block":"none"%>;">
				<td class="etiqueta"><bean:message key="noticia.fuente" />:</td>
				<td><html:text property="fuente" name="traducciones" size="50" maxlength="256" indexed="true" /></td>
			</tr>							
			<tr style="display:<%=tipovisible.equals("0") || tipovisible.equals("2")|| tipovisible.equals("4") ?"block":"none"%>;">
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


