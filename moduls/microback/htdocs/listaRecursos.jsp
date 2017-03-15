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
	<script type="text/javascript" src="js/coArbre2.js"></script>
</head>

<body>
<%
int indiceX=0;
%>
	<!-- titol pagina -->
	<h1>URLs generales</h1>
	
	<div id="menuArbol">
	
	
		<logic:notEmpty name="MVA_listaURLs">
		<logic:iterate id="i" name="MVA_listaURLs" indexId="indice">		
				<% indiceX++; %>
				<bean:define id="objetopadre" name="i" type="Object"/>
		   		<%
		   			if (objetopadre instanceof es.caib.gusite.microback.base.bean.Recurso) {
		   		%>
					<!-- recurso de nivel 1 -->
					<div id="m<%=indiceX%>" class="nivel1"><!-- id=m+id (m1) pq id=1 -->
						<table cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td class="visible"><input type="hidden" value="S" /><button name="visible" type="button" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
								<td class="padre"><button name="padre" type="button" title="Obrir carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir carpeta" /></button></td>
								<td class="icono"><img name="nivel1" src="imgs/menu/nivel1.gif" alt="" />
									<logic:equal name="i" property="head" value="0">
										<img src="imgs/menu/carpeta1.gif" alt="Nivell 1" />
									</logic:equal>
									<logic:equal name="i" property="head" value="1">								
										<img src="imgs/menu/carpeta.gif" alt="Nivell 1" />
									</logic:equal>								 
									<logic:equal name="i" property="head" value="10">
										<img src="imgs/menu/pagContenidos.gif" alt="<bean:write name="i" property="titulo"/>" />
									</logic:equal>
								</td>
								<td class="text"><bean:write name="i" property="titulo"/>
									<logic:equal name="i" property="head" value="10">
												<span class="opciones">
													<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="i" property="url"/>','<bean:write name="i" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
												</span>
									</logic:equal>
								</td>
							</tr>
						</tbody>
						</table>
					</div>	
				<%
						} else {
					%>				
		    		<!-- padre de nivel 1 -->
					<div id="m<%=indiceX%>" class="nivel1"><!-- id=m+id (m1) pq id=1 -->
						<table cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td class="visible"><input type="hidden" value="S" /><button name="visible" type="button" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
								<td class="padre"><button name="padre" type="button" title="Obrir carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir carpeta" /></button></td>
								<td class="icono"><img name="nivel1" src="imgs/menu/nivel1.gif" alt="" />
									<logic:equal name="i" property="head" value="0">
										<img src="imgs/menu/carpeta1.gif" alt="Nivell 1" />
									</logic:equal>
									<logic:equal name="i" property="head" value="1">								
										<img src="imgs/menu/carpeta.gif" alt="Nivell 1" />
									</logic:equal>								
								</td>
								<td class="text"><bean:write name="i" property="titulo"/>
									<logic:equal name="i" property="head" value="1">
												<span class="opciones">
													<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="i" property="url"/>','<bean:write name="i" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
												</span>
									</logic:equal>
								</td>
							</tr>
						</tbody>
						</table>
					</div>
						
					<!-- hijos de nivel 1 -->		    		
					<logic:iterate id="j" name="i" property="listacosas" indexId="indice">
						<%
							indiceX++;
						%>
						<bean:define id="objeto" name="j" type="Object"/>
				   		<%
				   			if (objeto instanceof es.caib.gusite.microback.base.bean.Recurso) {
				   		%>
							<div id="m<%=indiceX%>" class="pagCnoVisibleNivel1">
								<table cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
										<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
										<td class="icono"><img name="nivel2" src="imgs/menu/nivel2.gif" alt="" />
										<img src="imgs/menu/pagContenidos.gif" alt="<bean:write name="j" property="titulo"/>" />
										</td>
										<td class="text"><bean:write name="j" property="titulo"/>
											<span class="opciones">
												<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="j" property="url"/>','<bean:write name="j" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
											</span>
										</td>
									</tr>
								</tbody>
								</table>
							</div>				
						<%
											} else {
										%>
					    	<!-- padres de nivel 2 -->
							<div id="m<%=indiceX%>" class="nivel2noVisible">
								<table cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
										<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
										<td class="icono"><img name="nivel2" src="imgs/menu/nivel2.gif" alt="" /><img src="imgs/menu/carpeta.gif" alt="<bean:write name="j" property="titulo"/>" /></td>
										<td class="text"><bean:write name="j" property="titulo"/>
											<logic:equal name="j" property="head" value="1">
													<span class="opciones">
														<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="j" property="url"/>','<bean:write name="j" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
													</span>									
											</logic:equal>
										</td>
									</tr>
					
								</tbody>
								</table>
							</div>			    	
					    	
					    	
					    	
					    	<logic:iterate name="j" id="k" property="listacosas">
						    	<%
						    		indiceX++;
						    	%>
								<div id="m<%=indiceX%>" class="pagCnoVisibleNivel2">
									<table cellpadding="0" cellspacing="0">
									<tbody>
										<tr>
											<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
											<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
											<td class="icono"><img name="nivel3" src="imgs/menu/nivel3.gif" alt="" /><img src="imgs/menu/pagContenidos.gif" alt="<bean:write name="k" property="titulo"/>" /></td>
											<td class="text"><bean:write name="k" property="titulo"/>
												<span class="opciones">
													<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="k" property="url"/>','<bean:write name="k" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
												</span>
											</td>
										</tr>
									</tbody>
									</table>
								</div>				    	
					    	</logic:iterate>
				    	<%
				    		}
				    	%>
			    	</logic:iterate>
		    	<%
		    		}
		    	%>			    	
		</logic:iterate>
		</logic:notEmpty>	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


		<logic:notEmpty name="MVA_listaArbol">
		<h2>Arbol de contenidos</h2>
		<logic:iterate id="i" name="MVA_listaArbol" indexId="indice">		
				<%
							indiceX++;
						%>
				<!--  menu padre -->
				<div id="m<%=indiceX%>" class="nivel1"><!-- id=m+id (m1) pq id=1 -->
					<table cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td class="visible"><input type="hidden" value="S" /><button name="visible" type="button" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
							<td class="padre"><button name="padre" type="button" title="Obrir carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir carpeta" /></button></td>
							<td class="icono"><img name="nivel1" src="imgs/menu/nivel1.gif" alt="" /><img src="imgs/menu/carpeta1.gif" alt="Nivell 1" /></td>
							<td class="text"><bean:write name="i" property="titulo"/></td>
						</tr>
					</tbody>
					</table>
				</div>			
				<logic:iterate id="j" name="i" property="listacosas" indexId="indice">
					<%
						indiceX++;
					%>
					<bean:define id="objeto" name="j" type="Object"/>
			   		<%
			   			if (objeto instanceof es.caib.gusite.microback.base.bean.Recurso) {
			   		%>
						<div id="m<%=indiceX%>" class="pagCnoVisibleNivel1">
							<table cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
									<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
									<td class="icono"><img name="nivel2" src="imgs/menu/nivel2.gif" alt="" /><img src="imgs/menu/pagContenidos.gif" alt="<bean:write name="j" property="titulo"/>" /></td>
									<td class="text"><bean:write name="j" property="titulo"/>
										<span class="opciones">
											<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="j" property="url"/>','<bean:write name="j" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
										</span>
				
									</td>
								</tr>
							</tbody>
							</table>
						</div>				
					<%
			    	} else {
			    	%>
				    	
						<div id="m<%=indiceX%>" class="nivel2noVisible">
							<table cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
									<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
				
									<td class="icono"><img name="nivel2" src="imgs/menu/nivel2.gif" alt="" /><img src="imgs/menu/carpeta.gif" alt="<bean:write name="j" property="titulo"/>" /></td>
									<td class="text"><bean:write name="j" property="titulo"/></td>
								</tr>
				
							</tbody>
							</table>
						</div>			    	
				    	
				    	
				    	
				    	<logic:iterate name="j" id="k" property="listacosas">
					    	<% indiceX++; %>
							<div id="m<%=indiceX%>" class="pagCnoVisibleNivel2">
								<table cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
										<td class="padre"><button name="padre" type="button" title="Obrir Carpeta"><img src="imgs/menu/padreCerrado.gif" alt="Obrir Carpeta" /></button></td>
										<td class="icono"><img name="nivel3" src="imgs/menu/nivel3.gif" alt="" /><img src="imgs/menu/pagContenidos.gif" alt="<bean:write name="k" property="titulo"/>" /></td>
										<td class="text"><bean:write name="k" property="titulo"/>
											<span class="opciones">
												<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="k" property="url"/>','<bean:write name="k" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
											</span>
					
										</td>
									</tr>
								</tbody>
								</table>
							</div>				    	
				    	</logic:iterate>
			    	<%
			    	}
			    	%>
		    	</logic:iterate>
		</logic:iterate>
		</logic:notEmpty>



		<logic:notEmpty name="MVA_listaArchivos">
		<h2>Archivos disponibles</h2>
		<logic:iterate id="j" name="MVA_listaArchivos" indexId="indice">		
			<% indiceX++; %>
			<div id="m<%=indiceX%>" class="nivel1"><!-- id=m+id (m1) pq id=1 -->
				<table cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td class="visible"><input type="hidden"  value="S" /><button name="visible" type="button" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
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
					<table cellpadding="0" cellspacing="0">
		
					<tbody>
						<tr>
							<td class="visible"><input type="hidden" value="1" /><button type="button" name="visible" title="Visible" style="display:none;"><img src="imgs/menu/visible.gif" alt="Visible" /></button></td>
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
									<logic:match name="i" property="tipo" value="OPENDOCUMENT"><bean:define id="icono" value="odt.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="SUN.XML.WRITER"><bean:define id="icono" value="odt.gif"/></logic:match>		
									<logic:match name="i" property="tipo" value="STARDIVISION"><bean:define id="icono" value="odt.gif"/></logic:match>	
							
									<img name="nivel2" src="imgs/menu/nivel2.gif" alt="" /><img src='imgs/arxius/<bean:write name="icono"/>' alt='<bean:write name="i" property="tipo"/>' />
							
							</td>
							<td class="text"><bean:write name="i" property="titulo"/>
								<span class="opciones">
									<button name="preview" type="button" title="<bean:message key="op.12" />" onclick="previsualizarDoc('<bean:write name="i" property="url"/>');"><img src="imgs/iconos/sap_verdetalle.gif" alt="<bean:message key="op.12" />" /></button> 
									<button name="touse" type="button" title="usar este enlace" onclick="Rutilizaurl('<bean:write name="i" property="url"/>','<bean:write name="i" property="urlnom"/>');"><img src="imgs/iconos/sap_aceptar.gif" width="15" height="15" alt="usar este enlace"/></button> 
								</span>
		
							</td>
						</tr>
					</tbody>
					</table>
				</div>
			</logic:iterate>
		</logic:iterate>
		</logic:notEmpty>
	
		<h2><bean:message key="conte.recursos.enlaceforos"/></h2>
		<button name="foros" type="button" title="" onclick="document.location='/gforumback/popupForos.do?micro'"><bean:message key="conte.recursos.cargalistado"/></button> 

	</div>
	
</body>
</html>

<script type="text/javascript">
<!--

	<logic:equal name="MVA_forcetiny" value="true">
		function Rutilizaurl(url, descripcion) {
		 	opener.Rmeterurl_tiny(url, descripcion);
			window.close();
		}
	</logic:equal>

	<logic:equal name="MVA_forcetiny" value="false">
	  	function Rutilizaurl(url, descripcion) {
        opener.Rmeterurl(url, descripcion);
        window.close();
      }
	</logic:equal>
	 

 
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


-->
</script>
<jsp:include page="/moduls/pieControl.jsp"/>
