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
	<script type="text/javascript" src="js/coArbre.js"></script>
</head>

<body>

	<!-- molla pa -->
	<ul id="mollapa">
		<li><a href="microsites.do" target="_parent"><bean:message key="micro.listado.microsites" /></a></li>
		<li><a href="index_inicio.do"><bean:message key="op.7" /></a></li>
		<li class="pagActual"><bean:message key="menu.contenidos" /></li>
	</ul>

	<!-- titol pagina -->
	<h1><img src="imgs/titulos/contenido.gif" alt="<bean:message key="menu.contenidos" />" />
	<bean:message key="menu.contenidos" />. <span><bean:message key="editar.arbol" /></span></h1>


<%session.setAttribute("action_path_key",null);%>
<html:form action="/menuEdita.do" method="POST" enctype="multipart/form-data" styleId="accFormulario">


		<!-- *********************************** -->
		<!-- ***** CAPA MENU SUPERIOR ********** -->
		<!-- *********************************** -->

		<!--  Jsp que muestra mensajes de Info/Alerta y/o error -->
		<jsp:include page="/moduls/mensajes.jsp"/>	

		<input type="hidden" name="accion" value=""/>
		<!-- botonera -->
		<div id="botonera">
			<logic:present name="MVS_microsite">
			<span class="grup">
		   	<button type="button" title='<bean:message key="micro.boto.tornargestio.titol"/>' onclick='document.location.href="index_inicio.do";'>
		   		<img src="imgs/botons/tornar.gif" alt='<bean:message key="micro.boto.tornargestio.titol"/>' />
		   </button> 
			</span>
			</logic:present>
			<span class="grup">
				<button type="button" onclick="menuNuevo();" title='<bean:message key="menu.crear" />' ><img src="imgs/botons/nuevoNodo.gif" alt='<bean:message key="menu.crear" />' /></button>
			</span>
			<span class="grup">
				<button type="button" onclick="javascript:Rpopupurl();" title='<bean:message key="op.12" />'><img src="imgs/botons/ver.gif" alt='<bean:message key="op.12" />' /></button>
			</span>
			<logic:greaterThan name="menuForm" property="numeroobjectes" value="0">
			<logic:equal name="MVS_microsite" property="funcionalidadTraduccion" value="true">
			<span class="grup">				
			<button type="submit" title='<bean:message key="operacion.traducir"/>' onclick="submitForm('Traduir');">
			   		<img src="imgs/botons/clonar.gif" alt='<bean:message key="operacion.traducir"/>' /> &nbsp;<bean:message key="operacion.traducir" />
			</button>
			</span> 
			</logic:equal>
			</logic:greaterThan>
			<button type="button" title='<bean:message key="operacion.guardar"/>' onclick="submitForm('Guardar');">
		   		<img src="imgs/botons/guardar.gif" alt='<bean:message key="operacion.guardar"/>' /> &nbsp;<bean:message key="operacion.guardar" />
		   	</button>
		
		
		</div>

	<input type="hidden" name="espera" value="si" id="espera" />
	
		<!-- *********************************** -->
		<!-- CAPA PARA CREAR UN ELEMENTO DE MENU -->
		<!-- *********************************** -->

	<div id="CAPAcrearMenu">
		
		<html:hidden property="padreCM" value="0" styleId="padreCM"/>

		<h2><bean:message key="menu.crear" /></h2>
		<p id="crearMenu_parrafo"><bean:message key="menu.crearparrafo" />&nbsp;<strong></strong>.</p>

		<table cellpadding="0" cellspacing="0" class="edicio">
		<tr>
			<td class="etiqueta"><bean:message key="menu.visible" /> &gt; </td>
			<td><html:radio property="visibleCM" value="S" />&nbsp;Sí­<html:radio property="visibleCM" value="N" />&nbsp;No</td>
		</tr>
		<tr>
			<td class="etiqueta"><bean:message key="menu.icono" /> &gt; </td>
			<td>
				<html:file property="imagenCM" size="30"/>
				<br/>
				<bean:message key="menu.icono.mensa" />
			</td>
		</tr>
		<tr>
			<td class="etiqueta"><bean:message key="menu.nombremenu" /> &gt; </td>
			<td>
				<ul class="xIdioma">
				
				<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="i">
					<li><label><span><bean:message name="lang" /></span> <html:text property="<%="nombreCM["+i+"]"%>" size="20" maxlength="256" /></label></li>
   				</logic:iterate>
   				
				</ul>
			</td>
		</tr>
		</table>
		<div class="botonera">
				<button type="button" onclick="crearMenu();" title="<bean:message key="boton.insertar" />"><img src="imgs/botons/guardar.gif" alt="" /> &nbsp;<bean:message key="boton.insertar" /></button> 
				<button type="button" title="<bean:message key="boton.cerrar" />" onclick="menuNuevoEsconder();"><img src="imgs/botons/cerrar.gif" alt="" /> &nbsp;<bean:message key="boton.cerrar" /></button>
		</div>
		
	</div>

<!-- ************************************************************** -->
<!-- ************************** M E N U  ************************** -->
<!-- ************************************************************** -->

		<bean:define id="idis" name="es.caib.gusite.microback.LANGS_KEY"/>
		<% int nlangs=((java.util.List)idis).size();%>
		
 		<div id="menuArbol">

		<logic:iterate id="listaids" name="menuForm" property="ids" indexId="i">
		
			<logic:match name="menuForm" property="<%="tipos["+i+"]"%>" value="m">
		
			<!-- ******************************* -->
			<!-- ****** MENU NIVEL 1 y 2 ******* -->
			<!-- ******************************* -->
		
				<logic:equal name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">
					<div id="m<%=i%>" class="nivel1">
				</logic:equal>

				<logic:notEqual name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">
					<div id="m<%=i%>" class="nivel2">
				</logic:notEqual>

				<input type="hidden" name="<%="ids["+i+"]"%>" id="padre<%=i%>" value='<bean:write name="menuForm" property='<%="ids["+i+"]"%>' />' >
				<html:hidden property="<%="ordenes["+i+"]"%>"/>
				<html:hidden property="<%="tipos["+i+"]"%>"/>
				<html:hidden property="<%="idPadres["+i+"]"%>"/>
				
				<table cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="visible">
							<html:hidden property="<%="visibles["+i+"]"%>"/><button name="visible" type="button" title='<bean:message key="menu.visible" />'>
							<logic:equal name="menuForm" property='<%="visibles["+i+"]"%>' value="S">
								<img src="imgs/menu/visible.gif" alt='<bean:message key="menu.visible" />' />
							</logic:equal>
							<logic:notEqual name="menuForm" property='<%="visibles["+i+"]"%>' value="S">
								<img src="imgs/menu/visibleNo.gif" alt='<bean:message key="menu.visible" />' />
							</logic:notEqual>
							</button>
						</td>
						<td class="padre"><button name="padre" type="button" title="<bean:message key="menu.cerrarcarpeta" />"><img src="imgs/menu/padreAbierto.gif" alt="<bean:message key="menu.cerrarcarpeta" />" /></button></td>						

						<logic:equal name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">
							<td class="icono"><img name="nivel1" src="imgs/menu/nivel1.gif" alt="" />
								<logic:present name="menuForm" property="<%="imagenesid["+i+"]"%>">
									<img src='archivo.do?id=<bean:write name="menuForm" property="<%="imagenesid["+i+"]"%>"/>' alt=""/>
								</logic:present>
								<logic:notPresent name="menuForm" property="<%="imagenesid["+i+"]"%>">
									<img src="imgs/menu/carpeta1.gif"/>
								</logic:notPresent>
							</td>
						</logic:equal>

						<logic:notEqual name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">
							<td class="icono"><img name="nivel2" src="imgs/menu/nivel2.gif" alt="" />
								<logic:present name="menuForm" property="<%="imagenesid["+i+"]"%>">
									<img src='archivo.do?id=<bean:write name="menuForm" property="<%="imagenesid["+i+"]"%>"/>' alt=""/>
								</logic:present>
								<logic:notPresent name="menuForm" property="<%="imagenesid["+i+"]"%>">
									<img src="imgs/menu/carpeta.gif"/>
								</logic:notPresent>
							</td>
						</logic:notEqual>
						
						<td class="text"><bean:write name="menuForm" property="<%="traducciones["+(i.intValue()* nlangs)+"]"%>" />
							<span class="opciones">

							<button name="editar" type="button" title='<bean:message key="menu.botoneditar" />'><img src="imgs/menu/editar.gif" alt='<bean:message key="menu.botoneditar" />' /></button> 
							<logic:equal name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">
								<button name="nouMenu" type="button" title='<bean:message key="menu.botonnuevomenu" />'><img src="imgs/botons/nuevoNodo.gif" alt='<bean:message key="menu.botonnuevomenu" />' /></button> 
							</logic:equal>
							<button type="button" title='<bean:message key="menu.botonnuevocontenido" />' onclick='nuevoContenido(<bean:write name="menuForm" property="<%="ids["+i+"]"%>"/>);' ><img src="imgs/botons/nuevaPagina.gif" alt='<bean:message key="menu.botonnuevocontenido" />' /></button>
							<button name="moure" type="button" title='<bean:message key="menu.botonmover" />'><img src="imgs/menu/moure.gif" alt='<bean:message key="menu.botonmover" />' /></button>
							<button name="esborrar" type="button" title='<bean:message key="menu.botonborrar" />'><img src="imgs/menu/esborrar.gif" alt='<bean:message key="menu.botonborrar" />' /></button>
					
							</span>
						</td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="3">&nbsp;</td>
						<td>
							<ul class="xIdioma">
							
							<logic:equal name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">
															
								<li><label><span><bean:message key="menu.modo" />:</span>
				                <html:select property="<%="modos["+i+"]"%>">
				                	<html:option value="F"><bean:message key="menu.modo.fijo" /></html:option>
				                	<html:option value="C"><bean:message key="menu.modo.carpeta" /></html:option>			                	
				    	        </html:select>							
								 </label></li>
								 
							</logic:equal>
							<logic:notEqual name="menuForm" property="<%="idPadres["+i+"]"%>" value="0">							
								<li><label><html:hidden property="<%="modos["+i+"]"%>" /></label></li>
							</logic:notEqual>							
							
							<li class="icona">
							<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
								<li><label><span><bean:message name="lang" />:</span> 
			
								<html:text property="<%="traducciones["+((i.intValue()* nlangs)+j.intValue())+"]"%>" size="50" maxlength="256"
								styleClass="<%="menuIdi"+lang+""%>" />

								</label></li>
							</logic:iterate>
							</li>
							<li class="icona">
							
								<li><label><span><bean:message key="menu.icono" />:</span>
							
								<logic:notEmpty name="menuForm" property="<%="imagenesnom["+i+"]"%>">
									<bean:message key="boton.eliminar" />&nbsp;
				    				<bean:write name="menuForm" property="<%="imagenesnom["+i+"]"%>"/>&nbsp;
					        		<html:checkbox property="<%="imagenesbor["+i+"]"%>"/>
		    					</logic:notEmpty>

		    					<logic:empty name="menuForm" property="<%="imagenesnom["+i+"]"%>">
			    					<html:file property="<%="imagenes["+i+"]"%>" size="30"/>
		    					</logic:empty>
		    					<html:hidden property="<%="imagenesid["+i+"]"%>" />
		    					</label></li>
							</li>
							
							</ul>
							<p class="cerrar"><button name="tancar" type="button" title="<bean:message key="menu.cerraredicion" />"><img src="imgs/botons/cerrar.gif" alt="<bean:message key="menu.cerraredicion" />" /> &nbsp;<bean:message key="boton.cerrar" /></button></p>
						</td>
					</tr>
				</tfoot>
				</table>
				</div>
			
			</logic:match>
		
			<!-- ************************ -->
			<!-- ****** CONTENIDO ******* -->
			<!-- ************************ -->
			<logic:match name="menuForm" property="<%="tipos["+i+"]"%>" value="c">

			<div id="m<%=i%>" class="pagC">

				<input type="hidden" name="<%="ids["+i+"]"%>" id="padre<%=i%>" value='<bean:write name="menuForm" property='<%="ids["+i+"]"%>' />' >
				<html:hidden property="<%="ordenes["+i+"]"%>"/>
				<html:hidden property="<%="tipos["+i+"]"%>"/>
				<html:hidden property="<%="idPadres["+i+"]"%>"/>
				<table cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
					<td class="visible">
						<html:hidden property="<%="visibles["+i+"]"%>"/>
						<button name="visible" type="button" title='<bean:message key="menu.visible" />'>
						<logic:equal name="menuForm" property='<%="visibles["+i+"]"%>' value="S">
							<img src="imgs/menu/visible.gif" alt='<bean:message key="menu.visible" />' />
						</logic:equal>
						<logic:notEqual name="menuForm" property='<%="visibles["+i+"]"%>' value="S">
							<img src="imgs/menu/visibleNo.gif" alt='<bean:message key="menu.visible" />' />
						</logic:notEqual>
						</button>
					</td>
					<td class="padre"><button name="padre" type="button" title='<bean:message key="menu.cerrarcarpeta" />'><img src="imgs/menu/padreAbierto.gif" alt='<bean:message key="menu.cerrarcarpeta" />' /></button>
					</td>
					<td class="icono">
						<logic:match name="menuForm" property="<%="tipos["+i+"]"%>" value="c2">
							<img name="nivel3" src="imgs/menu/nivel3.gif" alt="" />
						</logic:match>
						<logic:match name="menuForm" property="<%="tipos["+i+"]"%>" value="c1">
							<img name="nivel2" src="imgs/menu/nivel2.gif" alt="" />
						</logic:match>
					<img src="imgs/menu/pagContenidos.gif" alt='<bean:message key="menu.paginacontenidos" />' />
					</td>
					
					<td class="text"><bean:write name="menuForm" property="<%="traducciones["+(i.intValue()* nlangs)+"]"%>" />
						<span class="opciones">
							<button onclick="javascript:editarContenido (<bean:write name="menuForm" property="<%="ids["+i+"]"%>" />);" type="button" title='<bean:message key="menu.editarpagina" />'><img src="imgs/menu/editar.gif" alt='<bean:message key="menu.botoneditar" />' /></button> 
							<button name="moure" type="button" title='<bean:message key="menu.botonmover" />'><img src="imgs/menu/moure.gif" alt='<bean:message key="menu.botonmover" />' /></button>
							<button name="esborrar" type="button" title='<bean:message key="menu.botonborrar" />'><img src="imgs/menu/esborrar.gif" alt='<bean:message key="menu.botonborrar" />' /></button>
							<button name="visualizar" type="button" title='<bean:message key="conte.visualizaalfa"/>' onclick="abrir('<bean:message key="url.aplicacion" />contenido.do?lang=ca&mkey=<bean:write name="MVS_microsite" property="claveunica"/>&amp;cont=<bean:write name="menuForm" property="<%="ids["+i+"]"%>" />&stat=no&tipo=alfa', '', 700, 500);"><img src="imgs/botons/previsualitzar.gif" alt='<bean:message key="conte.visualizaalfa"/>' /></button>
						</span>
					</td>
					</tr>
				</tbody>
					<logic:iterate id="lang" name="es.caib.gusite.microback.LANGS_KEY" indexId="j">
						<html:hidden property="<%="traducciones["+((i.intValue()* nlangs)+j.intValue())+"]"%>"/>
					</logic:iterate>
				</table>
			</div>
		
			</logic:match>
		
		
		</logic:iterate>
		 
		 
		</div>


</html:form>



</body>
</html>

<script type="text/javascript">
<!--

var alert1='<bean:message key="menu.alert1"/>';
var alert2='<bean:message key="menu.alert2"/>';
var alert3='<bean:message key="menu.alert3"/>';
var alert4='<bean:message key="error.menu.nombreCM"/>';

	function nuevoContenido (idMenu) {
		document.location.href="contenidosAcc.do?op=crear&idmenu="+idMenu;
	}

	function editarContenido (idConte) {
		document.location.href="contenidoEdita.do?id="+idConte;
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
			 //Se comprueba que el menu en catalan está informado al modificar
			 var lista =document.getElementsByClassName('menuIdica');
			 if(lista != null && lista.length > 0){
				 for (var i = 0; i < lista.length; i++) {
					if(lista[i].value =="" || lista[i].value == null){
						alert (alert4 );
						return;
					}
					
				}
			 }
			 
			 
			 accForm.accion.value="<bean:message key='operacion.guardar'/>";
		}
	    accForm.submit();
	}

    function Rpopupurl() {
		window.open('/sacmicrofront/menupreview.do?mkey=<bean:write name="MVS_microsite" property="claveunica"/>','preview','scrollbars=yes,width=290,height=400');
    }
    
	function guardar() {
		document.forms[0].accion.value="modifica";
		document.forms[0].submit();
		
	}
	
	function crearMenu() {
		if ( document.getElementsByName("nombreCM[0]")[0].value == ''){
			alert (alert4 );
			return;
		}

		document.forms[0].accion.value="crear";
		document.forms[0].submit();
	}
	
	

-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
