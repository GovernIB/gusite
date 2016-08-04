<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
	<logic:present name="MVS_micrositetitulo">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - Mapa</title>
	</logic:present>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v1/js/globales.js"></script>
	</head>

	
	<body>
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">
				<!-- trazabilidad -->
				<ul id="mollaPa">
					<li><a href="home.do?idsite=<bean:write name="MVS_idsite" />&amp;lang=<bean:write name="MVS_idioma" />"><bean:message key="general.inicio"/></a></li>
					<li><bean:message key="mapa.mapa"/></li>
				</ul>
				
				
				
				<div id="infoNoMenu">
							<h2 id="titolPagina"><bean:message key="mapa.mapaweb"/></h2>
							<div id="pagMapaWeb">
								
								<bean:define id="tipocabecera" value="1" />
								<logic:empty name="MVS_microsite" property="tipocabecera" >
									<bean:define id="tipocabecera" value="1" />
								</logic:empty>
								<logic:equal name="MVS_microsite" property="tipocabecera" value="1">
									<bean:define id="tipocabecera" value="1" />
								</logic:equal>
								<logic:equal name="MVS_microsite" property="tipocabecera" value="0">
									<bean:define id="tipocabecera" value="0" />
								</logic:equal>
								<logic:equal name="MVS_microsite" property="tipocabecera" value="2">
									<bean:define id="tipocabecera" value="2" />
								</logic:equal>
								<bean:define id="tipopie" value="1" />
								<logic:empty name="MVS_microsite" property="tipopie" >
									<bean:define id="tipopie" value="1" />
								</logic:empty>
								<logic:equal name="MVS_microsite" property="tipopie" value="1">
									<bean:define id="tipopie" value="1" />
								</logic:equal>
								<logic:equal name="MVS_microsite" property="tipopie" value="0">
									<bean:define id="tipopie" value="0" />
								</logic:equal>
								<logic:equal name="MVS_microsite" property="tipopie" value="2">
									<bean:define id="tipopie" value="2" />
								</logic:equal>					
							
							
								<h3><bean:message key="mapa.apartados"/></h3>
								<ul>
									<logic:equal name="tipocabecera" value="1">
									<logic:present name="MVS_listacabecera">
									    <logic:iterate id="i" name="MVS_listacabecera" indexId="indice2">
										    <li>
												<logic:match name="i" property="value1" value="http">
												    <a href="<bean:write name="i" property="value1"/>"><bean:write name="i" property="key"/></a>
												</logic:match>
												<logic:notMatch name="i" property="value1" value="http">
												    <a href="<bean:write name="i" property="value1"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:write name="i" property="key"/></a>
											    </logic:notMatch>
										    </li>
									    </logic:iterate>
								    </logic:present>
								    </logic:equal>
								    <logic:equal name="tipopie" value="1">
									<logic:present name="MVS_listapie">						    
									    <logic:iterate id="i" name="MVS_listapie" indexId="indice">
										    <li>
	
												<logic:match name="i" property="value" value="http">
												    <a href="<bean:write name="i" property="value1"/>"><bean:write name="i" property="key"/></a>
												</logic:match>
												<logic:notMatch name="i" property="value" value="http">
												    <a href="<bean:write name="i" property="value"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:write name="i" property="key"/></a>
											    </logic:notMatch>
										    
										    </li>
									    </logic:iterate>
									</logic:present>
									</logic:equal>  
							    </ul>								
								
								
								<h3><bean:write name="MVS_micrositetitulo" filter="false" ignore="true"/></h3>
								
								<logic:present name="MVS_menu">
									
									    <logic:iterate id="i" name="MVS_menu">
									    
<									    	<logic:present name="i" property="traduce">
									    
									    	<h3><bean:write name="i" property="traduce.nombre"/></h3>
									    	<logic:iterate name="i" id="j" property="listacosas">
										    	<ul>
												   <bean:define id="objeto" name="j" type="Object"/>
											   		<%
											    	if (objeto instanceof es.caib.gusite.micromodel.Contenido) {
											    	%>
											    		<li><a href="contenido.do?idsite=<bean:write name="MVS_idsite"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="id"/>"><bean:write name="j" property="traduce.titulo"/></a></li>
											    	<%
											    	} else {
											    	%>	
												    	<bean:size id="tamano" name="j" property="listacosas"/>
												    	<logic:notEqual name="tamano" value="1">
													    	<li><bean:write name="j" property="traduce.nombre"/>
														    	<ul>
																<logic:iterate name="j" id="k" property="listacosas">
																			<li><a href="contenido.do?idsite=<bean:write name="MVS_idsite"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="k" property="id"/>"><bean:write name="k" property="traduce.titulo"/></a></li>
																</logic:iterate>
																</ul>
															</li>
												    	</logic:notEqual>
				
												    	<logic:equal name="tamano" value="1">
															<li><a href="contenido.do?idsite=<bean:write name="MVS_idsite"/>&amp;lang=<bean:write name="MVS_idioma"/>&amp;cont=<bean:write name="j" property="listacosas[0].id"/>"><bean:write name="j" property="traduce.nombre"/></a></li>								    	
												    	</logic:equal>
											    	<%
											    	}
											   		%>
											   	</ul>
									   		</logic:iterate>
									   		
									   		</logic:present>
									   		
										</logic:iterate>
									
								</logic:present> 
								

							</div>
					</div>				
				
				
			</div>

			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
		</div>	
	</body>
</html>


