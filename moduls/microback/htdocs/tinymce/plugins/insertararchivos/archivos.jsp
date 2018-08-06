<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script language="javascript" type="text/javascript" src="tinymce/plugins/compat3x/tiny_mce_popup.js"></script>
<script type="text/javascript" src="js/funciones.js"></script>
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
	<h1>Insertar Archivo</h1>

	<div id="menuArbol">

		<logic:notEmpty name="MVA_listaArchivos">
		<logic:iterate id="j" name="MVA_listaArchivos" indexId="indice">		
			<% indiceX++; %>
			<div id="m<%=indiceX%>" class="nivel1"><!-- id=m+id (m1) pq id=1 -->
				<input type="hidden"  value="1<%=indiceX%>" />
				<input type="hidden" value="<%=indiceX%>" />
				<table cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="visible" style="width:.5em;"><input type="hidden"  value="S" /><button name="visible" type="button" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
						<td class="padre"><button name="padre" type="button" title="Obrir carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir carpeta" /></button></td>
						<td class="icono"><img name="nivel1" src="imgs/menu/nivel1.gif" alt="" /><img src="imgs/menu/carpeta1.gif" alt="Nivell 1" /></td>
						<td class="text"><bean:write name="j" property="titulo"/></td>
					</tr>
				</tbody>
				</table>
			</div>
			<logic:iterate id="i" name="j" property="listacosas" indexId="indice">
				<% indiceX++; %>
				<div id="m<%=indiceX%>" class="pagCnoVisibleNivel1">
					<input type="hidden" value="1<%=indiceX%>" />
					<input type="hidden" value="<%=indiceX%>" />
					<table cellpadding="0" cellspacing="0">
		
					<tbody>
						<tr>
							<td class="visible" style="width:.5em;"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
							<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
							<td class="icono">
							
									<bean:define id="icono" value="stream.gif"/>
									
									<logic:match name="i" property="tipo" value="EXCEL"><bean:define id="icono" value="xls.gif"/></logic:match>
									<logic:match name="i" property="tipo" value="POWERPOINT"><bean:define id="icono" value="powerpoint.gif"/></logic:match>							    
									<logic:match name="i" property="tipo" value="ZIP"><bean:define id="icono" value="zip.gif"/></logic:match>							    							    
									<logic:match name="i" property="tipo" value="WORD"><bean:define id="icono" value="word.gif"/></logic:match>							    							    
								    <logic:match name="i" property="tipo" value="RTF"><bean:define id="icono" value="word.gif"/></logic:match>							    							      							    
								    <logic:match name="i" property="tipo" value="PDF"><bean:define id="icono" value="pdf.gif"/></logic:match>							    							    
								    <logic:match name="i" property="tipo" value="PLAIN"><bean:define id="icono" value="txt.gif"/></logic:match>							    							    
								    <logic:match name="i" property="tipo" value="HTML"><bean:define id="icono" value="html.gif"/></logic:match>
								    <logic:match name="i" property="tipo" value="CSS"><bean:define id="icono" value="css.gif"/></logic:match>
								    <logic:match name="i" property="tipo" value="AUDIO"><bean:define id="icono" value="media.gif"/></logic:match>
									<logic:match name="i" property="tipo" value="MEDIA"><bean:define id="icono" value="media.gif"/></logic:match>
								    <logic:match name="i" property="tipo" value="VIDEO"><bean:define id="icono" value="media.gif"/></logic:match>
								    <logic:match name="i" property="tipo" value="IMAGE"><bean:define id="icono" value="image.gif"/></logic:match>							    
									<logic:match name="i" property="tipo" value="FLASH"><bean:define id="icono" value="flash.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="OPENDOCUMENT.TEXT"><bean:define id="icono" value="odt.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="SUN.XML.WRITER"><bean:define id="icono" value="odt.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="STARDIVISION.WRITER"><bean:define id="icono" value="odt.gif"/></logic:match>	
									
									<logic:match name="i" property="tipo" value="OPENDOCUMENT.PRESENTATION"><bean:define id="icono" value="odp.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="SUN.XML.IMPRESS"><bean:define id="icono" value="odp.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="STARDIVISION.IMPRESS"><bean:define id="icono" value="odp.gif"/></logic:match>	
									
									<logic:match name="i" property="tipo" value="OPENDOCUMENT.SPREADSHEET"><bean:define id="icono" value="ods.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="SUN.XML.CALC"><bean:define id="icono" value="ods.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="STARDIVISION.CALC"><bean:define id="icono" value="ods.gif"/></logic:match>	
												
							
									<img name="nivel2" src="imgs/menu/nivel2.gif" alt="" /><img src='imgs/arxius/<bean:write name="icono"/>' alt='<bean:write name="i" property="tipo"/>' />
							
							</td>
							<td class="text"><bean:write name="i" property="titulo"/>
								<span class="opciones">
									<button name="preview" type="button" title="<bean:message key="op.12" />" onclick="previsualizarDoc('<bean:write name="i" property="url"/>');"><img src="imgs/iconos/sap_verdetalle.gif" alt="<bean:message key="op.12" />" /></button> 

									<bean:define id="tipomime" name="i" property="tipo" />
									<% String tipomime2 = (String)tipomime; tipomime2=tipomime2.toLowerCase(); %>
									<% if (tipomime2.indexOf("image")!=-1) { %>
											<button name="touse" type="button" title="<bean:message key="op.11" />" onclick="incluirImg('<bean:write name="MVS_microsite" property="id"/>','<bean:write name="i" property="head"/>','<bean:write name="i" property="titulo"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="<bean:message key="op.11" />"/></button> 
								    <% } else { %>
											<button name="touse" type="button" title="<bean:message key="op.11" />" onclick="incluirDoc('<bean:write name="MVS_microsite" property="id"/>','<bean:write name="i" property="head"/>','<bean:write name="i" property="titulo"/>','<%=tipomime2%>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="<bean:message key="op.11" />"/></button> 										
								    <% } %>									
									
									
								</span>
		
							</td>
						</tr>
					</tbody>
					</table>
				</div>
			</logic:iterate>
		</logic:iterate>
		</logic:notEmpty>
	
	
	
	</div>


</body>
</html>



<script type="text/javascript">
<!--

	var tinyMCE;
	var tinyMCELang;

	function MM_openBrWindow(theURL,winName,features) { //v2.0
	  window.open(theURL,winName,features);
	}
	
	function abrirDoc(url) {
		MM_openBrWindow(url, '_blank', 'scrollbars=no, resizable=yes, width=400, height=300')
	}
	function previsualizarDoc(url) {
		abrirDoc(url);
		return;
	}	

	function incluirDoc(idsite, id, nom, mime) {
		
		var url = "<img alt=\""+nom+"\" src=\"imgs/archivos_prev/";
			if(mime=="application/msword" || mime=="application/rtf") url+="doc.gif";
			else if(mime=="application/pdf" || mime=="application/x-pdf") url+="pdf.gif";
			else if(mime=="application/vnd.ms-powerpoint" || mime=="application/powerpoint") url+="ppt.gif";
			else if(mime=="application/vnd.ms-excel" || mime=="application/x-msexcel" || mime=="application/ms-excel" || mime=="application/msexcel" || mime=="application/x-excel") url+="xls.gif";
			else if(mime=="application/zip" || mime=="application/x-gtar" || mime=="application/x-tar") url+="zip.gif";
			else if(mime=="text/plain") url+="txt.gif";
			else if(mime=="text/html") url+="htm.gif";
			else if(mime.indexOf("audio")!=-1 || mime.indexOf("video")!=-1) url+="media.gif";
			else if(mime=="application/vnd.sun.xml.writer" || mime=="application/vnd.oasis.opendocument.text" || mime=="application/vnd.stardivision.writer") url+="odt.gif";
			else if(mime=="application/vnd.sun.xml.calc" || mime=="application/vnd.oasis.opendocument.spreadsheet" || mime=="application/vnd.stardivision.calc") url+="ods.gif";
			else if(mime=="application/vnd.sun.xml.impress" || mime=="application/vnd.oasis.opendocument.presentation" || mime=="application/vnd.stardivision.impress") url+="odp.gif";
			
			
			else url+="file.gif";
		url+="\">";
	
		url+="<a href=\"archivopub.do?ctrl=MCRST"+idsite;
		url+="ZI"+id+"&id="+id+"\">"+nom+"</a>";
		//url+="ZI"+id+"&id="+id+"\">"+nom+"</a>";
			
		tinyMCE.execCommand('mceInsertContent',false,url);
		debugger;
		tinyMCEPopup.close();
	}
	
	function incluirImg(idsite, id, nom) {
		
		var url="";
		
		
	
			url="<img alt=\""+nom+"\" src=\"archivopub.do?ctrl=MCRST"+idsite;
			url+="ZI"+id+"&id="+id+"\">";
	
		tinyMCE.execCommand('mceInsertContent',false,url);
		tinyMCEPopup.close();
		
	}
	
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



