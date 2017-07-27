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
	<logic:present name="MVS_contacto_titulo">
			<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_contacto_titulo" /></title>
	</logic:present>
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v4/js/globales.js"></script>

	<jsp:include page="/v4/general/scriptmenu.jsp"/>
	
	<script language="JavaScript">
		<!-- Comienzo
		/***********************************************
		* Validador de campos genericos requeridos v1.10- By VRS
		***********************************************/
		function checkrequired(which) {
			var pass=true;
			for (var i = 0; i < which.length; i++){
			
				var obj = which.elements[i];
				if (obj.name.substring(0,8)=="required") {
					if (obj){
						switch(obj.type){
						case "select-one":
							if (obj.selectedIndex == -1 || obj.options[obj.selectedIndex].text == "")
								pass=false;
							break;
						case "select-multiple":
							if (obj.selectedIndex == -1)
								pass=false;
							break;
						case "text":
						case "textarea":
							if (obj.value == "" || obj.value == null)
								pass=false;
							break;
						default:
  					 	}
					 	if (obj.type == undefined){
							var blnchecked = false;
							for (var j = 0; j < obj.length; j++){
								if (obj[j].checked){
									blnchecked = true;
								}
							}
							if (!blnchecked){
								pass=false;
							}
					  	}
					}
				}
				if (!pass) break;
			}
			
			if (!pass) {
				alert("<bean:message key="contacto.camporequerido"/>");
				return false;
			}
			else
				return true;
			}
		//  fin -->
</script>
	
	
	
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
				

				<logic:present name="MVS_contacto">
					<h2><bean:write name="MVS_contacto_titulo" /></h2>
					<logic:equal name="MVS_contacto" property = "anexarch" value ="S">												
						<form action="enviocontacto.do" onsubmit="return checkrequired(this)" enctype="multipart/form-data" method="post" >
							<input type="hidden" name="idsite" value ="<bean:write name="MVS_idsite" />"/>
							<input type="hidden" name="lang" value ="<bean:write name="MVS_idioma" />"/>
							<input type="hidden" name="cont" value ="<bean:write name="MVS_contacto" property="id"/>"/>		
							<logic:iterate name="MVS_contacto_listatags" id="i" indexId="indice">
								<div class="separacio"></div>
								<div class="etiqueta"><label for="<bean:write name="i" property="key"/>"><bean:write name="i" property="key" filter="false"/></label></div> <bean:write name="i" property="value" filter="false"/>
							</logic:iterate>
							<div class="separacio"></div>
							<div class="etiqueta"><label><bean:message key="contacto.docanex"/></label> 
							<input type="file" name="docAnex" id="docAnex" size="30" />
							</div>	
							<p class="botonera"><input name="btnanar" type="submit" value="<bean:message key="contacto.enviar"/>" tabindex="500" /></p>					
						</form>
					</logic:equal>
					<logic:notEqual name="MVS_contacto" property = "anexarch" value ="S">	
					<form action="enviocontacto.do" onsubmit="return checkrequired(this)" method="post" >
							<input type="hidden" name="idsite" value ="<bean:write name="MVS_idsite" />"/>
							<input type="hidden" name="lang" value ="<bean:write name="MVS_idioma" />"/>
							<input type="hidden" name="cont" value ="<bean:write name="MVS_contacto" property="id"/>"/>		
							<logic:iterate name="MVS_contacto_listatags" id="i" indexId="indice">
								<div class="separacio"></div>
								<div class="etiqueta"><label for="<bean:write name="i" property="key"/>"><bean:write name="i" property="key"/></label></div> <bean:write name="i" property="value" filter="false"/>
							</logic:iterate>
							<div class="separacio"></div>	
							<p class="botonera"><input name="btnanar" type="submit" value="<bean:message key="contacto.enviar"/>" tabindex="500" /></p>				
						</form>
					</logic:notEqual>
				</logic:present>
							
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
