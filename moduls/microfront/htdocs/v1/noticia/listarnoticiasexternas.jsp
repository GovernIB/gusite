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
		<title><bean:write name="MVS_micrositetitulo" filter="false"/> - <bean:write name="MVS_tipolistado" ignore="true" /></title>			
	<logic:present name="MVS_css">
			<bean:write name="MVS_css" filter="false"/>
	</logic:present>
	<script type="text/javascript" src="v1/js/globales.js"></script>
	
	<jsp:include page="/v1/general/scriptmenu.jsp"/>
	

	</head>

	
	<body>
		
		<div id="contenedor">
			<!-- capçal -->
			<jsp:include page="/v1/general/cabecera.jsp"/>

			<!-- continguts -->
			<div id="continguts">
				
				<jsp:include page="/v1/general/inc_llistats_campana_menu.jsp"/>
		
				<jsp:include page="/general/inc_llistats_div_info.jsp"/>
				

				
								<!-- titol -->
								<h2 id="titolPagina"><bean:write name="MVS_tipolistado" /></h2>

									<logic:notEmpty name="MVS_mcstyle" >
										<style>
											<bean:write name="MVS_mcstyle" filter="false" />
										</style>	
									</logic:notEmpty>

									
									<logic:present name="MVS_mchtml">
									<form action="noticias.do" name="formnoticiasexterno" id="formnoticiasexterno" >
										<input type="hidden" name="lang" value="<bean:write name="MVS_idioma"/>">
										<input type="hidden" name="nameform" value="formnoticiasexterno">
										<input type="hidden" name="mkey" value="<bean:write name="MVS_microsite" property="claveunica" filter="false"/>">
										<input type="hidden" name="tipo" value="<bean:write name="MVS_claseelemento" property="id" filter="false"/>">
										<input type="hidden" name="pagina" value="1">
																			
										<bean:write name="MVS_mchtml" filter="false" />
									</form>	
									</logic:present>
												
				</div>	
				<!-- fin informacio -->
				

			</div>
			<!-- fin continguts -->
			
			
			<!-- peu -->
			<jsp:include page="/v1/general/pie.jsp"/>
			<!-- fin peu -->
		</div>	
	</body>
</html>

<logic:notEmpty name="MVS_mcjavascript" >
	<script type="text/javascript">
	<bean:write name="MVS_mcjavascript"  filter="false" />
	</script>
</logic:notEmpty>
