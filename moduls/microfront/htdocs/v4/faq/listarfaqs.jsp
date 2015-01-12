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
		<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:message key="listarfaqs.listado"/></title>
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
							<h2><bean:message key="listarfaqs.preguntas"/></h2>
							<!-- informacio FAQ-->
							<div id="infoFAQ">
							<dl>
								<!-- <br/> -->
								<logic:iterate name="MVS_listado" id="i">
									<li class="nivel1" tabindex="1">
									<!-- Todos menos explorer--> <span name="noie" id ="noie" style="visibility:visible;" class="nivel1"><h4><bean:write name="i" property="tema" ignore="true"/></h4></span> 
									<!-- explorer--> <span name ="ie" id="ie" style="visibility:hidden;"> <a href="#" class="nivel1"><h4><bean:write name="i" property="tema" ignore="true"/></h4><table class="falsa"><tr><td> </span>
										
										<logic:iterate name="i" id="j" property="listadopreguntas">
											<dl style="visibility:visible;">
												<dt><bean:write name="j" property="traduce.pregunta" ignore="true" filter="false"/></dt>
												<dd><bean:write name="j" property="traduce.respuesta" ignore="true" filter="false" />
												<logic:present name="j" property="traduce.url">
												     <bean:define id="externa" name="j" property="traduce.url" type="java.lang.String" />
													 <bean:define id="sitio" name="MVS_idsite"  />
													 <a href="<bean:write name="j" property="traduce.url" />"<%if(externa.indexOf("idsite="+sitio)==-1){out.println("target='_blanck'");}else{out.println("target='_self'");}%>>
														 <logic:notEmpty name="j" property="traduce.urlnom" >
															<bean:write name="j" property="traduce.urlnom" ignore="true"/>
														 </logic:notEmpty>
														 <logic:empty name="j" property="traduce.urlnom" >
															<bean:message key="url.adicional"/>
														 </logic:empty>		
													 </a>
												</logic:present>
												<br/>
												</dd>	
											</dl>							
										</logic:iterate>
										<!-- explorer --> <span id="ie" style="visibility:hidden;" ></td></tr></table></a> </span> 
									</li><br/>
								</logic:iterate>	
								</dl>
							</div>		
									
			</div>	
			<!-- fin informacio -->
		</div>
		<!-- fin continguts -->
		<!-- peu -->
		<jsp:include page="/v4/general/pie.jsp"/>
		<!-- fin peu -->

<script type="text/javascript">
 function browser() {
	  if (navigator.userAgent.indexOf("MSIE") !=-1) {navega ="iexplorer";}
	  else if (navigator.userAgent.indexOf('Firefox') !=-1) {navega ="firefox";}
	  else if (navigator.userAgent.indexOf('Chrome') !=-1) {navega ="chrome";}
	  else if (navigator.userAgent.indexOf('Opera') !=-1) {navega ="opera";}
	  return navega;
}	  
</script>	

<script type="text/javascript">
if ("iexplorer"==browser()){
	var nodes = document.getElementsByName('ie');
	for (i = 0; i<nodes.length; i++) {
		nodes[i].style.visibility = 'visible';
	}
	var nodes = document.getElementsByName('noie');
	for (i = 0; i<nodes.length; i++) {
		nodes[i].style.visibility = 'hidden';
	}	
} 	
</script>

</body>
</html>


