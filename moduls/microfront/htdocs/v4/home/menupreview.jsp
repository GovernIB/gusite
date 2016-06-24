<%@ page language="java"%>
<%@ page language="java" contentType="text/html; charset=utf8"  pageEncoding="utf8"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean" %>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.png" type="image/x-ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="Generator" content="<bean:message key="microsites.name"/>; version:<bean:message key="microsites.version"/>; build:<bean:message key="microsites.build"/>">
	<logic:present name="MVS_micrositetitulo">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - Menu preview</title>
	</logic:present>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v4/js/globales.js"></script>
	<jsp:include page="/v4/general/scriptmenu.jsp"/>	
	</head>

	
	<body>
		
		<div>
			<!-- capÃ¾al -->
			<!-- continguts -->
			<div id="continguts">
			
				<ul id="mollaPa">
							<logic:present name="MVS_seulet">
								<bean:define id="idi" name="MVS_idioma" />
								<% String idi2 = (String)idi; idi2=idi2.toLowerCase(); %>
								<bean:message key="cabecera.idioma"/>: 
								<bean:size id="tamano" name="MVS_microsite" property="idiomas"/>
							    <logic:iterate id="i" name="MVS_microsite" property="idiomas"  indexId="indice">
					    			<bean:define id="idiomaa" name="i" />
							    	<% if (idi2.equals(idiomaa)) { %>
									    <strong><bean:write name="i"/></strong>
							    	<% } else { %>
										<a href="<bean:write name="MVS_seulet"/>&amp;lang=<bean:write name="i"/>"><bean:write name="i"/></a>&nbsp;		    	
							    	<% } %>
							    	<%=(tamano.intValue()==(indice.intValue()+1)?"":" . ")%>
							    </logic:iterate>
							</logic:present>    
				</ul>
				
				<!-- menu -->
				<logic:present name="MVS_microsite">
							<logic:equal name="MVS_microsite" property="tipomenu" value="1">
								<div id="marcLateral">
									<jsp:include page="/v4/general/menupreview.jsp"/>
								</div>					
							</logic:equal>
							<logic:equal name="MVS_microsite" property="tipomenu" value="2">
								<div id="marcLateralAmbIcones">
									<jsp:include page="/v4/general/menupreview.jsp"/>
								</div>					
							</logic:equal>
				</logic:present>	
				<!-- fin menu -->

				
			</div>

		</div>	
	</body>
</html>
