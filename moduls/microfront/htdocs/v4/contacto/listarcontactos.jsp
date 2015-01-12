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
	<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:message key="listarcontactos.frmcontacto"/></title>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v4/js/globales.js"></script>
	
	<jsp:include page="/v4/general/scriptmenu.jsp"/>
	
	</head>

	
	<body>
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v4/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">

				
				<!-- campanya -->
				<logic:present name="MVS_home_tmp_campanya">
					<bean:write name="MVS_home_tmp_campanya" filter="false"/>
				</logic:present>
				<!-- fin campanya -->
				
				
				<!-- menu -->
				<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<div id="marcLateral">
									<jsp:include page="/v4/general/menu.jsp"/>
								</div>					
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<div id="marcLateralAmbIcones">
									<jsp:include page="/v4/general/menu.jsp"/>
								</div>					
							</logic:equal>
				</logic:present>	
				<!-- fin menu -->


				
				<!-- informacio con o sin menu -->
				<logic:equal name="MVS_microsite" property="tipomenu" value="0">
					<div id="infoNoMenu">
				</logic:equal>
				<logic:notEqual name="MVS_microsite" property="tipomenu" value="0">
					<div id="info">
				</logic:notEqual>					
				
								
				
				
						
										<!-- titol -->
										<h2><bean:message key="listarcontactos.frmcontacto"/></h2>
										
											<logic:equal name="MVS_parametros_pagina" property="nreg" value="0">
												<p><bean:message key="listarcontactos.nohay"/>.</p>
											</logic:equal>
											
											<logic:notEqual name="MVS_parametros_pagina" property="nreg" value="0">  
													<p><bean:message key="listarcontactos.frmdisponibles"/>:</p>
													
														<ul>
														<logic:iterate name="MVS_listado" id="i">
														<li>
																<a href="<bean:write name="i" property="value" />"><bean:write name="i" property="key" /></a>
														</li>
														</logic:iterate>
														</ul>
													
													<p id="navLlistat">
													<logic:present name="MVS_parametros_pagina" property="inicio">
											            &lt;&lt; <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="inicio"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.inicio"/></a>&nbsp;
											        </logic:present>
											        <logic:present name="MVS_parametros_pagina" property="anterior">
											            &lt; <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="anterior"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.anterior"/></a>&nbsp;
											        </logic:present>
											        - <bean:write name="MVS_parametros_pagina" property="cursor" /> a <bean:write name="MVS_parametros_pagina" property="cursor_final" /> de <bean:write name="MVS_parametros_pagina" property="nreg" /> -  
											        <logic:present name="MVS_parametros_pagina" property="siguiente">
												        <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="siguiente"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.siguiente"/></a> &gt;&gt;
											        </logic:present>
											        <logic:present name="MVS_parametros_pagina" property="final">
											            <a href="<bean:write name="MVS_seulet_sin"/>&pagina=<bean:write name="MVS_parametros_pagina" property="final"/>&amp;lang=<bean:write name="MVS_idioma"/>"><bean:message key="general.param.final"/></a> &gt;
											        </logic:present>
													</p>
													
											</logic:notEqual>						
				
				
				
				</div>	
				<!-- fin informacio -->
			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v4/general/pie.jsp"/>
			<!-- fin peu -->
			
		</div>	
		<!-- fin contenedor -->
	</body>
</html>


