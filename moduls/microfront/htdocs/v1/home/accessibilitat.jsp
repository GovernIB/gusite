<%@ page language="java"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
	<logic:present name="MVS_contacto_titulo">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_contacto_titulo" /></title>
	</logic:present>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v4/js/globales.js"></script>

	<jsp:include page="/v4/general/scriptmenu.jsp"/>
	
	</head>

	
	<body>
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

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
				
					<h2><bean:message key="accessibilitat.compromis"/></h2>
					<p><bean:message key="accessibilitat.compromis.texte1"/></p>
					<p><bean:message key="accessibilitat.compromis.texte2"/></p>
					<a href="http://www.w3.org/WAI/WCAG1AA-Conformance" title="Explicació del Nivell Doble-A de Conformitat"><img src="http://www.w3.org/WAI/wcag1AA" alt="Icona de conformitat amb el Nivell Doble-A, de les Directrius d'Accessibilitat per al Contingut Web 1.0 del W3C-WAI"></a>
					<p><bean:message key="accessibilitat.compromis.texte3"/></p>
					<h2><bean:message key="accessibilitat.dreceres"/></h2>
					<p><bean:message key="accessibilitat.dreceres.texte1"/></p>
					<dl class="dreseres">
						<bean:message key="accessibilitat.dreceres.texte2"/>
					</dl>
					<p><bean:message key="accessibilitat.dreceres.texte3"/></p>
		
					<dl class="dreseres">
						<bean:message key="accessibilitat.dreceres.texte4"/>
					</dl>					
					<h2><bean:message key="accessibilitat.eines"/></h2>
					<p><bean:message key="accessibilitat.eines.texte1"/></p>
					<p><bean:message key="accessibilitat.eines.texte2"/></p>
					<ul>
						<li><a href="http://www.mozilla-europe.org/es/products/firefox/">Firefox</a></li>
						<li><a href="http://www.mozilla-europe.org/es/products/mozilla1x/">Mozilla</a></li>
						<li><a href="http://www.microsoft.com/spain/windows/ie/experiences/default.mspx">Internet Explorer</a></li>
						<li><a href="http://browser.netscape.com/ns8/download/default.jsp">Netscape</a></li>
						<li><a href="http://www.opera.com/download/">Opera</a></li>
						<li><a href="http://www.kde.org/">Konqueror</a></li>
						<li><a href="http://www.apple.com/es/macosx/features/safari/">Safari</a></li>
					</ul>
				
				</div>	
				<!-- fin informacio -->
			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
			<!-- fin peu -->
			
		</div>	
		<!-- fin contenedor -->			
			
			
	</body>
</html>
