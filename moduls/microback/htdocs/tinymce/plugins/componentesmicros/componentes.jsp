<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%> 
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script language="javascript" type="text/javascript" src="tinymce/plugins/compat3x/tiny_mce_popup.js"></script>
<script language="javascript" type="text/javascript" src="tinymce/plugins/componentesmicros/jscripts/functions.js"></script>
<script type="text/javascript" src="js/coArbre2.js"></script>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--[if !IE]><!--><meta http-equiv="X-UA-Compatible" content="IE=edge" /><!--<![endif]-->
	<title>Gestor Microsites</title>
	<link href="css/estils.css" rel="stylesheet" type="text/css" />
	<base target="_self" />	
</head>

<body>
<%
int indiceX=1;
%>
	<!-- titol pagina -->
	<h1>Insertar componente</h1>

	<div id="menuArbol">

		<logic:notEmpty name="MVA_listacomponentes">
		<logic:iterate id="i" name="MVA_listacomponentes" indexId="indice">		
			<% indiceX++; %>
			<div id="m<%=indiceX%>" class="nivel1"><!-- id=m+id (m1) pq id=1 -->
				<input type="hidden"  value="1<%=indiceX%>" />
				<input type="hidden" value="<%=indiceX%>" />
				<table cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="visible" style="width:.5em;"><input type="hidden"  value="S" /><button name="visible" type="button" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
						<td class="padre" style="width:.5em;"><button name="padre" type="button" title="Obrir carpeta" style="display:none;"><img src="imgs/menu/padreCerrado.gif" alt="Obrir carpeta" /></button></td>
						<td class="icono">
							<bean:define id="icono" value="ico_componente.gif"/>
									
							<logic:match name="i" property="tipo" value="NTCS0"><bean:define id="icono" value="ico_listado.gif"/></logic:match>
							<logic:match name="i" property="tipo" value="NCSTS"><bean:define id="icono" value="ico_encuesta.gif"/></logic:match>							    
							<logic:match name="i" property="tipo" value="GND00"><bean:define id="icono" value="ico_agenda.gif"/></logic:match>	
							
							<img name="nivel1" src="imgs/menu/nivel1.gif" alt="" /><img src="imgs/iconos/<bean:write name="icono"/>" alt="Nivell 1" />
						</td>
						<td class="text">
							<bean:write name="i" property="titulo"/>
							<span class="opciones">
							
								<c:set var="tituloJavaScript"><bean:write name="i" property="titulo"/></c:set>	
													
								<button name="touse" type="button" title="usar este enlace" onclick="javascript:insertComponente('<bean:write name="i" property="tipo"/>','<bean:write name="i" property="url"/>','<% 										
								String tituloJS = (String)pageContext.getAttribute("tituloJavaScript");								
								%><%=tituloJS.replace("&#39;", "\\\'")%>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este componente"/></button> 
							</span>						
						</td>
					</tr>
				</tbody>
				</table>
			</div>

		</logic:iterate>
		</logic:notEmpty>
	
	
	
	</div>


</body>
</html>



<script type="text/javascript">
<!--

	var tinyMCE;
	var tinyMCELang;

	
	//functiones recogidas del tiny popup
	function findWin(w) {
		var c;

		// Check parents
		c = w;
		while (c && (c = c.parent) != null) {
			if (typeof(c.tinyMCE) != "undefined")
				return c;
		}

		// Check openers
		c = w;
		while (c && (c = c.opener) != null) {
			if (typeof(c.tinyMCE) != "undefined")
				return c;
		}

		// Try top
		if (typeof(top.tinyMCE) != "undefined")
			return top;

		return null;
	}
	
	function initTinyMCE() {
		var win = window.opener ? window.opener : window.dialogArguments, c;
		var inst;

		if (!win)
			win = this.findWin(window);

		if (!win) {
			alert("tinyMCE object reference not found from popup.");
			return;
		}

		// Setup parent references
		tinyMCE = win.tinyMCE;
		tinyMCELang = win.tinyMCELang;

	}	
	
	initTinyMCE();


-->
</script>






